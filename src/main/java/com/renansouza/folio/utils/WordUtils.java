package com.renansouza.folio.utils;

import java.util.Objects;

public class WordUtils {
    private static String capitalizeFirstLetter(String word) {
        if (Objects.isNull(word) || word.length() == 0) {
            return word;
        }

        String lowerCaseWord = word.toLowerCase();
        char[] charArray = lowerCaseWord.toCharArray();
        boolean foundSpace = true;

        for (int i = 0; i < charArray.length; i++) {
            if (Character.isLetter(charArray[i])) {
                if (foundSpace) {
                    charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                }
            } else {
                foundSpace = true;
            }
        }
        return String.valueOf(charArray);
    }
}
