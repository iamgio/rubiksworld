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

# Model version price calculation

SELECT MV.id,
       M.name,
       SUM(C.price) + M.price as base_price,
       IF(ISNULL(M.discount_percentage), base_price, base_price - base_price * M.discount_percentage) as price
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
GROUP BY MV.id