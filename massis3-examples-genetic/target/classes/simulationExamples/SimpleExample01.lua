-- A simple simulation where we create one pedestrian outside the faculty going to the first classroom. 
-- Its walking speed is randomly between 1 - 5 and it is using the behavior FollowingPath

Scenario = {
    CameraConfig = {
        location = { 85.0, 100.0, 54.0 },
        rotation = { 90.0, 0.0, 0.0 },
        lookAt = { 85.0, 0.0, 54.0 }
    },
    AgentsDescriptions = {
        population = {
            behavior = "FollowingPath",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "Class1"
            }
        }
    }
}

Commands :

MassisLua.createHuman("population", 1, "MainGate")
-- HallPrinicpal.Entrada,HallPrinicpal.Biblioteca,PasilloSalonActos.Hall,PasilloAulas.Aula1,