package com.spectrum.Recruitment.JobDescription.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiRecruitmentController {

    @Value("${gemini.api.key}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    @PostMapping("/shortlist-candidates")
    public ResponseEntity<?> shortlistCandidates(@RequestBody Map<String, Object> request) {
        Object candidateData = request.get("candidates");
        String target = (String) request.get("requirement");
        Integer count = (Integer) request.get("topN");

        if (candidateData == null || target == null || count == null) {
            return ResponseEntity.badRequest().body("Required fields: candidates, requirement, topN");
        }

        String prompt = """
            You are an AI recruiter. You are given a list of candidates and a job requirement.
            
            Task: Select the top %d most suitable candidates from the list based on the requirement below dont do exact match give most relevent as well try to match demand .
            
            Return the top %d shortlisted candidates **in JSON array format only** (no markdown, no text, no explanation), exactly as received from the input list.
            
            Job Requirement:
            %s
            
            Candidates List (JSON):
            %s
            """.formatted(count, count, target, candidateData.toString());
            

        Map<String, Object> content = new HashMap<>();
        content.put("parts", new Object[]{Map.of("text", prompt)});
        content.put("role", "user");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", new Object[]{content});
        requestBody.put("generationConfig", Map.of("temperature", 0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String apiUrlWithKey = GEMINI_API_URL + "?key=" + apiToken;
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrlWithKey, requestEntity, String.class);
            JsonNode resultNode = extractJsonFromResponse(response.getBody());

            if (resultNode == null || !resultNode.isArray()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to extract candidate list.");
            }

            return ResponseEntity.ok(resultNode);

        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body("API Error: " + ex.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error: " + e.getMessage());
        }
    }

    // Reusable method to extract pure JSON from Gemini response
    private JsonNode extractJsonFromResponse(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            String rawText = root.path("candidates").get(0)
                                 .path("content").path("parts").get(0)
                                 .path("text").asText().trim();

            if (rawText.startsWith("```json")) {
                rawText = rawText.replaceAll("```json", "")
                                 .replaceAll("```", "").trim();
            }

            return mapper.readTree(rawText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}