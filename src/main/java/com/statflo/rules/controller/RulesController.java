package com.statflo.rules.controller;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.statflo.rules.bean.RulesRequest;

@RestController
public class RulesController {
	
	@Autowired
	private Rules rules;
	
	@GetMapping("invoke-rules")
	public Boolean invokeRules(@RequestParam String role, @RequestParam String recordSource) {
		RulesRequest request = RulesRequest.builder().role(role).recordSource(recordSource).build();
		Facts facts = new Facts();
		facts.put("request", request);
		facts.put("rules_result", true);
		RulesEngine rulesEngine = new DefaultRulesEngine();
		rulesEngine.fire(rules, facts);
		return facts.get("rules_result");
	}

}
