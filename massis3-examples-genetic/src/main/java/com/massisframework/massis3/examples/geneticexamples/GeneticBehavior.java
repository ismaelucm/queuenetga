package com.massisframework.massis3.examples.geneticexamples;

import com.massisframework.massis3.Pair;
import com.massisframework.massis3.examples.Helpers.GeneticExampleHelper;
import com.massisframework.massis3.examples.Threads.BestPopulationconfigWorkerPull;
import com.massisframework.massis3.examples.Threads.BestQueueSimConfigWorkerPull;
import com.massisframework.massis3.examples.genetic_v2.*;
import com.massisframework.massis3.examples.genetic_v2.CrossMethods.UniformCross;
import com.massisframework.massis3.examples.genetic_v2.SelectionMethods.Tournaments;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.SimulationResult;
import com.massisframework.massis3.examples.simulator.Configuration.ArquetypeSetDescription;
import com.massisframework.massis3.examples.simulator.Configuration.SimulationDescription;
import com.massisframework.massis3.examples.simulator.Core.Simulation;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestPopulationConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestQueueSim;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.IndividualQueueSimConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianGA;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianIndividual;
import com.massisframework.massis3.examples.simulator.Genetic.Problems.FindBestQueueSimConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Problems.FindBestWorldConfiguration;
import com.massisframework.massis3.examples.utils.MainUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneticBehavior {


    public static int defaultElitism = 5;
    public static float defaultMinMutationRate = 0.2f;
    public static float defaultMaxMutationRate = 0.5f;
    public static float defaultMinIndividualMutateGenesPerMutation = 0.5f;
    public static float defaultMaxIndividualMutateGenesPerMutation = 0.5f;
    public static float defaultCrossoverRate = 0.5f;
    public static int defaultPopulationSize = 400;
    public static int numGenes = 150;
    //public static int defaultChromosomeLenght = 250;
    public static int defaultNumMaxGeneration = 3000;

    public static int defaultPedestrianPathSize = 8;

    //public static boolean initialValues = false;
    public static boolean isParrallel = true;
    public static int numWorkers = 6;

    public static final String dafaulSimulationResult = "/massis3-examples-genetic/src/main/resources/Entrada_Clase.txt";
    public static final String defaultSolutionFileOutput = "/SolutionGeneticBehavior.txt";
    public static final String queuNetConfig = "/SolutionQN_96_05p_25_04.txt";

    public static final String defaultArquetype = "/massis3-examples-genetic/src/main/resources/ArquetypeSimulationConfigExample.xml";
    public static final String defaulScene = "/massis3-examples-genetic/src/main/resources/SceneExample.xml";


    public static void main(String[] args) {



        String currentPath = MainUtils.getCurrentPath();
        if (args.length > 0) {
            //configuración por consola
        }

        GeneticExampleHelper.createPedestrianDecoders();
        //Simulation simulation = GeneticBehavior.CreateSimulation();

        int elitism = defaultElitism;
        float minMutationRate = defaultMinMutationRate;
        float maxMutationRate = defaultMaxMutationRate;
        float minIndividualMutateGenesPerMutation = defaultMinIndividualMutateGenesPerMutation;
        float maxIndividualMutateGenesPerMutation = defaultMaxIndividualMutateGenesPerMutation;
        float crossoverRate = defaultCrossoverRate;
        int populationSize = defaultPopulationSize;
        int numMaxGeneration = defaultNumMaxGeneration;
        int pedestrianPathSize = defaultPedestrianPathSize;
        //int chromosomeLenght = defaultChromosomeLenght;
        String dataFile = currentPath + dafaulSimulationResult;
        String solutionFile = currentPath + defaultSolutionFileOutput;
        //int pathSize = cst_defaultPathSize;


        Simulation simulation = createSimulationWithInitialValues(queuNetConfig,dataFile,false);

        CrossMethod crossMethod = new UniformCross();
        SelectionMethod selectionMethod = new Tournaments(10,20);
        //List<String> listStr = simulation.getSimulator().getWorld().getAllNodes();
        //String[] orderNames = listStr.toArray(new String[listStr.size()]);
        //List<Pair<String, String>> pairList = GeneticQNSimConfigurator.generateNodePairConnection(orderNames, simulation);
        SimulationResult simulationResult = GeneticQNSimConfigurator.createSimulationResult(dataFile);

        FindBestWorldConfiguration problem = null; //POR AQUI


        if(isParrallel)
        {
            List<CalcFitnessBestPopulationConfig> listOfCalculators = new ArrayList<>();
            for(int i = 0; i < numWorkers; ++i)
            {
                //Simulation _simulation, String[] _orderNames,
                //List<Pair<String, String>> _transitionOrder, SimulationResult _resultToCompareFitness
                CalcFitnessBestPopulationConfig calculators = null;
                if(i == 0)
                    calculators = new CalcFitnessBestPopulationConfig(simulation,simulationResult);
                else {
                    Simulation newSimulation = createSimulationWithInitialValues(queuNetConfig,dataFile,false);
                    calculators = new CalcFitnessBestPopulationConfig(newSimulation, simulationResult);
                }
                listOfCalculators.add(calculators);
            }

            BestPopulationconfigWorkerPull workersPull = new BestPopulationconfigWorkerPull(listOfCalculators);
            problem = new FindBestWorldConfiguration(simulation.getSimulator().getWorld(),numGenes,pedestrianPathSize, minMutationRate,crossMethod, selectionMethod,workersPull);
        }
        else
            problem = new FindBestWorldConfiguration(simulation, numGenes,pedestrianPathSize, minMutationRate, simulationResult, crossMethod, selectionMethod );


        GeneticAlgorithmSetup geneticAlgorithmSetup = new GeneticAlgorithmSetup(elitism, minMutationRate, maxMutationRate, minIndividualMutateGenesPerMutation, maxIndividualMutateGenesPerMutation, crossoverRate, populationSize, numMaxGeneration);

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(problem, geneticAlgorithmSetup);

        geneticAlgorithm.init();

        try {
            long startTime = System.nanoTime();
            geneticAlgorithm.run(true);
            long endTime   = System.nanoTime();
            long totalTime = endTime - startTime;
            System.out.println("----- Time  "+totalTime+" -------");
            //obtener la solución de la mejor configuración.
            PedestrianIndividual solution = geneticAlgorithm.<PedestrianIndividual>getSolution();



            writeSolution(solutionFile, solution, geneticAlgorithm);

            CalcFitnessBestPopulationConfig calculators = new CalcFitnessBestPopulationConfig(simulation,simulationResult);

            calculators.calcFitness(solution);
            SimulationResult sresult = (SimulationResult) calculators.getSimulation().getResultType();

            Map<String, Integer> results =  simulationResult.getAllResults();
            for(Map.Entry<String, Integer> entry : results.entrySet())
            {
                Integer pedestrianSimulated = sresult.getSensorResult(entry.getKey());
                System.out.println(entry.getKey()+" pedestrians simulated "+entry.getValue() + " pedestrians queue "+pedestrianSimulated);
            }

            //escribirla en el fichero.
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(isParrallel)
            problem.join();


        /*String currentPath = MainUtils.getCurrentPath();
        if (args.length > 0) {
            //configuración por consola
        }

        AbstractSelection.AbstractSelectionMethods selectionMethod = cst_defaulSelectionMethod;
        AbstractCross.AbstractCrossMethods crossMethod = cst_defaultCrossMethod;
        int pathSize = cst_defaultPathSize;
        float hierarchicalAValue = defaultHierarchicalAValue;
        float hierarchicalBValue = defaultHierarchicalBValue;
        int torunementSize = defaultTournamentSize;


        int elitism = defaultElitism;
        float mutationRate = defaultMutationRate;
        float crossoverRate = defaultCrossoverRate;
        int populationSize = defaultPopulationSize;
        int chromosomeLenght = defaultChromosomeLenght;
        int numMaxGeneration = defaultNumMaxGeneration;

        Simulation simulation = CreateSimulation();
        String dataFile = currentPath + dafaulSimulationResult;
        String solutionFile = currentPath + defaultSolutionFileOutput;

        PedestrianGene.Init(simulation.getNodes());

        AbstractSelection<PedestrianGene> selection = AbstractSelection.getSelectionMethod(selectionMethod);
        if (selectionMethod == AbstractSelection.AbstractSelectionMethods.HIERARCHICAL)
            ((Hierarchical<PedestrianGene>) (selection)).setAdicionalConfiguration(hierarchicalAValue, hierarchicalBValue);
        else if (selectionMethod == AbstractSelection.AbstractSelectionMethods.TOURNAMENT)
            ((Tournament<PedestrianGene>) (selection)).setAdicionalConfiguration(torunementSize);

        AbstractCross<PedestrianGene> crossover = AbstractCross.getCrossMethod(crossMethod);
        //Simulation simulation, String dataToCompareFile,
        // AbstractSelection<PedestrianGene> selectionMethod,AbstractCross<PedestrianGene> crossMethod,
        // int pathSize
        FindBestWorldConfiguration problem = new FindBestWorldConfiguration(simulation, dataFile, selection, crossover, pathSize);


        //float _mutationRate, float _crossoverRate, int _populationSize, int chromosomeLenght, int _numMaxGeneratoin, int _elitism
        GeneticAlSetup setup = new GeneticAlSetup(mutationRate, crossoverRate, populationSize, chromosomeLenght, numMaxGeneration, elitism);

        GeneticAl<PedestrianGene> genenticAl = new GeneticAl<PedestrianGene>(problem, setup, solutionFile);


        try {

            boolean stop = false;
            genenticAl.init();
            do {

                genenticAl.run(true);

                System.out.println("maximum number of generations reached. Do you want to continue? Y/N");
                Scanner s = new Scanner(System.in);
                String line = s.nextLine();
                if (line.compareToIgnoreCase("y") == 0) {
                    int max = genenticAl.getMaxGenerations();
                    genenticAl.setMaxGenerations(max * 2);
                } else
                    stop = true;
            } while (!stop);

            genenticAl.writeSolution();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }

        Individual<PedestrianGene> theBest = genenticAl.getPopulation().getIndividual(0);

        System.out.println("The best " + theBest.getFitness());*/


    }

    public static Simulation createSimulationWithInitialValues(String configureFilePath,String dataFile,boolean debugging) {
        String currentPath = MainUtils.getCurrentPath();

        String arquetype = currentPath + defaultArquetype;
        System.out.println("arquetype=>" + arquetype);
        String scene = currentPath + defaulScene;
        System.out.println("scene=>" + scene);
        String configurationFile = currentPath + configureFilePath;

        try {
            InputStream arquetypeStream = new FileInputStream(arquetype);
            InputStream sceneStream = new FileInputStream(scene);


            ArquetypeSetDescription arquetypeSetDescription = new ArquetypeSetDescription(arquetypeStream);
            SimulationDescription simDescription = new SimulationDescription(sceneStream);

            Simulation simulation = new Simulation();
            simulation.addArquetype(arquetypeSetDescription);
            simulation.configure(simDescription);

            configureSimulation(simulation,configurationFile,dataFile,debugging);


            return simulation;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void configureSimulation(Simulation simulation, String configureStream,String dataFile, boolean debugging)
    {
        try {

            Path path = Paths.get(configureStream);

            BufferedReader reader = Files.newBufferedReader(path);
            String configurationNodeSizes = reader.readLine();
            String configurationNodeTimes = reader.readLine();
            String configurationTransitionSize = reader.readLine();
            String orderNodeNames = reader.readLine();
            String trasitionOrder = reader.readLine();

            String[] configurationNodeSizesArray = configurationNodeSizes.split(",");
            String[] configurationNodeTimesArray = configurationNodeTimes.split(",");
            String[] configurationTransitionSizeArray = configurationTransitionSize.split(",");
            String[] orderNodeNamesArray = orderNodeNames.split(",");
            String[] trasitionOrderArray = trasitionOrder.split(",");

            int[] intConfigurationNodeSizesArray = new int[configurationNodeSizesArray.length];
            int[] intConfigurationNodeTimesArray = new int[configurationNodeTimesArray.length];
            int[] intConfigurationTransitionSizeArray = new int[configurationTransitionSizeArray.length];


            for(int i=0; i < configurationNodeSizesArray.length; ++i)
            {
                String value = configurationNodeSizesArray[i];
                intConfigurationNodeSizesArray[i] = Integer.parseInt(value);
            }

            for(int i=0; i < configurationNodeTimesArray.length; ++i)
            {
                String value = configurationNodeTimesArray[i];
                intConfigurationNodeTimesArray[i] = (int) Float.parseFloat(value);
                //TODO se exporta a float, exportar a int.
            }

            for(int i=0; i < configurationTransitionSizeArray.length; ++i)
            {
                String value = configurationTransitionSizeArray[i];
                intConfigurationTransitionSizeArray[i] = (int) Float.parseFloat(value);
            }

            List<Pair<String, String>> pairList = new ArrayList<>();
            for(String s : trasitionOrderArray)
            {
                String[] pairStr = s.split("<=>");
                Pair<String, String> pair = new Pair<>(pairStr[0],pairStr[1]);
                pairList.add(pair);
            }


            simulation.getSimulator().getWorld().configure(intConfigurationNodeSizesArray,intConfigurationNodeTimesArray,
                    intConfigurationTransitionSizeArray,orderNodeNamesArray,pairList);

            if(debugging) {
                SimulationResult simulationResult = GeneticQNSimConfigurator.createSimulationResult(dataFile);
                CalcFitnessBestQueueSim calculators = new CalcFitnessBestQueueSim(simulation, orderNodeNamesArray, pairList, simulationResult);

                float similarity = calculators.calcSimilarity();
                System.out.println("Similarity " + similarity);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public static Simulation CreateSimulation() {
        String currentPath = MainUtils.getCurrentPath();

        String arquetype = currentPath + defaultArquetype;
        System.out.println("arquetype=>" + arquetype);
        String scene = currentPath + defaulScene;
        System.out.println("scene=>" + scene);

        try {

            InputStream arquetypeStream = new FileInputStream(arquetype);
            InputStream sceneStream = new FileInputStream(scene);

            ArquetypeSetDescription arquetypeSetDescription = new ArquetypeSetDescription(arquetypeStream);

            SimulationDescription simDescription = new SimulationDescription(sceneStream);

            Simulation simulation = new Simulation();
            simulation.addArquetype(arquetypeSetDescription);
            simulation.configure(simDescription);

            return simulation;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void writeSolution(String file,PedestrianIndividual solution, GeneticAlgorithm ga) {
        System.out.println("------ SOLUTION --------");
        System.out.println("Best individual " + solution.getFitness());
        List<PedestrianGA> pedestrianList = solution.getPedestrianGAList();
        String s = "";
        for(int i = 0; i < pedestrianList.size(); ++i)
        {
            PedestrianGA p = pedestrianList.get(i);
            List<String> path = p.getPath();
            String pathStr = "";
            for(int j = 0; j < path.size(); ++j)
            {
                pathStr+=path.get(j);
                if(j <  (path.size()-1))
                    pathStr += ",";
            }
            s += path;
            s+= "\n";
        }
        System.out.print(s);
        if (file != null && file.compareTo("") != 0) {
            System.out.println("\nwriting the best individual found in " + file);
            Path path = Paths.get(file);
            try {
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(s);
                    writer.write("");
                    writer.write("" + solution.getFitness());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
