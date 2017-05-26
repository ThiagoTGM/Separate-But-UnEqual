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
    
    public static final String THREAD_NAME = "Scene Text Updater";
    private static final String ERROR = "Could not load Scene text";
    
    private final JTextArea sceneDisplay;
    
    private String sceneText;
    private long delay;

    public SceneDisplayer( JTextArea sceneDisplay ) {

        this.sceneDisplay = sceneDisplay;
        
    }
    
    public void showScene( String text ) {
    
        this.sceneText = text;
        sceneDisplay.setText( null );
        delay = DEFAULT_DELAY / ResourceManager.getTextSpeedMultiplier();
        
    }
    
    public void skip() {
        
        sceneDisplay.setText( ( sceneText == null ) ? ERROR : sceneText );
        
    }
    
    public void clear() {
        
        sceneDisplay.setText( null );
        
    }

    @Override
    public void run() {

        if ( sceneText == null ) {
            skip();
            return;
        }
        try {
            Thread.sleep( delay );
        } catch ( InterruptedException e ) {
            return;
        }
        for ( int i = 0; i < sceneText.length(); i++ ) {
            
            char curChar = sceneText.charAt( i );
            sceneDisplay.append( String.valueOf( curChar ) );
            try {
                switch ( curChar ) {
                    
                    case '.':
                    case '!':
                    case '?':
                    case ':':
                        Thread.sleep( delay * SENTENCE_DELAY_MULTIPLIER );
                        break;
                    case '\n':
                        Thread.sleep( delay * LINE_BREAK_DELAY_MULTIPLIER );
                        break;
                    default:
                        Thread.sleep( delay );
                        
                }
            } catch ( InterruptedException e ) {
                return;
            }
            
        }
        
    }
    
}
