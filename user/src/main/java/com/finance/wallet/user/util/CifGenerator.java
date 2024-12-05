package com.finance.wallet.user.util;

import java.util.Random;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CifGenerator {
    private static final Random RANDOM = new Random();

    public static String generateRandomCif() {
        final int firstDigit = RANDOM.nextInt(9) + 1;
        return firstDigit + String.format("%09d", RANDOM.nextInt(1000000000));
    }
}
