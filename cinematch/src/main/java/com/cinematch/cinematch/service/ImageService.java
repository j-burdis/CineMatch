package com.cinematch.cinematch.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.Map.Entry;
@Service
public class ImageService {

    public String getDominantColour(String imageUrl) {
        return getTopColour(imageUrl, null, 0);
    }

    public String getSecondaryColour(String imageUrl, String dominantHex) {
        int[] dominantRGB = hexToRGB(dominantHex);
        // Minimum Euclidean distance to be considered "different"
        int minDistance = 100;
        return getTopColour(imageUrl, dominantRGB, minDistance);
    }

    public String getTopColour(String imageUrl, int[] excludeRGB, int minDistance) {
        try {
            BufferedImage image = ImageIO.read(new URL(imageUrl));
            if (image == null) {
                System.out.println("Failed to load image.");
                return null;
            }

            Map<Integer, Integer> colourCount = new HashMap<>();
            int width = image.getWidth();
            int height = image.getHeight();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = image.getRGB(x, y);
                    int[] rgbArr = getRGBArray(rgb);

                    // skip grays
                    if (isGray(rgbArr)) {
                        continue;
                    }

                    // skip if too close to excluded colour
                    if (excludeRGB != null && colourDistance(rgbArr, excludeRGB) <= minDistance) {
                        continue;
                    }

                    // count occurrence
                    colourCount.put(rgb, colourCount.getOrDefault(rgb, 0) + 1);
                }
            }

            return colourCount.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .map(entry -> "#" + rgbToHex(entry.getKey()))
                    .orElse(null);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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

    private static int[] hexToRGB(String hex) {
        hex = hex.replace("#", "");
        int red   = Integer.parseInt(hex.substring(0, 2), 16);
        int green = Integer.parseInt(hex.substring(2, 4), 16);
        int blue  = Integer.parseInt(hex.substring(4, 6), 16);
        return new int[]{red, green, blue};
    }

    public static boolean isGray(int[] rgbArr){
        int red = rgbArr[0];
        int green = rgbArr[1];
        int blue = rgbArr[2];

        int rgDiff = Math.abs(red - green);
        int gbDiff = Math.abs(green - blue);
        int rbDiff = Math.abs(red - blue);

        int tolerance = 25;

        return rgDiff < tolerance && gbDiff < tolerance && rbDiff < tolerance;
    }

    private static double colourDistance(int[] c1, int[] c2) {
        return Math.sqrt(
                Math.pow(c1[0] - c2[0], 2) +
                        Math.pow(c1[1] - c2[1], 2) +
                        Math.pow(c1[2] - c2[2], 2)
        );
    }
}