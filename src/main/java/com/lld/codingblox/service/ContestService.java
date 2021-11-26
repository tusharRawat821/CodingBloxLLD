package com.lld.codingblox.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.lld.codingblox.model.Contest;
import com.lld.codingblox.model.ContestStatus;
import com.lld.codingblox.model.Level;
import com.lld.codingblox.model.Question;
import com.lld.codingblox.model.User;
import com.lld.codingblox.repo.ContestRepo;
import com.lld.codingblox.repo.QuestionRepo;
import com.lld.codingblox.repo.UserRepo;
import com.lld.codingblox.strategy.ScoreStrategy;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ContestService {
	
	private ContestRepo contestRepo;
	private QuestionRepo questionRepo;
	private UserService userService;
	private UserRepo userRepo;
	private ScoreStrategy scoreStrategy;

	
	public Contest createContest(String name, Level level, String username) {
		List<Long> contestsQuestions = selectContestQuestionBasedOnLevel(level);
		Contest newContest = contestRepo.save(name, level, contestsQuestions, username);
		// host is by-default attend the contest.
		userService.attendContest(newContest.getId(), username);
		return newContest;
	}
	
	/**
	 * Implementation is important
	 * @param id
	 * @param username
	 */
	public void runContest(Long id, String username) {
		// Approach:
			// validate if user is correct
			// validate if contest is not ACTIVE and ENDED
			// change contest status = ACTIVE
			// get all users participating in this contest
			// pick random questions from contest and assign them as solved questions for user. Do it for all contestUsers.
			// update their final score from the score they earned in this round based on the strategy
			// change contest status = finished
		
		validateContestAndUser(id, username);
		
		Contest contest = contestRepo.findById(id);
		
		contest.setStatus(ContestStatus.ACTIVE);
		
		List<User> allContestUsers = userService.getAllContestUser(contest.getId());
		allContestUsers.forEach((user) -> {
			userService.setUserContestQuestions(user, contest);
		});
		allContestUsers.forEach((user) -> {
			final List<Question> solvedQuestions = getUserQuestions(user, contest.getId());
			calculateUserNewScore(user, solvedQuestions, contest.getLevel());
		});
		
		contest.setStatus(ContestStatus.ENDED);
	}
	
    private List<Question> getUserQuestions(User user, Long contestId) {
        final List<Long> questions = user.getContestQuestions().get(contestId);
        if (questions == null) 
        	return new ArrayList<>();
        return questions.stream().map(i -> questionRepo.findBy(i)).toList();
    }
	
	private void calculateUserNewScore(User user, List<Question> solvedQuestions, Level level) {
		BigInteger currentContestScore = solvedQuestions.stream()
									.map(question -> BigInteger.valueOf(question.getScore()))
									.reduce(BigInteger::add).orElseGet(() -> BigInteger.ZERO);
		BigInteger currentUserScore = user.getScore();
		BigInteger newScore = currentUserScore.add(scoreStrategy.getFinalContestScore(currentContestScore, level));
		user.setScore(newScore);
	}
	
	public void listContest(Level level) {
		List<Contest> contests = null;
		if(level == null)
			contests = contestRepo.getAll();
		else
			contests = contestRepo.getAllBy(level);
		
		// logging
		for(Contest contest: contests) {
			System.out.println(contest);
		}
	}
	
	
	
	
	
	private void validateContestAndUser(Long id, String username) {
		User user = userRepo.findById(username);
		if(user == null)
			throw new RuntimeException("user: " + username + " does not exist");
		
		Contest contest = contestRepo.findById(id);
		if(contest == null)
			throw new RuntimeException("contest: " + id + " does not exist");
		
		if(contest.getStatus() == ContestStatus.ACTIVE)
			throw new RuntimeException(contest + " is already in progress.");
		if(contest.getStatus() == ContestStatus.ENDED)
			throw new RuntimeException(contest + " is over.");
		
		if(!contest.getHostUsername().equals(username))
			throw new RuntimeException(username  + " is not authorized to start the contest:" + contest);
		
	}
	
	private List<Long> selectContestQuestionBasedOnLevel(Level level) {
		return questionRepo.getAllBy(level).stream().map(question -> question.getId()).toList();
	}

	
	
}
