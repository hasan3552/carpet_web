package com.company.service;

import com.company.dto.attach.AttachDTO;
import com.company.dto.product.ProductAttachDTO;
import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.entity.*;
import com.company.enums.AttachStatus;
import com.company.enums.ProductType;
import com.company.enums.ProfileRole;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.exp.NoPermissionException;
import com.company.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AttachService {

    @Value("${attach.folder}")
    private String attachFolder;

    @Value("${server.url}")
    private String serverUrl;
        @Autowired
    private ProfileService profileService;
    @Autowired
    private AttachRepository attachRepository;
//    @Autowired
//    private ProfileRepository profileRepository;
    @Autowired
    private FactoryRepository factoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductAttachRepository productAttachRepository;

    public byte[] openGeneral(String fileUUID) {
        AttachEntity attach = get(fileUUID);

        byte[] data;
        try {
            // fileName -> zari.jpg
            String path = "attaches/" + attach.getPath() + "/" + fileUUID + "." + attach.getExtension();
            Path file = Paths.get(path);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public Resource download(String fileUUID) {

        AttachEntity attach = get(fileUUID);
        try {

            String path = "attaches/" + attach.getPath() + "/" + fileUUID + "." + attach.getExtension();
            Path file = Paths.get(path);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2022/04/23
    }

    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Attach Not found");
        });
    }

    public PageImpl pagination(Integer page, Integer size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "uuid");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AttachEntity> all = attachRepository.findAll(pageable);

        List<AttachEntity> list = all.getContent();

        List<AttachDTO> dtoList = new LinkedList<>();

        list.forEach(attach -> {
            AttachDTO dto = new AttachDTO();
            dto.setId(attach.getUuid());
            dto.setUrl(serverUrl + "attach/open?fileId=" + attach.getUuid());
            dto.setOriginalName(attach.getOriginalName());
            dto.setPath(attach.getPath());
            dtoList.add(dto);
        });

        return new PageImpl(dtoList, pageable, all.getTotalElements());
    }

    public AttachDTO saveToSystemForProfile(MultipartFile file) {


        ProfileEntity profile = profileService.getProfile();

        AttachEntity attach = attachSaveFilesAndDB(file);
        AttachEntity oldAttach = null;
        if (profile.getPhoto() != null) {
            oldAttach = profile.getPhoto();
        }

        profile.setPhoto(attach);
        profileService.save(profile);

        if (oldAttach != null) {
            deletedFilesAndDB(oldAttach);
        }

        AttachDTO dto = new AttachDTO();
        dto.setUrl(serverUrl + "attach/open?fileId=" + attach.getUuid());
        return dto;
    }

    public AttachDTO saveToSystemForFactory(MultipartFile file, String key) {

        Optional<FactoryEntity> optional1 = factoryRepository.findByKey(key);
        if (optional1.isEmpty()) {
            throw new NoPermissionException("Factory not found");
        }

        FactoryEntity factory = optional1.get();

        AttachEntity attach = attachSaveFilesAndDB(file);

        AttachEntity oldAttach = null;
        if (factory.getAttach() != null) {
            oldAttach = factory.getAttach();
            System.out.println(oldAttach);
        }

        factory.setAttach(attach);
        factoryRepository.save(factory);

        if (oldAttach != null) {
            deletedFilesAndDB(oldAttach);
        }

        AttachDTO dto = new AttachDTO();
        dto.setUrl(serverUrl + "attach/open?fileId=" + attach.getUuid());
        return dto;

    }

    private void deletedFilesAndDB(AttachEntity attach) {
        try {
            Files.delete(Path.of(attachFolder + attach.getPath() + "/" + attach.getUuid() + "." + attach.getExtension()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        attachRepository.delete(attach);
    }

    private AttachEntity attachSaveFilesAndDB(MultipartFile file) {

        File folder = new File(attachFolder + getYmDString());

        AttachEntity attach = new AttachEntity();
        attach.setExtension(getExtension(file.getOriginalFilename()));
        attach.setOriginalName(getOriginalName(file));
        attach.setSize(file.getSize());
        attach.setPath(getYmDString());
        attachRepository.save(attach);

        String fileName = attach.getUuid() + "." + getExtension(file.getOriginalFilename());
        if (!folder.exists()) {
            folder.mkdirs();
        }

        byte[] bytes;
        try {
            bytes = file.getBytes();
            Path path = Paths.get(attachFolder + getYmDString() + "/" + fileName);
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return attach;
    }
    private String getOriginalName(MultipartFile file){
        return file.getOriginalFilename()
                .replace(("." + getExtension(file.getOriginalFilename())), "");
    }
    public ProductDTO saveToSystemForProduct(MultipartFile file, String productId) {

        ProductAttachEntity entity = new ProductAttachEntity();
        ProductEntity product = productService.get(productId);
        AttachEntity attach = attachSaveFilesAndDB(file);

        entity.setAttach(attach);
        entity.setProduct(product);

        productAttachRepository.save(entity);

        return new ProductDTO(listProductImageUrl(product));
    }
    private List<String> listProductImageUrl(ProductEntity product) {

        List<ProductAttachEntity> list = productAttachRepository
                .findAllByProductAndStatusAndVisible(product, AttachStatus.ACTIVE, Boolean.TRUE);

        List<String> urlList = new ArrayList<>();
        list.forEach(productAttachEntity -> {

            urlList.add((serverUrl + "attach/open?fileId=" + productAttachEntity.getAttach().getUuid()));

        });

        return urlList;
    }

//    public void deletedProductAttach(Integer profileId, String attachId, ProductAttachDTO dto) {
//
//        Optional<ProfileEntity> optional = profileRepository.findById(profileId);
//        if (optional.isEmpty()){
//            throw new ItemNotFoundException("User not found");
//        }
//
//        ProfileEntity profile = optional.get();
//        if (profile.getRole().equals(ProfileRole.CUSTOMER)){
//            throw new NoPermissionException("NO access");
//        }
//
//        Optional<AttachEntity> optional1 = attachRepository.findById(attachId);
//        if (optional1.isEmpty()){
//            throw new ItemNotFoundException("Attach  not found");
//        }
//
//        AttachEntity attach = optional1.get();
//
//        String productId = dto.getProductId();
//        ProductType type = dto.getType();
//
//        ProductAttachEntity entity;
//        if (type.equals(ProductType.COUNTABLE) && carpetRepository.existsById(productId)){
//            Optional<ProductAttachEntity> optional2 = productAttachRepository
//                    .findByAttachAndTypeAndCarpet(attach, type, new CarpetEntity(productId));
//
//            if (optional2.isEmpty()){
//                throw new ItemNotFoundException("Bad request");
//            }
//
//            entity = optional2.get();
//        }else if (type.equals(ProductType.UNCOUNTABLE) && rugRepository.existsById(productId)){
//            Optional<ProductAttachEntity> optional2 = productAttachRepository
//                    .findByAttachAndTypeAndRug(attach, type, new RugEntity(productId));
//
//
//            if (optional2.isEmpty()){
//                throw new ItemNotFoundException("Bad request");
//            }
//
//            entity = optional2.get();
//        }else {
//            throw new BadRequestException("Exception! Please try again few minute");
//        }
//        productAttachRepository.delete(entity);
//        deletedFilesAndDB(attach);
//    }

    public String deletedFactory(String key) {

        Optional<FactoryEntity> optional = factoryRepository.findByKey(key);
        if (optional.isEmpty()){
            throw new ItemNotFoundException("Factory not fount");
        }

        FactoryEntity factory = optional.get();
        AttachEntity attach = factory.getAttach();

        if (attach != null){
            factory.setAttach(null);
            factoryRepository.save(factory);
            deletedFilesAndDB(attach);
        }

        return "success deleted";
    }

    public String deletedProfile() {

        ProfileEntity profile = profileService.getProfile();
        AttachEntity attach = profile.getPhoto();

        if (attach != null){
            profile.setPhoto(null);
            profileService.save(profile);
            deletedFilesAndDB(attach);
        }

        return "success deleted";
    }

    public AttachDTO saveToSystem(MultipartFile file) {

          ProfileEntity entity = profileService.getProfile();

        try {

            File folder = new File(attachFolder + getYmDString());

            AttachEntity attach = new AttachEntity();
//            split[0], split[1], file.getSize(), folder.getPath(), article
            attach.setExtension(getExtension(file.getOriginalFilename()));
            attach.setOriginalName(file.getOriginalFilename()
                    .replace(("." + getExtension(file.getOriginalFilename())), ""));
            attach.setSize(file.getSize());
            attach.setPath(getYmDString());
            attachRepository.save(attach);

            String fileName = attach.getUuid() + "." + getExtension(file.getOriginalFilename());
            if (!folder.exists()) {
                folder.mkdirs();
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(attachFolder + getYmDString() + "/" + fileName);
            Files.write(path, bytes);

            AttachDTO dto = new AttachDTO();
            dto.setUrl(serverUrl + "attach/open?fileId=" + attach.getUuid());
            return dto;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] loadImage(String fileId) {
        byte[] imageInByte;

        Optional<AttachEntity> optional = attachRepository.findById(fileId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Attach not found");
        }

        AttachEntity attach = optional.get();
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File("attaches/" + attach.getPath() + "/" + attach.getUuid()));
        } catch (Exception e) {
            return new byte[0];
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(originalImage, attach.getExtension(), baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageInByte;
    }
}
