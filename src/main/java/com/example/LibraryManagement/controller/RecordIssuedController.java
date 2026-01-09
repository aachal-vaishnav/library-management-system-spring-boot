package com.example.LibraryManagement.controller;

import com.example.LibraryManagement.entity.RecordIssued;
import com.example.LibraryManagement.service.RecordIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issuerecords")
public class RecordIssuedController {

    @Autowired
    RecordIssueService recordIssueService;

    @PostMapping("/issuebook/{bookId}")
    public ResponseEntity<RecordIssued> issueTheBook(@PathVariable Long bookId){
        return ResponseEntity.ok(recordIssueService.issueTheBook(bookId));
    }

    @PostMapping("/returnbook/{issueRecordId}")
    public ResponseEntity<RecordIssued> returnTheBook(@PathVariable Long issueRecordId){
        return ResponseEntity.ok(recordIssueService.returnTheBook(issueRecordId));
    }
}
