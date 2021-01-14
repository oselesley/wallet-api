package com.internship.walletapi.utils;


import com.internship.walletapi.exceptions.ResourceCreationException;
import com.internship.walletapi.exceptions.ResourceNotFoundException;
import com.internship.walletapi.models.BaseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Slf4j
public class ResourceHelper {
    public static <T>  T validateResourceExists (Optional<T> resource, String message) {
        if (resource.isEmpty())
            throw new ResourceNotFoundException(message);
        return resource.get();
    }

    public static <T extends BaseModel>  Long saveResource (T resource, JpaRepository<T, Long> repository) {
        Long id;
        try {
            id = repository.save(resource).getId();
        } catch (Exception e) {
            log.info(e + "");
            throw new ResourceCreationException(e.getMessage());
        }
        return id;
    }
}
