## CONTENTS OF THIS FILE
1. Modifications to the provided code
2. Added classes and interfaces (includes extension descriptions)
3. Deviations from the project description
4. List of all extensions


## 1. MODIFICATIONS TO THE PROVIDED CONTENT

```
.
└── 📦 ch
    └── 📦 epfl
        └── 📦 cs107
            └── 📦 play
                ├── Play.java ════════════════════════════════════════════════════════════╗
                │   ║ └> Added new thread to compute the actual FPS                       ║
                │   ╚═════════════════════════════════════════════════════════════════════╝
                ├── 📦 game
                │   ├── Game.java ════════════════════════════════════════════════════════════╗
                │   │   ║ └> Set the FPS to 60                                                ║
                │   │   ╚═════════════════════════════════════════════════════════════════════╝  
                │   ├── 📦 actor
                │   │   └── TextGraphics.java ════════════════════════════════════════════════════╗
                │   │       ║ └> Implemented Serializable interface                               ║
                │   │       ╚═════════════════════════════════════════════════════════════════════╝
                │   ├── 📦 areagame
                │   │   ├── Area.java ════════════════════════════════════════════════════════════╗
                │   │   │   ║ └> Removed viewCenter attribute                                     ║
                │   │   │   ║ └> Added Camera instance                                            ║
                │   │   │   ║ └> Moved methods to update camera to Camera.java                    ║
                │   │   │   ║ └> Created getter for camera (allows to call shake() method)        ║
                │   │   │   ║ └> Adapted class to work with Camera                                ║
                │   │   │   ╚═════════════════════════════════════════════════════════════════════╝
                │   │   ├── AreaGame.java ════════════════════════════════════════════════════════╗
                │   │   │   ║ └> Added MenuStateManager instance                                  ║
                │   │   │   ║ └> Modified update() method to work with menu states                ║
                │   │   │   ╚═════════════════════════════════════════════════════════════════════╝
                │   │   └── 📦 actor
                │   │       └── Foreground.java ══════════════════════════════════════════════════════╗
                │   │           ║ └> Modified depth to SuperPacmanDepth enum                          ║
                │   │           ╚═════════════════════════════════════════════════════════════════════╝
                │   └── 📦 rpg
                │       ├── RPG.java ═════════════════════════════════════════════════════════════╗
                │       │   ║ └> Modified forceBegin to true in update() method for creating      ║
                │       │   ║    new areas                                                        ║
                │       │   ╚═════════════════════════════════════════════════════════════════════╝
                │       └── 📦 actor
                │           └── Door.java ════════════════════════════════════════════════════════════╗
                │               ║ └> Set to implement Interactor (allows to fade screen when player   ║
                │               ║    is in range)                                                     ║
                │               ║ └> Activates debug drawing when debutMode is set from the menu      ║
                │               ╚═════════════════════════════════════════════════════════════════════╝
                └── 📦 window
                    ├── Keyboard.java ════════════════════════════════════════════════════════╗
                    │   ║ └> Added more key codes (ENTER, SHIFT, CTRL, ALT, ESC)              ║
                    │   ╚═════════════════════════════════════════════════════════════════════╝
                    └── 📦 swing
                        ├── SoundItem.java ═══════════════════════════════════════════════════════╗
                        │   ║ └> Added .loop() method in start() for audio clips                  ║
                        │   ║ └> Removed loop condition in update()                               ║
                        │   ╚═════════════════════════════════════════════════════════════════════╝
                        └── SwingWindow.java ═════════════════════════════════════════════════════╗
                            ║ └> Added in constructor .setLocationRelativeTo(null) to frame       ║
                            ╚═════════════════════════════════════════════════════════════════════╝
```

  
  
## 2. ADDED CLASSED AND INTERFACES

