package com.box.challenge.controller;

import com.box.challenge.controller.dto.DocumentResponse;
import com.box.challenge.security.UserPrincipal;
import com.box.challenge.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/hash", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadDocuments(
            @RequestParam("hashType") String hashType,
            @RequestParam("documents") List<MultipartFile> files,
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        Map<String, Object> response = documentService.processUpload(hashType, files, currentUser);
        return ResponseEntity.status((int) response.get("status")).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getAllDocuments(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<DocumentResponse> documents = documentService.getAllDocumentsByUser(currentUser);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{hashType}")
    public ResponseEntity<DocumentResponse> getDocumentByHash(
            @PathVariable String hashType,
            @RequestParam String hash,
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        DocumentResponse document = documentService.getDocumentByHash(hashType, hash, currentUser);
        return ResponseEntity.ok(document);
    }
}