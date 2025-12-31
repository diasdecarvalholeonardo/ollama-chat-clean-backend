package com.leo.ai.ollamachat.dataset.repository;

import com.leo.ai.ollamachat.dataset.entity.ValidationData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationDataRepository extends JpaRepository<ValidationData, Long> {
}

