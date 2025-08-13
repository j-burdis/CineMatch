package com.cinematch.cinematch.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "paintmatches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaintMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    private String paint_1;
    private String paint_2;
    private String paint_3;
    private String paint_4;
    private String paint_5;
    private String paint_6;
    private String paint_7;
    private String paint_8;
    private String paint_9;
    private String paint_10;
    private String paint_11;
    private String paint_12;

    // Secondary matches
    private String secondary_paint_1;
    private String secondary_paint_2;
    private String secondary_paint_3;
    private String secondary_paint_4;
    private String secondary_paint_5;
    private String secondary_paint_6;
    private String secondary_paint_7;
    private String secondary_paint_8;
    private String secondary_paint_9;
    private String secondary_paint_10;
    private String secondary_paint_11;
    private String secondary_paint_12;
}
