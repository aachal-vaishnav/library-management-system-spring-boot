package com.example.LibraryManagement.repository;

import com.example.LibraryManagement.entity.RecordIssued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordIssuedRepository extends JpaRepository<RecordIssued,Long> {
}
