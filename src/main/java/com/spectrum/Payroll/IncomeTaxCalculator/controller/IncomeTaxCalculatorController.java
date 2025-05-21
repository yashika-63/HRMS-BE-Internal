package com.spectrum.Payroll.IncomeTaxCalculator.controller;

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
@RequestMapping("/api/incomeTax")
public class IncomeTaxCalculatorController {

    @Value("${gemini.api.key}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    @PostMapping("/calculate-tds")
    public ResponseEntity<?> calculateTDSFromText(@RequestBody Map<String, String> request) {
        String userPrompt = request.get("prompt");

        if (userPrompt == null || userPrompt.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Prompt is required.");
        }

        // Build more deterministic and strict prompt
        String prompt = """
Based on the following salary and tax declaration details, calculate the **exact yearly TDS** for the employee.

Use the **Old Tax Regime** for FY 2024-25 as per Indian income tax slabs. Include:
- ₹50,000 standard deduction
- 80C deductions up to ₹1,50,000 (PF, LIC, etc.)
- Other exemptions (HRA, 80D, 80G, etc.)
- Rebate under Section 87A if applicable (₹12,500 for income ≤ ₹5L)
- 4% cess on total tax
- 30% surcharge for income above ₹50 lakh

Follow these slabs:
- Up to ₹2,50,000 – Nil
- ₹2,50,001 to ₹5,00,000 – 5%
- ₹5,00,001 to ₹10,00,000 – 20%
- Above ₹10,00,000 – 30%

Return the result in **strict JSON format without any markdown or explanation** like this:
{
  "grossIncome": <number> ,
  "totalExemptions": <number>,
  "totalDeductions": <number>,
  "taxableIncome": <number>,
  "incomeTax": <number>,
  "surcharge": <number>,
  "cess": <number>,
  "rebate": <number>,
  "totalTDS": <number>,
 
}
Do not include backticks, code fences, or markdown. Just pure JSON.
Now calculate based on:
""" + userPrompt;

        Map<String, Object> content = new HashMap<>();
        content.put("parts", new Object[]{Map.of("text", prompt)});
        content.put("role", "user");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", new Object[]{content});
        requestBody.put("generationConfig", Map.of("temperature", 0)); // Force deterministic output

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String apiUrlWithKey = GEMINI_API_URL + "?key=" + apiToken;
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrlWithKey, requestEntity, String.class);

            // Extract only the ₹ amount string from response
            JsonNode resultNode = extractTDSJson(response.getBody());

            if (resultNode == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to parse TDS response.");
            }
            
            return ResponseEntity.ok(resultNode);  // Return full parsed JSON
            

        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body("API Error: " + ex.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error: " + e.getMessage());
        }
    }

    // Extract "₹XXXX" from Gemini JSON response
    private JsonNode extractTDSJson(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            String rawText = root.path("candidates").get(0)
                                 .path("content").path("parts").get(0)
                                 .path("text").asText().trim();
    
            // Clean up if JSON comes with code fences
            if (rawText.startsWith("```json")) {
                rawText = rawText.replaceAll("```json", "")
                                 .replaceAll("```", "").trim();
            }
    
            return mapper.readTree(rawText); // Returns the parsed JSON node
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    






    ///
    @PostMapping("/calculate-tds-new")
public ResponseEntity<?> calculateTDSFromTextNewRegime(@RequestBody Map<String, String> request) {
    String userPrompt = request.get("prompt");

    if (userPrompt == null || userPrompt.trim().isEmpty()) {
        return ResponseEntity.badRequest().body("Prompt is required.");
    }

    // Strict prompt for new tax regime
    String prompt = """
Based on the following salary and tax declaration details, calculate the **exact yearly TDS** for the employee.

Use the **New Tax Regime** for FY 2024-25 as per Indian income tax slabs. Consider:
- ₹50,000 standard deduction
- No exemptions under 80C, HRA, etc.
- ₹15,000 deduction for family pension (if applicable)
- Section 87A rebate (₹12,500 if taxable income ≤ ₹7L)
- 4% cess on total tax
- Surcharge: 10% for income > ₹50L, 15% for > ₹1Cr, 25% for > ₹2Cr, 37% for > ₹5Cr

Slabs for New Regime:
- Up to ₹3,00,000 – Nil
- ₹3,00,001 to ₹6,00,000 – 5%
- ₹6,00,001 to ₹9,00,000 – 10%
- ₹9,00,001 to ₹12,00,000 – 15%
- ₹12,00,001 to ₹15,00,000 – 20%
- Above ₹15,00,000 – 30%

Return the result in **strict JSON format without any markdown or explanation** like this:
{
  "grossIncome": <number>,
  "totalExemptions": <number>,
  "totalDeductions": <number>,
  "taxableIncome": <number>,
  "incomeTax": <number>,
  "surcharge": <number>,
  "cess": <number>,
  "rebate": <number>,
  "totalTDS": <number>
}
Do not include backticks, code fences, or markdown. Just pure JSON.
Now calculate based on:
""" + userPrompt;

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
        JsonNode resultNode = extractTDSJson(response.getBody());

        if (resultNode == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to parse TDS response.");
        }

        return ResponseEntity.ok(resultNode);

    } catch (HttpStatusCodeException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body("API Error: " + ex.getResponseBodyAsString());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error: " + e.getMessage());
    }
}

}















