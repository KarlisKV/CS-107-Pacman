## CONTENTS OF THIS FILE
1. [Introduction](#introduction)
2. [Configuration](#configuration)
3. [Controls](#controls)  
    3.1 [Menu Navigation](#menu-navigation)  
    3.2 [Game Navigation](#game-navigation)  
4. [Menus](#menus)  
    4.1 [Main Menu](#main-menu)  
    4.2 [Options](#options)  
    4.3 [Paused](#paused)  
5. [Gameplay Features](#gameplay-features)  
    5.1 [Difficulties](#difficulties)  
6. [How to Play](#how-to-play)
7. [Additional Notes](#additional-notes)
8. [Final Words](#final-words)
9. [References](#references)  
  9.1 [Art](#art)  
  9.2 [Sound](#sound)  
  9.3 [Animations](#animations)  
  9.4 [Inspiration](#inspiration)  
  9.4 [Links](#links)  
    



<a name="introduction"></a>
## INTRODUCTION




<a name="configuration"></a>
## CONFIGURATION

1. Open terminal
  
2. Compile the java files
    ````shell script
    javac -cp . java/.../*.java   // to check
    ````

3. Run the game
    ````shell script
    java -cp java: ch.epfl.cs107.Play   // to check
    ````
   
   
   
<a name="controls"></a>
## CONTROLS
<a name="menu-navigation"></a>
#### Menu navigation
- Press UP and DOWN arrow keys to move up and down the selection of options
- Press ENTER to execute the current selection or to choose sub options
- Press ESC to pause the game
- Pressing SHIFT, CTRL, OPTION together activates Debug mode (can be activated only in menus), to turn it off, the game must be quit and reopened

<a name="game-navigation"></a>
#### Game navigation
- Use UP, DOWN, LEFT and RIGHT arrow keys to move pacman


<a name="menus"></a>
## MENUS
<a name="main-menu"></a>
#### Main Menu
| Option      	| Description                                                  	|
|-------------	|--------------------------------------------------------------	|
| Play        	| Starts the game                                              	|
| Options     	| Shows the configurable options                               	|
| Help        	| Show help page                                               	|
| Quit        	| Terminates the application                                   	|
| Leaderboard 	| Shows leaderboard page with the 10 best games ranked by score |
| Credits     	| Show the credits page                                        	|

<a name="options"></a>
#### Options
| Option            	| Description                                                     	|
|-------------------	|-----------------------------------------------------------------	|
| Sound             	| Toggles the sound on and off                                     	|
| Camera Shake      	| Toggles camera shake on and off                                 	|
| Show FPS          	| Toggles the fps counter on and off                              	|
| Difficulty        	| Choose a game difficulty from Easy, Normal, Hard and Impossible 	|
| Clear Leaderboard 	| Clears the leaderboard and tmp files linked to it               	|
| Back              	| Returns to main menu                                            	|

<a name="paused"></a>
#### Paused
| Option            	| Description                                                     	|
|-------------------	|-----------------------------------------------------------------	|
| Resume            	| Resumes the game                                                	|
| End Game          	| Ends the game without saving it                                 	|


<a name="gameplay-features"></a>
## GAMEPLAY FEATURES
<a name="difficulties"></a>
#### Difficulties
- EASY
    - difficulty does not increase over time
    - ghosts move slowly
    - ghosts become frightened for 15 seconds
    - ghosts are not the smartest
- NORMAL
    - difficulty increases over time*
    - ghosts move at a normal speed (16 to 12)
    - ghosts become frightened for 10 seconds (stays at 10)
    - ghosts are a bit smarter
- HARD
    - difficulty increases over time
    - ghosts move at a fast speed, up to the same as the player (14 to 10)
    - ghosts become frightened for 8 seconds (8 to 6)
    - ghosts are smart
- IMPOSSIBLE
    - difficulty increases over time
    - ghosts move at a super fast speed, up to a faster speed than the player (12 to 8)
    - ghosts become frightened for 6 seconds (6 to 4)
    - ghosts are even smarter

*(it increases the longer you stay in the same level and resets each time you go to another level)


<a name="how-to-play"></a>
## HOW TO PLAY



<a name="additional-notes"></a>
## ADDITIONAL NOTES




<a name="final-words"></a>
## FINAL WORDS




<a name="references"></a>
## REFERENCES
<a name="art"></a>
#### Art
- jdaster64 - SuperPacman sprites: [https://www.spriters-resource.com/game_boy_gbc/mspacman/sheet/25056/](https://www.spriters-resource.com/game_boy_gbc/mspacman/sheet/25056/)
- Arcade pixel art: [https://www.wallpaperflare.com/ahoy-arcade-arcade-machine-space-invaders-pac-man-artwork-wallpaper-udovr](https://www.wallpaperflare.com/ahoy-arcade-arcade-machine-space-invaders-pac-man-artwork-wallpaper-udovr)
- "Emulogic" font: [https://www.classicgaming.cc/classics/pac-man/fonts](https://www.classicgaming.cc/classics/pac-man/fonts)
- Arrow keys pixel art: [http://pixelartmaker.com/art/7efa119103ffc71](http://pixelartmaker.com/art/7efa119103ffc71)

<a name="sound"></a>
#### Sound
- Inthegrave & NamelessSpriter - Pacman sounds: [https://www.sounds-resource.com/arcade/pacman/sound/10603/](https://www.sounds-resource.com/arcade/pacman/sound/10603/)
- Menu sounds: [https://kenney.nl/assets/interface-sounds](https://kenney.nl/assets/interface-sounds)

<a name="animations"></a>
#### Animations
- Mathematical easing functions: [https://easings.net](https://easings.net)

<a name="inspiration"></a>
#### Inspiration
- Hyago Oliveira - Pacman in Unity with post processing: [https://connect.unity.com/p/pac-man-g](https://connect.unity.com/p/pac-man-g)
- Retro Game Mechanics Explained - Pac-Man Ghost AI Explained: [https://www.youtube.com/watch?v=ataGotQ7ir8](https://www.youtube.com/watch?v=ataGotQ7ir8)
- Retro Game Mechanics Explained - Pac-Man Kill Screen Explained: [https://www.youtube.com/watch?v=NKKfW8X9uYk](https://www.youtube.com/watch?v=NKKfW8X9uYk)

<a name="links"></a>
#### LINKS
- Google Docs Project Setup/Ideas [[open]](https://docs.google.com/document/d/1qb4lyvMLUWU2ZAJjJG8gABQYsB7a3YBG-m40DlPxCrY/edit?usp=sharing)






