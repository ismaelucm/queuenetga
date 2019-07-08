package com.massisframework.massis3.examples.simulator.Core;

import com.massisframework.massis3.examples.simulator.Components.Pedestrian;
import com.massisframework.massis3.examples.simulator.Configuration.*;
import com.massisframework.massis3.examples.simulator.Entity.Entity;
import com.massisframework.massis3.examples.simulator.Entity.EntityTransform;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianGA;
import com.massisframework.massis3.examples.simulator.Genetic.PedestrianGene;
import com.massisframework.massis3.examples.simulator.World.SimulationNode;
import com.massisframework.massis3.examples.simulator.World.World;

import java.util.*;

public class Simulator implements IRunnable {
    private World _world;
    private Map<String, ArquetypeSetDescription> _arquetypes;
    private List<Entity> _activeEntitys;
    private List<Entity> _desactivatedEntitys;
    private Map<String, List<Entity>> _tagMapper;
    private Object _result;

    private List<Entity> _entityToBeActive;
    private List<Entity> _entityToBeDesactive;
    private boolean _finished;
    private SimulatorConfig _simCofig;


    public Simulator() {
        _world = null;
        _activeEntitys = new ArrayList<>();
        _desactivatedEntitys = new ArrayList<>();
        _entityToBeActive = new ArrayList<>();
        _entityToBeDesactive = new ArrayList<>();
        _tagMapper = new LinkedHashMap<>();
        _arquetypes = new LinkedHashMap<>();
        _finished = false;
    }


    protected void setWorld(World world)
    {
        _world = world;
    }

    protected void addDesactiveEntity(Entity entity)
    {
        _desactivatedEntitys.add(entity);
    }

    protected void addEntityToBeDesactive(Entity entity)
    {
        _entityToBeDesactive.add(entity);
    }

    protected void addArquetypeList(String key,List<Entity> entities)
    {
        _tagMapper.put(key,entities);
    }

    public Object getResult() {
        return _result;
    }

    public <T> T getResultType() {
        return (T) _result;
    }

    public void setResults(Object res) {
        _result = res;
    }

    public void addArquetype(ArquetypeSetDescription ar) {
        _arquetypes.put(ar.getName(), ar);
    }

