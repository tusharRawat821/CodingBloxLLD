package com.lld.codingblox.strategy;

import java.math.BigInteger;

import com.lld.codingblox.model.Level;

public class DefaultScoreStrategy implements ScoreStrategy {

	public BigInteger getFinalContestScore(BigInteger currContestScore, Level level) {

		BigInteger penalty = null;
		switch (level) {
		case LOW:
			penalty = BigInteger.valueOf(50);
			break;
		case MEDIUM:
			penalty = BigInteger.valueOf(30);
			break;
		case HIGH:
			penalty = BigInteger.ZERO;
			break;
		default:
			break;
		}
		
		return currContestScore.subtract(penalty);
	}
}
