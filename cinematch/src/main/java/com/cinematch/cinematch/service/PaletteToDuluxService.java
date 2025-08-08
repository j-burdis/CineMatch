package com.cinematch.cinematch.service;
import com.cinematch.cinematch.model.DuluxColour;
import com.cinematch.cinematch.repository.DuluxColourRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaletteToDuluxService {

    private final DuluxColourRepository duluxColourRepository;

    public PaletteToDuluxService(DuluxColourRepository duluxColourRepository) {
        this.duluxColourRepository = duluxColourRepository;
    }

    public List<DuluxColour> getClosestPaintMatches(List<String> paletteHexCodes) {
        List<DuluxColour> paintColours = duluxColourRepository.findAll();
        List<Integer> indices = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11); // 1st, 5th, 9th, 12th
        List<DuluxColour> matches = new ArrayList<>();
        for (int i : indices) {
            String hex = paletteHexCodes.get(i);
            DuluxColour closest = findClosestPaintMatch(hex, paintColours);
            matches.add(closest);
        }
        return matches;
    }
    private DuluxColour findClosestPaintMatch(String hexColour, List<DuluxColour> paintColours) {
        int[] rgb = hexToRGB(hexColour);
        DuluxColour closest = null;
        double minDistance = Double.MAX_VALUE;
        for (DuluxColour paint : paintColours) {
            double distance = Math.sqrt(
                    Math.pow(rgb[0] - paint.getRgbRed(), 2) +
                            Math.pow(rgb[1] - paint.getRgbGreen() ,2) +
                            Math.pow(rgb[2] - paint.getRgbBlue() ,2)
            );
            if (distance < minDistance) {
                minDistance = distance;
                closest = paint;
            }
        }
        return closest;
    }
    private int[] hexToRGB(String hex) {
        hex = hex.replace("#", "");
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new int[] { r, g, b };
    }
}