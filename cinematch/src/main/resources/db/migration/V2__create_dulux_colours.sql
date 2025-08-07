DROP TABLE IF EXISTS dulux_colours;

CREATE TABLE dulux_colours (
    id BIGSERIAL PRIMARY KEY,
    colour_name VARCHAR(100),
    hex_code VARCHAR(10),
    rgb_red INTEGER,
    rgb_green INTEGER,
    rgb_blue INTEGER
);

CREATE INDEX idx_colour_name ON dulux_colours(colour_name);
