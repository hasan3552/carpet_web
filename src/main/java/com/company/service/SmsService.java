package com.company.service;

import com.company.dto.SmsRequestDTO;
import com.company.dto.SmsResponseDTO;
import com.company.entity.SmsEntity;
import com.company.exp.ItemNotFoundException;
import com.company.repository.SmsRepository;
import com.company.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SmsService {

    @Value("${sms.url}")
    private String smsUrl;

    @Value("${sms.key}")
    private String smsKey;
    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private RestTemplate restTemplate;


    public void sendRegistrationSms(String phone) {
        String code = RandomUtil.getRandomSmsCode();
        String message = "Sharq Gilamlari partali uchun\n registratsiya kodi: " + code;

        SmsResponseDTO responseDTO = send(phone, message);

        SmsEntity entity = new SmsEntity();
        entity.setPhone(phone);
        entity.setCode(code);
        entity.setStatus(responseDTO.getSuccess());
        entity.setStatus(Boolean.FALSE);

        smsRepository.save(entity);
    }

    public boolean verifySms(String phone, String code) {
        Optional<SmsEntity> optional = smsRepository.findTopByPhoneOrderByCreatedDateDesc(phone);
        if (optional.isEmpty()) {
            return false;
        }
        SmsEntity sms = optional.get();
        LocalDateTime validDate = sms.getCreatedDate().plusMinutes(1);

        return sms.getCode().equals(code) && validDate.isAfter(LocalDateTime.now());
    }

    private SmsResponseDTO send(String phone, String message) {
        SmsRequestDTO requestDTO = new SmsRequestDTO();
        requestDTO.setKey(smsKey);
        requestDTO.setPhone(phone);
        requestDTO.setMessage(message);
        System.out.println("Sms Request: message " + message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SmsRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

        SmsResponseDTO response = restTemplate.postForObject(smsUrl, entity, SmsResponseDTO.class);
        System.out.println("Sms Response  " + response);
        return response;
    }

    public SmsEntity getSmsByPhone(String phone) {
        Optional<SmsEntity> optional = smsRepository.findTopByPhoneOrderByCreatedDateDesc(phone);

        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Phone Not Found");
        }

        return optional.get();
    }


    public PageImpl pagination(Integer page, Integer size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SmsEntity> all = smsRepository.findAll(pageable);

        List<SmsEntity> list = all.getContent();

        List<SmsRequestDTO> dtoList = new LinkedList<>();

        list.forEach(sms -> {
            SmsRequestDTO dto = new SmsRequestDTO();
            dto.setId(sms.getId());
            dto.setKey(smsKey);
            dto.setMessage(sms.getCode());
            dto.setPhone(sms.getPhone());
            dtoList.add(dto);
        });

        return new PageImpl(dtoList, pageable, all.getTotalElements());
    }

    public Long getSmsCount(String phone) {

        return smsRepository.countResend(phone);
    }

    public void save(SmsEntity sms) {
        smsRepository.save(sms);
    }
}
