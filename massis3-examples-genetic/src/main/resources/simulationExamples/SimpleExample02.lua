-- This simulation creates two groups of pedestrian, one of then in the main gate and another in the back gate. 
-- Both walking with different speeds between 1-5 and with the destination at the first classroom.

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

MassisLua.createHuman("population", 20, "MainGate")
MassisLua.createHuman("population", 20, "BackGate")
-- HallPrinicpal.Entrada,HallPrinicpal.Biblioteca,PasilloSalonActos.Hall,PasilloAulas.Aula1,