package techpriest.Url_Shortener.util;

import java.security.SecureRandom;

/**
 * Generates random Base62 short codes for URLs.
 *
 * <p>A code of the default length yields 62^7 (~3.5 trillion) possible values.
 * Generation is random, so callers are responsible for handling the (rare)
 * collision case — e.g. retrying on a unique-constraint violation.
 */
public final class ShortCodeGenerator {

    private static final String BASE62 =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int DEFAULT_LENGTH = 7;
    private static final SecureRandom RANDOM = new SecureRandom();

    private ShortCodeGenerator() {}

    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    public static String generate(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length must be at least 1, was " + length);
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(BASE62.charAt(RANDOM.nextInt(BASE62.length())));
        }
        return sb.toString();
    }
}
