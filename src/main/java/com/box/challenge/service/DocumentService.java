package com.box.challenge.service;
import com.box.challenge.controller.dto.DocumentResponse;
import com.box.challenge.model.Document;
import com.box.challenge.model.User;
import com.box.challenge.repository.DocumentRepository;
import com.box.challenge.repository.UserRepository;
import com.box.challenge.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    public DocumentService(UserRepository userRepository, DocumentRepository documentRepository) {
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
    }

    public Map<String, Object> processUpload(String hashType, List<MultipartFile> files, UserPrincipal currentUser) {
        Map<String, Object> response = new HashMap<>();
        if (files.isEmpty() || (!hashType.equals("SHA-256") && !hashType.equals("SHA-512"))) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("error", "El parámetro 'hashType' solo puede ser 'SHA-256' o 'SHA-512'");
            return response;
        }

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Document> documents = files.stream().map(file -> {
            String fileName = file.getOriginalFilename();
            String hash;
            try {
                hash = calculateHash(file.getBytes(), hashType);
            } catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo: " + e.getMessage());
            }
            Document document = new Document();
            document.setFileName(fileName);
            document.setUser(user);
            if (hashType.equals("SHA-256")) {
                document.setHashSha256(hash);
            } else if (hashType.equals("SHA-512")) {
                document.setHashSha512(hash);
            }
            return document;
        }).collect(Collectors.toList());

        documentRepository.saveAll(documents);

        response.put("status", HttpStatus.CREATED.value());
        response.put("algorithm", hashType);
        response.put("documents", documents.stream()
                .map(this::convertToDocumentResponse)
                .collect(Collectors.toList()));
        return response;
    }

    private String calculateHash(byte[] data, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = md.digest(data);
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular el hash: " + e.getMessage());
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    public List<DocumentResponse> getAllDocumentsByUser(UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Document> documents = documentRepository.findByUser(user);
        return documents.stream()
                .map(this::convertToDocumentResponse)
                .collect(Collectors.toList());
    }

    public DocumentResponse getDocumentByHash(String hashType, String hash, UserPrincipal currentUser) {
        User user = this.userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Document document;
        if (hashType.equals("SHA-256")) {
            document = documentRepository.findByUserAndHashSha256(user, hash);
        } else if (hashType.equals("SHA-512")) {
            document = documentRepository.findByUserAndHashSha512(user, hash);
        } else {
            throw new RuntimeException("El parámetro 'hashType' solo puede ser 'SHA-256' o 'SHA-512'");
        }

        if (document == null) {
            throw new RuntimeException("No hay ningún documento con ese hash para el usuario");
        }

        return convertToDocumentResponse(document);
    }

    private DocumentResponse convertToDocumentResponse(Document document) {
        DocumentResponse response = new DocumentResponse();
        response.setFileName(document.getFileName());
        response.setHashSha256(document.getHashSha256());
        response.setHashSha512(document.getHashSha512());
        response.setLastUpload(document.getLastUpload());
        return response;
    }
}