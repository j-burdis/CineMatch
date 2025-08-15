package com.cinematch.cinematch.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageService {
//    main public methods
    public String getDominantColour(String imageUrl) {
        List<Map.Entry<Integer, Integer>> topColors = getTopColors(imageUrl, null, 0, 1);
        return topColors.isEmpty() ? null : "#" + rgbToHex(topColors.get(0).getKey());
    }

    public String getSecondaryColour(String imageUrl, String dominantHex) {
        int[] dominantRGB = hexToRGB(dominantHex);
        // minimum distance for distinct colors
        int minDistance = 100;

        // Get multiple top colors and find the most different one
        List<Map.Entry<Integer, Integer>> topColors = getTopColors(imageUrl, dominantRGB, minDistance, 10);

        if (topColors.isEmpty()) {
//             try with looser criteria if no colors found with strict distance above
            topColors = getTopColors(imageUrl, dominantRGB, 75, 10);
        }

        if (topColors.isEmpty()) {
            return null;
        }

        // Find the colour most different from dominant using perceptual color difference
        int[] bestColor = null;
        double maxDifference = 0;

        for (Map.Entry<Integer, Integer> entry : topColors) {
            int[] candidateRGB = getRGBArray(entry.getKey());
            double perceptualDiff = perceptualColorDistance(dominantRGB, candidateRGB);

            if (perceptualDiff > maxDifference) {
                maxDifference = perceptualDiff;
                bestColor = candidateRGB;
            }
        }

        return bestColor != null ? "#" + rgbToHex(bestColor) : null;
    }

//    core image processing
    private List<Map.Entry<Integer, Integer>> getTopColors(String imageUrl, int[] excludeRGB, int minDistance, int count) {
        try {
            BufferedImage image = ImageIO.read(new URL(imageUrl));
            if (image == null) {
                System.out.println("Failed to load image.");
                return Collections.emptyList();
            }

            Map<Integer, Integer> colourCount = new HashMap<>();
            int width = image.getWidth();
            int height = image.getHeight();

            // Sample every nth pixel for better performance on large images
            int sampleRate = Math.max(1, Math.min(width, height) / 200);

            for (int x = 0; x < width; x += sampleRate) {
                for (int y = 0; y < height; y += sampleRate) {
                    int rgb = image.getRGB(x, y);
                    int[] rgbArr = getRGBArray(rgb);

                    // Skip very dark colors (likely shadows/black areas)
                    if (isDarkColor(rgbArr)) {
                        continue;
                    }

                    // Skip very light colors (likely overexposed/white areas)
                    if (isLightColor(rgbArr)) {
                        continue;
                    }

                    // Skip grays with tighter tolerance
                    if (isGray(rgbArr, 20)) {
                        continue;
                    }

                    // Skip if too close to excluded colour
                    if (excludeRGB != null && colourDistance(rgbArr, excludeRGB) <= minDistance) {
                        continue;
                    }

                    // Quantize colors to reduce noise (group similar colors together)
                    int quantizedRGB = quantizeColor(rgb);
                    colourCount.put(quantizedRGB, colourCount.getOrDefault(quantizedRGB, 0) + 1);
                }
            }

            return colourCount.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                    .limit(count)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Quantize color to reduce noise and group similar colors
    private int quantizeColor(int rgb) {
        int[] rgbArr = getRGBArray(rgb);
        int quantizeFactor = 16; // Group colors in chunks of 16

        int quantizedR = (rgbArr[0] / quantizeFactor) * quantizeFactor;
        int quantizedG = (rgbArr[1] / quantizeFactor) * quantizeFactor;
        int quantizedB = (rgbArr[2] / quantizeFactor) * quantizeFactor;

        return (quantizedR << 16) | (quantizedG << 8) | quantizedB;
    }

//    colour filtering methods
    // Check if color is too dark
    private boolean isDarkColor(int[] rgb) {
        int brightness = (rgb[0] + rgb[1] + rgb[2]) / 3;
        return brightness < 30; // Threshold for darkness
    }

    // Check if color is too light
    private boolean isLightColor(int[] rgb) {
        int brightness = (rgb[0] + rgb[1] + rgb[2]) / 3;
        return brightness > 240; // Threshold for lightness
    }

    // Overloaded method with custom tolerance
    public static boolean isGray(int[] rgbArr, int tolerance) {
        int red = rgbArr[0];
        int green = rgbArr[1];
        int blue = rgbArr[2];

        int rgDiff = Math.abs(red - green);
        int gbDiff = Math.abs(green - blue);
        int rbDiff = Math.abs(red - blue);

        return rgDiff < tolerance && gbDiff < tolerance && rbDiff < tolerance;
    }

    // original method for backward compatibility
    public static boolean isGray(int[] rgbArr) {
        return isGray(rgbArr, 25);
    }

//    colour analysis utilities
    // perceptual color distance using weighted RGB (human eye sensitivity)
    private double perceptualColorDistance(int[] c1, int[] c2) {
        // Human eye is more sensitive to green, less to blue
        double deltaR = c1[0] - c2[0];
        double deltaG = c1[1] - c2[1];
        double deltaB = c1[2] - c2[2];

        // Weighted Euclidean distance
        return Math.sqrt(2 * deltaR * deltaR + 4 * deltaG * deltaG + 3 * deltaB * deltaB);
    }

    private static double colourDistance(int[] c1, int[] c2) {
        return Math.sqrt(
                Math.pow(c1[0] - c2[0], 2) +
                        Math.pow(c1[1] - c2[1], 2) +
                        Math.pow(c1[2] - c2[2], 2)
        );
    }

//    Colour conversion methods
    private static int[] getRGBArray(int rgb) {
        int red   = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue  = rgb & 0xFF;
        return new int[]{red, green, blue};
    }

    private static String rgbToHex(int rgb) {
        int red   = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue  = rgb & 0xFF;
        return String.format("%02x%02x%02x", red, green, blue);
    }

    private static String rgbToHex(int[] rgb) {
        return String.format("%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }

    private static int[] hexToRGB(String hex) {
        hex = hex.replace("#", "");
        int red   = Integer.parseInt(hex.substring(0, 2), 16);
        int green = Integer.parseInt(hex.substring(2, 4), 16);
        int blue  = Integer.parseInt(hex.substring(4, 6), 16);
        return new int[]{red, green, blue};
    }
}