package com.dsa.tests;

import com.dsa.util.InputClosedException;
import com.dsa.util.InputValidator;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static com.dsa.tests.TestHelper.assertEquals;
import static com.dsa.tests.TestHelper.assertThrows;
import static com.dsa.tests.TestHelper.check;

public final class InputValidatorTest {

    private InputValidatorTest() {
    }

    public static void run() {
        TestHelper.section("InputValidator");

        int[] holder = new int[1];
        String output = TestHelper.captureOutput(() ->
                holder[0] = validator("abc\n\n-5\n99999999999\n7\n").readNonNegativeInt("> "));
        assertEquals(7, holder[0], "readNonNegativeInt skips letters, empty, negative and too-large input");
        check(output.contains("'abc' is not a valid whole number"), "letters produce a friendly error");
        check(output.contains("Input cannot be empty"), "empty input produces a friendly error");
        check(output.contains("Negative numbers are not allowed"), "negative input produces a friendly error");
        check(output.contains("too large"), "a number too large for int produces a friendly error");

        TestHelper.captureOutput(() -> holder[0] = validator("0\n10\n3\n").readIntInRange("> ", 1, 9));
        assertEquals(3, holder[0], "readIntInRange re-prompts until the value is within range");

        TestHelper.captureOutput(() -> holder[0] = validator("  -12  \n").readInt("> "));
        assertEquals(-12, holder[0], "readInt trims spaces and accepts negative numbers");

        String[] text = new String[1];
        TestHelper.captureOutput(() -> text[0] = validator("a b\n\nthis-name-is-far-too-long-ok\nCity_1\n")
                .readVertexName("> "));
        assertEquals("City_1", text[0], "readVertexName rejects spaces, empty and over-long names");

        boolean[] answer = new boolean[1];
        TestHelper.captureOutput(() -> answer[0] = validator("maybe\nY\n").readYesNo("> "));
        check(answer[0], "readYesNo re-prompts and accepts 'Y'");

        assertThrows(InputClosedException.class,
                () -> TestHelper.captureOutput(() -> validator("abc\n").readInt("> ")),
                "end of input raises InputClosedException instead of looping forever");
    }

    private static InputValidator validator(String input) {
        return new InputValidator(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }
}
