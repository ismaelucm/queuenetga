-- The simulation simulates an evacuation of the five classrooms. The path to follow has been determined in the simulation.
-- The populations Aula1, Class2, Class3 are evacuated through the main door
-- The populations Aula4, Aula5, Class3 are evacuated through the back door
-- the population Cafeteria is evacuated using the shortes path to through the main door
-- All they using the behavior FollowingPath

Scenario = {
    CameraConfig = {
        location = { 33.0, 165.0, 54.0 },
        rotation = { 90.0, 0.0, 0.0 },
        lookAt = { 0.0, 0.0, 0.0 }
    },
    AgentsDescriptions = {
        Class1 = {
            behavior = "FollowingPathWithSpeed",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "ClassesLobby.Class1,LobbyEventRoom.Hall,MainHall.Entrance,MainGate",
                SpeedPath = "1.0,5.0,10.0,15.0"
            }
        },
        Class2 = {
            behavior = "FollowingPathWithSpeed",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "ClassesLobby.Class2,ClassesLobby.Class1,LobbyEventRoom.Hall,MainHall.Entrance,MainGate",
                SpeedPath = "1.0,5.0,10.0,15.0"
            }
        },
        Class3 = {
            behavior = "FollowingPathWithSpeed",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "ClassesLobby.Class3,ClassesLobby.Class2,ClassesLobby.Class1,MainHall.Entrance,MainGate",
                SpeedPath = "1.0,5.0,10.0,15.0"
            }
        },
        Class4 = {
            behavior = "FollowingPathWithSpeed",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "ClassesLobby.Class4,ClassesLobby.Class5,HallBackGate.Lobby,BackGate",
                SpeedPath = "1.0,5.0,10.0"
            }
        },
        Class5 = {
            behavior = "FollowingPathWithSpeed",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "ClassesLobby.Class5,HallBackGate.Lobby,BackGate",
                SpeedPath = "1.0,5.0,10.0"
            }
        },
        Cafeteria = {
            behavior = "FollowingPathWithSpeed",
            SpeedMin = 1.0,
            SpeedMax = 5.0,
            AnimationSpeedReference = 4.0,
            RewriteParameter = {
                Path = "MainGate",
                SpeedPath = "1.0"
            }
        }
    }
}

Commands :

MassisLua.createHuman("Class1", 30, "Class1")
MassisLua.createHuman("Class2", 30, "Class2")
MassisLua.createHuman("Class3", 30, "Class3")
MassisLua.createHuman("Class4", 30, "Class4")
MassisLua.createHuman("Class5", 30, "Class5")
MassisLua.createHuman("Cafeteria", 30, "ProfessorsCafeteria")
