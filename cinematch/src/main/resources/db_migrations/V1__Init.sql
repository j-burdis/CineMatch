DROP TABLE IF EXISTS ColourPalette;
DROP TABLE IF EXISTS Movies;

CREATE TABLE Movies (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(250) NOT NULL,
  release_date INT,
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
  colour_5 VARCHAR(50)
);