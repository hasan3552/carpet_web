package com.company.controller;

import com.company.dto.media.MediaDTO;
import com.company.entity.MediaEntity;
import com.company.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestParam("file") MultipartFile file) {

        MediaDTO dto = mediaService.uploadMedia(file);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {

        MediaEntity media = mediaService.getMedia(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + media.getFileName())
                .body(new ByteArrayResource(media.getFileData()));
    }

}
