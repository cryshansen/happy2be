package com.artogco.happy2be.service;

import ai.djl.Application;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.translate.TranslatorFactory;
//import ai.djl.translate.TranslatorFactoryImpl;
//import ai.djl.translate.TranslatorParameters;
import ai.djl.translate.Batchifier;
import ai.djl.translate.NoBatchifyTranslator;

import java.util.List;

public class SentimentAnalysis {
    /*public static void main(String[] args) throws ModelException, TranslateException {
        // Load model from Hugging Face
        String modelName = "distilbert/distilbert-base-uncased-finetuned-sst-2-english";
        Model model = Model.newInstance(modelName, Application.NLP.SENTIMENT_ANALYSIS);

        // Create tokenizer
        HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.newInstance(modelName);

        // Translate text input
        Translator<String, Classifications> translator = new NoBatchifyTranslator<String, Classifications>() {
            @Override
            public Batchifier getBatchifier() {
                return null;
            }

            @Override
            public Classifications processOutput(TranslatorContext ctx, String output) {
                return new Classifications(List.of("NEGATIVE", "POSITIVE"), List.of(0.1f, 0.9f));
            }

            @Override
            public String processInput(TranslatorContext ctx, String input) {
                return tokenizer.encode(input);
            }
        };

        // Use the model for prediction
        try (Predictor<String, Classifications> predictor = model.newPredictor(translator)) {
            Classifications result = predictor.predict("I love this product!");
            System.out.println("Sentiment Analysis Result: " + result);
        }
    }*/
}
