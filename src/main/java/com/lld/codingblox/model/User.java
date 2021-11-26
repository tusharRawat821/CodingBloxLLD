package com.lld.codingblox.model;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lld.codingblox.constants.Constants;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class User {
	
	private String username;
	
	@Setter
	private BigInteger score;
	
	private Map<Long, List<Long>> contestQuestions = new HashMap<>();
	
	public User(@NonNull String username) {
		this.username = username;
		this.score = Constants.DEFAULT_SCORE;
	}
	
}
