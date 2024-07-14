package com.assignment.journalservice.bean;

import lombok.Data;

@Data
public class Journal {
    private int id;
    private String eventMessage;
    private String createdAt;

}