```
.
└── 📦 ch
    └── 📦 epfl
        └── 📦 cs107
            └── 📦 play
                ├── 📦 game
                │   ├── 📦 areagame
                │   │   └── 📦 actor
                │   │       └── (Abstract Class) CollectableAreaEntity.java ══════════════════════════╗
                │   │           ║ Allows the user to devvelop and decide more
                │   │           ║ content
                │   │           ╚═════════════════════════════════════════════════════════════════════╝
                │   └── 📦 superpacman
                │       ├── (Class) SoundUtility.java ..................................... [extention]
                │       ├── (Class) SuperPacman.java
                │       ├── (Class) SuperPacmanStatusGUI.java ............................. [extention]
                │       ├── 📦 actor
                │       │   ├── (Class) Gate.java
                │       │   ├── (Class) SuperPacmanPlayer.java
                │       │   ├── (Class) SuperPacmanPlayerStatusGUI.java
                │       │   ├── (Class) Wall.java
                │       │   ├── 📦 collectables
                │       │   │   ├── (Class) Bonus.java
                │       │   │   ├── (Class) Cake.java
                │       │   │   ├── (Class) Cherry.java
                │       │   │   ├── (Class) Diamond.java
                │       │   │   ├── (Class) Key.java
                │       │   │   ├── (Class) Pellet.java
                │       │   │   └── (Class) PowerPellet.java
                │       │   └── 📦 ghosts
                │       │       ├── (Class) Blinky.java
                │       │       ├── (Class) Clyde.java .................................... [extention]
                │       │       ├── (Abstract Class) Ghost.java
                │       │       ├── (Class) GhostsBehavior.java ........................... [extention]
                │       │       ├── (Class) Inky.java
                │       │       └── (Class) Pinky.java
                │       ├── 📦 area
                │       │   ├── (Abstract Class) SuperPacmanArea.java
                │       │   ├── (Class) SuperPacmanAreaBehavior.java
                │       │   ├── 📦 camera
                │       │   │   ├── (Abstract Class) Camera.java .......................... [extention]
                │       │   │   ├── (Class) Fixed.java .................................... [extention]
                │       │   │   ├── (Class) Follow.java ................................... [extention]
                │       │   │   ├── (Class) SmoothFollow.java ............................. [extention]
                │       │   │   └── (Class) SmoothLimited.java ............................ [extention]
                │       │   └── 📦 levels
                │       │       ├── (Class) Level0.java
                │       │       ├── (Class) Level1.java
                │       │       ├── (Class) Level2.java
                │       │       ├── (Class) Level3.java ................................... [extention]
                │       │       └── (Class) LevelEPFL.java ................................ [extention]
                │       ├── 📦 globalenums
                │       │   ├── (Enum) SuperPacmanDepth.java .............................. [extention]
                │       │   ├── (Enum) SuperPacmanDifficulty.java ......................... [extention]
                │       │   └── (Enum) SuperPacmanSound.java .............................. [extention]
                │       ├── 📦 graphics
                │       │   ├── (Class) Arcade.java ....................................... [extention]
                │       │   ├── (Class) Glow.java ......................................... [extention]
                │       │   └── (Class) ScreenFade.java ................................... [extention]
                │       ├── 📦 handler
                │       │   └── (Interface) SuperPacmanInteractionVisitor.java
                │       ├── 📦 leaderboard
                │       │   ├── (Class) GameScore.java .................................... [extention]
                │       │   └── (Class) LeaderboardGameScores.java ........................ [extention]
                │       └── 📦 menus
                │           ├── (Abstract Class) Menu.java ................................ [extention]
                │           ├── (Final Class) MenuStateManager.java ....................... [extention]
                │           ├── (Enum) Option.java ........................................ [extention]
                │           ├── (Enum) SubOption.java ..................................... [extention]
                │           └── 📦 pages
                │               ├── (Class) Credits.java .................................. [extention]
                │               ├── (Class) GameOver.java ................................. [extention]
                │               ├── (Class) Help.java ..................................... [extention]
                │               ├── (Class) HelpGhosts.java ............................... [extention]
                │               ├── (Class) HelpScore.java ................................ [extention]
                │               ├── (Class) Leaderboard.java .............................. [extention]
                │               ├── (Class) MainMenu.java ................................. [extention]
                │               ├── (Class) Options.java .................................. [extention]
                │               ├── (Class) Pause.java .................................... [extention]
                │               ├── (Class) Play.java ..................................... [extention]
                │               └── (Class) Quit.java ..................................... [extention]
                ├── 📦 io
                │   └── (Class) Serialization.java ........................................ [extention]
                └── 📦 math
                    └── 📦 transitions
                        ├── (Class) EaseInOutCubic.java ................................... [extention]
                        ├── (Class) Linear.java ........................................... [extention]
                        └── (Abstract Class) Transition.java .............................. [extention]
```