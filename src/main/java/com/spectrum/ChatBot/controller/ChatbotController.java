package com.spectrum.ChatBot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Value("${gemini.api.key}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    @PostMapping("/ask")
    public ResponseEntity<?> askChatbot(@RequestBody Map<String, String> request) {
        String Prompt = request.get("prompt");
        String predefinedPrompt = "for following ask question choose appropriate api or page direction mention below pass data in that api and give appropriate responce and if not related to that show irrelevent data of ask appropriate question  .\n";
        String prompt = predefinedPrompt + Prompt; // Combine predefined
       
        if (prompt == null || prompt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Prompt is required.");
        }
    
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", new Object[]{
                Map.of("parts", new Object[]{
                        Map.of("text", prompt)
                })
        });
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        String apiUrlWithKey = GEMINI_API_URL + "?key=" + apiToken;
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
    
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrlWithKey, requestEntity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body("API Error: " + ex.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error: " + e.getMessage());
        }
    }
}    