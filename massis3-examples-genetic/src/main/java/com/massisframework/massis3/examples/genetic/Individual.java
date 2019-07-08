package com.massisframework.massis3.examples.genetic;


import java.util.ArrayList;
import java.util.List;

public class Individual<T extends Gene> {

    List<T> _genotype;
    float _fitness;
    int _chromosomeLeng;


    public Individual(int chromosomeLengt) {
        _chromosomeLeng = chromosomeLengt;
        _genotype = new ArrayList<>();
    }


    public Individual(Individual<T> individual) {
        this(individual.size());
        for (int i = 0; i < individual.size(); ++i) {
            _genotype.add(individual.getGen(i));
        }

        _fitness = individual.getFitness();
    }

    public List<T> getGenotype() {
        return _genotype;
    }

    public void add(T gene) {
        _genotype.add(gene);
    }

    public void randomInit(IProblem<T> problem) {
        for (int i = 0; i < _chromosomeLeng; ++i) {
            _genotype.add(problem.createRandomGene());
        }
        //Generación aleatoria....
    }

    public T getGen(int i) {
        return _genotype.get(i);
    }

    public void setGen(int position, T gen) {
        _genotype.set(position, gen);
    }

    public void setFitness(float f) {
        _fitness = f;
    }

    public float getFitness() {
        return _fitness;
    }

    public int size() {
        return _genotype.size();
    }

    public Individual clone() {
        return new Individual(this);
    }

    public void show() {
        for (int i = 0; i < _chromosomeLeng; ++i) {
            _genotype.get(i).showGene();
        }
        System.out.println("fitness " + _fitness);
        System.out.println("");
    }

    public String serializePlain() {
        String file = "";
        for (int i = 0; i < _chromosomeLeng; ++i) {
            String line = _genotype.get(i).setializate();
            file += line + "\n";
        }
        return file;
    }
}
