package com.github.thiagotgm.separate_but_unequal;

import javax.swing.JTextArea;

import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;

/**
 * Manages the displaying of a scene text into a JTextArea.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-25
 */
public class SceneDisplayer implements Runnable {
    
    private static final long DEFAULT_DELAY = 50;
    private static final long SENTENCE_DELAY_MULTIPLIER = 10;
    private static final long LINE_BREAK_DELAY_MULTIPLIER = 20;
    
    /** Thread name to be used for objects of this type. */
    public static final String THREAD_NAME = "Scene Text Updater";
    private static final String ERROR = "Could not load Scene text";
    
    private final JTextArea sceneDisplay;
    
    private String sceneText;
    private long delay;

    /**
     * Intializes a new Displayer that displays a scene onto a given text area.
     *
     * @param sceneDisplay TextArea to display the scene text on.
     */
    public SceneDisplayer( JTextArea sceneDisplay ) {

        this.sceneDisplay = sceneDisplay;
        
    }
    
    /**
     * Sets the text of the scene to be displayed next.
     *
     * @param text Text of the scene.
     */
    public void showScene( String text ) {
    
        this.sceneText = text;
        sceneDisplay.setText( null );
        delay = DEFAULT_DELAY / ResourceManager.getTextSpeedMultiplier();
        
    }
    
    /**
     * Skips the slow text insertion phase, displaying the full scene text at once.<br>
     * If the scene text is null, shows an error message instead.
     */
    public void skip() {
        
        sceneDisplay.setText( ( sceneText == null ) ? ERROR : sceneText );
        
    }
    
    /**
     * Clears the text display area.
     */
    public void clear() {
        
        sceneDisplay.setText( null );
        
    }

    /**
     * Displays the scene text, one character at a time.<br>
     * There is a delay between each character, and larger delays after a sentence and after a newline.<br>
     * If the scene text is null, shows an error message instead.
     */
    @Override
    public void run() {

        if ( sceneText == null ) { // No scene text.
            skip();
            return;
        }
        try { 
            Thread.sleep( delay ); // Delay before the first character.
        } catch ( InterruptedException e ) {
            return; // Stop displaying.
        }
        for ( int i = 0; i < sceneText.length(); i++ ) { // Prints each character of the text.
            
            char curChar = sceneText.charAt( i );
            sceneDisplay.append( String.valueOf( curChar ) );
            try {
                switch ( curChar ) { // Decide delay before next char.
                    
                    case '.': // End of sentence.
                    case '!':
                    case '?':
                    case ':':
                        Thread.sleep( delay * SENTENCE_DELAY_MULTIPLIER );
                        break;
                    case '\n': // End of line.
                        Thread.sleep( delay * LINE_BREAK_DELAY_MULTIPLIER );
                        break;
                    default: // Normal character.
                        Thread.sleep( delay );
                        
                }
            } catch ( InterruptedException e ) {
                return; // Stop displaying.
            }
            
        }
        
    }
    
}
