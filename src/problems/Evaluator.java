package problems;

import solutions.Solution;

public interface Evaluator {
	
	public abstract int getNumberOfMedians();

	public abstract int getDomainSize();

	public abstract int evaluate(Solution sol);
}
