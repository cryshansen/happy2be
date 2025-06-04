package com.artogco.happy2be.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.artogco.happy2be.service.EmotionService;
import com.artogco.happy2be.service.TokenizerService;


@RestController
@RequestMapping("/api/emotion")
public class EmotionController {
   // private final TokenizerService tokenizerService;
    private final EmotionService emotionService;
    public EmotionController(EmotionService tokenizerService) {
        this.emotionService = tokenizerService;
    }

    @PostMapping
    public String classifyEmotion(@RequestBody String text) throws Exception {
        return emotionService.classifyEmotion(text);
    }
}
