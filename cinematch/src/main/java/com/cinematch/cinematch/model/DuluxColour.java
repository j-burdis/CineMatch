package com.cinematch.cinematch.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dulux_colours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuluxColour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String colourName;

    @Column(unique = true)
    private String hexCode;

    private Integer rgbRed;
    private Integer rgbGreen;
    private Integer rgbBlue;

    public String getPurchaseUrl() {
        if (colourName == null || colourName.trim().isEmpty()) {
            return null;
        }

        String urlSlug = colourName.toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("\\s*\\d+$", "");

        return "https://www.dulux.co.uk/en/colour-details/" + urlSlug;
    }
}
