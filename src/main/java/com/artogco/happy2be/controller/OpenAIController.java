package com.artogco.happy2be.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.artogco.happy2be.service.OpenAIService;
import com.artogco.happy2be.service.SentimentAnalyzer;

@RestController
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private SentimentAnalyzer sentimentAIService;

    @GetMapping("/ask-happy")
    public String askOpenAI(@RequestParam String prompt) {
        return openAIService.callOpenAI(prompt);
    }


    @GetMapping("/happy-ai")
    public ResponseEntity<Map<String, String>> getAIHappy(@RequestParam(value = "category", defaultValue = "general") String category) {
        String fortune = openAIService.getAiHappy(category).block();
        //return openAIService.getAiFortune(category); // a string of the message called message
        Map<String, String> response = new HashMap<>();
        response.put("happy", fortune); // Wrap it in a JSON-friendly structure
         return ResponseEntity.ok(response);
    }

   public String analyzeSentiment(String message) {
        String sentimentPrompt = """
            Classify the following message as: Positive, Neutral, or Negative.
            
            Message: "%s"
            
            Return only one word (Positive, Neutral, or Negative).
        """.formatted(message);
        //String response = sentimentAIService.analyzeSentiment(sentimentPrompt);// openAIService.callAiHappy(sentimentPrompt).block();
        try {
                // Use your sentiment AI service to call the model
                String response = sentimentAIService.analyzeSentiment(sentimentPrompt);
                return response;  // Return the sentiment response (Positive/Negative/Neutral)
        /*} catch (ModelException e) {
                // Handle issues related to model loading or prediction
                System.err.println("Error loading the model or making predictions: " + e.getMessage());
                return "Error: Model failure";  // You can customize the error message
        } catch (IOException e) {
                // Handle I/O related issues like network errors or file issues
                System.err.println("I/O error occurred: " + e.getMessage());
                return "Error: I/O failure";
        } catch (TranslateException e) {
                // Handle translation-related issues
                System.err.println("Translation error occurred: " + e.getMessage());
                return "Error: Translation failure";
                 */
        } catch (Exception e) {
                // Handle any other unexpected errors
                System.err.println("Unexpected error occurred: " + e.getMessage());
                return "Error: Unexpected failure";
        }     
        
    }
    public String getSupportiveResponse(String sentiment, String message) {
        Map<String, List<String>> responseDatabase = new HashMap<>();
        
        responseDatabase.put("Negative", List.of(
            "I understand how tough things can be. You're not alone—reach out to someone who cares! 💙",
            "It's okay to feel this way. Take a deep breath and know that brighter days are ahead. ☀️",
            "You're stronger than you think. Have you tried journaling or talking to a friend about this? 📖"
        ));
        
        responseDatabase.put("Neutral", List.of(
            "That sounds like something worth reflecting on. What’s something positive you can focus on today? 😊",
            "You're on the right track! Keep pushing forward, and good things will follow. 🚀"
        ));
        
        responseDatabase.put("Positive", List.of(
            "That’s wonderful to hear! Keep spreading positivity and joy. 🌈",
            "Sounds amazing! What’s one thing that made you smile today? 😄"
        ));

        // Pick a random response from the sentiment category
        List<String> possibleResponses = responseDatabase.getOrDefault(sentiment, List.of("Stay strong and keep moving forward!"));
        return possibleResponses.get(new Random().nextInt(possibleResponses.size()));
    }

    @GetMapping("/happy-sent")
    //public String generateResponse(String inmessage) {
    public  ResponseEntity<Map<String, String>> generateResponse(@RequestParam(value = "message", defaultValue = "general") String inmessage){
        // Step 1: Get Sentiment
        String sentiment = analyzeSentiment(inmessage);
        
        // Step 2: Fetch a Context-Aware Response
        String supportiveMessage = getSupportiveResponse(sentiment, inmessage);
        
        //return "Sentiment Detected: " + sentiment + "\n" + supportiveMessage;

        //String fortune = openAIService.getAiHappy(category).block();
        //return openAIService.getAiFortune(category); // a string of the message called message
        Map<String, String> response = new HashMap<>();
        response.put("happy", supportiveMessage); // Wrap it in a JSON-friendly structure
         return ResponseEntity.ok(response);





    }
    //can also try Hugging Face sentiment models (like distilbert-base-uncased-finetuned-sst-2-english 
    
    


}
