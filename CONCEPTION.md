## 0. CONTENTS OF THIS FILE
1. [MODIFICATIONS TO THE PROVIDED CODE](#modifications)  
2. [ADDED CLASSES AND INTERFACES (includes extension descriptions)](#added-classes-interfaces)  
    2.1 [Required content](#required-content)  
    2.2 [Extensions](#extensions)  
3. [DEVIATIONS FROM THE PROJECT DESCRIPTION](#deviations)  
4. [LIST OF ALL EXTENSIONS](#list-extensions)  

<a id="modifications"></a>
## 1. MODIFICATIONS TO THE PROVIDED CONTENT

**Note:** In each modified class, a comment containing "[modification]" has been left. This allows to quick search and find where exactly the code has been altered.

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
                │   │   ├── RPG.java ═════════════════════════════════════════════════════════════════╗
                │   │   │   ║ └> Modified forceBegin to true in update() method for creating          ║
                │   │   │   ║    new areas                                                            ║
                │   │   │   ╚═════════════════════════════════════════════════════════════════════════╝
                │   │   └── 📦 actor
                │   │       └── Door.java ════════════════════════════════════════════════════════════╗
                │   │           ║ └> Set to implement Interactor (allows to fade screen when player   ║
                │   │           ║    is in range)                                                     ║
                │   │           ║ └> Activates debug drawing when debutMode is set from the menu      ║
                │   │           ║ └> Added method isDestinationSameArea() to determine if door leads  ║
                │   │           ║    to the same area                                                 ║
                │   │           ╚═════════════════════════════════════════════════════════════════════╝
                │   └── 📦 superpacman
                │        └── 📦 actor
                │             └── Wall.java ══════════════════════════════════════════════════════════╗
                │                 ║ └> Changed default Wall pathname to new .png                      ║
                │                 ║ └> Added condition to set the wall pathname to red wall for EPFL  ║
                │                 ║    level                                                          ║
                │                 ║ └> Added setDepth() to the constructor                            ║
                │                 ║ └> Added isViewInteractable = true                                ║
                │                 ╚═══════════════════════════════════════════════════════════════════╝
                ├── 📦 math
                │   └── Vector.java ══════════════════════════════════════════════════════════════════╗
                │       ║ └> Added method dist() to compute distance between two Vector points        ║
                │       ╚═════════════════════════════════════════════════════════════════════════════╝
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

  
<a id="added-classes-interfaces"></a>
## 2. ADDED CLASSES AND INTERFACES

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
                        ├── (Class) <a href="#EaseOutCirc">EaseOutCirc.java</a> ...................................... [extension]
                        ├── (Class) <a href="#Linear">Linear.java</a> ........................................... [extension]
                        └── (Abstract Class) <a href="#Transition">Transition.java</a> .............................. [extension]
</pre>


<a id="required-content"></a>
### 2.1 Required content

<a id="CollectableAreaEntity"></a>
> **(Abstract Class) [CollectableAreaEntity.java](java/ch/epfl/cs107/play/game/areagame/actor/CollectableAreaEntity.java)** - extends MovableAreaEntity.java  
> This abstract class defines a Collectable Entity in an Area. Points have been integrated and can be overridden by its sub-classes.
---
<a id="SuperPacman"></a>
> **(Class) [SuperPacman.java](java/ch/epfl/cs107/play/game/superpacman/SuperPacman.java)** - extends RPG.java  
> The class manages the SuperPacmanPlayer and creates the areas.  
>> Extensions: 
>> - Used new Sprites(.png) 
>> - Includes the player's [LeaderboardGameScores.java](#LeaderboardGameScores)
>> - Deserializing it in begin() method and adding new [GameScore.java](#GameScore) to it
>> - Manages the state of the arcade (on/off) and an animation for the cameraScaleFactor.
---
<a id="Gate"></a>
> **(Class) [Gate.java](java/ch/epfl/cs107/play/game/superpacman/actor/Gate.java)** - extends AreaEntity.java  
> Allows to block parts of the level. Can be unlocked with a key ([Key.java](#Key)).
>> Extensions:
>> - Used new Sprites(.png)
---
<a id="SuperPacmanPlayer"></a>
> **(Class) [SuperPacmanPlayer.java](java/ch/epfl/cs107/play/game/superpacman/actor/SuperPacmanPlayer.java)** - extends Player.java  
> Class for the main player. Contains its GUI [SuperPacmanPlayerStatusGUI.java](#SuperPacmanPlayerStatusGUI). Contains sub-class for the interactions with a Door, Ghost, Key, Cake, Pellet, PowerPellet.
>> Extensions: 
>> - Used new Sprites(.png) 
>> - Includes a death animation
>> - Glow effect
>> - Added collision ranged interaction with Walls and camera shake in Ghost interactions
---
<a id="SuperPacmanPlayerStatusGUI"></a>
> **(Class) [SuperPacmanPlayerStatusGUI.java](java/ch/epfl/cs107/play/game/superpacman/actor/SuperPacmanPlayerStatusGUI.java)** - implements Graphics.java  
> Indicates the score and lives of the player.
>> Extensions:
>> - Used new Sprites(.png)
>> - Repositioned GUI
>> - Added a pellet counter
>> - Added combos for score
>> - Added timer for current area and past areas
>> - Added difficulty indicator and multiplier
---
<a id="Bonus"></a>
> **(Class) [Bonus.java](java/ch/epfl/cs107/play/game/superpacman/actor/collectables/Bonus.java)** - extends [CollectableAreaEntity.java](#CollectableAreaEntity)  
> Class representing "PowerPellet" - not used in project, required.
---
<a id="Cherry"></a>
> **(Class) [Cherry.java](java/ch/epfl/cs107/play/game/superpacman/actor/collectables/Cherry.java)** - extends [CollectableAreaEntity.java](#CollectableAreaEntity)    
> Class representing "Cake" - not used in project, required.
---
<a id="Diamond"></a>
> **(Class) [Diamond.java](java/ch/epfl/cs107/play/game/superpacman/actor/collectables/Diamond.java)** - extends [CollectableAreaEntity.java](#CollectableAreaEntity)    
> Class representing "Pellet" - not used in project, required.
---
<a id="Key"></a>
> **(Class) [Key.java](java/ch/epfl/cs107/play/game/superpacman/actor/collectables/Key.java)** - extends [CollectableAreaEntity.java](#CollectableAreaEntity)    
> Can be collected by the player to unlock gates ([Gate.java](#Gate)).
>> Extensions:
>> - Used a new Sprite(.png)
---
<a id="Blinky"></a>
> **(Class) [Blinky.java](java/ch/epfl/cs107/play/game/superpacman/actor/ghosts/Blinky.java)** - extends [Ghost.java](#Ghost)  
> Class defining Blinky's movement algorithm. All override methods return null, making Blinky choose a random Orientation at any given moment.
---
<a id="Ghost"></a>
> **(Abstract Class) [Ghost.java](java/ch/epfl/cs107/play/game/superpacman/actor/ghosts/Ghost.java)** - extends MovableAreaEntity.java, implements Interactor.java  
> More abstract in order to define a ghost. Computes the paths, moves/orientates the ghost, sets him as frightened and eaten. Also, includes interaction with the player.
>> Extensions:
>> - Used new Sprites(.png)
>> - Added interaction with doors
>> - Added movement rules (see getValidOrientations())
>> - Added glow
>> - Added eyes animations when eaten
>> - Added back to home path
>> - Added frightened blinking animation (blue & white)
>> - Added a pause and combo text when Ghost is eaten
---
<a id="Inky"></a>
> **(Class) [Inky.java](java/ch/epfl/cs107/play/game/superpacman/actor/ghosts/Inky.java)** - extends [Ghost.java](#Ghost)  
> Class defining Inky's movement algorithm. He follows the player when in range and tries to escape him while frightened. By default, he targets a random in his range.
---
<a id="Pinky"></a>
> **(Class) [Pinky.java](java/ch/epfl/cs107/play/game/superpacman/actor/ghosts/Pinky.java)** - extends [Ghost.java](#Ghost)  
> Class defining Pinky's movement algorithm. Also follows the player when in range and tries to escape him while frightened. By default, he targets a random in his range.
---
<a id="SuperPacmanArea"></a>
> **(Abstract Class) [SuperPacmanArea.java](java/ch/epfl/cs107/play/game/superpacman/area/SuperPacmanArea.java)** - extends Area.java  
> Defines the area for the SuperPacman game.
>> Extensions:
>> - Added abstract method isEndingLevel() defining if a level can end the game
---
<a id="SuperPacmanAreaBehavior"></a>
> **(Class) [SuperPacmanAreaBehavior.java](java/ch/epfl/cs107/play/game/superpacman/area/SuperPacmanAreaBehavior.java)** - extends AreaBehavior.java  
> Class to create grids with all the cells from behavior images. Contains also private enum of all the cell types.
>> Extensions:
>> - Added new cell types to enum (WALL_RED, FREE_WITH_CLYDE)
>> - Adapted registerActors() to new cell types
>> - Added [SuperPacmanDifficulty.java](#SuperPacmanDifficulty) attribute
>> - Added [GhostsBehavior.java](#GhostsBehavior) attribute with difficulty integration
---
<a id="Level0"></a>
> **(Class) [Level0.java](java/ch/epfl/cs107/play/game/superpacman/area/levels/Level0.java)** - extends [SuperPacmanArea.java](#SuperPacmanArea)  
> Represents Level 0 of the game.
>> Extensions:
>> - Modified behavior .png to add red wall effect
>> - Added secret door to EPFL level
---
<a id="Level1"></a>
> **(Class) [Level1.java](java/ch/epfl/cs107/play/game/superpacman/area/levels/Level1.java)** - extends [SuperPacmanArea.java](#SuperPacmanArea)  
> Represents Level 1 of the game.
---
<a id="Level2"></a>
> **(Class) [Level2.java](java/ch/epfl/cs107/play/game/superpacman/area/levels/Level2.java)** - extends [SuperPacmanArea.java](#SuperPacmanArea)  
> Represents Level 2 of the game.
---
<a id="SuperPacmanInteractionVisitor"></a>
> **(Interface) [SuperPacmanInteractionVisitor.java](java/ch/epfl/cs107/play/game/superpacman/handler/SuperPacmanInteractionVisitor.java)**  
> Defines the interactions between actors.

<a id="extensions"></a>
### 2.2 Extensions

#### 📦 java/ch/epfl/cs107/play/game/superpacman
     These classes below are more general and apply to the whole game. This explains their 
     current location.

<a id="SoundUtility"></a>
> **(Class) [SoundUtility.java](java/ch/epfl/cs107/play/game/superpacman/SoundUtility.java)** - implements Acoustics.java  
> This class processes and receives multiple sound requests. It adds them to a List ("Queue"), and plays them once. The methods provide more control to other class who use this class.
---
<a id="SuperPacmanStatusGUI"></a>
> **(Class) [SuperPacmanStatusGUI.java](java/ch/epfl/cs107/play/game/superpacman/SuperPacmanStatusGUI.java)** - implements Graphics.java  
> Allows to display the FPS and Debug Mode text.
---

#### 📦 java/ch/epfl/cs107/play/game/superpacman/actor/collectables
     We decided to create this package that would group all actors which are collectables.
     
<a id="Cake"></a>
> **(Class) [Cake.java](java/ch/epfl/cs107/play/game/superpacman/actor/collectables/Cake.java)** - extends [CollectableAreaEntity.java](#CollectableAreaEntity)  
> Acts the same as a "cherry". Can be collected by the player for extra points.
---
<a id="Pellet"></a>
> **(Class) [Pellet.java](java/ch/epfl/cs107/play/game/superpacman/actor/collectables/Pellet.java)** - extends [CollectableAreaEntity.java](#CollectableAreaEntity)  
> Acts the same as a "diamond". Can be collected by the player for points and to open gates. Added pellet counter for each area, glow effect, as well as interaction with the player for magnet effect.
---
<a id="PowerPellet"></a>
> **(Class) [PowerPellet.java](java/ch/epfl/cs107/play/game/superpacman/actor/collectables/PowerPellet.java)** - extends [CollectableAreaEntity.java](#CollectableAreaEntity)  
> Acts the same as a "Bonus". Can be collected by the player to set ghosts frightened. Added glow effect.
---

#### 📦 java/ch/epfl/cs107/play/game/superpacman/actor/ghosts
     Similarly to collectables, this package groups all actors which are ghosts.

<a id="Clyde"></a>
> **(Class) [Clyde.java](java/ch/epfl/cs107/play/game/superpacman/actor/ghosts/Clyde.java)** - extends [Ghost.java](#Ghost)  
> Class defining Clyde's movement algorithm. He chases the player wherever he is in the level. While frightened, he finds the path the furthest away from the player.
---
<a id="GhostsBehavior"></a>
> **(Class) [GhostsBehavior.java](java/ch/epfl/cs107/play/game/superpacman/actor/ghosts/GhostsBehavior.java)** - implements Updatable.java  
> This class contains all the ghosts present in a level. It can update all their states at the same time. Also, it allows to control the difficulty of the ghosts.
---

#### 📦 java/ch/epfl/cs107/play/game/superpacman/area/camera
     Because the camera is specific to every area, it makes sense to add it to a /camera 
     package in /area.
     
<a id="Camera"></a>
> **(Abstract Class) [Camera.java](java/ch/epfl/cs107/play/game/superpacman/area/camera/Camera.java)** - implements Updatable.java  
> Conception of a camera for each Area. Gets updated by the current Area, and it's defined concretely in its sub-classes.
---
<a id="Fixed"></a>
> **(Class) [Fixed.java](java/ch/epfl/cs107/play/game/superpacman/area/camera/Fixed.java)** - extends [Camera.java](#Camera)  
> Defines a camera positioned in the center of the Area. It does not follow the player.
---
<a id="Follow"></a>
> **(Class) [Follow.java](java/ch/epfl/cs107/play/game/superpacman/area/camera/Follow.java)** - extends [Camera.java](#Camera)  
> Defines a camera always centered on the player.
---
<a id="SmoothFollow"></a>
> **(Class) [SmoothFollow.java](java/ch/epfl/cs107/play/game/superpacman/area/camera/SmoothFollow.java)** - extends [Camera.java](#Camera)  
> Defines a camera centered on the player but with a smooth catch-up speed.
---
<a id="SmoothLimited"></a>
> **(Class) [SmoothLimited.java](java/ch/epfl/cs107/play/game/superpacman/area/camera/SmoothLimited.java)** - extends [Camera.java](#Camera)  
> Defines a [SmoothFollow.java](#SmoothFollow) camera but with edge boundaries given in the constructor.
---

#### 📦 java/ch/epfl/cs107/play/game/superpacman/area/levels
     Levels define an area, therefore the added levels also define an area.

<a id="Level3"></a>
> **(Class) [Level3.java](java/ch/epfl/cs107/play/game/superpacman/area/levels/Level3.java)** - extends [SuperPacmanArea.java](#SuperPacmanArea)  
> Added a new level with a new behavior and tunnels at the sides of the map.
---
<a id="LevelEPFL"></a>
> **(Class) [LevelEPFL.java](java/ch/epfl/cs107/play/game/superpacman/area/levels/LevelEPFL.java)** - extends [SuperPacmanArea.java](#SuperPacmanArea)  
> Added an easter egg: an EPFL themed level. Available through level0 and after completing it, the player moves to Level1.
---

#### 📦 java/ch/epfl/cs107/play/game/superpacman/globalenums
     These enums are placed in this package to locate gobal parameter more easily.

<a id="SuperPacmanDepth"></a>
> **(Enum) [SuperPacmanDepth.java](java/ch/epfl/cs107/play/game/superpacman/globalenums/SuperPacmanDepth.java)**  
> Defines all the different depths for organising drawing order.
---
<a id="SuperPacmanDifficulty"></a>
> **(Enum) [SuperPacmanDifficulty.java](java/ch/epfl/cs107/play/game/superpacman/globalenums/SuperPacmanDifficulty.java)**  
> Defines the difficulties for the SuperPacman game with all the specific parameters.
---
<a id="SuperPacmanSound"></a>
> **(Enum) [SuperPacmanSound.java](java/ch/epfl/cs107/play/game/superpacman/globalenums/SuperPacmanSound.java)**  
> Represents the sound for the SuperPacman game.
---

#### 📦 java/ch/epfl/cs107/play/game/superpacman/graphics
     For graphics specific to the game, nothing existed, so we created this new package.

<a id="Arcade"></a>
> **(Class) [Arcade.java](java/ch/epfl/cs107/play/game/superpacman/graphics/Arcade.java)** - implements Graphics.java, Acoustics.java  
> Acts as Menu background and game screen overlay. Gets also access to window keyboard to move joystick depending on user input.
---
<a id="Glow"></a>
> **(Class) [Glow.java](java/ch/epfl/cs107/play/game/superpacman/graphics/Glow.java)** - implements Graphics.java  
> Allows to easily create a glow effect parented to a sprite.
---
<a id="ScreenFade"></a>
> **(Class) [ScreenFade.java](java/ch/epfl/cs107/play/game/superpacman/graphics/ScreenFade.java)** - implements Graphics.java  
> Allows to draw a black overlay covering the whole screen. It has been eased-in and out with [Transition.java](#Transition).
---

#### 📦 java/ch/epfl/cs107/play/game/superpacman/leaderboard
     Both these classes are linked to a leaderboard, have them together strengthens the 
     structure of the project.

<a id="GameScore"></a>
> **(Class) [GameScore.java](java/ch/epfl/cs107/play/game/superpacman/leaderboard/GameScore.java)** - implements Serializable.java  
> Represents all the statistic from a finished game.
---
<a id="LeaderboardGameScores"></a>
> **(Class) [LeaderboardGameScores.java](java/ch/epfl/cs107/play/game/superpacman/leaderboard/LeaderboardGameScores.java)** - implements Serializable.java  
> Contains a List with all of the [GameScore.java](#GameScore). Is used to display it's content on the leaderboard.
---

#### 📦 java/ch/epfl/cs107/play/game/superpacman/menus
     In order to better organise the menu, general classes and enums related to the menu 
     are placed in /menus and all the menu pages are located in /menus/pages.

<a id="Menu"></a>
> **(Abstract Class) [Menu.java](java/ch/epfl/cs107/play/game/superpacman/menus/Menu.java)** - implements Graphics.java, Acoustics.java, Serializable.java  
> Defines a menu page that can be drawn on the screen. Controls up and down user input.
---
<a id="MenuStateManager"></a>
> **(Final Class) [MenuStateManager.java](java/ch/epfl/cs107/play/game/superpacman/menus/MenuStateManager.java)** - implements Updatable.java, Graphics.java, Acoustics.java  
> Contains all the menu states and updates only the current state menu page. A Deque is used for all the states.
---
<a id="Option"></a>
> **(Enum) [Option.java](java/ch/epfl/cs107/play/game/superpacman/menus/Option.java)**  
> In here are contained all the options that a menu page can contain.
---
<a id="SubOption"></a>
> **(Enum) [SubOption.java](java/ch/epfl/cs107/play/game/superpacman/menus/SubOption.java)**  
> Contains all the sub-options that an [Option.java](#Option) can contain.
---

#### 📦 java/ch/epfl/cs107/play/game/superpacman/menus/pages
     Here are all the different menu pages
<a id="Credits"></a>
> **(Class) [Credits.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/Credits.java)** - extends [Menu.java](#Menu)  
> Defines and draws the credits page.
---
<a id="GameOver"></a>
> **(Class) [GameOver.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/GameOver.java)** - extends [Menu.java](#Menu)  
> Defines and draws the game over page.
---
<a id="Help"></a>
> **(Class) [Help.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/Help.java)** - extends [Menu.java](#Menu)  
> Defines and draws the help page.
---
<a id="HelpGhosts"></a>
> **(Class) [HelpGhosts.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/HelpGhosts.java)** - extends [Menu.java](#Menu)  
> Defines and draws the ghost more help page from [Help.java](#Help).
---
<a id="HelpScore"></a>
> **(Class) [HelpScore.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/HelpScore.java)** - extends [Menu.java](#Menu)  
> Defines and draws the score more help page from [Help.java](#Help).
---
<a id="Leaderboard"></a>
> **(Class) [Leaderboard.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/Leaderboard.java)** - extends [Menu.java](#Menu)  
> Defines and draws the leaderboard page.
---
<a id="MainMenu"></a>
> **(Class) [MainMenu.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/MainMenu.java)** - extends [Menu.java](#Menu)  
> Defines and draws the main menu page.
---
<a id="Options"></a>
> **(Class) [Options.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/Options.java)** - extends [Menu.java](#Menu)  
> Defines and draws the options page.
---
<a id="Pause"></a>
> **(Class) [Pause.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/Pause.java)** - extends [Menu.java](#Menu)  
> Defines and draws the pause page.
---
<a id="Play"></a>
> **(Class) [Play.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/Play.java)** - extends [Menu.java](#Menu)  
> Defines and draws the play page. In this case, the play page draws nothing and contains no options. It simply represents the state of the game.
---
<a id="Quit"></a>
> **(Class) [Quit.java](java/ch/epfl/cs107/play/game/superpacman/menus/pages/Quit.java)** - extends [Menu.java](#Menu)  
> Defines and draws the play page. In this case, the quit page also draws nothing and contains no options. It also represents the state of the game.
---

#### 📦 java/ch/epfl/cs107/play/io
     The location of this class makes sense here because of its utility and function.
<a id="Serialization"></a>
> **(Class) [Serialization.java](java/ch/epfl/cs107/play/io/Serialization.java)** - extends [Transition.java](#Transition)  
> Containing static methods, this utility class allows to serialize and deserialize objects.
---

#### 📦 java/ch/epfl/cs107/play/math/transitions
     The location of this class makes sense here because of its utility and function.
     Transitons are mathematical equations.
<a id="EaseInOutCubic"></a>
> **(Class) [EaseInOutCubic.java](java/ch/epfl/cs107/play/math/transitions/EaseInOutCubic.java)** - extends [Transition.java](#Transition)  
> Allows the transition to behave through ease in and out using cubic formula.
---
<a id="EaseOutCirc"></a>
> **(Class) [EaseOutCirc.java](java/ch/epfl/cs107/play/math/transitions/EaseOutCirc.java)** - extends [Transition.java](#Transition)  
> Allows the transition to behave through ease in and out using circ. formula.
---
<a id="Linear"></a>
> **(Class) [Linear.java](java/ch/epfl/cs107/play/math/transitions/Linear.java)** - extends [Transition.java](#Transition)  
> Allows the transition to behave in a linear fashion.
---
<a id="Transition"></a>
> **(Abstract Class) [Transition.java](java/ch/epfl/cs107/play/math/transitions/Transition.java)**  
> Defines the behavior of a transition with its methods.

<a id="deviations"></a>
## 3. DEVIATIONS FROM THE PROJECT DESCRIPTION

> - Added path to EPFL map from level0. Added level3 at the end such that the game doesn't end at level2.
> - Modified audio settings. 
> - Modified camera update method.
> - Changed the design of how the score and lives are displayed.
> - Replaced Cherry, Diamond and Bonus with Cake, Pellet and PowerPellet.
> - Modified the sprites, the design of the walls and added a glow effect.

<a id="list-extensions"></a>
## 4. LIST OF ALL EXTENSIONS
> - Glow
> - Wall design
> - Arcade
> - Arcade responsive joystick
> - Easter Egg
> - Bonus map
> - New Ghost (Clyde)
> - Magnet effect for PowerPellet
> - Tunnels
> - Improved graphics 
> - Intro music
> - Sound effects
> - Pellet counter
> - Time tracker
> - FPS display
> - Menu
> - Difficulty options
> - Performance options
> - Camera options
> - Camera shake
> - Leaderboard
> - Help menu with multiple pages
> - Quit game
> - Pause game
> - End game