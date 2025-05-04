package com.knowYourfan.backEnd.Repositories;

import com.knowYourfan.backEnd.Entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {}