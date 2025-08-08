package com.cinematch.cinematch.repository;

import com.cinematch.cinematch.model.ColourModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ColourRepository extends JpaRepository<ColourModel, String> {
}
