package com.cinematch.cinematch.service;

import com.cinematch.cinematch.DTO.ColourDTO;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColourService {
    //set up web client
    private final WebClient webClient;


    public ColourService(WebClient webClient) {
        this.webClient = webClient;
    }

    //api call to colour api
    public List<String> getColours(String hex) {
        ColourDTO resp = webClient.get()
                .uri("https://www.thecolorapi.com/scheme?hex=" + hex +"&mode=monochrome&count=12")
                .retrieve()
                .bodyToMono(ColourDTO.class)
                .block();

        //handle empty response, returning empty list
        if (resp == null || resp.getColors() == null) {
            return List.of(); // return empty list if null
        }

        //return list of strings
        return resp.getColors().stream()
                .map(c -> c.getHex().getValue())
                .collect(Collectors.toList());
    }
}
