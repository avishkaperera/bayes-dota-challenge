package gg.bayes.challenge.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeParseException;

@SpringBootTest
class CommonUtilTest {

    @Test
    void test_extractTimeDiffInMilliseconds_success() {
        Assertions.assertEquals(1806080, CommonUtil.extractTimeDiffInMilliseconds("00:30:06.080"));
    }

    @Test
    void test_extractTimeDiffInMilliseconds_invalidTime() {
        Assertions.assertThrows(DateTimeParseException.class, () -> CommonUtil.extractTimeDiffInMilliseconds("test"), "Text 'test' could not be parsed at index 0");
    }

    @Test
    void test_extractTimeDiffInMilliseconds_invalidTime2() {
        Assertions.assertThrows(DateTimeParseException.class, () -> CommonUtil.extractTimeDiffInMilliseconds("00:30"), "Text 'test' could not be parsed at index 0");
    }
}