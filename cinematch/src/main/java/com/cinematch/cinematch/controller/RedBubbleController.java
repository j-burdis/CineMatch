package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.service.RedBubbleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/redbubble")
public class RedBubbleController {

    @Autowired
    private RedBubbleService RedBubbleService;

    @GetMapping("/search")
    public ResponseEntity<Map<String, String>> getSearchInfo(@RequestParam String query) {
        try {
            Map<String, String> response = new HashMap<>();
            response.put("searchUrl", RedBubbleService.getSearchUrl(query));
            response.put("searchText", RedBubbleService.getSearchText(query));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in RedBubble controller: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
