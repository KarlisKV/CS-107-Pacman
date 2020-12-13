## CONTENTS OF THIS FILE
1. Modifications to the provided code
2. Added classes and interfaces (includes extension descriptions)
3. Deviations from the project description
4. List of all extensions


## 1. MODIFICATIONS TO THE PROVIDED CONTENT

<pre>
.
└── 📦 ch
    └── 📦 epfl
        └── 📦 cs107
            └── 📦 play
                ├── Play.java ════════════════════════════════════════════════════════════════════════╗
                │   ║ └> Added new thread to compute the actual FPS                                   ║
                │   ╚═════════════════════════════════════════════════════════════════════════════════╝
                ├── 📦 game
                │   ├── Game.java ════════════════════════════════════════════════════════════════════╗
                │   │   ║ └> Set the FPS to 60                                                        ║
                │   │   ╚═════════════════════════════════════════════════════════════════════════════╝  
                │   ├── 📦 actor
                │   │   └── TextGraphics.java ════════════════════════════════════════════════════════╗
                │   │       ║ └> Implemented Serializable interface                                   ║
                │   │       ╚═════════════════════════════════════════════════════════════════════════╝
                │   ├── 📦 areagame
                │   │   ├── Area.java ════════════════════════════════════════════════════════════════╗
                │   │   │   ║ └> Removed viewCenter attribute                                         ║
                │   │   │   ║ └> Added Camera instance                                                ║
                │   │   │   ║ └> Moved methods to update camera to Camera.java                        ║
                │   │   │   ║ └> Created getter for camera (allows to call shake() method)            ║
                │   │   │   ║ └> Adapted class to work with Camera                                    ║
                │   │   │   ╚═════════════════════════════════════════════════════════════════════════╝
                │   │   ├── AreaGame.java ════════════════════════════════════════════════════════════╗
                │   │   │   ║ └> Added MenuStateManager instance                                      ║
                │   │   │   ║ └> Modified update() method to work with menu states                    ║
                │   │   │   ╚═════════════════════════════════════════════════════════════════════════╝
                │   │   └── 📦 actor
                │   │       └── Foreground.java ══════════════════════════════════════════════════════╗
                │   │           ║ └> Modified depth to SuperPacmanDepth enum                          ║
                │   │           ╚═════════════════════════════════════════════════════════════════════╝
                │   └── 📦 rpg
                │       ├── RPG.java ═════════════════════════════════════════════════════════════════╗
                │       │   ║ └> Modified forceBegin to true in update() method for creating          ║
                │       │   ║    new areas                                                            ║
                │       │   ╚═════════════════════════════════════════════════════════════════════════╝
                │       └── 📦 actor
                │           └── Door.java ════════════════════════════════════════════════════════════╗
                │               ║ └> Set to implement Interactor (allows to fade screen when player   ║
                │               ║    is in range)                                                     ║
                │               ║ └> Activates debug drawing when debutMode is set from the menu      ║
                │               ╚═════════════════════════════════════════════════════════════════════╝
                └── 📦 window
                    ├── Keyboard.java ════════════════════════════════════════════════════════════════╗
                    │   ║ └> Added more key codes (ENTER, SHIFT, CTRL, ALT, ESC)                      ║
                    │   ╚═════════════════════════════════════════════════════════════════════════════╝
                    └── 📦 swing
                        ├── SoundItem.java ═══════════════════════════════════════════════════════════╗
                        │   ║ └> Added .loop() method in start() for audio clips                      ║
                        │   ║ └> Removed loop condition in update()                                   ║
                        │   ╚═════════════════════════════════════════════════════════════════════════╝
                        └── SwingWindow.java ═════════════════════════════════════════════════════════╗
                            ║ └> Added in constructor .setLocationRelativeTo(null) to frame           ║
                            ╚═════════════════════════════════════════════════════════════════════════╝
</pre>

  
  
## 2. ADDED CLASSED AND INTERFACES

