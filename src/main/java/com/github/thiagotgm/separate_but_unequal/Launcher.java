package com.github.thiagotgm.separate_but_unequal;

import javax.swing.JFrame;

import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;

/**
 * Class that launches the game on startup.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-23
 */
public class Launcher {
    
    /** Code used on program exit to indicate an error was encountered while loading resources. */
    public static final int LOADING_ERROR_CODE = 1;

    public static void main( String[] args ) {

        ResourceManager.getInstance();
        
        JFrame program = new JFrame( "Separate but UnEqual" );
        program.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        new MenuManager( program );
        program.setVisible( true );

    }

}
