package com.example.movies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ApplicationTest {
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream out;
    private ByteArrayOutputStream err;

    @BeforeEach
    public void setUp() {
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testPrintUsage() {
        Application.main(new String[] {});
        assertTrue(err.toString().trim().startsWith("Usage:"));
        assertEquals("", out.toString());
    }

    @Test
    public void testPrintHelp() {
        Application.main(new String[] { "--help" });
        assertTrue(out.toString().trim().startsWith("Usage:"));
        assertEquals("", err.toString());
    }
}
