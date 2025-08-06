package com.cinematch.cinematch.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ColourDTO {
    private String mode;
    private int count;

    private List<Color> colors;

    @Data
    public static class Color {
        private Hex hex;
    }

    @Data
    public static class Hex {
        private String value;
        private String clean;
    }
}