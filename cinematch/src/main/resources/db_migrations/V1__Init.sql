DROP TABLE IF EXISTS ColourPalette;
DROP TABLE IF EXISTS Movies;

CREATE TABLE Movies (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  release_date DATE,
  poster_url VARCHAR(255),
  dominant_colour VARCHAR(255)
);

CREATE TABLE ColourPalette (
  id BIGSERIAL PRIMARY KEY,
  movie_id BIGINT REFERENCES Movies(id) ON DELETE CASCADE,
  colour_1 VARCHAR(50),
  colour_2 VARCHAR(50),
  colour_3 VARCHAR(50),
  colour_4 VARCHAR(50),
  colour_5 VARCHAR(50),
  colour_6 VARCHAR(50),
  colour_7 VARCHAR(50),
  colour_8 VARCHAR(50),
  colour_9 VARCHAR(50),
  colour_10 VARCHAR(50),
  colour_11 VARCHAR(50),
  colour_12 VARCHAR(50),
);