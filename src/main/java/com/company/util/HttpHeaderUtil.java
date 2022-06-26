package com.company.util;

import com.company.dto.JwtDTO;
import com.company.enums.ProfileRole;
import com.company.exp.NoPermissionException;

import javax.servlet.http.HttpServletRequest;

public class HttpHeaderUtil {

    public static Integer getId(HttpServletRequest request, ProfileRole requiredRole) {
        JwtDTO jwtDTO = (JwtDTO) request.getAttribute("jwtDTO");
        Integer id = jwtDTO.getId();

        if (requiredRole != null && !requiredRole.equals(jwtDTO.getRole())) {
                throw new NoPermissionException("Not Access");
        }

        return id;
    }

    public static Integer getId(HttpServletRequest request) {
        return getId(request, null);
    }
}
