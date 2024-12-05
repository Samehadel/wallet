package com.finance.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.wallet.user.util.CifGenerator;

import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
class TestFunctionality {

    @Test
    void testCifGenerator() {
        String cif = CifGenerator.generateRandomCif();
        log.info("Generated CIF: {}", cif);

        assertThat(cif)
            .isNotNull()
            .isNotEmpty()
            .hasSize(10);

        assertThat(cif.charAt(0))
            .isBetween('1', '9');
    }
}
