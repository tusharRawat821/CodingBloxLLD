package com.lld.codingblox.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Question {
	
	private Long id;
	
	@Setter
	private String statement;
	@Setter
	private Level level;
	@Setter
	private Integer score;
	
	public Question(@NonNull Long id, @NonNull String statement, @NonNull Level level, @NonNull Integer score) {
		this.id = id;
		this.statement = statement;
		this.level = level;
		this.score = score;
	}
	
}
