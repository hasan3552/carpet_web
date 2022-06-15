package com.company.service;

import com.company.dto.media.MediaDTO;
import com.company.entity.AttachEntity;
import com.company.entity.MediaEntity;
import com.company.exp.ItemNotFoundException;
import com.company.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    public MediaDTO uploadMedia(MultipartFile file){

        MediaEntity media = new MediaEntity();
        try {

            media.setFileData(file.getBytes());
            media.setFileType(file.getContentType());
            media.setFileName(file.getName());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mediaRepository.save(media);

        return getDTO(media);

    }

    private MediaDTO getDTO(MediaEntity media) {

        MediaDTO dto = new MediaDTO();
        dto.setCreatedDate(media.getCreatedDate());
        dto.setFileData(media.getFileData());
        dto.setId(media.getId());
        dto.setVisible(media.getVisible());
        dto.setFileName(media.getFileName());
        dto.setFileType(media.getFileType());

        return dto;
    }

    public MediaEntity getMedia(Integer id) {

        Optional<MediaEntity> optional = mediaRepository.findById(id);
        if (optional.isEmpty()){
            throw new ItemNotFoundException("not fount");
        }

        MediaEntity media = optional.get();

        return media;
    }
}
