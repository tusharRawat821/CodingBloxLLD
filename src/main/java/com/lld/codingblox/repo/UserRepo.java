package com.lld.codingblox.repo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lld.codingblox.model.User;

public class UserRepo {
	private Map<String, User> usersDB = new HashMap<>();

	public void save(String username) {
		if (usersDB.get(username) != null)
			throw new RuntimeException(username + " already exists");
		usersDB.put(username, new User(username));
	}
	
	public User findById(String username) {
		return usersDB.get(username);
	}
	
	public List<User> getAll() {
		return usersDB.values().stream().collect(Collectors.toList());
	}
	
	public List<User> getAllSortByScore(String order) {
		List<User> users = getAll();
		Collections.sort(users, (u1, u2) -> {
			Integer score1 = Integer.valueOf(String.valueOf(u1.getScore()));
			Integer score2 = Integer.valueOf(String.valueOf(u1.getScore()));
			return order.equals("asc") ? score1 - score2 : score2 - score1;
		});
		return users;
	}

}
