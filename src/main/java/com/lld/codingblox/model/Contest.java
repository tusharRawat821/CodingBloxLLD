package com.lld.codingblox.model;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Contest {
	private Long id;
	
	@Setter
	private String name;
	
	private Level level;
	private List<Long> questions;
	@Setter
	private ContestStatus status;
	
	private String hostUsername; // creater of contest
	
	public Contest(@NonNull Long id, @NonNull String name, @NonNull String hostUsername, @NonNull Level level, @NonNull List<Long> questions) {
		this.id = id;
		this.name = name;
		this.hostUsername = hostUsername;
		this.level = level;
		this.questions = questions;
		this.status = ContestStatus.NOT_STARTED;
	}
	
}
