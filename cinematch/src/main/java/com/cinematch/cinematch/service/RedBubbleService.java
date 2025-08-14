package com.cinematch.cinematch.service;

import org.springframework.stereotype.Service;

@Service
public class RedBubbleService {

    public String getSearchUrl(String movieTitle) {
        // Create RedBubble search URL
        return "https://www.redbubble.com/shop?query=" +
                movieTitle.replace(" ", "%20") +
                "&ref=search_box";
    }

    public String getSearchText(String movieTitle) {
        return "Find " + movieTitle + " merchandise on RedBubble";
    }
}
