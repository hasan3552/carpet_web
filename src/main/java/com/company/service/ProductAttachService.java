package com.company.service;

import com.company.entity.CarpetEntity;
import com.company.entity.ProductAttachEntity;
import com.company.enums.AttachStatus;
import com.company.repository.CarpetRepository;
import com.company.repository.ProductAttachRepository;
import com.company.repository.RugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductAttachService {


    @Autowired
    private CarpetRepository carpetRepository;

    @Autowired
    private RugRepository rugRepository;

    @Autowired
    private ProductAttachRepository productAttachRepository;

    @Value("${server.url}")
    private String serverUrl;


}
