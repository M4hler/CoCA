package com.coca.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "test")
public class ExampleTest {
    @Test
    public void test() {
        Assertions.assertTrue(true);
    }
}
