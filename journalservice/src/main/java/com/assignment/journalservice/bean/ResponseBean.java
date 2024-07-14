package com.assignment.journalservice.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseBean {

	private Object data;
	private boolean isValid;
	private String message="No Data";
}
