package com.cinematch.cinematch.repository;

import com.cinematch.cinematch.model.DuluxColour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuluxColourRepository extends JpaRepository<DuluxColour, Long> {
    boolean existsByHexCode(String hexCode);
}
