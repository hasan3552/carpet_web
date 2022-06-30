package com.company.service;

import com.company.dto.product.ProductCreateDTO;
import com.company.entity.DetailEntity;
import com.company.entity.ProfileEntity;
import com.company.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailService {

    @Autowired
    private DetailRepository detailRepository;

    public void saveDetail(Integer profileId, ProductCreateDTO dto) {

        DetailEntity detail = new DetailEntity();
        detail.setProfile(new ProfileEntity(profileId));
        detail.setAction("create product : " + dto.toString());

        detailRepository.save(detail);
    }

}
