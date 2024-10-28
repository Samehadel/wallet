package com.finance.wallet.user.service;

public interface PasswordEncryptor {

    String hashPassword(char[] password);

    boolean checkPassword(char[] password, String encrypted);
}
