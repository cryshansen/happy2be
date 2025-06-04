package com.artogco.happy2be.service;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;

@Service
public class EmotionService {

    private final Predictor<String, Classifications> predictor;
    private static final String modelPath = "models/distilbert-base-uncased-finetuned-sst-2-english/pytorch_model.bin";

    public EmotionService() throws ModelException {
        Criteria<String, Classifications> criteria = Criteria.builder()
                .setTypes(String.class, Classifications.class)
                .optModelPath(Paths.get(modelPath))
                .optTranslator(new DistilBertTranslator())  // Use your custom Translator
                .build();

        try {
            ZooModel<String, Classifications> model = ModelZoo.loadModel(criteria);
            this.predictor = model.newPredictor();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load model: " + e.getMessage(), e);
        }
    }

    /*public List<Long> tokenize(String text) {
        // ✅ Tokenize input and return token IDs
        long[] tokenIds = tokenizer.encode(text).getIds();
        return Arrays.stream(tokenIds).boxed().collect(Collectors.toList());
    }
*/
    public String classifyEmotion(String text) throws TranslateException {
        Classifications result = predictor.predict(text);
        return result.best().getClassName();
    }
}
