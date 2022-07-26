package com.company.repository.custom;

import com.company.dto.product.ProductFilterDTO;
import com.company.entity.CarpetEntity;
import com.company.entity.RugEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class CustomRugRepository {
    @Autowired
    private EntityManager entityManager;

    public List<RugEntity> filter(ProductFilterDTO dto){

        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT a FROM RugEntity a ");
        builder.append(" where visible = true ");

        if (dto.getName() != null) {
            builder.append(" and a.product.name ilike '%" + dto.getName() + "%' ");
        }

        if (dto.getFactoryName() != null) {
            builder.append(" and a.product.factory.name ilike '%" + dto.getFactoryName() + "%' ");
        }

        if (dto.getWeight() != null) {
            builder.append(" and a.weight=" + dto.getWeight() + " ");
        }

        if (dto.getHeight() != null) {
            builder.append(" and a.height=" + dto.getHeight() + " ");
        }

        if (dto.getColour() != null) {
            builder.append(" and a.product.colour= '" + dto.getColour() + "' ");
        }

        if (dto.getDesign() != null) {
            builder.append(" and a.product.design= '" + dto.getDesign() + "' ");
        }

        if (dto.getPon() != null) {
            builder.append(" and a.product.pon= '" + dto.getPon() + "' ");
        }

        if (dto.getPublishedDateFrom() != null && dto.getPublishedDateTo() == null) {
            // builder.append(" and a.publishDate = '" + dto.getPublishedDateFrom() + "' ");
            LocalDate localDate = LocalDate.parse(dto.getPublishedDateFrom());
            LocalDateTime fromTime = LocalDateTime.of(localDate, LocalTime.MIN); // yyyy-MM-dd 00:00:00
            LocalDateTime toTime = LocalDateTime.of(localDate, LocalTime.MAX); // yyyy-MM-dd 23:59:59
            builder.append(" and a.publishDate between '" + fromTime + "' and '" + toTime + "' ");

        } else if (dto.getPublishedDateFrom() != null && dto.getPublishedDateTo() != null) {
            builder.append(" and a.publishDate between '" + dto.getPublishedDateFrom() + "' and '" + dto.getPublishedDateTo() + "' ");
        }

        Query query = entityManager.createQuery(builder.toString());
        List<RugEntity> rugList = query.getResultList();

        return rugList;
    }
}
