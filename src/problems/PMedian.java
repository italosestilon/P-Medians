package problems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

import solutions.Solution;

public class PMedian implements Evaluator {
		
	protected int medians; // number of medians
	protected int nodes;   // number of nodes
	
	protected Integer[][] dist; 
	
	public PMedian(String filename) throws IOException {
		readInput(filename);
	}
	
	@Override
	public int getNumberOfMedians() {
		return medians;
	}
	
	@Override
	public int getDomainSize() {
		return nodes;
	}
	
	@Override
	public int evaluate(Solution sol) {
		
		int sum = 0;
		
		for (int i = 0; i < nodes; ++i) {
			
			int min = Integer.MAX_VALUE;

			for (int j : sol) {	
				if (dist[i][j] < min) min = dist[i][j];
			}

			sum += min;
		}
		
		return (sol.cost = sum);
	}
	
	protected void readInput(String filename) throws IOException { // capture values from input file
		
		Reader fileInst = new BufferedReader(new FileReader(filename));
		StreamTokenizer stok = new StreamTokenizer(fileInst);
		
		stok.nextToken();
		nodes = (int)stok.nval;
		
		stok.nextToken();
		medians = (int)stok.nval;
		
		Integer[] X =  new Integer[nodes];
		Integer[] Y =  new Integer[nodes];
		
		dist = new Integer[nodes][nodes];
		
		for (int i = 0; i < nodes; ++i) {
			stok.nextToken();
			X[i] = (int)stok.nval;
			stok.nextToken();
			Y[i] = (int)stok.nval;
		}
		
		for (int i = 0; i < nodes; ++i) { // otbém a distância entre os nodes
			for(int j = i+1; j < nodes; ++j) {
				double hypot = (double)Math.hypot(X[i]-X[j], Y[i]-Y[j]);
				int distance = (int)Math.floor(hypot+0.5);
				dist[i][j] = distance;
				dist[j][i] = distance;
			}
			dist[i][i] = 0;
		}
	}

}
