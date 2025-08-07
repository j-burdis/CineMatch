package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.DuluxColour;
import com.cinematch.cinematch.repository.DuluxColourRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColourScraperService {

    private final DuluxColourRepository repository;

    public void scrapeColors() throws IOException {
        String baseUrl = "https://www.dulux.co.uk/en/colour-details";
        List<String> hues = List.of(
                "",
                "Red", "Orange", "Gold", "Yellow", "Lime",
                "Green", "Teal", "Blue", "Violet", "Cool%20Neutral", "Warm%20Neutral"
        );

        for (String hue : hues) {
            String url = hue.isEmpty()
                    ? baseUrl
                    : baseUrl + "/filters/h_" + hue;

            Document doc = Jsoup.connect(url).get();
            Elements cards = doc.select(".color-card");

            for (Element card : cards) {
                String style = card.attr("style");
                String hex = null;

                if (style.contains("#")) {
                    hex = style.substring(style.indexOf("#")).trim();
                }

                if (hex != null) {
                    if (repository.existsByHexCode(hex)) {
                        continue;
                    }

                    Color color = Color.decode(hex);
                    int r = color.getRed();
                    int g = color.getGreen();
                    int b = color.getBlue();

                    Element labelEl = card.selectFirst(".color-card-label");
                    String colourName = labelEl != null ? labelEl.text().trim() : "Unknown";

                    DuluxColour colour = DuluxColour.builder()
                            .colourName(colourName)
                            .hexCode(hex)
                            .rgbRed(r)
                            .rgbGreen(g)
                            .rgbBlue(b)
                            .build();

                    repository.save(colour);
                }
            }
        }
    }
}