<pre>
.
└── 📦 ch
    └── 📦 epfl
        └── 📦 cs107
            └── 📦 play
                ├── 📦 game
                │   ├── 📦 areagame
                │   │   └── 📦 actor
                │   │       └── (Abstract Class) <a href="#CollectableAreaEntity">CollectableAreaEntity.java</a>
                │   └── 📦 superpacman
                │       ├── (Class) <a href="#SoundUtility">SoundUtility.java</a> ..................................... [extension]
                │       ├── (Class) SuperPacman.java
                │       ├── (Class) SuperPacmanStatusGUI.java ............................. [extension]
                │       ├── 📦 actor
                │       │   ├── (Class) Gate.java
                │       │   ├── (Class) SuperPacmanPlayer.java
                │       │   ├── (Class) SuperPacmanPlayerStatusGUI.java
                │       │   ├── (Class) Wall.java
                │       │   ├── 📦 collectables
                │       │   │   ├── (Class) Bonus.java
                │       │   │   ├── (Class) Cake.java ..................................... [extension]
                │       │   │   ├── (Class) Cherry.java
                │       │   │   ├── (Class) Diamond.java
                │       │   │   ├── (Class) Key.java
                │       │   │   ├── (Class) Pellet.java ................................... [extension]
                │       │   │   └── (Class) PowerPellet.java .............................. [extension]
                │       │   └── 📦 ghosts
                │       │       ├── (Class) Blinky.java
                │       │       ├── (Class) Clyde.java .................................... [extension]
                │       │       ├── (Abstract Class) Ghost.java
                │       │       ├── (Class) GhostsBehavior.java ........................... [extension]
                │       │       ├── (Class) Inky.java
                │       │       └── (Class) Pinky.java
                │       ├── 📦 area
                │       │   ├── (Abstract Class) SuperPacmanArea.java
                │       │   ├── (Class) SuperPacmanAreaBehavior.java
                │       │   ├── 📦 camera
                │       │   │   ├── (Abstract Class) Camera.java .......................... [extension]
                │       │   │   ├── (Class) Fixed.java .................................... [extension]
                │       │   │   ├── (Class) Follow.java ................................... [extension]
                │       │   │   ├── (Class) SmoothFollow.java ............................. [extension]
                │       │   │   └── (Class) SmoothLimited.java ............................ [extension]
                │       │   └── 📦 levels
                │       │       ├── (Class) Level0.java
                │       │       ├── (Class) Level1.java
                │       │       ├── (Class) Level2.java
                │       │       ├── (Class) Level3.java ................................... [extension]
                │       │       └── (Class) LevelEPFL.java ................................ [extension]
                │       ├── 📦 globalenums
                │       │   ├── (Enum) SuperPacmanDepth.java .............................. [extension]
                │       │   ├── (Enum) SuperPacmanDifficulty.java ......................... [extension]
                │       │   └── (Enum) SuperPacmanSound.java .............................. [extension]
                │       ├── 📦 graphics
                │       │   ├── (Class) Arcade.java ....................................... [extension]
                │       │   ├── (Class) Glow.java ......................................... [extension]
                │       │   └── (Class) ScreenFade.java ................................... [extension]
                │       ├── 📦 handler
                │       │   └── (Interface) SuperPacmanInteractionVisitor.java
                │       ├── 📦 leaderboard
                │       │   ├── (Class) GameScore.java .................................... [extension]
                │       │   └── (Class) LeaderboardGameScores.java ........................ [extension]
                │       └── 📦 menus
                │           ├── (Abstract Class) Menu.java ................................ [extension]
                │           ├── (Final Class) MenuStateManager.java ....................... [extension]
                │           ├── (Enum) Option.java ........................................ [extension]
                │           ├── (Enum) SubOption.java ..................................... [extension]
                │           └── 📦 pages
                │               ├── (Class) Credits.java .................................. [extension]
                │               ├── (Class) GameOver.java ................................. [extension]
                │               ├── (Class) Help.java ..................................... [extension]
                │               ├── (Class) HelpGhosts.java ............................... [extension]
                │               ├── (Class) HelpScore.java ................................ [extension]
                │               ├── (Class) Leaderboard.java .............................. [extension]
                │               ├── (Class) MainMenu.java ................................. [extension]
                │               ├── (Class) Options.java .................................. [extension]
                │               ├── (Class) Pause.java .................................... [extension]
                │               ├── (Class) Play.java ..................................... [extension]
                │               └── (Class) Quit.java ..................................... [extension]
                ├── 📦 io
                │   └── (Class) Serialization.java ........................................ [extension]
                └── 📦 math
                    └── 📦 transitions
                        ├── (Class) EaseInOutCubic.java ................................... [extension]
                        ├── (Class) Linear.java ........................................... [extension]
                        └── (Abstract Class) Transition.java .............................. [extension]
</pre>
#### Required content
<a name="CollectableAreaEntity"></a>
> **(Abstract Class) CollectableAreaEntity.java**  
> This abstract class defines a Collectable Entity in an Area. Points have been integrated and can be overridden by its sub-classes.

#### Extensions
<a name="SoundUtility"></a>
> **(Class) SoundUtility.java**  
> This class processes and receives multiple sound requests. It adds them to a List ("Queue"), and plays them once. The methods provide more control to other class who use this class.