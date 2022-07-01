package com.company;

import com.company.entity.CarpetEntity;
import com.company.enums.ProductStatus;
import com.company.repository.CarpetRepository;
import com.company.util.CurrencyUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CarpetWebApplicationTests {

	@Autowired
	private CarpetRepository carpetRepository;

	@Test
	void contextLoads() {

		for (CarpetEntity carpet : carpetRepository.pagination(5, 1, ProductStatus.ACTIVE.name())) {
			System.out.println(carpet);
		}


		System.out.println(CurrencyUtil.getRate());
	}

}
