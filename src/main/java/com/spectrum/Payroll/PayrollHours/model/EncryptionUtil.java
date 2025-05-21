package com.spectrum.Payroll.PayrollHours.model;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    // Pre-generated 256-bit AES key (32 bytes)
    private static final byte[] KEY_BYTES = new byte[] {
            (byte) 0x25, (byte) 0x7E, (byte) 0x15, (byte) 0x16,
            (byte) 0x28, (byte) 0xAE, (byte) 0xD2, (byte) 0xA6,
            (byte) 0xAB, (byte) 0xF7, (byte) 0x15, (byte) 0x88,
            (byte) 0x09, (byte) 0xCF, (byte) 0x4F, (byte) 0x3C,
            (byte) 0x25, (byte) 0x7E, (byte) 0x15, (byte) 0x16,
            (byte) 0x28, (byte) 0xAE, (byte) 0xD2, (byte) 0xA6,
            (byte) 0xAB, (byte) 0xF7, (byte) 0x15, (byte) 0x88,
            (byte) 0x09, (byte) 0xCF, (byte) 0x4F, (byte) 0x3C
    };

    // Pre-generated IV (16 bytes)
    private static final byte[] IV_BYTES = new byte[] {
            (byte) 0x3D, (byte) 0xAF, (byte) 0xBA, (byte) 0x42,
            (byte) 0x9D, (byte) 0x9E, (byte) 0xB4, (byte) 0x30,
            (byte) 0xB4, (byte) 0x22, (byte) 0xDA, (byte) 0x80,
            (byte) 0x2C, (byte) 0x9F, (byte) 0xAC, (byte) 0x41
    };

    private static final SecretKey secretKey = new SecretKeySpec(KEY_BYTES, "AES");
    private static final IvParameterSpec iv = new IvParameterSpec(IV_BYTES);

    public static String encryptInt(int value) {
        try {
            String valueStr = String.valueOf(value);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encrypted = cipher.doFinal(valueStr.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting integer", e);
        }
    }

    public static int decryptInt(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return Integer.parseInt(new String(decrypted));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting integer", e);
        }
    }

    public static String encryptBoolean(Boolean value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encrypted = cipher.doFinal(value.toString().getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting boolean", e);
        }
    }

    public static Boolean decryptBoolean(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return Boolean.parseBoolean(new String(decrypted));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting boolean", e);
        }
    }




     // Encrypt Double
    public static String encryptDouble(Double value) {
        try {
            String valueStr = String.valueOf(value);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encrypted = cipher.doFinal(valueStr.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting double", e);
        }
    }

    // Decrypt Double
    public static Double decryptDouble(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return Double.parseDouble(new String(decrypted));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting double", e);
        }
    }

    // Encrypt Long
    public static String encryptLong(Long value) {
        try {
            String valueStr = String.valueOf(value);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encrypted = cipher.doFinal(valueStr.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting long", e);
        }
    }

    // Decrypt Long
    public static Long decryptLong(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return Long.parseLong(new String(decrypted));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting long", e);
        }
    }
}