package org.poo.utils;

import java.util.Random;

/**
 * Utility class providing methods for generating unique IBANs, card numbers,
 * and resetting random number generators.
 */
public final class Utils {

    /**
     * Seed for the IBAN random generator.
     */
    private static final int IBAN_SEED = 1;

    /**
     * Seed for the card number random generator.
     */
    private static final int CARD_SEED = 2;

    /**
     * Upper bound for digits in the generated numbers.
     */
    private static final int DIGIT_BOUND = 10;

    /**
     * The number of digits to generate for card numbers.
     */
    private static final int DIGIT_GENERATION = 16;

    /**
     * Prefix for generated IBANs.
     */
    private static final String RO_STR = "RO";

    /**
     * Additional identifier for generated IBANs.
     */
    private static final String POO_STR = "POOB";

    /**
     * Random number generator for IBAN generation.
     */
    private static Random ibanRandom = new Random(IBAN_SEED);

    /**
     * Random number generator for card number generation.
     */
    private static Random cardRandom = new Random(CARD_SEED);

    /**
     * Number for procent.
     */
    public static final int PROCENT = 100;

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private Utils() {
    }

    /**
     * Generates a unique IBAN code.
     *
     * @return the generated IBAN as a String
     */
    public static String generateIBAN() {
        StringBuilder sb = new StringBuilder(RO_STR);
        for (int i = 0; i < RO_STR.length(); i++) {
            sb.append(ibanRandom.nextInt(DIGIT_BOUND));
        }

        sb.append(POO_STR);
        for (int i = 0; i < DIGIT_GENERATION; i++) {
            sb.append(ibanRandom.nextInt(DIGIT_BOUND));
        }

        return sb.toString();
    }

    /**
     * Generates a unique card number.
     *
     * @return the generated card number as a String
     */
    public static String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DIGIT_GENERATION; i++) {
            sb.append(cardRandom.nextInt(DIGIT_BOUND));
        }

        return sb.toString();
    }

    /**
     * Resets the random number generators to their initial state.
     */
    public static void resetRandom() {
        ibanRandom = new Random(IBAN_SEED);
        cardRandom = new Random(CARD_SEED);
    }
}
