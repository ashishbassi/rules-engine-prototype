package com.statflo.rules.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeasy.rules.api.Fact;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.RuleBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.statflo.rules.bean.RuleData;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RulesConfig {
	
	@Bean
	public Rules readRules() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<RuleData> rules = mapper.readValue(this.getClass().getClassLoader().getResourceAsStream("rules.json"), new TypeReference<List<RuleData>>() {});
		Set<Rule> ruleSet = new HashSet<>();
		for (RuleData ruleData : rules) {
			Rule rule = new RuleBuilder().name(ruleData.getName())
					.description(ruleData.getDescription())
					.when(facts -> parseExpression(ruleData.getName(), facts, ruleData.getExpression()))
					.then(facts -> then(ruleData.getName(), facts))
					.build();
			ruleSet.add(rule);
		}
		Rules statfloRules = new Rules(ruleSet);
		return statfloRules;
	}
	
	private void then(String ruleName, Facts facts) {
		log.error(ruleName + " - Then------");
		facts.add(new Fact<Boolean>("rules_result", false));
	}
	
	private boolean parseExpression(String ruleName, Facts facts, String expression) {
		ExpressionParser expressionParser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext(facts.get("request"));
		boolean eval = (boolean) expressionParser.parseExpression(expression).getValue(context);
		log.error(ruleName + " : Expression result : " + eval);
		return !eval;
	}

}
