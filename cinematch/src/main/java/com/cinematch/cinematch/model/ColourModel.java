package com.cinematch.cinematch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "colourpalette")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColourModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

//    primary palette
    @Column
    private String colour_1;

    @Column
    private String colour_2;

    @Column
    private String colour_3;

    @Column
    private String colour_4;

    @Column
    private String colour_5;

    @Column
    private String colour_6;

    @Column
    private String colour_7;

    @Column
    private String colour_8;

    @Column
    private String colour_9;

    @Column
    private String colour_10;

    @Column
    private String colour_11;

    @Column
    private String colour_12;

//    secondary palette
    @Column
    private String secondary_colour_1;

    @Column
    private String secondary_colour_2;

    @Column
    private String secondary_colour_3;

    @Column
    private String secondary_colour_4;

    @Column
    private String secondary_colour_5;

    @Column
    private String secondary_colour_6;

    @Column
    private String secondary_colour_7;

    @Column
    private String secondary_colour_8;

    @Column
    private String secondary_colour_9;

    @Column
    private String secondary_colour_10;

    @Column
    private String secondary_colour_11;

    @Column
    private String secondary_colour_12;
}