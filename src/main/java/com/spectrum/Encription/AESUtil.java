package com.spectrum.Encription;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.util.Base64;

import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESUtil {
    
    // Generate a new encryption key
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Use 256-bit key size
        return keyGenerator.generateKey();
    }

    // Generate initialization vector
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    // Encrypt a string value
    public static String encrypt(String algorithm, String value, SecretKey key, IvParameterSpec iv) 
            throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryptedBytes = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt a string value
    public static String decrypt(String algorithm, String encrypted, SecretKey key, IvParameterSpec iv) 
            throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(decryptedBytes);
    }

    // Store IV with encrypted data (prepend to encrypted string)
    public static String encryptWithIv(String algorithm, String value, SecretKey key) throws Exception {
        IvParameterSpec iv = generateIv();
        String encrypted = encrypt(algorithm, value, key, iv);
        String ivBase64 = Base64.getEncoder().encodeToString(iv.getIV());
        return ivBase64 + ":" + encrypted;
    }

    // Decrypt data that includes IV
    public static String decryptWithIv(String algorithm, String encryptedWithIv, SecretKey key) 
            throws Exception {
        String[] parts = encryptedWithIv.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid encrypted data format");
        }

        byte[] iv = Base64.getDecoder().decode(parts[0]);
        String encryptedData = parts[1];
        
        return decrypt(algorithm, encryptedData, key, new IvParameterSpec(iv));
    }

    // Utility method to securely store the key (should be implemented based on your security requirements)
    public static void storeKey(SecretKey key, String keyStorePath, String keyAlias, String keyStorePassword) {
        // Implementation for secure key storage
        // This could involve storing in a KeyStore, HSM, or secure key management service
        throw new UnsupportedOperationException("Implement secure key storage based on your requirements");
    }

    // Utility method to load the stored key (should be implemented based on your security requirements)
    public static SecretKey loadKey(String keyStorePath, String keyAlias, String keyStorePassword) {
        // Implementation for loading the stored key
        // This could involve loading from a KeyStore, HSM, or secure key management service
        throw new UnsupportedOperationException("Implement secure key loading based on your requirements");
    }
    
    // Example of key configuration class
    public static class KeyConfig {
        private static final String KEY_STORE_PATH = "path/to/keystore.jks";
        private static final String KEY_ALIAS = "encryption-key";
        private static final String KEY_STORE_PASSWORD = "your-secure-password";
        
        public static SecretKey getKey() {
            try {
                return loadKey(KEY_STORE_PATH, KEY_ALIAS, KEY_STORE_PASSWORD);
            } catch (UnsupportedOperationException e) {
                // Fallback for development/testing - DO NOT USE IN PRODUCTION
                try {
                    return generateKey();
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException("Failed to generate key", ex);
                }
            }
        }
    }
}