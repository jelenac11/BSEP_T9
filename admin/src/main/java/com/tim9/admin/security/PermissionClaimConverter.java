package com.tim9.admin.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PermissionClaimConverter implements
        Converter<Map<String, Object>, Map<String, Object>> {

    private final MappedJwtClaimSetConverter delegate =
            MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        Map<String, Object> convertedClaims = this.delegate.convert(claims);
        
        List<?> list = new ArrayList<>();
        if (convertedClaims.get("permissions").getClass().isArray()) {
            list = Arrays.asList((Object[])convertedClaims.get("permissions"));
        } else if (convertedClaims.get("permissions") instanceof Collection) {
            list = new ArrayList<>((Collection<?>)convertedClaims.get("permissions"));
        }
        
        String scope = convertedClaims.get("scope").toString();
        for (Object perm: list) {
        	scope += " " + perm.toString();			
		}

        convertedClaims.put("scope", scope);

        return convertedClaims;
    }
}