    public boolean reset() {
        _finished = false;
        WorldConfig worldConfig = _simCofig.get_worldConfig();
        CreateWorld(worldConfig);
        ArquetypeSetDescription arquetype = getArquetype(_simCofig.getArquetypeName());
        try {
            CreateScene(_simCofig.get_instanciations(), arquetype);
            return true;
        } catch (Exception e) {
            System.out.println("Error in the scene creation " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean configure(SimulatorConfig simConfig) {
        _simCofig = simConfig;
        return reset();
    }

    public void resetPopulation(List<PedestrianGA> individual)
    {
        ArquetypeSetDescription arquetype = getArquetype("ArquetypeSimulationConfigExample");
        try {
            ResetInididuals(individual, arquetype,"Pedestrian");

        } catch (Exception e) {
            System.out.println("Error in the scene creation " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void resetPopulationSim(List<PedestrianGene> individual, String arquetypeSetName) throws Exception {
        List<Entity> pedestrianEntity = getEntitiesByComponent(Pedestrian.class);
        if (pedestrianEntity == null || pedestrianEntity.size() == 0) {
            //creamos los pedestrian.
            ArquetypeSetDescription arquetype = getArquetype(arquetypeSetName);

            if (arquetype != null) {
                for (int i = 0; i < individual.size(); ++i) {
                    PedestrianGene genotype = individual.get(i);
                    int gen = genotype.get(0);
                    String place = PedestrianGene.decodificate(gen);
                    InstanciationConfig inst = null;
                    try {
                        inst = createInstanciationCondigFromPedestrian(place);
                    } catch (Exception e) {
                        System.out.println("Error when create the Entity in resetPopulationSim");
                    }
                    Entity entity = CreateEntity("",place, arquetype.getArquetype(inst.getArquetype()), inst);
                    addEntity(entity);
                    Pedestrian pedestrian = entity.getComponent(Pedestrian.class);
                    pedestrian.reset(individual.get(i));
                }
            } else {
                System.out.println("The arquitype set " + arquetypeSetName + " not exist in the database");
                throw new Exception("The arquitype set " + arquetypeSetName + " not exist in the database");

            }

        } else {
            //los reseteamos...
            if (pedestrianEntity.size() == individual.size()) {
                for (int i = 0; i < individual.size(); ++i) {
                    EntityTransform et = (EntityTransform) pedestrianEntity.get(i);

                    pedestrianEntity.get(i).setName("Pedestrian."+et.getTransform().getPosition().getName()+""+i);
                    Pedestrian pedestrian = pedestrianEntity.get(i).getComponent(Pedestrian.class);
                    pedestrian.reset(individual.get(i));
                }
            } else {
                System.out.println("Error, the pedestrianEntitis size is different to the indiidual list");
                throw new Exception("Error, the pedestrianEntitis size is different to the indiidual list");
            }

        }

        /*for(int i = 0; i < individual.size(); ++i) {
            _activeEntitys
        }*/
    }


    public InstanciationConfig createInstanciationCondigFromPedestrian(String place, List<String> path) {
        InstanciationConfig inst = new InstanciationConfig();
        List<ComponentConfig> componentList = new ArrayList<>();
        ComponentConfig pedestrian = new ComponentConfig();
        pedestrian.setEnable(true);
        pedestrian.setComponent("Pedestrian");
        Map<String, String> attr = new LinkedHashMap<>();
        String sPath = buildStringPath(path);
        attr.put("path", sPath);
        pedestrian.setAttributes(attr);

        inst.setComponentConfigOverride(componentList);
        inst.setArquetype("Pedestrian");
        inst.setPlace(place);
        return inst;
    }

    public InstanciationConfig createInstanciationCondigFromPedestrian(String place) {
        InstanciationConfig inst = new InstanciationConfig();
        inst.setArquetype("Pedestrian");
        inst.setPlace(place);
        return inst;
    }

    public String buildStringPath(List<String> path) {
        String sPath = "";
        for (int i = 0; i < path.size(); ++i) {
            if (i < (path.size() - 1))
                sPath += path.get(i) + ",";
            else
                sPath += path.get(i);
        }
        return sPath;
    }

    public List<String> getAdjacents(String node) {
        List<SimulationNode> simNode = _world.getNeighborn(node);
        List<String> simNodeString = extractNames(simNode);
        return simNodeString;
    }

    protected List<String> extractNames(List<SimulationNode> simNode) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < simNode.size(); ++i) {
            list.add(simNode.get(i).getName());
        }
        return list;
    }

    public ArquetypeSetDescription getArquetype(String name) {
        return _arquetypes.get(name);
    }

    public void addEntity(Entity e) {
        _activeEntitys.add(e);
        addInTagMapper(e);
    }

    public World getWorld() {
        return _world;
    }

    public void removeEntity(Entity e) {
        if (_activeEntitys.indexOf(e) >= 0)
            _activeEntitys.remove(e);
        else if (_desactivatedEntitys.indexOf(e) >= 0)
            _desactivatedEntitys.remove(e);

        removeOfTagMapper(e);
    }

    public Object getSimulationResults() {
        return null;
    }

    public void start() throws Exception {
        for (int i = 0; i < _activeEntitys.size(); ++i) {
            _activeEntitys.get(i).start();
        }
    }

    public void update(int time) throws Exception {
        completeDefferredDisactivateEntitys();
        completeDefferredActivateEntitys();

        for (int i = 0; i < _activeEntitys.size(); ++i) {
            _activeEntitys.get(i).update(time);
        }
    }

    public void activateEntity(Entity e) {
        _entityToBeActive.add(e);
    }

    public void desactivateEntity(Entity e) {
        _entityToBeDesactive.add(e);
    }


    public List<Entity> getEntitiesByTag(String tag) {
        return _tagMapper.get(tag);
    }

    public List<Entity> getEntitiesByComponent(Class<? extends Component> clazz) {
        List<Entity> l = new ArrayList<>();

        for (int i = 0; i < _activeEntitys.size(); ++i) {
            Component c = _activeEntitys.get(i).getComponent(clazz);
            if (c != null) {
                if (clazz.isAssignableFrom(c.getClass()))
                    l.add(c.getEntity());
            }
        }

        return l;
    }

    public boolean isSimulatoinFinished() {
        return _finished;
    }

    public void setSimulationFinished(boolean finisehd) {
        _finished = finisehd;
    }

    public <T extends Component> List<T> getComponentsInTheSimulator(Class<T> clazz) {
        List<T> l = new ArrayList<>();

        for (int i = 0; i < _activeEntitys.size(); ++i) {
            Component c = _activeEntitys.get(i).getComponent(clazz);
            if (c != null) {
                if (clazz.isAssignableFrom(c.getClass()))
                    l.add(c.getEntity().getComponent(clazz));
            }
        }

        return l;
    }

    protected void CreateWorld(WorldConfig worldConfig) {
        _world = new World(worldConfig);
    }

    protected void ResetInididuals(List<PedestrianGA> individual, ArquetypeSetDescription arquetypeDes, String arquetype) throws EntityCreationException, IllegalAccessException, InstantiationException {
        List<Entity> entities = getEntitiesByComponent(Pedestrian.class);

        if(entities.size() != individual.size())
            throw new RuntimeException("The entity size and de individual of the GA do not have the same dimension entities "+entities.size() + " individual "+individual.size());
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            Pedestrian pedestrian = entity.getComponent(Pedestrian.class);
            PedestrianGA pedestrianGa = individual.get(i);
            List<String> path = pedestrianGa.getPath();
            pedestrian.setPath(path);
            SimulationNode currentPosition = _world.getNode(path.get(0));
            pedestrian.getTransfor().setPosition(currentPosition);
        }

    }
    protected void CreateScene(List<InstanciationConfig> instanciationList, ArquetypeSetDescription arquetypeDes) throws EntityCreationException, IllegalAccessException, InstantiationException {
        _activeEntitys.clear();
        for (InstanciationConfig inst : instanciationList) {
            String arquetype = inst.getArquetype();
            int numElements = inst.getNumElements();
            String instanceName = inst.getName();
            String name = new String(instanceName);
            ArquetypeConfig arquetypeConf = arquetypeDes.getArquetype(arquetype);
            String placeName = inst.getPlace();

            for (int i = 0; i < numElements; ++i) {
                String finalName = numElements > 1 ? name + i : name;
                Entity entity = CreateEntity(finalName,placeName, arquetypeConf, inst);
                addEntity(entity);
            }
        }
    }

    public Entity CreateEntity(String name, String placeName, ArquetypeConfig arquetypeConf, InstanciationConfig inst) throws EntityCreationException, IllegalAccessException, InstantiationException {
        Entity entity = (placeName == null || placeName.compareTo("") == 0) ? new Entity(name,arquetypeConf, inst, this) :
                new EntityTransform(name,arquetypeConf, inst, this);
        return entity;
    }


    protected void completeDefferredDisactivateEntitys() {
        for (int i = 0; i < _entityToBeDesactive.size(); ++i) {
            _activeEntitys.remove(_entityToBeDesactive.get(i));
            _desactivatedEntitys.add(_entityToBeDesactive.get(i));
        }
        _entityToBeDesactive.clear();
    }

    protected void completeDefferredActivateEntitys() {
        for (int i = 0; i < _entityToBeActive.size(); ++i) {
            _desactivatedEntitys.remove(_entityToBeActive.get(i));
            _activeEntitys.add(_entityToBeActive.get(i));
        }
        _entityToBeActive.clear();
    }

    protected void addInTagMapper(Entity e) {
        List<Entity> l = _tagMapper.containsKey(e.getTag()) ? _tagMapper.get(e.getTag()) : new ArrayList<>();
        if (l.indexOf(e) >= 0)
            l.add(e);
    }

    protected void removeOfTagMapper(Entity e) {
        if (_tagMapper.containsKey(e.getTag())) {
            List<Entity> l = _tagMapper.get(e.getTag());
            l.remove(e);
        }
    }

    public void end() {
        for (int i = 0; i < _activeEntitys.size(); ++i) {
            _activeEntitys.get(i).end();
        }
        for (int i = 0; i < _activeEntitys.size(); ++i) {
            _activeEntitys.get(i).Destroy();
        }
        _activeEntitys.clear();
        _desactivatedEntitys.clear();
    }

}
