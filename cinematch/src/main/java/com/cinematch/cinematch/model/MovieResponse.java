package com.cinematch.cinematch.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieResponse {
    private List<ApiMovie> results;
}