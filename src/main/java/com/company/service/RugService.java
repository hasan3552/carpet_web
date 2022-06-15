package com.company.service;

import com.company.repository.RugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RugService {

    @Autowired
    private RugRepository rugRepository;
}
