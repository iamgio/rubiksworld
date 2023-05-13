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
AND (NOT ? OR is_magnetic)
