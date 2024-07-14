package com.assignment.userservice.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEvent {

	private String eventType;
    private String username;
}
