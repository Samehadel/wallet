package com.finance.common.service;

import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class PasswordSHA1Encryption implements PasswordEncryptor {
    private final ExceptionService exceptionService;

    @Override
    public String hashPassword(final char[] password) {
        try {
            log.info("Hashing password");
            final var saltByte = generateSalt();

            String hashedPassword = hashPassword(password, saltByte);
            log.info("Password hashed");

            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw exceptionService.throwBadRequestException(SharedApplicationError.INVALID_ALGORITHM);
        }
    }

    private String hashPassword(final char[] password, final byte[] saltByte) throws NoSuchAlgorithmException {
        final var digest = MessageDigest.getInstance("SHA-256");
        digest.update(saltByte);
        final var hashedBytes = digest.digest(new String(password).getBytes());

        final var saltBase64 = Base64.getEncoder().encodeToString(saltByte);
        final var hashBase64 = Base64.getEncoder().encodeToString(hashedBytes);

        return saltBase64 + ":" + hashBase64;
    }

    private byte[] generateSalt() {
        final var random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

    @Override
    public boolean checkPassword(final char[] password, final String storedHash) {
        try {
            log.info("Checking password");
            String[] storedPasswordParts = storedHash.split(":");
            byte[] storedSalt = Base64.getDecoder().decode(storedPasswordParts[0]);

            final var hashedInput = this.hashPassword(password, storedSalt).split(":")[1];

            return hashedInput.equals(storedPasswordParts[1]);
        } catch (Exception e) {
            log.error("Error checking password", e);
            return false;
        }
    }
}
