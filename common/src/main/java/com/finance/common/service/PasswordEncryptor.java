package com.finance.common.service;

public interface PasswordEncryptor {

    String hashPassword(char[] password);

    boolean passwordMatch(char[] plainPassword, String hashedPassword);
}
