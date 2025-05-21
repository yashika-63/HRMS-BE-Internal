package com.spectrum.PerformanceManagement.Feedback.service;


import org.springframework.stereotype.Service;




@Service
public class FeedbackAIService {

    // @Value("${openai.api.key}")
    // private String openAiApiKey;

    // private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    // private static final int MAX_RETRIES = 3; // Number of retries
    // private static final int RETRY_DELAY_MS = 5000; // 5 seconds delay

    // public List<String> generateFeedbackQuestions(String feedbackDescription) {
    //     List<String> questions = new ArrayList<>();
    //     try {
    //         RestTemplate restTemplate = new RestTemplate();
    //         ObjectMapper objectMapper = new ObjectMapper();
    
    //         // OpenAI request body
    //         String requestBody = "{"
    //                 + "\"model\": \"gpt-3.5-turbo\","
    //                 + "\"messages\": [{\"role\": \"user\", \"content\": \"Generate 5 specific feedback questions based on this description: "
    //                 + feedbackDescription + "\"}],"
    //                 + "\"temperature\": 0.7"
    //                 + "}";
    
    //         // Call OpenAI API
    //         String response = restTemplate.postForObject(
    //                 OPENAI_URL,
    //                 new HttpEntity<>(requestBody, createHeaders()),
    //                 String.class
    //         );
    
    //         // Parse JSON response
    //         JsonNode root = objectMapper.readTree(response);
    //         JsonNode choices = root.path("choices");
    
    //         if (choices.isArray() && choices.size() > 0) {
    //             String generatedText = choices.get(0).path("message").path("content").asText();
    
    //             // Extract and split questions
    //             String[] splitQuestions = generatedText.split("\n");
    //             for (String question : splitQuestions) {
    //                 if (!question.trim().isEmpty()) {
    //                     questions.add(question.trim());
    //                 }
    //             }
    //         } else {
    //             questions.add("No questions generated. Please try again.");
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         questions.add("Error generating questions. Please try again.");
    //     }
    
    //     // Ensure a result is always returned
    //     return questions;
    // }
    

    // private HttpHeaders createHeaders() {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.set("Authorization", "Bearer " + openAiApiKey);
    //     headers.setContentType(MediaType.APPLICATION_JSON);
    //     return headers;
    // }
            

 
    // @Value("${gemei.api.key}")
    // private String gemeiApiKey;

    // @Value("${gemei.api.url}")
    // private String gemeiApiUrl;

    // private final RestTemplate restTemplate;

    // public FeedbackAIService(RestTemplate restTemplate) {
    //     this.restTemplate = restTemplate;
    // }

    // public String generateFeedbackQuestions(String prompt) {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.set("Authorization", "Bearer " + gemeiApiKey);
    //     headers.set("Content-Type", "application/json");

    //     Map<String, String> requestBody = new HashMap<>();
    //     requestBody.put("prompt", prompt);

    //     HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

    //     ResponseEntity<String> response = restTemplate.exchange(
    //             gemeiApiUrl, HttpMethod.POST, entity, String.class);

    //     return response.getBody();
    // }
    
}
