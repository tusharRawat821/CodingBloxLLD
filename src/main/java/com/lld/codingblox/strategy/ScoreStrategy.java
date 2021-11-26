package com.lld.codingblox.strategy;

import java.math.BigInteger;

import com.lld.codingblox.model.Level;

public interface ScoreStrategy {
	public BigInteger getFinalContestScore(BigInteger currContestScore, Level level);
}
