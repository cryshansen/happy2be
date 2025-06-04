package com.artogco.happy2be.service;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import ai.djl.Model;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;

/**
 * This is not working due to the model tbd if integrate  in  future -- use python(happyfaceflask) and an http java injestion instead. 
 * the token file works and tied t use  the  sam approach but the model path needs to be defined
 */

@Service
public class SentimentAnalyzer {
 private static final String modelPath = "/happy2be/models/distilbert-base-uncased-finetuned-sst-2-english"; 

    public String analyzeSentiment(String text) {

             // Create a model object
        Model model = Model.newInstance(modelPath);
        // Load Hugging Face model for sentiment analysis
        //Model model = Model.newInstance(MODEL_URL, Application.NLP.TEXT_CLASSIFICATION);

        // Set up the tokenizer (this will be used for text input)
        HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.newInstance("distilbert-base-uncased");

            // Tokenize input text
            //String text = "This is an awesome movie!";
            long[] tokens = tokenizer.encode(text).getIds();
            // ✅ Print tokenized output
            System.out.println("Token IDs: " + Arrays.toString(tokens));
           
        return "Hello from analyzer" + Arrays.toString(tokens);
    }

}
