package com.github.thiagotgm.separate_but_unequal;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.thiagotgm.separate_but_unequal.gui.MainMenuPanel;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;

/**
 * Class that launches the game on startup.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-23
 */
public class Launcher {
    
    private static final Logger log = LoggerFactory.getLogger( Launcher.class );
    
    /** Code used on program exit to indicate an error was encountered while loading resources. */
    public static final int LOADING_ERROR_CODE = 1;
    /** Code used on program exit to indicate successful exit. */
    public static final int EXIT_SUCCESS = 0;

    public static void main( String[] args ) {

        ResourceManager.getInstance();
        
        final JFrame program = new JFrame( MainMenuPanel.TITLE );
        program.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        program.addWindowListener( new WindowAdapter() {
            
            @Override
            public void windowClosing( WindowEvent e ) {
                
                log.info( "Closing program window." );
                program.setVisible( false );
                ResourceManager.getInstance().save(); // Save state.
                program.dispose();
                
            }
            
            @Override
            public void windowClosed( WindowEvent e ) {
                
                log.info( "Window closed. Exiting." );
                System.exit( EXIT_SUCCESS ); // Exit program.
                
            }
            
        });
        
        new MenuManager( program ); // Initialize menu.
        program.setVisible( true ); // Open window.

    }

}
