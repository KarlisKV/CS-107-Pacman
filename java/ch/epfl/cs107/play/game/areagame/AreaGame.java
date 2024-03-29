package ch.epfl.cs107.play.game.areagame;

import ch.epfl.cs107.play.game.Game;
import ch.epfl.cs107.play.game.superpacman.menus.MenuStateManager;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

import java.util.HashMap;
import java.util.Map;


/**
 * AreaGame: concept of Game displayed in a (MxN) Grid called Area
 */
abstract public class AreaGame implements Game {

    // Context objects
    private Window window;
    private FileSystem fileSystem;
    /// A map containing all the Area of the Game
    private Map<String, Area> areas;
    /// The current area the game is in
    private Area currentArea;

	// [modification] - Menu for the game
	private MenuStateManager menuStateManager;
	private static final float EXIT_TIMER = 1;
    private float timer = EXIT_TIMER;

    /**
     * Add an Area to the AreaGame list
     * @param a (Area): The area to add, not null
     */
    protected final void addArea(Area a) {
        areas.put(a.getTitle(), a);
    }

    /**
     * Setter for the current area: Select an Area in the list from its key
     * - the area is then begin or resume depending if the area is already started or not and if it is forced
     * @param key        (String): Key of the Area to select, not null
     * @param forceBegin (boolean): force the key area to call begin even if it was already started
     * @return (Area): after setting it, return the new current area
     */
    protected final Area setCurrentArea(String key, boolean forceBegin) {
        Area newArea = areas.get(key);

		if(newArea == null) {
			System.out.println("New Area not found, keep previous one");
		}else {
			// Stop previous area if it exist
			if(currentArea != null){
				currentArea.suspend();
				currentArea.purgeRegistration(); // Is useful?
			}

			currentArea = newArea;

			// Start/Resume the new one
			if (forceBegin || !currentArea.isStarted()) {
				currentArea.begin(window, fileSystem);
			} else {
				currentArea.resume(window, fileSystem);
			}
		}

		return currentArea;
	}

	/**@return (Window) : the Graphic and Audio context*/
	protected final Window getWindow(){
		return window;
	}

	/**@return (FIleSystem): the linked file system*/
	protected final FileSystem getFileSystem(){
		return fileSystem;
	}

	/**
	 * Getter for the current area
	 * @return (Area)
	 */
	protected final Area getCurrentArea(){
		return this.currentArea;
	}

	/// AreaGame implements Playable

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		// Keep context
		this.window = window;
		this.fileSystem = fileSystem;
		menuStateManager = new MenuStateManager(window);

		areas = new HashMap<>();
		return true;
	}


	@Override
	public void update(float deltaTime) {
		// [modification] - Added state contions
		if (MenuStateManager.isQuit()) {
			// Dispose the window
			timer -= deltaTime;
			if (timer <= 0) {
				end();
			}
		} else {
			currentArea.update(deltaTime);
		}
		menuStateManager.update(deltaTime);
		menuStateManager.draw(window);
		if (!MenuStateManager.isSoundDeactivated()) {
			menuStateManager.bip(window);
		}
	}

	@Override
	public void end() {
		System.exit(0);
	}
}
