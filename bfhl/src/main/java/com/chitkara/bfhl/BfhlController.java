package com.chitkara.bfhl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;


@RestController
@CrossOrigin(origins = "*")
public class BfhlController {

    private static final String YOUR_EMAIL = "amisha0276.be23@chitkara.edu.in";
    private static final String GEMINI_API_KEY = "AIzaSyCuobWsuWdwUmiL7A_1CB0D7BbUEuzK5oI"; // ⚠️ ADD YOUR KEY


    // ✅ GET /health
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("is_success", true);
        response.put("official_email", "amisha@chitkara.edu.in");
        return ResponseEntity.ok(response);
    }

    // ✅ POST /bfhl
    @PostMapping("/bfhl")
    public ResponseEntity<Map<String, Object>> bfhl(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            // Validate that exactly one key exists
            if (request.isEmpty()) {
                response.put("is_success", false);
                response.put("official_email", "amisha@chitkara.edu.in");
                response.put("error", "Request body cannot be empty");
                return ResponseEntity.badRequest().body(response);
            }

            // FIBONACCI
            if (request.containsKey("fibonacci")) {
                int n = ((Number) request.get("fibonacci")).intValue();
                if (n < 0) {
                    response.put("is_success", false);
                    response.put("official_email", "amisha@chitkara.edu.in");
                    response.put("error", "Fibonacci input must be non-negative");
                    return ResponseEntity.badRequest().body(response);
                }
                response.put("is_success", true);
                response.put("official_email","amisha@chitkara.edu.in");
                response.put("data", fibonacci(n));
                return ResponseEntity.ok(response);
            }

            // PRIME
            if (request.containsKey("prime")) {
                List<Integer> numbers = convertToList(request.get("prime"));
                if (numbers.isEmpty()) {
                    response.put("is_success", false);
                    response.put("official_email", "amisha@chitkara.edu.in");
                    response.put("error", "Prime array cannot be empty");
                    return ResponseEntity.badRequest().body(response);
                }
                List<Integer> primes = new ArrayList<>();
                for (int num : numbers) {
                    if (isPrime(num)) primes.add(num);
                }
                response.put("is_success", true);
                response.put("official_email","amisha@chitkara.edu.in");
                response.put("data", primes);
                return ResponseEntity.ok(response);
            }

            // LCM
            if (request.containsKey("lcm")) {
                List<Integer> numbers = convertToList(request.get("lcm"));
                if (numbers.isEmpty()) {
                    response.put("is_success", false);
                    response.put("official_email", "amisha@chitkara.edu.in");
                    response.put("error", "LCM array cannot be empty");
                    return ResponseEntity.badRequest().body(response);
                }
                long result = numbers.get(0);
                for (int i = 1; i < numbers.size(); i++) {
                    result = lcm(result, numbers.get(i));
                }
                response.put("is_success", true);
                response.put("official_email", "amisha@chitkara.edu.in");
                response.put("data", result);
                return ResponseEntity.ok(response);
            }

            // HCF
            if (request.containsKey("hcf")) {
                List<Integer> numbers = convertToList(request.get("hcf"));
                if (numbers.isEmpty()) {
                    response.put("is_success", false);
                    response.put("official_email", "amisha@chitkara.edu.in");
                    response.put("error", "HCF array cannot be empty");
                    return ResponseEntity.badRequest().body(response);
                }
                long result = numbers.get(0);
                for (int i = 1; i < numbers.size(); i++) {
                    result = gcd(result, numbers.get(i));
                }
                response.put("is_success", true);
                response.put("official_email","amisha@chitkara.edu.in");
                response.put("data", result);
                return ResponseEntity.ok(response);
            }

            // AI
            if (request.containsKey("AI")) {
                String question = (String) request.get("AI");
                if (question == null || question.trim().isEmpty()) {
                    response.put("is_success", false);
                    response.put("official_email", "amisha@chitkara.edu.in");
                    response.put("error", "AI question cannot be empty");
                    return ResponseEntity.badRequest().body(response);
                }
                String answer = askGemini(question);
                response.put("is_success", true);
                response.put("official_email", "amisha@chitkara.edu.in");
                response.put("data", answer);
                return ResponseEntity.ok(response);
            }

            // No valid key found
            response.put("is_success", false);
            response.put("official_email", "amisha@chitkara.edu.in");
            response.put("error", "Invalid request key. Must be one of: fibonacci, prime, lcm, hcf, AI");
            return ResponseEntity.badRequest().body(response);

        } catch (ClassCastException e) {
            response.put("is_success", false);
            response.put("official_email", "amisha@chitkara.edu.in");
            response.put("error", "Invalid input type: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("is_success", false);
            response.put("official_email", "amisha@chitkara.edu.in");
            response.put("error", "Internal error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ========== MATH LOGIC ==========

    private List<Integer> fibonacci(int n) {
        List<Integer> series = new ArrayList<>();
        if (n <= 0) return series;
        int a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            series.add(a);
            int temp = a + b;
            a = b;
            b = temp;
        }
        return series;
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private long lcm(long a, long b) {
        return Math.abs(a * b) / gcd(a, b);
    }

    private long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private List<Integer> convertToList(Object obj) {
        List<Integer> list = new ArrayList<>();
        if (obj instanceof List) {
            for (Object item : (List<?>) obj) {
                list.add(((Number) item).intValue());
            }
        }
        return list;
    }

    // ========== GEMINI AI ==========

    private String askGemini(String question) throws Exception {
        if (GEMINI_API_KEY.equals("AIzaSyCuobWsuWdwUmiL7A_1CB0D7BbUEuzK5oI") || GEMINI_API_KEY.isEmpty()) {
            return "API_KEY_NOT_CONFIGURED";
        }

        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        String prompt = "Answer this question in exactly ONE word only, no punctuation, no explanation: " + question;

        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, Object> content = new LinkedHashMap<>();
        Map<String, Object> part = new LinkedHashMap<>();
        part.put("text", prompt);
        content.put("parts", List.of(part));
        body.put("contents", List.of(content));

        String jsonBody = mapper.writeValueAsString(body);

        Request httpRequest = new Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent?key=" + GEMINI_API_KEY)
                .post(okhttp3.RequestBody.create(jsonBody, MediaType.get("application/json")))
                .build();

        try (Response httpResponse = client.newCall(httpRequest).execute()) {
            if (!httpResponse.isSuccessful()) {
                throw new Exception("Gemini API error: " + httpResponse.code());
            }
            String responseBody = httpResponse.body().string();
            JsonNode root = mapper.readTree(responseBody);
            String answer = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText().trim();

            // Extract first word only
            return answer.split("\\s+")[0].replaceAll("[^a-zA-Z0-9]", "");
        }
    }
}
