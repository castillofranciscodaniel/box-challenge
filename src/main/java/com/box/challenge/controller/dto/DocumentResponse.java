package com.box.challenge.controller.dto;

import java.time.LocalDateTime;

// DocumentResponse.java
public class DocumentResponse {
    private String fileName;
    private String hashSha256;
    private String hashSha512;
    private LocalDateTime lastUpload;

    // Getters and Setters


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHashSha256() {
        return hashSha256;
    }

    public void setHashSha256(String hashSha256) {
        this.hashSha256 = hashSha256;
    }

    public String getHashSha512() {
        return hashSha512;
    }

    public void setHashSha512(String hashSha512) {
        this.hashSha512 = hashSha512;
    }

    public LocalDateTime getLastUpload() {
        return lastUpload;
    }

    public void setLastUpload(LocalDateTime lastUpload) {
        this.lastUpload = lastUpload;
    }

    @Override
    public String toString() {
        return "DocumentResponse{" +
                "fileName='" + fileName + '\'' +
                ", hashSha256='" + hashSha256 + '\'' +
                ", hashSha512='" + hashSha512 + '\'' +
                ", lastUpload=" + lastUpload +
                '}';
    }
}