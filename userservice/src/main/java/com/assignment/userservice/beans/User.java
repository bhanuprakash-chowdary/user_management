package com.assignment.userservice.beans;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class User {
	
	private String userEmail;
	private String userId;
	private String userMobileNumber;
	private String userName;
	private String userPassword;
	private String userRole;
	private Set<String> roles;
}
