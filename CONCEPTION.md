## CONTENTS OF THIS FILE
1. Modifications to the provided code
2. Added classes and interfaces (includes extension descriptions)
3. Deviations from the project description
4. List of all extensions


## 1. MODIFICATIONS TO THE PROVIDED CONTENT

```
.
└── ch
    └── epfl
        └── cs107
            └── play
                ├── Play.java
                │   └> Added new thread to compute the actual FPS
                ├── game
                │   ├── Game.java
                │   │   └> Set the FPS limit to 60   
                │   ├── actor
                │   │   └── TextGraphics.java
                │   │       └> Implemented Serializable interface
                │   ├── areagame
                │   │   ├── Area.java
                │   │   │   └> Removed viewCenter attribute
                │   │   │   └> Added Camera instance
                │   │   │   └> Moved methods to update camera to Camera.java
                │   │   │   └> Created getter for camera (allows to call shake() method)
                │   │   │   └> Adapted class to work with Camera
                │   │   ├── AreaGame.java
                │   │   │   └> Added MenuStateManager instance
                │   │   │   └> Modified update() method to work with menu states
                │   │   └── actor
                │   │       └── Foreground.java
                │   │           └> Modified depth to SuperPacmanDepth enum
                │   └── rpg
                │       ├── RPG.java
                │       │   └> Modified forceBegin to true in update() method for creating new areas
                │       └── actor
                │           └── Door.java
                │               └> Set to implement Interactor (allows to fade screen when play is in range)
                │               └> Activates debug drawing when debutMode is set from the menu
                └── window
                    ├── Keyboard.java
                    │   └> Added more key codes (ENTER, SHIFT, CTRL, ALT, ESC)
                    └── swing
                        ├── SoundItem.java
                        │   └> Added .loop() method in start() for audio clips
                        │   └> Removed loop condition in update()
                        └── SwingWindow.java
                            └> Added in constructor .setLocationRelativeTo(null) to frame
```

  
  
## 2. ADDED CLASSED AND INTERFACES

```
.
└── ch
    └── epfl
        └── cs107
            └── play
                ├── game
                │   ├── areagame
                │   │   └── actor
                │   │       └── CollectableAreaEntity.java
                │   └── superpacman
                │       ├── SoundUtility.java
                │       ├── SuperPacman.java
                │       ├── SuperPacmanStatusGUI.java
                │       ├── actor
                │       │   ├── Gate.java
                │       │   ├── SuperPacmanPlayer.java
                │       │   ├── SuperPacmanPlayerStatusGUI.java
                │       │   ├── Wall.java
                │       │   ├── collectables
                │       │   │   ├── Bonus.java
                │       │   │   ├── Cake.java
                │       │   │   ├── Cherry.java
                │       │   │   ├── Diamond.java
                │       │   │   ├── Key.java
                │       │   │   ├── Pellet.java
                │       │   │   └── PowerPellet.java
                │       │   └── ghosts
                │       │       ├── Blinky.java
                │       │       ├── Clyde.java
                │       │       ├── Ghost.java
                │       │       ├── GhostsBehavior.java
                │       │       ├── Inky.java
                │       │       └── Pinky.java
                │       ├── area
                │       │   ├── SuperPacmanArea.java
                │       │   ├── SuperPacmanAreaBehavior.java
                │       │   ├── camera
                │       │   │   ├── Camera.java
                │       │   │   ├── Fixed.java
                │       │   │   ├── Follow.java
                │       │   │   ├── SmoothFollow.java
                │       │   │   └── SmoothLimited.java
                │       │   └── levels
                │       │       ├── Level0.java
                │       │       ├── Level1.java
                │       │       ├── Level2.java
                │       │       ├── Level3.java
                │       │       └── LevelEPFL.java
                │       ├── globalenums
                │       │   ├── SuperPacmanDepth.java
                │       │   ├── SuperPacmanDifficulty.java
                │       │   └── SuperPacmanSound.java
                │       ├── graphics
                │       │   ├── Arcade.java
                │       │   ├── Glow.java
                │       │   └── ScreenFade.java
                │       ├── handler
                │       │   └── SuperPacmanInteractionVisitor.java
                │       ├── leaderboard
                │       │   ├── GameScore.java
                │       │   └── LeaderboardGameScores.java
                │       └── menus
                │           ├── Menu.java
                │           ├── MenuStateManager.java
                │           ├── Option.java
                │           ├── SubOption.java
                │           └── pages
                │               ├── Credits.java
                │               ├── GameOver.java
                │               ├── Help.java
                │               ├── HelpGhosts.java
                │               ├── HelpScore.java
                │               ├── Leaderboard.java
                │               ├── MainMenu.java
                │               ├── Options.java
                │               ├── Pause.java
                │               ├── Play.java
                │               └── Quit.java
                ├── io
                │   └── Serialization.java
                └── math
                    └── transitions
                        ├── EaseInOutCubic.java
                        ├── Linear.java
                        └── Transition.java
```