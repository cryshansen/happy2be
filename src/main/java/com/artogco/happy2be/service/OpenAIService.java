package com.artogco.happy2be.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;



@Service
public class OpenAIService {
    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String openAiApiKey;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";



    @PostConstruct
    public void init() {
        //System.out.println("🔑 OpenAI API Key: " + openAiApiKey);
    }
/* 
    public OpenAIService() {
        System.out.println("🔑 Using API Key: " + openAiApiKey);
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
*/
    // ✅ Inject API key via constructor
    public OpenAIService(@Value("${openai.api.key}") String openAiApiKey, WebClient.Builder webClientBuilder) {
        this.openAiApiKey = openAiApiKey;

        System.out.println("🔑 Using API Key: " + this.openAiApiKey); // Should print correctly now

        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1") 
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.openAiApiKey)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();


    }

    public Mono<String> getAiHappy(String inmessage) {
        //String prompt = "Give me a fortune about " + category + ".";
        String prompt = "Analyze the following message for anxiety or depression, and provide a supportive response: " + (inmessage != null && !inmessage.isBlank() ? inmessage : "happiness") + ".";
        

       return webClient.post()
            .uri("/chat/completions")
            .bodyValue(Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", List.of(Map.of("role", "user", "content", prompt)),
                    "max_tokens", 250
            ))
            .retrieve()
            .bodyToMono(Map.class)
            .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)) // 🔄 Retries up to 3 times with a 5s delay
            .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests))
            .map(response -> {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                    

                    return (String) message.get("content");
                }
                return "No help available right now.";
            });



     }

    //sentiment block called by analyzeSentiment
     public Mono<String> callAiHappy(String inmessage) {
        //String prompt = "Give me a fortune about " + category + ".";
       // String prompt = "Analyze the following message for anxiety or depression, and provide a supportive response: " + (inmessage != null && !inmessage.isBlank() ? inmessage : "happiness") + ".";
        //ANXIETY_WORDS = ["anxious", "worried", "stressed", "nervous", "overwhelmed"]
        //DEPRESSION_WORDS = ["sad", "hopeless", "empty", "tired", "lost", "worthless"]
 
       //below explicitly asks for  seniment detection
        String prompt = """
            Analyze  the following message and determine if it reflects anxiety, depression, or positive emotions. 
            Then, generate a context-aware and supportive response based on the detected sentiment. 

            Message: "%s"

            Provide a brief sentiment analysis result and a supportive response.
            """.formatted(inmessage != null && !inmessage.isBlank() ? inmessage : "happiness");

       return webClient.post()
            .uri("/chat/completions")
            .bodyValue(Map.of(
                    "model", "gpt-4-turbo",//free doesnt allow this.
                    "messages", List.of(Map.of("role", "user", "content", prompt)),
                    "max_tokens", 250
            ))
            .retrieve()
            .bodyToMono(Map.class)
            .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)) // 🔄 Retries up to 3 times with a 5s delay
            .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests))
            .map(response -> {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                    

                    return (String) message.get("content");
                }
                return "No help available right now.";
            });



     }


    
    public String callOpenAI(String prompt) {
        // Create an HTTP client (RestTemplate)
        RestTemplate restTemplate = new RestTemplate();

        // Create the headers and add the Authorization
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + this.openAiApiKey);

        // Construct the body of the request
        String body = "{"
            + "\"model\": \"text-davinci-003\","
            + "\"prompt\": \"" + prompt + "\","
            + "\"max_tokens\": 100"
            + "}";

        // Wrap the body and headers into an HttpEntity object
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Send the POST request and get the response
        ResponseEntity<String> response = restTemplate.exchange(
            OPENAI_API_URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }





}

