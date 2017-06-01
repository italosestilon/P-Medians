package metaheuristics.ga;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import problems.Evaluator;
import solutions.Solution;

public abstract class AbstractGA {

	@SuppressWarnings("serial")
	public class Population extends ArrayList<Chromosome> {
	}
	
	@SuppressWarnings("serial")
	public class Chromosome extends BitSet {
	}

	public static final Random rng = new Random(0);
	
	public static boolean verbose = true;
	
	protected Evaluator objFunction;

	protected int chromosomeSize;

	protected int popSize;

	protected int generations;
	
	protected double mutationRate;
	
	protected long time;
	
	protected abstract Solution decode(Chromosome chromosome);

	protected abstract Chromosome generateRandomChromosome();

	protected abstract int fitness(Chromosome chromosome);

	protected abstract void mutateGene(Chromosome chromosome, int locus);
	
	protected abstract Population initializeLatinHypercubePopulation();
	
	public AbstractGA(Evaluator objFunction, int generations, int factor, double mutationRate, long time) {
		this.objFunction = objFunction;
		this.generations = generations;
		int total = factor*(int)Math.log(objFunction.getDomainSize());		
		this.popSize = total+total%2;		
		this.mutationRate = mutationRate;
		this.chromosomeSize = objFunction.getDomainSize();
		this.time = time;
	}

	public Solution solve() {

		long limitTime = System.currentTimeMillis() + time;
		
		Chromosome bestChromosome = new Chromosome();
		int bestFitness = Integer.MAX_VALUE;
		
		Integer[] fitnessIndividual = new Integer[popSize];
		
        Population population = initializePopulation();
		
		for (int k = 0; k < generations && System.currentTimeMillis() < limitTime; ++k) {
			
			int incumbentFitness = Integer.MAX_VALUE;
			int incumbentIndex = 0;
			
			for(int i = 0; i < population.size(); ++i){
				
				fitnessIndividual[i] = fitness(population.get(i));
				
				if (fitnessIndividual[i] < incumbentFitness){
					incumbentFitness = fitnessIndividual[i];
					incumbentIndex = i;
				}
			}
			
			Chromosome incumbentChromosome = population.get(incumbentIndex);
			
			if (incumbentFitness < bestFitness) {
				
				bestChromosome = (Chromosome)incumbentChromosome.clone();
				bestFitness = incumbentFitness;
				
				if (verbose) {
					System.out.println("Best Sol = " + decode(bestChromosome));
				}
			}
			
			Population parents = selectParents(population, fitnessIndividual);
			Population offsprings = crossover(parents);
			Population mutants = mutate(offsprings);
			
			population = mutants;
						
			for (int i = 0; i < population.size(); ++i) {
				fitnessIndividual[i] = fitness(population.get(i));
			}
		}
		
		return decode(bestChromosome);
	}

	protected Population initializePopulation() {

		//Population population = new Population();
		//while (population.size() < popSize) {
		//	population.add(generateRandomChromosome());
		//}
		//return population;
		
		return initializeLatinHypercubePopulation();
	}

	protected Population selectParents(Population population, Integer[] fitnessIndividual) {

		Population parents = new Population();

		while (parents.size() < popSize) {
			
			int index1 = rng.nextInt(popSize);
			int index2 = rng.nextInt(popSize);
			
			Chromosome parent1 = population.get(index1);
			Chromosome parent2 = population.get(index2);
			
			if (fitnessIndividual[index1] < fitnessIndividual[index2]) {
				parents.add(parent1);
			} else {
				parents.add(parent2);
			}
		}

		return parents;
	}

	protected Population crossover(Population parents) {

		Population offsprings = new Population();

		for (int i = 0; i < popSize; i = i + 2) {

			Chromosome parent1 = parents.get(i);
			Chromosome parent2 = parents.get(i + 1);

			int crosspoint = rng.nextInt(chromosomeSize + 1);

			Chromosome offspring1 = new Chromosome();
			Chromosome offspring2 = new Chromosome();

			for (int j = 0; j < chromosomeSize; j++) {
				if (j >= crosspoint) {
					offspring1.set(j, parent2.get(j));
					offspring2.set(j, parent1.get(j));
				} else {
					offspring1.set(j, parent1.get(j));
					offspring2.set(j, parent2.get(j));
				}
			}
			
			offsprings.add(offspring1);
			offsprings.add(offspring2);
		}

		return offsprings;
	}

	protected Population mutate(Population offsprings) {

		for (Chromosome c : offsprings) {
			for (int locus = 0; locus < chromosomeSize; locus++) {
				if (rng.nextDouble() < mutationRate) {
					mutateGene(c, locus);
				}
			}
		}

		return offsprings;
	}
}
