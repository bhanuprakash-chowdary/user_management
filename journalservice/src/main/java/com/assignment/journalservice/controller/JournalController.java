package com.assignment.journalservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.journalservice.bean.ResponseBean;
import com.assignment.journalservice.service.JournalService;

@RestController
@RequestMapping("/api/journals")
public class JournalController {

	@Autowired
    private JournalService journalService;

    @GetMapping
    public ResponseBean getAllJournals() {
        return journalService.getJournals();
    }

    @GetMapping("/{id}")
    public ResponseBean getJournalById(@PathVariable Long id) {
        return journalService.getJournalById(id);
    }
}

