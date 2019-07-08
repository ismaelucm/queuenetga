package com.massisframework.massis3.examples.geneticexamples;

import com.massisframework.massis3.Pair;
import com.massisframework.massis3.examples.Threads.BestQueueSimConfigWorkerPull;
import com.massisframework.massis3.examples.genetic_v2.*;
import com.massisframework.massis3.examples.genetic_v2.CrossMethods.UniformCross;
import com.massisframework.massis3.examples.genetic_v2.SelectionMethods.Tournaments;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.SimulationResult;
import com.massisframework.massis3.examples.simulator.Configuration.ArquetypeSetDescription;
import com.massisframework.massis3.examples.simulator.Configuration.SimulationDescription;
import com.massisframework.massis3.examples.simulator.Core.Simulation;
import com.massisframework.massis3.examples.simulator.Core.Simulator;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestQueueSim;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.Calculators;
import com.massisframework.massis3.examples.simulator.Genetic.Problems.FindBestQueueSimConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.IndividualQueueSimConfig;
import com.massisframework.massis3.examples.simulator.World.SimulationNode;
import com.massisframework.massis3.examples.simulator.World.World;
import com.massisframework.massis3.examples.utils.MainUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GeneticQNSimConfigurator {

    private static class Experiment
    {
        public String simulationResult;
        public String solutionFileOutput;
        public String scene;


        public Experiment(String simulationResult, String solutionFileOutput, String defaulScene) {
            this.simulationResult = simulationResult;
            this.solutionFileOutput = solutionFileOutput;
            this.scene = defaulScene;
        }
    }

    private static class ExperimentSimulationConfig
    {
        public int ID;
        public Simulation simulation;
        public String[] orderNames;
        List<Pair<String, String>> pairList;
        SimulationResult simulationResult;



        public ExperimentSimulationConfig(int id,Simulation simulation, String[] orderNames,
                                          List<Pair<String, String>> pairList,
                                          SimulationResult simulationResult) {
            this.ID = id;
            this.simulation = simulation;
            this.orderNames = orderNames;
            this.pairList = pairList;
            this.simulationResult = simulationResult;
        }
    }

    public static int defaultElitism = 0;
    public static float defaultMinMutationRate = 0.5f;
    public static float defaultMaxMutationRate = 0.9f;
    public static float defaultMinIndividualMutateGenesPerMutation = 0.1f;
    public static float defaultMaxIndividualMutateGenesPerMutation = 0.75f;
    public static float defaultCrossoverRate = 0.5f;
    public static int defaultPopulationSize = 4000;
    public static int[] experimentsID = new int[]{0,2,3,4};
    public static int validationExperimentID = 1;
    public static int defaultNumMaxGeneration = 50;

    public static boolean isParrallel = true;
    public static int numWorkers = 4;
    public static final String resourcePath = "/src/main/resources";
    public static final String defaultArquetype = "/ArquetypeSimulationConfigExample.xml";
    //public static final String defaultArquetype = "/massis3-examples-genetic/src/main/resources/ArquetypeSimulationConfigExample.xml";
    public static Experiment[] _experiments;


    //public static final AbstractCross.AbstractCrossMethods cst_defaultCrossMethod = AbstractCross.AbstractCrossMethods.MULTIPOINT_CROSS;
    //public static final AbstractSelection.AbstractSelectionMethods cst_defaulSelectionMethod = AbstractSelection.AbstractSelectionMethods.TOURNAMENT;

    public static final int cst_defaultPathSize = 8;

    public static void main(String[] args) {
        String currentPath = MainUtils.getCurrentPath();
        if (args.length > 0) {
            //configuración por consola
        }

        CreateExperiments();

        //Simulation simulation = GeneticBehavior.CreateSimulation();

        int elitism = defaultElitism;
        float minMutationRate = defaultMinMutationRate;
        float maxMutationRate = defaultMaxMutationRate;
        float minIndividualMutateGenesPerMutation = defaultMinIndividualMutateGenesPerMutation;
        float maxIndividualMutateGenesPerMutation = defaultMaxIndividualMutateGenesPerMutation;
        float crossoverRate = defaultCrossoverRate;
        int populationSize = defaultPopulationSize;
        int numMaxGeneration = defaultNumMaxGeneration;
        //int chromosomeLenght = defaultChromosomeLenght;
        //String solutionFile = currentPath + defaultSolutionFileOutput;
        //int pathSize = cst_defaultPathSize;

        CrossMethod crossMethod = new UniformCross();
        SelectionMethod selectionMethod = new Tournaments(4,20);
        FindBestQueueSimConfig problem = CreateProblem(experimentsID,crossMethod,selectionMethod);

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
            IndividualQueueSimConfig solution = geneticAlgorithm.<IndividualQueueSimConfig>getSolution();

            WriteFinalSolution(experimentsID,solution,problem,geneticAlgorithm,validationExperimentID);

            //escribirla en el fichero.
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(isParrallel)
            problem.join();
    }

    private static FindBestQueueSimConfig CreateProblem(int[] experimentsID,CrossMethod crossMethod,
                                                        SelectionMethod selectionMethod)
    {
        FindBestQueueSimConfig problem = null;
        List<ExperimentSimulationConfig> expResConfig = new ArrayList<>();
        for(int i = 0; i < experimentsID.length; ++i) {

            ExperimentSimulationConfig erc = CreateExperimentConfig(experimentsID[i]);
            expResConfig.add(erc);
        }



        if(isParrallel)
        {
            List<List<CalcFitnessBestQueueSim>> listOfCalculators = new ArrayList<>();
            World world = expResConfig.get(0).simulation.getSimulator().getWorld();
            for(int i = 0; i < numWorkers; ++i)
            {
                List<CalcFitnessBestQueueSim> calculatorsByExp = new ArrayList<>();
                for(int j = 0; j < expResConfig.size(); ++j)
                {
                    ExperimentSimulationConfig expResCfg = expResConfig.get(j);
                    Simulation newSimulation = createSimulationWithRandom(expResCfg.ID);
                    CalcFitnessBestQueueSim calc = new CalcFitnessBestQueueSim(newSimulation, expResCfg.orderNames, expResCfg.pairList, expResCfg.simulationResult);
                    calculatorsByExp.add(calc);
                }

                listOfCalculators.add(calculatorsByExp);

                /*CalcFitnessBestQueueSim[] calculators = null;
                List<CalcFitnessBestQueueSim> calculatorsByExp = new ArrayList<>();
                for(int j = 0; i < expResConfig.size(); ++j)
                {

                    if(i == 0)
                        calculators = new CalcFitnessBestQueueSim(simulation,orderNames,pairList,simulationResult);
                    else {
                        Simulation newSimulation = createSimulationWithRandom(experimentID);
                        calculators = new CalcFitnessBestQueueSim(newSimulation, orderNames, pairList, simulationResult);
                    }
                    calculatorsByExp.add(calculators)
                }*/


                //listOfCalculators.add(calculators);
            }

            BestQueueSimConfigWorkerPull workersPull = new BestQueueSimConfigWorkerPull(listOfCalculators);
            problem = new FindBestQueueSimConfig(crossMethod, selectionMethod,workersPull);
        }
        else
        {
            List<Calculators> calcs = new ArrayList<>();
            for(int i = 0; i < expResConfig.size(); ++i)
            {
                ExperimentSimulationConfig expResCfg = expResConfig.get(i);
                CalcFitnessBestQueueSim calc = new CalcFitnessBestQueueSim(expResCfg.simulation, expResCfg.orderNames, expResCfg.pairList, expResCfg.simulationResult);
                calcs.add(calc);
            }

            World world = expResConfig.get(0).simulation.getSimulator().getWorld();
            problem = new FindBestQueueSimConfig(world, crossMethod, selectionMethod, calcs);
        }

        //end of creating the simulation.
        return problem;
    }

    public static void CreateExperiments()
    {
        String currentPath = MainUtils.getCurrentPath();
        _experiments = new Experiment[5];

        final String dafaulSimulationResult01 = currentPath+resourcePath+"/Entrada_Clase.txt";
        final String defaultSolutionFileOutput01 = currentPath+"/SolutionQNexpNew01.txt";
        final String defaulScene01 = currentPath+resourcePath+"/SceneExample.xml";
        _experiments[0] = new Experiment(dafaulSimulationResult01,defaultSolutionFileOutput01,defaulScene01);

        final String dafaulSimulationResult02 = currentPath+resourcePath+"/Evacuacion_exp02.txt";
        final String defaultSolutionFileOutput02 = currentPath+"/SolutionQNexpNew02.txt";
        final String defaulScene02 = currentPath+resourcePath+"/SceneExample02.xml";
        _experiments[1] = new Experiment(dafaulSimulationResult02,defaultSolutionFileOutput02,defaulScene02);

        final String dafaulSimulationResult03 = currentPath+resourcePath+"/exp03.txt";
        final String defaultSolutionFileOutput03 = currentPath+"/SolutionQNexpNew03.txt";
        final String defaulScene03 = currentPath+resourcePath+"/SceneExample03.xml";
        _experiments[2] = new Experiment(dafaulSimulationResult03,defaultSolutionFileOutput03,defaulScene03);

        final String dafaulSimulationResult04 = currentPath+resourcePath+"/exp04.txt";
        final String defaultSolutionFileOutput04 = currentPath+"/SolutionQNexpNew04.txt";
        final String defaulScene04 = currentPath+resourcePath+"/SceneExample04.xml";
        _experiments[3] = new Experiment(dafaulSimulationResult04,defaultSolutionFileOutput04,defaulScene04);

        final String dafaulSimulationResult05 = currentPath+resourcePath+"/exp05.txt";
        final String defaultSolutionFileOutput05 = currentPath+"/SolutionQNexpNew05.txt";
        final String defaulScene05 = currentPath+resourcePath+"/SceneExample05.xml";
        _experiments[4] = new Experiment(dafaulSimulationResult05,defaultSolutionFileOutput05,defaulScene05);
    }

    public static SimulationResult createSimulationResult(String file) {
        SimulationResult simulationResult = null;
        Path path = Paths.get(file);
        int numLine = 0;
        boolean readingNumPedestrian = false;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = null;
            do {
                if (simulationResult == null)
                    simulationResult = new SimulationResult();

                line = reader.readLine();
                if(line != null) {
                    if (line.compareTo(".") == 0)
                        readingNumPedestrian = true;
                    else if (readingNumPedestrian) {
                        String[] splitArr = line.split(":");
                        simulationResult.setSensorResult(splitArr[0], Integer.parseInt(splitArr[1]));
                    }

                    numLine++;
                }
            } while (line != null);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return simulationResult;
    }

    private static ExperimentSimulationConfig CreateExperimentConfig(int experimentID)
    {
        Experiment experiment = _experiments[experimentID];
        String dataFile = experiment.simulationResult;


        //Creating the simulation to configure the problem
        Simulation simulation = createSimulationWithRandom(experimentID);
        List<String> listStr = simulation.getSimulator().getWorld().getAllNodes();
        String[] orderNames = listStr.toArray(new String[listStr.size()]);
        List<Pair<String, String>> pairList = generateNodePairConnection(orderNames, simulation);
        SimulationResult simulationResult = createSimulationResult(dataFile);

        ExperimentSimulationConfig erc = new ExperimentSimulationConfig(experimentID,simulation,orderNames,pairList,simulationResult);
        return erc;
    }

    private static void WriteFinalSolution(int[] experiemntsID,  IndividualQueueSimConfig solution,
                                           GeneticALProblem problem, GeneticAlgorithm geneticAlgorithm,
                                           int validationIDExp)
    {
        for(int i = 0; i < problem.getNumCalculators(); ++i) {

            Experiment experiment = _experiments[experiemntsID[i]];
            CalcFitnessBestQueueSim calc = (CalcFitnessBestQueueSim) problem.getCalculator(i);
            List<String> listStr = calc.getSimulation().getSimulator().getWorld().getAllNodes();
            //writeSolution(experiment.solutionFileOutput, solution, geneticAlgorithm, listStr, pairList);
            writeSolution(experiment.solutionFileOutput, solution, geneticAlgorithm, listStr, calc.getPairs());

            //CalcFitnessBestQueueSim calculators = new CalcFitnessBestQueueSim(simulation, orderNames, pairList, simulationResult);
            CalcFitnessBestQueueSim calculators = new CalcFitnessBestQueueSim(calc.getSimulation(), calc.getOrderNames(),
                    calc.getPairs(), calc.getSimulationResult());

            calculators.calcFitness(solution);
            SimulationResult sresult = (SimulationResult) calculators.getSimulation().getResultType();

            Map<String, Integer> results = calc.getSimulationResult().getAllResults();
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                Integer pedestrianSimulated = sresult.getSensorResult(entry.getKey());
                System.out.println(entry.getKey() + " pedestrians simulated " + entry.getValue() + " pedestrians queue " + pedestrianSimulated);
            }
        }

        //Comparamos con uno para validar (Generalización)
        if(validationIDExp >= 0)
        {
            ExperimentSimulationConfig expValid = CreateExperimentConfig(validationIDExp);
            CalcFitnessBestQueueSim calculatorValid = new CalcFitnessBestQueueSim(expValid.simulation, expValid.orderNames,
                    expValid.pairList, expValid.simulationResult);
            float validationFitness = calculatorValid.calcFitness(solution);
            System.out.println("fitness validation with experiments "+(validationIDExp+1) + " is " +validationFitness);

            SimulationResult sresult = (SimulationResult) calculatorValid.getSimulation().getResultType();
            Map<String, Integer> results = calculatorValid.getSimulationResult().getAllResults();
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                Integer pedestrianSimulated = sresult.getSensorResult(entry.getKey());
                System.out.println(entry.getKey() + " pedestrians simulated " + entry.getValue() + " pedestrians queue " + pedestrianSimulated);
            }
        }
    }

    private static void writeSolution(String file, IndividualQueueSimConfig solution, GeneticAlgorithm ga, List<String> listStr, List<Pair<String, String>> pairList) {
        System.out.println("------ SOLUTION --------");
        System.out.println("Best individual " + solution.getFitness());
        String s = solution.serialize();
        s+= "\n";
        s += listNodesAndTransitions(listStr,pairList);
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

    private static String listNodesAndTransitions(List<String> listStr, List<Pair<String, String>> pairList)
    {
        String s = "";

        int count = 0;
        for(String nodeName : listStr)
        {
            s += nodeName;
            if(count < (listStr.size()-1))
                s += ",";
            count++;
        }

        s += "\n";
        count = 0;
        for(Pair<String, String> pair : pairList)
        {
            s += pair.get_first()+"<=>"+pair.get_second();
            if(count < (listStr.size()-1))
                s += ",";
            count++;
        }

        return s;
    }

    public static List<Pair<String, String>> generateNodePairConnection(String[] orderNames, Simulation simulation) {
        List<Pair<String, String>> pairList = new ArrayList<>();

        //Get the list of neighborns pairs.
        Set<String> set = new HashSet<>();
        for (int i = 0; i < orderNames.length; ++i) {

            String name = orderNames[i];
            List<SimulationNode> neighborns = simulation.getSimulator().getWorld().getNeighborn(name);

            for (int j = 0; j < neighborns.size(); ++j) {
                SimulationNode simNode = neighborns.get(j);
                if(!set.contains(name+" "+simNode.getName())) {
                    pairList.add(new Pair<String, String>(name, simNode.getName()));
                    set.add(name+" "+simNode.getName());
                    set.add(simNode.getName()+" "+name);
                }

            }
        }
        return pairList;
    }

    private static Simulation createSimulationWithRandom(int experimentID) {
        Simulation sim = createSimulationWithInitialValues(experimentID);
        Simulator simulator = sim.getSimulator();
        World world = simulator.getWorld();
        world.configureRandom();
        return sim;
    }

    private static Simulation createSimulationWithInitialValues(int experimentID) {

        String currentPath = MainUtils.getCurrentPath();
        String arquetype = currentPath + resourcePath +  defaultArquetype;
        Experiment experiment = _experiments[experimentID];

        System.out.println("arquetype=>" + arquetype);
        String scene = experiment.scene;
        System.out.println("scene=>" + scene);

        //Vamos a partir de una escena configurada a mano para no generar una población totalmente aleatoria.
        //En base a esta configuración geenramos versiones modificadas.
        //Hacemos otros experimento con generaciones totalmente aleatorias y comparamos...


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
}
