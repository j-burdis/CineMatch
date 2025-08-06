package ImageIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.Map.Entry;

public class ImageTester {

public static void main(String[] args) {
    try {
        String imageUrl = "https://image.tmdb.org/t/p/w200/2CAL2433ZeIihfX1Hb2139CX0pW.jpg";

        BufferedImage image = ImageIO.read(new URL(imageUrl));
        if(image == null){
            System.out.println("Failed to load image.");
            return;
        }

        Map<Integer, Integer> colorCount = new HashMap<>();

        int width = image.getWidth();
        int height = image.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                colorCount.put(rgb, colorCount.getOrDefault(rgb, 0) + 1);


            }
        }


        int dominantColorRGB = colorCount.entrySet()
                .stream()
                .max(Comparator.comparingInt(Entry::getValue))
                .map(Entry::getKey)
                .orElse(0);


        String hex = rgbToHex(dominantColorRGB);
        System.out.println("Most dominant color : #" + hex);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private static String rgbToHex(int rgb) {
        int red   = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue  = rgb & 0xFF;
        return String.format("%02x%02x%02x", red, green, blue);
    }
}
