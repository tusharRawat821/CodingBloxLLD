package com.lld.codingblox.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lld.codingblox.model.Contest;
import com.lld.codingblox.model.Level;

public class ContestRepo {
	private Map<Long, Contest> contestDB = new HashMap<>();

	public Contest findById(Long contestId) {
		return contestDB.get(contestId);
	}
	
	public Contest save(String name, Level level, List<Long> questions, String username) {
		Long generatedContestId = generateId();
		Contest newContest = new Contest(generatedContestId, name, username, level, questions);
		contestDB.put(generatedContestId, newContest);
		return newContest;
	}
	
	public List<Contest> getAll() {
		return contestDB.values().stream().toList();
	}
	
	public List<Contest> getAllBy(Level level) {
		return contestDB.values().stream().filter(contest -> contest.getLevel() == level).toList();
	}
	
	private Long generateId() {
		return Long.valueOf(contestDB.size()) + 1;
	}
	
}
