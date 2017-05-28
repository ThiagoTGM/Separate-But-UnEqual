package com.github.thiagotgm.separate_but_unequal;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.github.thiagotgm.separate_but_unequal.gui.GamePanel;
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
    
    /** Code used on program exit to indicate an error was encountered while loading resources. */
    public static final int LOADING_ERROR_CODE = 1;
    
    private static final double WIDTH_MULTIPLIER = 0.5;
    private static final double HEIGHT_MULTIPLIER = 0.8;

    public static void main( String[] args ) {

        ResourceManager.load();
        
        JFrame program = new JFrame( "Separate but UnEqual" );
        program.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        Toolkit defKit = Toolkit.getDefaultToolkit();
        Dimension screenSize = defKit.getScreenSize();
        program.setSize( (int) ( screenSize.getWidth() * WIDTH_MULTIPLIER ),
                         (int) ( screenSize.getHeight() * HEIGHT_MULTIPLIER ) );
        
        MenuManager manager = new MenuManager( program );
        program.setVisible( true );

    }

}
