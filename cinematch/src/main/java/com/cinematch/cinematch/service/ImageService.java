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
                    int[] rgbA = getRGBArray(rgb);
                    if (!isGray(rgbA))
                        colourCount.put(rgb, colourCount.getOrDefault(rgb, 0) + 1);
                }
            }

            int dominantColorRGB = colourCount.entrySet()
                    .stream()
                    .max(Comparator.comparingInt(Entry::getValue))
                    .map(Entry::getKey)
                    .orElse(0);


            String hex = rgbToHex(dominantColorRGB);
            return ("#" + hex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
}