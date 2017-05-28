package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class MainMenu extends JPanel {
    
    

    /**
     * Initializes a double-buffered MainMenu.
     * 
     * @see #MainMenu(boolean)
     */
    public MainMenu() {

        this( true );
        
    }

    public MainMenu( boolean isDoubleBuffered ) {
        
        super( new BorderLayout(), isDoubleBuffered );
        JLabel title = new JLabel( "" );

    }

}
