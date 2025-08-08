package com.cinematch.cinematch.repository;

import com.cinematch.cinematch.model.DuluxColour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DuluxColourRepository extends JpaRepository<DuluxColour, Long> {
    boolean existsByHexCode(String hexCode);

    Optional<DuluxColour> findByHexCode(String hex);
}
