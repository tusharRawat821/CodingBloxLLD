package com.lld.codingblox.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lld.codingblox.model.Contest;
import com.lld.codingblox.model.Level;
import com.lld.codingblox.model.Question;
import com.lld.codingblox.repo.QuestionRepo;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class QuestionService {

	private QuestionRepo questionRepo;

	public void createQuestion(String statement, Level level, Integer score) {
		questionRepo.save(statement, level, score);
	}

	public void listQuestions(Level level) {
		List<Question> questions = null;
		if (level == null)
			questions = questionRepo.getAll();
		else
			questions = questionRepo.getAllBy(level);

		// logging
		for (Question question : questions) {
			System.out.println(question);
		}
	}

	public List<Long> getAllQuestionLeveWise(@NonNull Level level) {
		return questionRepo.getAllBy(level).stream().map(question -> question.getId()).toList();
	}
	
	public List<Long> selectSolvedContestQuestions(@NonNull Contest contest) {
		int numQuestions = contest.getQuestions().size();
		List<Long> solvedQuestionsIndices = getSolvedQuestionIndices(numQuestions);
		List<Long> solvedQuestions = new ArrayList<>();
		final List<Long> contestQuestions = contest.getQuestions();
		for (Long index : solvedQuestionsIndices) {
			final Long actualQuesId = contestQuestions.get(Integer.valueOf(String.valueOf(index)));
			solvedQuestions.add(actualQuesId);
		}
		return solvedQuestions;
	}

	private List<Long> getSolvedQuestionIndices(int totalQuestionCount) {
		final Random random = new Random();
		int userQuestionCount = random.nextInt(totalQuestionCount + 1);
		while (userQuestionCount == 0) {
			userQuestionCount = random.nextInt(totalQuestionCount + 1);
		}
		return findRandomHelper(userQuestionCount, totalQuestionCount);
	}

	private List<Long> findRandomHelper(int size, int bound) {
		List<Long> randomIndices = new ArrayList<>();
		final Random random = new Random();
		while (randomIndices.size() != size) {
			Long randomIndex = Long.valueOf(random.nextInt(bound));
			if (!randomIndices.contains(randomIndex)) {
				randomIndices.add(randomIndex);
			}
		}
		return randomIndices;
	}

}
