package com.zmpc.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectUtilsTest {

    @Test
    void testCheckNotNull_null_value() {
        Object obj = null;

        assertThatThrownBy(() -> ObjectUtils.notNull(obj))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Object must not be null.");
    }

    @Test
    void testCheckNotNull_not_null_value() {
        String str = "Some data";
        assertThatNoException().isThrownBy(() -> ObjectUtils.notNull(str));
    }
}