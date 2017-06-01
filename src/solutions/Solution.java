package solutions;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Solution extends ArrayList<Integer> {
	
	public int cost = Integer.MAX_VALUE;
	
	public Solution() {
		super();
	}
	
	public Solution(Solution sol) {
		super(sol);
		cost = sol.cost;
	}

	@Override
	public String toString() {
		return "{ cost = " + cost + ", size = " + this.size() + ", elements = " + super.toString() + " }";
	}

}

