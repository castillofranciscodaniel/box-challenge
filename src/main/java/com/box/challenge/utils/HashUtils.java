package com.box.challenge.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    public static String calculateSHA256Hash(byte[] data) {
        return calculateHash(data, "SHA-256");
    }

    public static String calculateSHA512Hash(byte[] data) {
        return calculateHash(data, "SHA-512");
    }

    private static String calculateHash(byte[] data, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = md.digest(data);
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular el hash: " + e.getMessage());
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    // Example usage
    public static void main(String[] args) throws IOException {
        byte[] data = "Hola, este es un ejemplo de datos para calcular el hash.".getBytes();

        String sha256Hash = calculateSHA256Hash(data);
        System.out.println("SHA-256 Hash: " + sha256Hash);

        String sha512Hash = calculateSHA512Hash(data);
        System.out.println("SHA-512 Hash: " + sha512Hash);
    }
}