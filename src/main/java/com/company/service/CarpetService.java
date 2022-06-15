package com.company.service;

import com.company.repository.CarpetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarpetService {

    @Autowired
    private CarpetRepository carpetRepository;
}
