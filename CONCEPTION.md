## CONTENTS OF THIS FILE
1. [Modifications to the provided code](#modifications)
2. [Added classes and interfaces (includes extension descriptions)](#added-classed-interfaces)
3. [Deviations from the project description](#deviations)
4. [List of all extensions](#list-extensions)

<a id="modifications"></a>
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

  
<a id="added-classed-interfaces"></a>
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
                │       ├── (Class) <a href="#SuperPacman">SuperPacman.java</a>
                │       ├── (Class) <a href="#SuperPacmanStatusGUI">SuperPacmanStatusGUI.java</a> ............................. [extension]
                │       ├── 📦 actor
                │       │   ├── (Class) <a href="#Gate">Gate.java</a>
                │       │   ├── (Class) <a href="#SuperPacmanPlayer">SuperPacmanPlayer.java</a>
                │       │   ├── (Class) <a href="#SuperPacmanPlayerStatusGUI">SuperPacmanPlayerStatusGUI.java</a>
                │       │   ├── (Class) <a href="#Wall">Wall.java</a>
                │       │   ├── 📦 collectables
                │       │   │   ├── (Class) <a href="#Bonus">Bonus.java</a>
                │       │   │   ├── (Class) <a href="#Cake">Cake.java</a> ..................................... [extension]
                │       │   │   ├── (Class) <a href="#Cherry">Cherry.java</a>
                │       │   │   ├── (Class) <a href="#Diamond">Diamond.java</a>
                │       │   │   ├── (Class) <a href="#Key">Key.java</a>
                │       │   │   ├── (Class) <a href="#Pellet">Pellet.java</a> ................................... [extension]
                │       │   │   └── (Class) <a href="#PowerPellet">PowerPellet.java</a> .............................. [extension]
                │       │   └── 📦 ghosts
                │       │       ├── (Class) <a href="#Blinky">Blinky.java</a>
                │       │       ├── (Class) <a href="#Clyde">Clyde.java</a> .................................... [extension]
                │       │       ├── (Abstract Class) <a href="#Ghost">Ghost.java</a>
                │       │       ├── (Class) <a href="#GhostsBehavior">GhostsBehavior.java</a> ........................... [extension]
                │       │       ├── (Class) <a href="#Inky">Inky.java</a>
                │       │       └── (Class) <a href="#Pinky">Pinky.java</a>
                │       ├── 📦 area
                │       │   ├── (Abstract Class) <a href="#SuperPacmanArea">SuperPacmanArea.java</a>
                │       │   ├── (Class) <a href="#SuperPacmanAreaBehavior">SuperPacmanAreaBehavior.java</a>
                │       │   ├── 📦 camera
                │       │   │   ├── (Abstract Class) <a href="#Camera">Camera.java</a> .......................... [extension]
                │       │   │   ├── (Class) <a href="#Fixed">Fixed.java</a> .................................... [extension]
                │       │   │   ├── (Class) <a href="#Follow">Follow.java</a> ................................... [extension]
                │       │   │   ├── (Class) <a href="#SmoothFollow">SmoothFollow.java</a> ............................. [extension]
                │       │   │   └── (Class) <a href="#SmoothLimited">SmoothLimited.java</a> ............................ [extension]
                │       │   └── 📦 levels
                │       │       ├── (Class) <a href="#Level0">Level0.java</a>
                │       │       ├── (Class) <a href="#Level1">Level1.java</a>
                │       │       ├── (Class) <a href="#Level2">Level2.java</a>
                │       │       ├── (Class) <a href="#Level3">Level3.java</a> ................................... [extension]
                │       │       └── (Class) <a href="#LevelEPFL">LevelEPFL.java</a> ................................ [extension]
                │       ├── 📦 globalenums
                │       │   ├── (Enum) <a href="#SuperPacmanDepth">SuperPacmanDepth.java</a> .............................. [extension]
                │       │   ├── (Enum) <a href="#SuperPacmanDifficulty">SuperPacmanDifficulty.java</a> ......................... [extension]
                │       │   └── (Enum) <a href="#SuperPacmanSound">SuperPacmanSound.java</a> .............................. [extension]
                │       ├── 📦 graphics
                │       │   ├── (Class) <a href="#Arcade">Arcade.java</a> ....................................... [extension]
                │       │   ├── (Class) <a href="#Glow">Glow.java</a> ......................................... [extension]
                │       │   └── (Class) <a href="#ScreenFade">ScreenFade.java</a> ................................... [extension]
                │       ├── 📦 handler
                │       │   └── (Interface) <a href="#SuperPacmanInteractionVisitor">SuperPacmanInteractionVisitor.java</a>
                │       ├── 📦 leaderboard
                │       │   ├── (Class) <a href="#GameScore">GameScore.java</a> .................................... [extension]
                │       │   └── (Class) <a href="#LeaderboardGameScores">LeaderboardGameScores.java</a> ........................ [extension]
                │       └── 📦 menus
                │           ├── (Abstract Class) <a href="#Menu">Menu.java</a> ................................ [extension]
                │           ├── (Final Class) <a href="#MenuStateManager">MenuStateManager.java</a> ....................... [extension]
                │           ├── (Enum) <a href="#Option">Option.java</a> ........................................ [extension]
                │           ├── (Enum) <a href="#SubOption">SubOption.java</a> ..................................... [extension]
                │           └── 📦 pages
                │               ├── (Class) <a href="#Credits">Credits.java</a> .................................. [extension]
                │               ├── (Class) <a href="#GameOver">GameOver.java</a> ................................. [extension]
                │               ├── (Class) <a href="#Help">Help.java</a> ..................................... [extension]
                │               ├── (Class) <a href="#HelpGhosts">HelpGhosts.java</a> ............................... [extension]
                │               ├── (Class) <a href="#HelpScore">HelpScore.java</a> ................................ [extension]
                │               ├── (Class) <a href="#Leaderboard">Leaderboard.java</a> .............................. [extension]
                │               ├── (Class) <a href="#MainMenu">MainMenu.java</a> ................................. [extension]
                │               ├── (Class) <a href="#Options">Options.java</a> .................................. [extension]
                │               ├── (Class) <a href="#Pause">Pause.java</a> .................................... [extension]
                │               ├── (Class) <a href="#Play">Play.java</a> ..................................... [extension]
                │               └── (Class) <a href="#Quit">Quit.java</a> ..................................... [extension]
                ├── 📦 io
                │   └── (Class) <a href="#Serialization">Serialization.java</a> ........................................ [extension]
                └── 📦 math
                    └── 📦 transitions
                        ├── (Class) <a href="#EaseInOutCubic">EaseInOutCubic.java</a> ................................... [extension]
                        ├── (Class) <a href="#Linear">Linear.java</a> ........................................... [extension]
                        └── (Abstract Class) <a href="#Transition">Transition.java</a> .............................. [extension]
</pre>
#### Required content

<details>
<summary>
<i>Like this? </i>
</summary>
<p>

<a id="CollectableAreaEntity"></a>
> **(Abstract Class) CollectableAreaEntity.java**  
> This abstract class defines a Collectable Entity in an Area. Points have been integrated and can be overridden by its sub-classes.

<a id="SuperPacman"></a>
> **(Class) SuperPacman.java**  
> 

<a id="Gate"></a>
> **(Class) Gate.java**  
> 

<a id="SuperPacmanPlayer"></a>
> **(Class) SuperPacmanPlayer.java**  
> 

<a id="SuperPacmanPlayerStatusGUI"></a>
> **(Class) SuperPacmanPlayerStatusGUI.java**  
> 

<a id="Wall"></a>
> **(Class) Wall.java**  
> 

<a id="Bonus"></a>
> **(Class) Bonus.java**  
> 

<a id="Cherry"></a>
> **(Class) Cherry.java**  
> 

<a id="Diamond"></a>
> **(Class) Diamond.java**  
> 

<a id="Key"></a>
> **(Class) Key.java**  
> 

<a id="Blinky"></a>
> **(Class) Blinky.java**  
> 

<a id="Ghost"></a>
> **(Abstract Class) Ghost.java**  
> 

<a id="Inky"></a>
> **(Class) Inky.java**  
> 

<a id="Pinky"></a>
> **(Class) Pinky.java**  
> 

<a id="SuperPacmanArea"></a>
> **(Abstract Class) SuperPacmanArea.java**  
> 

<a id="SuperPacmanAreaBehavior"></a>
> **(Class) SuperPacmanAreaBehavior.java**  
> 

<a id="Level0"></a>
> **(Class) Level0.java**  
> 

<a id="Level1"></a>
> **(Class) Level1.java**  
> 

<a id="Level2"></a>
> **(Class) Level2.java**  
> 

<a id="SuperPacmanInteractionVisitor"></a>
> **(Interface) SuperPacmanInteractionVisitor.java**  
> 

</p>
</details>

#### Extensions
<a id="SoundUtility"></a>
> **(Class) SoundUtility.java**  
> This class processes and receives multiple sound requests. It adds them to a List ("Queue"), and plays them once. The methods provide more control to other class who use this class.

<a id="SuperPacmanStatusGUI"></a>
> **(Class) SuperPacmanStatusGUI.java**  
> 

<a id="Cake"></a>
> **(Class) Cake.java**  
> 

<a id="Pellet"></a>
> **(Class) Pellet.java**  
> 

<a id="PowerPellet"></a>
> **(Class) PowerPellet.java**  
> 

<a id="Clyde"></a>
> **(Class) Clyde.java**  
> 

<a id="GhostsBehavior"></a>
> **(Class) GhostsBehavior.java**  
> 

<a id="Camera"></a>
> **(Abstract Class) Camera.java**  
> 

<a id="Fixed"></a>
> **(Class) Fixed.java**  
> 

<a id="Follow"></a>
> **(Class) Follow.java**  
> 

<a id="SmoothFollow"></a>
> **(Class) SmoothFollow.java**  
> 

<a id="SmoothLimited"></a>
> **(Class) SmoothLimited.java**  
> 

<a id="Level3"></a>
> **(Class) Level3.java**  
> 

<a id="LevelEPFL"></a>
> **(Class) LevelEPFL.java**  
> 

<a id="SuperPacmanDepth"></a>
> **(Enum) SuperPacmanDepth.java**  
> 

<a id="SuperPacmanDifficulty"></a>
> **(Enum) SuperPacmanDifficulty.java**  
> 

<a id="SuperPacmanSound"></a>
> **(Enum) SuperPacmanSound.java**  
> 

<a id="Arcade"></a>
> **(Class) Arcade.java**  
> 

<a id="Glow"></a>
> **(Class) Glow.java**  
> 

<a id="ScreenFade"></a>
> **(Class) ScreenFade.java**  
> 

<a id="GameScore"></a>
> **(Class) GameScore.java**  
> 

<a id="LeaderboardGameScores"></a>
> **(Class) LeaderboardGameScores.java**  
> 

<a id="Menu"></a>
> **(Abstract Class) Menu.java**  
> 

<a id="MenuStateManager"></a>
> **(Final Class) MenuStateManager.java**  
> 

<a id="Option"></a>
> **(Enum) Option.java**  
> 

<a id="SubOption"></a>
> **(Enum) SubOption.java**  
> 

<a id="Credits"></a>
> **(Class) Credits.java**  
> 

<a id="GameOver"></a>
> **(Class) GameOver.java**  
> 

<a id="Help"></a>
> **(Class) Help.java**  
> 

<a id="HelpGhosts"></a>
> **(Class) HelpGhosts.java**  
> 

<a id="HelpScore"></a>
> **(Class) HelpScore.java**  
> 

<a id="Leaderboard"></a>
> **(Class) Leaderboard.java**  
> 

<a id="MainMenu"></a>
> **(Class) MainMenu.java**  
> 

<a id="Options"></a>
> **(Class) Options.java**  
> 

<a id="Pause"></a>
> **(Class) Pause.java**  
> 

<a id="Play"></a>
> **(Class) Play.java**  
> 

<a id="Quit"></a>
> **(Class) Quit.java**  
> 

<a id="Serialization"></a>
> **(Class) Serialization.java**  
> 

<a id="EaseInOutCubic"></a>
> **(Class) EaseInOutCubic.java**  
> 

<a id="Linear"></a>
> **(Class) Linear.java**  
> 

<a id="Transition"></a>
> **(Abstract Class) Transition.java**  
> 


<a id="deviations"></a>
## 3. DEVIATIONS FROM THE PROJECT DESCRIPTION

<a id="list-extensions"></a>
## 4. LIST OF ALL EXTENSIONS
