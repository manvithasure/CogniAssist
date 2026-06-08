package com.cogniassist.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiAIService {

    @Value("${groq.api.key}")
    private String apiKey;

    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    public String analyzeFatigue(int keystrokeCount,
                                 double typingSpeed, int errorCount) {
        try {
            OkHttpClient client = new OkHttpClient();

            String prompt = String.format(
                    "You are a cognitive fatigue analyzer. " +
                            "Based on these typing metrics, classify fatigue as " +
                            "exactly one word: LOW, MEDIUM, or HIGH.\n\n" +
                            "Metrics:\n" +
                            "- Keystrokes: %d\n" +
                            "- Typing Speed: %.1f keys/min\n" +
                            "- Errors: %d\n\n" +
                            "Reply with only one word: LOW, MEDIUM, or HIGH.",
                    keystrokeCount, typingSpeed, errorCount
            );

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "llama-3.3-70b-versatile");

            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messages.put(message);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 10);

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(GROQ_URL)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            System.out.println("🔍 Groq Response: " + responseBody);

            JSONObject jsonResponse = new JSONObject(responseBody);
            String result = jsonResponse
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim()
                    .toUpperCase();

            if (result.contains("LOW")) return "LOW";
            if (result.contains("MEDIUM")) return "MEDIUM";
            if (result.contains("HIGH")) return "HIGH";
            return "MEDIUM";

        } catch (Exception e) {
            System.out.println("❌ Groq API error: " + e.getMessage());
            return "MEDIUM";
        }
    }
}