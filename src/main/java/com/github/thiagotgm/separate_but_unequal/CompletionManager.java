package com.github.thiagotgm.separate_but_unequal;

import java.util.Map;

import com.github.thiagotgm.separate_but_unequal.resource.EndScene;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;
import com.github.thiagotgm.separate_but_unequal.resource.Story;

/**
 * Class that keeps track of the player's progress throughout the game.<br>
 * Follows the Singleton pattern.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-29
 */
public class CompletionManager {
    
    private Map<Character, Long> endings;

    private static CompletionManager instance;
    
    /**
     * Creates a new CompletionManager, loading in the completion trackers that were previously saved.
     */
    private CompletionManager() {
        
        endings = ResourceManager.getInstance().getEndingTrackers();
        
    }
    
    /**
     * Retrieves the currently running instance of this class, or creates one if it doesn't exist.
     * 
     * @return An instance of this class.
     */
    public static CompletionManager getInstance() {
        
        if ( instance == null ) {
            instance = new CompletionManager();
        }
        return instance;
        
    }
    
    /**
     * Determines if a certain ending in a certain story was already reached by the player.<br>
     * The story code must be in the range {@value Story#MIN_CODE} to {@value Story#MAX_CODE} (inclusive), and the end
     * code must be in the range {@value EndScene#MIN_CODE} to {@value EndScene#MAX_CODE} (inclusive).
     * 
     * @param storyCode The code of the story to be checked.
     * @param endCode The code of the ending in that story to be checked.
     * @return true if the player reached that ending before, false otherwise.
     * @throws IllegalArgumentException If the story or end code are out of the acceptable range.
     */
    public boolean isReached( char storyCode, int endCode ) throws IllegalArgumentException {
        
        if ( ( storyCode < Story.MIN_CODE ) || ( storyCode > Story.MAX_CODE ) ) {
            throw new IllegalArgumentException( Story.CODE_OOB );
        }
        if ( ( endCode < EndScene.MIN_CODE ) || ( endCode > EndScene.MAX_CODE ) ) {
            throw new IllegalArgumentException( EndScene.CODE_OOB_ERROR );
        }
        
        long tracker = endings.get( storyCode );
        long mask = 0x1 << ( endCode - EndScene.MIN_CODE );
        return ( tracker & mask ) != 0;
        
    }
    
    /**
     * Sets that a given ending in a given story was reached by the player.<br>
     * The story code must be in the range {@value Story#MIN_CODE} to {@value Story#MAX_CODE} (inclusive), and the end
     * code must be in the range {@value EndScene#MIN_CODE} to {@value EndScene#MAX_CODE} (inclusive).
     * 
     * @param storyCode The code of the story.
     * @param endCode The code of the ending in that story to be set.
     * @throws IllegalArgumentException If the story or end code are out of the acceptable range.
     */
    public void setReached( char storyCode, int endCode ) throws IllegalArgumentException {
        
        if ( ( storyCode < Story.MIN_CODE ) || ( storyCode > Story.MAX_CODE ) ) {
            throw new IllegalArgumentException( Story.CODE_OOB );
        }
        if ( ( endCode < EndScene.MIN_CODE ) || ( endCode > EndScene.MAX_CODE ) ) {
            throw new IllegalArgumentException( EndScene.CODE_OOB_ERROR );
        }
        
        long tracker = endings.get( storyCode );
        long mask = 0x1 << ( endCode - EndScene.MIN_CODE );
        tracker |= mask;
        endings.put( storyCode, tracker );
        ResourceManager.getInstance().saveEndingTracker( storyCode, tracker );
        
    }

}
