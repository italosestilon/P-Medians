package problems.solvers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;

import metaheuristics.ga.AbstractGA;
import problems.PMedian;
import solutions.Solution;

public class GA_PMedian extends AbstractGA {

	protected HashMap<BitSet,Integer> myhash;
	
	public GA_PMedian(int generations, int factor, double mutationRate, String filename, long time) throws IOException {
		super(new PMedian(filename), generations, factor, mutationRate, time);
		myhash = new HashMap<BitSet,Integer>();
	}

	@Override
	protected Solution decode(Chromosome chromosome) {
		
		Solution solution = new Solution();
		
		for (int locus = 0; locus < chromosome.size(); ++locus) {
			if (chromosome.get(locus)) {
				solution.add(locus);
			}
		}
		
		if (solution.size() != objFunction.getNumberOfMedians()) {
			repair(chromosome, solution);
		}
		
		if (myhash.containsKey(chromosome)) {
			int value = myhash.get(chromosome);
			solution.cost = value;
		}
		else {		
			objFunction.evaluate(solution);
			myhash.put(chromosome, solution.cost);
		}
		
		return solution;
	}

	@Override
	protected Chromosome generateRandomChromosome() {

		Chromosome chromosome = new Chromosome();
		
		for (int i = 0; i < chromosomeSize; ++i) {
			boolean value = rng.nextBoolean();
			chromosome.set(i, value);
		}
		
		return chromosome;
	}

	@Override
	protected int fitness(Chromosome chromosome) {
		return decode(chromosome).cost;
	}

	@Override
	protected void mutateGene(Chromosome chromosome, int locus) {
		boolean value = chromosome.get(locus);
		int newLocus;

		newLocus = locus;
		while(chromosome.get(newLocus) == value){
			newLocus = rng.nextInt(chromosomeSize);
		}


		chromosome.set(locus, !value);
		chromosome.set(newLocus, value);
	}

	protected void repair(Chromosome chromosome, Solution solution) {
		
		int medians = objFunction.getNumberOfMedians();
		int nodes = objFunction.getDomainSize();
		Solution fake = new Solution();
		
        while (solution.size() < medians) { // greedy
        	
        	int min = Integer.MAX_VALUE;
        	int k = -1;
        	
        	for (int i = 0; i < nodes; ++i) {
        		
        		if (chromosome.get(i)) continue;
        		
        		fake.clear();
        		fake.add(i);
    
                Integer cost = objFunction.evaluate(fake);
   
                if (cost < min) { min = cost; k = i; }
        	}

        	chromosome.set(k);
        	solution.add(k);
        }
        
        while (solution.size() > medians) { // stingy
        	
        	int max = Integer.MIN_VALUE;
        	int k = -1;
        	
        	for (int i : solution) {
        		
           		fake.clear();
        		fake.add(i);
    
                Integer cost = objFunction.evaluate(fake);
        		
                if (cost > max) { max = cost; k = i; }
        	}
  
        	chromosome.clear(k);
        	solution.remove(new Integer(k));
        }
	}
	
	protected Population initializeLatinHypercubePopulation() {

		ArrayList<Boolean> col = new ArrayList<Boolean>();
		Population population = new Population();
		
		for (int i = 0; i < popSize; ++i) {
			population.add(new Chromosome());
			boolean valeu = (i%2) == 1;
			col.add(new Boolean(valeu));
		}
		
		for (int i = 0; i < chromosomeSize; ++i) {
			Collections.shuffle(col, rng);
			for (int j = 0; j < popSize; ++j) {
				boolean value = col.get(j);
				population.get(j).set(i, value);				
			}
		}
		
		return population;
	}
	
	public static void main(String[] args) throws IOException {

		String filename = "instances/instance2.pmp";
		int factor = 5;
		double rate = 0.2;
		long limitTime = 30*60*1000;
		GA_PMedian ga = new GA_PMedian(10000, factor, rate, filename, limitTime);
		long startTime = System.currentTimeMillis();
		Solution bestSol = ga.solve();
		System.out.println("Min Cost = " + bestSol);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time = " + (double) totalTime / (double) 1000 + " seg");
		
		/*String filename = args[0];
		int factor = Integer.parseInt(args[1]);
		double rate = Double.parseDouble(args[2]);
		long limitTime = 5*60*1000;
		
		long startTime = System.currentTimeMillis();
		GA_PMedian ga = new GA_PMedian(10000, factor, rate, filename, limitTime);
		Solution bestSol = ga.solve();
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(bestSol.cost+" "+(double) totalTime / (double) 1000);*/
	}
}
