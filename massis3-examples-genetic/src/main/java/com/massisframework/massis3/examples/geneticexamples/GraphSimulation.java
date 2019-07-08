package com.massisframework.massis3.examples.geneticexamples;

import com.massisframework.massis3.examples.Helpers.GeneticExampleHelper;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.SimulationResult;
import com.massisframework.massis3.examples.simulator.Configuration.ArquetypeSetDescription;
import com.massisframework.massis3.examples.simulator.Configuration.SimulationDescription;
import com.massisframework.massis3.examples.simulator.Core.Simulation;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.Calculators;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianGA;
import com.massisframework.massis3.examples.utils.MainUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class GraphSimulation {

    public static void main(String[] args) throws Exception {
        String currentPath = MainUtils.getCurrentPath();


        String arquetype = currentPath + "/massis3-examples-genetic/src/main/resources/ArquetypeSimulationConfigExample.xml";
        System.out.println("arquetype=>" + arquetype);
        String scene = currentPath + "/massis3-examples-genetic/src/main/resources/SceneExample.xml";
        System.out.println("scene=>" + scene);

        String resultFile = currentPath + "/massis3-examples-genetic/src/main/resources/Entrada_Clase.txt";


        SimulationResult simulationResultFile = GeneticQNSimConfigurator.createSimulationResult(resultFile);



        try {
            InputStream arquetypeStream = new FileInputStream(arquetype);
            InputStream sceneStream = new FileInputStream(scene);

            ArquetypeSetDescription arquetypeSetDescription = new ArquetypeSetDescription(arquetypeStream);

            SimulationDescription simDescription = new SimulationDescription(sceneStream);

            Simulation simulation = new Simulation();
            simulation.addArquetype(arquetypeSetDescription);
            simulation.configure(simDescription);



            simulation.run();



            /*Simulation simulation2 = new Simulation();
            simulation2.addArquetype(arquetypeSetDescription);
            simulation2.configure(simDescription);

            simulation2.run();*/

            //simulation.end();


            System.out.println("----Sim 1----");
            SimulationResult simulationResult = simulation.getResultType();
            Map<String, Integer> resMap = simulationResult.getAllResults();
            for (Map.Entry<String, Integer> entry : resMap.entrySet()) {
                if(simulationResultFile.getAllResults().containsKey(entry.getKey()))
                    System.out.println(entry.getKey() + ":" + entry.getValue() + " espected "+simulationResultFile.getSensorResult(entry.getKey()));
            }

            /*System.out.println("----Sim 2----");
            SimulationResult simulationResult2 = simulation2.getResultType();
            Map<String, Integer> resMap2 = simulationResult2.getAllResults();
            for (Map.Entry<String, Integer> entry : resMap2.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }*/
            //int numDetected =  simulationResult.getSensorResult("Sensor1");

            float similarity=Calculators.calcFitnessStatic(simulationResultFile,simulationResult);
            System.out.println("similarity "+similarity);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
