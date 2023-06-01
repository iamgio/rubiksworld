USE rubiksworld;

# Search

SET @txt = ?;
SET @txt_like = CONCAT('%', @txt, '%');
SELECT *
FROM Models
WHERE (@txt = '' # Empty text
    OR name LIKE @txt_like
    OR maker LIKE @txt_like
    OR category_name LIKE @txt_like)
AND (NOT ? OR is_speed_cube)
AND (NOT ? OR is_stickerless)
AND (NOT ? OR is_magnetic);

# User log-in/registration

INSERT INTO Users
    (nickname, name, surname)
VALUES
    (?, ?, ?)
ON DUPLICATE KEY UPDATE
                     name    = VALUES(name),
                     surname = VALUES(surname);

# Model version price calculation

SELECT MV.id,
       M.name,
       SUM(C.price) + M.price                                     AS base_price,
       IF(ISNULL(M.discount_percentage), SUM(C.price) + M.price,
          (SUM(C.price) + M.price) * (1 - M.discount_percentage)) AS price
FROM Applications A,
     Customizations C,
     Models M,
     ModelVersions MV
WHERE MV.model_name = M.name
  AND MV.model_maker = M.maker
  AND A.model_name = M.name
  AND A.model_maker = M.maker
  AND C.model_name = M.name
  AND C.model_maker = M.maker
  AND A.customization_part = C.part
  AND A.customization_change = C.change
GROUP BY MV.id;

# Create a model version and add it to cart

SET @model_name = ?;
SET @model_maker = ?;

# Create version
INSERT INTO ModelVersions
    (model_name, model_maker)
VALUES
    (@model_name, @model_maker);

SET @model_version_id = LAST_INSERT_ID();

# For each customization
INSERT INTO Applications
    (model_name, model_maker, customization_part, customization_change, model_version_id)
VALUES
    (@model_name, @model_maker, ?, ?, @model_version_id);

# Add to cart
INSERT INTO CartPresences
    (user_nickname, model_version_id)
VALUES
    (?, @model_version_id);

# Add to wishlist
INSERT INTO WishlistPresences
    (user_nickname, model_name, model_maker)
VALUES
    (?, ?, ?);

# Coupon validity
SELECT *
FROM Coupons
WHERE code = ?;

# Get personal solves
SELECT *
FROM Solves
WHERE user_nickname = ?
ORDER BY solve_time;

# Get friends' solves + personal solves
SET @user_nickname = ?;
SELECT DISTINCT S.*
FROM Solves S,
     Friendships F
WHERE (F.sender_nickname = @user_nickname
    AND S.user_nickname = F.receiver_nickname)
   OR S.user_nickname = @user_nickname
ORDER BY S.solve_time;

# Get global solves
SELECT *
FROM Solves
ORDER BY solve_time;


# Get top solve for each model
SELECT MIN(solve_time) as time, model_name, model_maker
FROM Solves
WHERE user_nickname = ?
GROUP BY model_name, model_maker;

# Register solve
INSERT INTO Solves
    (user_nickname, solve_time, registration_date, model_name, model_maker)
VALUES
    (?, ?, NOW(), ?, ?);

# Get orders
SELECT *
FROM Orders
WHERE user_nickname = ?
ORDER BY order_date DESC;

# Get model versions from order
SELECT MV.*
FROM OrderPresences O JOIN ModelVersions MV ON O.model_version_id = MV.id
WHERE O.order_id = ?
  AND O.order_date = ?;

# Find user
SELECT *
FROM Users
WHERE CONCAT(nickname, name, ' ', surname) LIKE CONCAT('%', ?, '%')
  AND nickname <> ?;

# Add friend
SET @sender = ?;
SET @receiver = ?;
INSERT INTO Friendships
    (sender_nickname, receiver_nickname)
SELECT @sender, @receiver
WHERE @sender <> @receiver;

# Place order

# Get next ID
SET @order_id = (SELECT IFNULL(MAX(id) + 1, 0)
           FROM Orders
           WHERE order_date = CURDATE());

SET @user_nickname = ?;

# Place order
INSERT INTO Orders
    (id, order_date, order_time, shipping_date, total, user_nickname)
VALUES
    (@order_id, CURDATE(), NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY), ?, @user_nickname);

# Add model versions from cart to the order
INSERT INTO OrderPresences (order_id, order_date, model_version_id)
SELECT @order_id, CURDATE(), model_version_id
FROM CartPresences
WHERE user_nickname = @user_nickname;

# Empty cart
DELETE FROM CartPresences
WHERE user_nickname = @user_nickname;

# Apply one coupon (for each coupon)
INSERT INTO Discounts
(order_id, order_date, coupon_code)
VALUES
    (@order_id, CURDATE(), ?);