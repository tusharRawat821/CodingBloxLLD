package com.lld.codingblox;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lld.codingblox.model.Contest;
import com.lld.codingblox.model.Level;
import com.lld.codingblox.repo.ContestRepo;
import com.lld.codingblox.repo.QuestionRepo;
import com.lld.codingblox.repo.UserRepo;
import com.lld.codingblox.service.ContestService;
import com.lld.codingblox.service.QuestionService;
import com.lld.codingblox.service.UserService;
import com.lld.codingblox.strategy.DefaultScoreStrategy;
import com.lld.codingblox.strategy.ScoreStrategy;

class AppTest {
	
	private ContestService contestService;
	private UserService userService;
	private QuestionService questionService;
	
	@BeforeEach
	void setUp() {
		ContestRepo contestRepo = new ContestRepo();
		QuestionRepo questionRepo = new QuestionRepo();
		UserRepo userRepo = new UserRepo();
		
		ScoreStrategy scoreStrategy = new DefaultScoreStrategy();
		
		questionService = new QuestionService(questionRepo);
		userService = new UserService(userRepo, contestRepo, questionService);
		contestService = new ContestService(contestRepo, questionRepo, userService, userRepo, scoreStrategy);
	}
	
	@Test
	void codingBloxAppTest () {
		String Ross = "Ross";
		String Monica = "Monica";
		String Joey = "Joey";
		String Chandler = "Chandler";
		userService.createUser(Ross);
		userService.createUser(Monica);
		userService.createUser(Joey);
		userService.createUser(Chandler);
		
		System.out.println(userService.getAllUsers());
		
		String Q1 = "Q1";
		String Q2 = "Q2";
		String Q3 = "Q3";
		String Q4 = "Q4";
		String Q5 = "Q5";
		String Q6 = "Q6";
		questionService.createQuestion(Q1, Level.LOW, 20);
		questionService.createQuestion(Q2, Level.MEDIUM, 30);
		questionService.createQuestion(Q3, Level.HIGH, 50);
		questionService.createQuestion(Q4, Level.HIGH, 30);
		questionService.createQuestion(Q5, Level.HIGH, 40);
		questionService.createQuestion(Q6, Level.LOW, 25);
		
		System.out.println("all questions");
		questionService.listQuestions(null);
		System.out.println("High level questions");
		questionService.listQuestions(Level.HIGH);
		
		Contest contest1 =  contestService.createContest("contest-christmas", Level.MEDIUM, "Joey");
		Contest contest2 = contestService.createContest("contest-newyear", Level.HIGH, "Chandler");
		
		contestService.listContest(Level.LOW);
		contestService.listContest(Level.MEDIUM);
		contestService.listContest(Level.HIGH);
		
		userService.attendContest(contest1.getId(), Ross);
		userService.attendContest(contest1.getId(), Joey);
		userService.attendContest(contest2.getId(), Chandler);
		userService.attendContest(contest2.getId(), Monica);
		
		System.out.println("updated users ---");
		System.out.println(userService.getAllUsers());

		userService.showLeaderBoard("asc");
		
		assertThrows(RuntimeException.class, () -> contestService.runContest(contest1.getId(), "non-owner"));
		
		System.out.println("contest-1 started");
		contestService.runContest(contest1.getId(), contest1.getHostUsername());
		System.out.println("contest-1 over");
		

		System.out.println("contest-2 started");
		contestService.runContest(contest2.getId(), contest2.getHostUsername());
		System.out.println("contest-2 over");
		
		userService.showLeaderBoard("asc");
	}
	
}

