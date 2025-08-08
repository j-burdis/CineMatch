CREATE TABLE PaintMatches (
  id BIGSERIAL PRIMARY KEY,
  movie_id BIGINT REFERENCES Movies(id) ON DELETE CASCADE,
  paint_1 VARCHAR(50),
  paint_2 VARCHAR(50),
  paint_3 VARCHAR(50),
  paint_4 VARCHAR(50),
  paint_5 VARCHAR(50),
  paint_6 VARCHAR(50),
  paint_7 VARCHAR(50),
  paint_8 VARCHAR(50),
  paint_9 VARCHAR(50),
  paint_10 VARCHAR(50),
  paint_11 VARCHAR(50),
  paint_12 VARCHAR(50)
);
