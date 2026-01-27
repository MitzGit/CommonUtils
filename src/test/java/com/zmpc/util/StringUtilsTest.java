package com.zmpc.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @CsvSource(textBlock = """
            hello, Hello
            helloWorld, HelloWorld
            two words, Two words
            """)
    @ParameterizedTest(name = "Test: {0} -> {1}")
    void testCapitalizeFirstLetter_params(String str, String expected) {
        String actual = StringUtils.capitalizeFirstLetter(str);
        //assertThat(actual).isEqualTo(expected);
        assertEquals(expected, actual);
    }
}