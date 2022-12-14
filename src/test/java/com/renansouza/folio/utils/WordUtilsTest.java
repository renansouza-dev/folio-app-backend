package com.renansouza.folio.utils;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordUtilsTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void isBlank_ShouldReturnTrueForAllTypesOfBlankStrings(String input) {
        assertTrue(Strings.isBlank(WordUtils.capitalizeFully(input)));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void capitalizeFully(String input) {
        assertEquals("Spring Boot", WordUtils.capitalizeFully(input));
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("spring boot"),
                Arguments.of("SPRING BOOT"),
                Arguments.of("sPRING bOOT"),
                Arguments.of("spring Boot")
        );
    }

}