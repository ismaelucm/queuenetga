package com.massisframework.massis3.examples.genetic;

public class GeneticAlSetup {
    private int _populationSize;
    private int _chromosomeLenght;

    private float _mutationRate;
    private float _crossoverRate;
    private int _elitism;

    private int _numMaxGeneratoin;


    public GeneticAlSetup(float _mutationRate, float _crossoverRate, int _populationSize, int chromosomeLenght, int _numMaxGeneratoin, int _elitism) {
        this._mutationRate = _mutationRate;
        this._crossoverRate = _crossoverRate;
        this._populationSize = _populationSize;
        this._numMaxGeneratoin = _numMaxGeneratoin;
        this._elitism = _elitism;
        this._chromosomeLenght = chromosomeLenght;
    }


    public float getMutationRate() {
        return _mutationRate;
    }

    public void setMutationRate(float _mutationRate) {
        this._mutationRate = _mutationRate;
    }

    public float getCrossoverRate() {
        return _crossoverRate;
    }

    public void setCrossoverRate(float _crossoverRate) {
        this._crossoverRate = _crossoverRate;
    }

    public int getPopulationSize() {
        return _populationSize;
    }

    public void setPopulationSize(int _populationSize) {
        this._populationSize = _populationSize;
    }

    public int getNumMaxGeneratoin() {
        return _numMaxGeneratoin;
    }

    public void setNumMaxGeneratoin(int _numMaxGeneratoin) {
        this._numMaxGeneratoin = _numMaxGeneratoin;
    }

    public int getElitism() {
        return _elitism;
    }

    public void setElitism(int _elitism) {
        this._elitism = _elitism;
    }

    public int getChromosomeLenght() {
        return this._chromosomeLenght;
    }

    public void setChromosomeLenght(int chromosomeLenght) {
        _chromosomeLenght = chromosomeLenght;
    }
}
