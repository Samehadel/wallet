package com.finance.common.service;

public interface PasswordEncryptor {

    String hashPassword(char[] password);

    boolean checkPassword(char[] password, String encrypted);
}
