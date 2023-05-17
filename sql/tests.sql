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
    (?, @model_version_id)