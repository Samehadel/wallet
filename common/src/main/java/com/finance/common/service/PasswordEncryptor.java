package com.finance.common.service;

public interface PasswordEncryptor {

    String hashPassword(char[] password);

    boolean passwordMatch(char[] password, String encrypted);
}
