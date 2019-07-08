package com.massisframework.massis3.examples.genetic;

import com.massisframework.massis3.examples.genetic.Exceptions.SubGenotypeException;

import java.util.ArrayList;
import java.util.List;

public class MultiGenotypeIndividual {
    List<List<Gene>> _genotype;
    int[] _chromosomesLengs;
    float _fitness;

    public MultiGenotypeIndividual(int... genSizes) {
        _chromosomesLengs = new int[genSizes.length];
        _genotype = new ArrayList<>();
        for (int i = 0; i < genSizes.length; ++i) {
            _chromosomesLengs[i] = genSizes[i];
            List<Gene> list = new ArrayList<>();
            _genotype.add(list);
        }
        _fitness = 0f;
    }

    public MultiGenotypeIndividual(MultiGenotypeIndividual individual) {

    }

    public <T extends Gene> T getGeneCast(int index, int gen) throws SubGenotypeException {
        if (index >= _genotype.size())
            throw new SubGenotypeException("The subGenotype " + index + " is not found");
        return (T) _genotype.get(index).get(gen);
    }

    public Gene getGene(int index, int gen) throws SubGenotypeException {
        if (index >= _genotype.size())
            throw new SubGenotypeException("The subGenotype " + index + " is not found");
        return _genotype.get(index).get(gen);
    }

    public <T extends Gene> List<T> getSubGenotypeCast(int index) throws SubGenotypeException {
        if (index >= _genotype.size())
            throw new SubGenotypeException("The subGenotype " + index + " is not found");
        return (List<T>) _genotype.get(index);
    }

    public List<Gene> getSubGenotype(int index) throws SubGenotypeException {
        if (index >= _genotype.size())
            throw new SubGenotypeException("The subGenotype " + index + " is not found");
        return _genotype.get(index);
    }

    public void addGen(int index, Gene gene) throws SubGenotypeException {
        if (index >= _genotype.size())
            throw new SubGenotypeException("The subGenotype " + index + " is not found");
        _genotype.get(index).add(gene);
    }

    public void set(int index, List<Gene> gene) throws SubGenotypeException {
        if (index >= _genotype.size())
            throw new SubGenotypeException("The subGenotype " + index + " is not found");
        _genotype.set(index, gene);
    }

    public void randomInit(IProblemMultyType problem) {
        for (int i = 0; i < _genotype.size(); ++i) {
            _genotype.get(i).add(problem.createRandomGene(i));
        }
        //Generación aleatoria....
    }

    public void setFitness(float f) {
        _fitness = f;
    }

    public float getFitness() {
        return _fitness;
    }

    public int numSubgenotypes() {
        return _genotype.size();
    }

    public int size(int i) throws SubGenotypeException {
        if (i >= _genotype.size())
            throw new SubGenotypeException("The subGenotype " + i + " is not found");
        return _chromosomesLengs[i];
    }

    public MultiGenotypeIndividual clone() {
        return new MultiGenotypeIndividual(this);
    }

    public void show() {
        for (int i = 0; i < _genotype.size(); ++i) {
            for (int j = 0; j < _genotype.get(i).size(); ++j) {
                _genotype.get(i).get(j).showGene();
            }
        }
        System.out.println("fitness " + _fitness);
        System.out.println("");
    }

    public String serializePlain(String genDelimeter) {
        String file = "";

        for (int i = 0; i < _genotype.size(); ++i) {
            String line = "";
            for (int j = 0; j < _genotype.get(i).size(); ++j) {
                line += _genotype.get(i).get(j).setializate();
                line += line + genDelimeter;
            }
            file += line + "\n";
        }
        return file;
    }
}