package com.company.repository.custom;

import com.company.dto.profile.ProfileFilterDTO;
import com.company.entity.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class CustomProfileRepository {

    @Autowired
    private EntityManager entityManager;

    public List<ProfileEntity> filter(ProfileFilterDTO dto){

        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT a FROM ProfileEntity a ");
        builder.append(" where visible = true ");

        if (dto.getId() != null) {
            builder.append(" and a.id = " + dto.getId() + " ");
        }

        // Select a from ArticleEntity a where title = 'asdasd'; delete from sms-- fdsdfsdfs'
        if (dto.getStatus() != null) {
            builder.append(" and a.status = '" + dto.getStatus() + "' ");
        }

        if (dto.getRole() != null) {
            builder.append(" and a.role = '" + dto.getRole() + "' ");
        }

        if (dto.getSurname() != null) {
            builder.append(" and a.surname ilike '%" + dto.getSurname() + "%' ");
        }

        if (dto.getName() != null) {
            builder.append(" and a.name ilike '%" + dto.getName() + "%' ");
        }

        if (dto.getPhoneNumber() != null) {
            builder.append(" and a.phone_number ilike '%" + dto.getPhoneNumber() + "%' ");
        }

        if (dto.getCreatedDateFrom() != null && dto.getCreatedDateTo() == null) {
            // builder.append(" and a.publishDate = '" + dto.getPublishedDateFrom() + "' ");
            LocalDate localDate = LocalDate.parse(dto.getCreatedDateFrom());
            LocalDateTime fromTime = LocalDateTime.of(localDate, LocalTime.MIN); // yyyy-MM-dd 00:00:00
            LocalDateTime toTime = LocalDateTime.of(localDate, LocalTime.MAX); // yyyy-MM-dd 23:59:59
            builder.append(" and a.createdDate between '" + fromTime + "' and '" + toTime + "' ");

        } else if (dto.getCreatedDateFrom() != null && dto.getCreatedDateTo() != null) {
            builder.append(" and a.createdDate between '" + dto.getCreatedDateFrom() + "' and '" + dto.getCreatedDateTo() + "' ");
        }

        Query query = entityManager.createQuery(builder.toString());
        List<ProfileEntity> profileList = query.getResultList();

        return profileList;
    }
}
