package com.statflo.rules.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RulesRequest {
	
	private String role;
	
	private String recordSource;

}
