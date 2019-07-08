package com.massisframework.massis3.examples.simulator.Components.HelpperClasses;

public class GeneticMaths {

    public static final int ONE_MINUS = 0;
    public static final int INVERSE = 1;
    public static final int SIGMOIDAL = 2;
    public static final int DIRECTED = 3;

    public static double log(double base, double x)
    {
        double num = Math.log(x);
        double den = Math.log(base);
        return num/den;
    }

    public static int diversityFuntion(int min, int max, float populationDiversity, int METHOD)
    {
        float f = 0f;
        switch(METHOD) {
            case ONE_MINUS:
                f = 1-populationDiversity;
                break;
            case DIRECTED:
                f = populationDiversity;
                break;
        }

        return Math.round ((max-min)*(f)+min);
    }

    public static float diversityFuntion(float min, float max, float populationDiversity, int METHOD)
    {
        float f = 0f;
        switch(METHOD) {
            case ONE_MINUS:
                f = oneMinusFunction(populationDiversity);
                break;
            case INVERSE:
                f = oneMinusFunction(populationDiversity);
                break;
            case SIGMOIDAL:
                f = oneMinusFunction(populationDiversity);
                break;
            case DIRECTED:
                f = populationDiversity;
                break;
        }
        return (max-min)*(f)+min;
    }

    public static float oneMinusFunction(float populationDiversity)
    {
        return 1f-populationDiversity;
    }

    public static float inverseFunction(float populationDiversity)
    {
        return 1f/populationDiversity;
    }

    public static float sigmoidalFunction(float populationDiversity)
    {
        return 1f/(1f+(float)(Math.exp(-populationDiversity)));
    }
}
