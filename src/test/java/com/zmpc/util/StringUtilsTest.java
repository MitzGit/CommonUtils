package com.zmpc.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testIsNullOrEmpty_Null() {
        assertTrue(StringUtils.isNullOrEmpty(null));
    }

    @Test
    void testIsNullOrEmpty_Empty() {
        assertTrue(StringUtils.isNullOrEmpty(""));
    }

    @Test
    void testIsNullOrEmpty_NotEmpty() {
        assertFalse(StringUtils.isNullOrEmpty("Text"));
    }

    @Test
    void testEmptyIfNull_Null() {
        assertEquals("", StringUtils.emptyIfNull(null));
    }

    @Test
    void testEmptyIfNull_NotNull() {
        assertEquals("abc", StringUtils.emptyIfNull("abc"));
    }

    @Test
    void testSplitByIndex_Null() {
        assertNotNull(StringUtils.splitByIndex(null, 0));
    }
}