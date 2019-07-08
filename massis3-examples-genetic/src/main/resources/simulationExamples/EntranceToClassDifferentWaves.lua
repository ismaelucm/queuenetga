-- The simulation has two pedestrian waves.
-- In the first wave, the simulation creates twelve groups of pedestrian, six of them in the main gate and another six in the back gate. 
-- All groups walking with different speeds between 1-5 and with the destination at the different classrooms and the cafeteria.
-- The second wave is the same of the first wave, but start 50 seconds after.
-- All they using the behavior FollowingPath

Scenario = {
    CameraConfig = {
        location = { 33.0, 165.0, 54.0 },
        rotation = { 90.0, 0.0, 0.0 },
        lookAt = { 0.0, 0.0, 0.0 }
    },
    AgentsDescriptions = {
        Class1 = {
            behavior = "FollowingPath",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "Class1"
            }
        },
        Class2 = {
            behavior = "FollowingPath",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "Class2"
            }
        },
        Class3 = {
            behavior = "FollowingPath",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "Class3"
            }
        },
        Class4 = {
            behavior = "FollowingPath",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "Class4"
            }
        },
        Class5 = {
            behavior = "FollowingPath",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "Class5"
            }
        },
        Cafeteria = {
            behavior = "FollowingPath",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "StudentsCafeteria"
            }
        }
    }
}

Commands :

MassisLua.createHuman("Class1", 10, "MainGate")
MassisLua.createHuman("Class1", 10, "BackGate")
MassisLua.createHuman("Class2", 10, "MainGate")
MassisLua.createHuman("Class2", 10, "BackGate")
MassisLua.createHuman("Class3", 10, "MainGate")
MassisLua.createHuman("Class3", 10, "BackGate")
MassisLua.createHuman("Class4", 10, "MainGate")
MassisLua.createHuman("Class4", 10, "BackGate")
MassisLua.createHuman("Class5", 10, "MainGate")
MassisLua.createHuman("Class5", 10, "BackGate")
MassisLua.createHuman("Cafeteria", 10, "MainGate")
MassisLua.createHuman("Cafeteria", 10, "BackGate")

MassisLua.createHumanDeferred("Class1", 10, "MainGate", 50)
MassisLua.createHumanDeferred("Class1", 10, "BackGate", 50)
MassisLua.createHumanDeferred("Class2", 10, "MainGate", 50)
MassisLua.createHumanDeferred("Class2", 10, "BackGate", 50)
MassisLua.createHumanDeferred("Class3", 10, "MainGate", 50)
MassisLua.createHumanDeferred("Class3", 10, "BackGate", 50)
MassisLua.createHumanDeferred("Class4", 10, "MainGate", 50)
MassisLua.createHumanDeferred("Class4", 10, "BackGate", 50)
MassisLua.createHumanDeferred("Class5", 10, "MainGate", 50)
MassisLua.createHumanDeferred("Class5", 10, "BackGate", 50)
MassisLua.createHumanDeferred("Cafeteria", 10, "MainGate", 50)
MassisLua.createHumanDeferred("Cafeteria", 10, "BackGate", 50)