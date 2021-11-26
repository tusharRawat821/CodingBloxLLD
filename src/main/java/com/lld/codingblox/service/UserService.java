package com.lld.codingblox.service;

import java.util.List;

import com.lld.codingblox.model.Contest;
import com.lld.codingblox.model.ContestStatus;
import com.lld.codingblox.model.User;
import com.lld.codingblox.repo.ContestRepo;
import com.lld.codingblox.repo.UserRepo;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class UserService {
	
	private UserRepo userRepo;
	private ContestRepo contestRepo;
	private QuestionService questionService;
	
	public void createUser(String username) {
		userRepo.save(username);	
	}
	
	public void attendContest(Long contestId, String username) {
		validateUserAndContest(contestId, username);
		User user = userRepo.findById(username);
		if(user.getContestQuestions().containsKey(contestId))
			return;	// already registered.
		
		user.getContestQuestions().put(contestId, null);
	}
	
	public void showLeaderBoard(String order) {
		System.out.println("---------------LeaderBoard-------------");
		for(User user: userRepo.getAllSortByScore(order)) {
			System.out.println(user.getUsername() + ": " + user.getScore());
		}
		System.out.println("----------------------------------------");
	}
	
	
	public List<User> getAllContestUser(Long id) {
		return userRepo.getAll().stream()
				.filter(user -> { 
					return user.getContestQuestions().containsKey(id); 
				}).toList();
	}
	
	public void setUserContestQuestions(User user, Contest contest) {
		List<Long> solvedQuestions = questionService.selectSolvedContestQuestions(contest);
		if(user.getContestQuestions().get(contest.getId()) != null)
			return;
		
		user.getContestQuestions().put(contest.getId(), solvedQuestions);
	}
	
	// Additional functionality
	public void withdrawContest(Long contestId, String username) {
		validateUserAndContest(contestId, username);
		Contest contest = contestRepo.findById(contestId);
		if(contest.getHostUsername().equals(username))
			throw new RuntimeException("Host: "+username+" cannot withdraw from contest");
		
		if(contest.getStatus() == ContestStatus.ACTIVE || contest.getStatus() == ContestStatus.ENDED)
			throw new RuntimeException("Cannot withdraw now");
		User user = userRepo.findById(username);
		if(user.getContestQuestions().containsKey(contestId))
			user.getContestQuestions().remove(contestId);
		else
			return;
	}
	
	private void validateUserAndContest(Long contestId, String username) {
		validateUser(username);
		validateContest(contestId);
	}
	
	private void validateContest(Long id) {
		if(contestRepo.findById(id) == null)
			throw new RuntimeException("contest: "+id+" does not exist");
	}

	private void validateUser(String username) {
		if(userRepo.findById(username) == null)
			throw new RuntimeException("user: "+username+" does not exist");
	}
	
	public List<User> getAllUsers() {
		return userRepo.getAll();
	}
	
}
