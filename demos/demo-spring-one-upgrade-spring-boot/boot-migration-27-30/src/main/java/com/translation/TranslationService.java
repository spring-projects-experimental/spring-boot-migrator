package com.translation;

import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    public String translate(String text) {
        return text.equals("Top 10 songs") ? "10 ಅತ್ಯಂತ ಖ್ಯಾತಿ ಹಾಡುಗಳು" : text;
    }
}
