package com.company.service;

import com.company.repository.CarpetRepository;
import com.company.repository.ProductAttachRepository;
import com.company.repository.RugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductAttachService {


    @Autowired
    private CarpetRepository carpetRepository;

    @Autowired
    private RugRepository rugRepository;

    @Autowired
    private ProductAttachRepository productAttachRepository;

}
