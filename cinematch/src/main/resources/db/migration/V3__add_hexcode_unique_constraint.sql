ALTER TABLE dulux_colours
ADD CONSTRAINT unique_hex_code UNIQUE (hex_code);

ALTER TABLE dulux_colours
ADD CONSTRAINT unique_colour_name UNIQUE (colour_name);
