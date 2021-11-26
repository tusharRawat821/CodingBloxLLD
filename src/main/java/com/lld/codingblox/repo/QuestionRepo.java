package com.lld.codingblox.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lld.codingblox.model.Level;
import com.lld.codingblox.model.Question;

public class QuestionRepo {
	
	private Map<Long, Question> questionDB = new HashMap<>();
	
	public void save(String statement, Level level, Integer score) {
		Long generatedQuesId = generateId();
		questionDB.put(generatedQuesId, new Question(generatedQuesId, statement, level, score));
	}
	
	public List<Question> getAll() {
		return questionDB.values().stream().collect(Collectors.toList());
	}
	
	public List<Question> getAllBy(Level level) {
		return questionDB.values().stream().filter(question -> {
			return question.getLevel() == level;
		}).collect(Collectors.toList());
	}
	
	public Question findBy(Long id) {
		return questionDB.get(id);
	}
	
	private Long generateId() {
		return Long.valueOf(questionDB.size()) + 1;
	}
	
}
