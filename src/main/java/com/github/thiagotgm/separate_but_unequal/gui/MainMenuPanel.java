package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class MainMenuPanel extends JPanel {
    
    private static final String TITLE = "Separate But UnEqual";
    private static final float TITLE_FONT_SIZE = 50f;
    
    private static final String BOTTOM_TEXT = "Author: Thiago Marback             Version: ";
    
    /** Action command that identifies that the "Start" button was pressed. */
    public static final String START_COMMAND = "START";
    /** Action command that identifies that the "Load" button was pressed. */
    public static final String LOAD_COMMAND = "LOAD";
    /** Action command that identifies that the "Exit" button was pressed. */
    public static final String EXIT_COMMAND = "EXIT";
    
    private static final double MAX_BUTTON_HEIGHT = 1;
    private static final double MAX_BUTTON_WIDTH = 5;
    private static final double BUTTON_PADDING = 0.2;
    
    private final List<ActionListener> listeners;
    
    /**
     * Initializes a double-buffered MainMenu.
     * 
     * @see #MainMenu(boolean)
     */
    public MainMenuPanel() {

        this( true );
        
    }

    /**
     * Initializes a MainMenuPanel with a specified buffering strategy.
     * 
     * @param isDoubleBuffered A boolean, true for double-buffering, which uses additional memory space to achieve fast,
     *                         flicker-free updates
     * @see JPanel#JPanel(boolean)
     */
    public MainMenuPanel( boolean isDoubleBuffered ) {
        
        super( new BorderLayout(), isDoubleBuffered );
        
        JLabel title = new JLabel( TITLE );
        title.setFont( title.getFont().deriveFont( TITLE_FONT_SIZE ) );
        Scalable.scaleFont( title );
        title.setHorizontalAlignment( SwingConstants.CENTER );
        add( title, BorderLayout.NORTH );
        
        JLabel bottom = new JLabel( BOTTOM_TEXT + getClass().getPackage().getImplementationVersion() );
        Scalable.scaleFont( bottom );
        bottom.setHorizontalAlignment( SwingConstants.CENTER );
        add( bottom, BorderLayout.SOUTH );
        
        int maxHeight = Scalable.scaleToInt( MAX_BUTTON_HEIGHT );
        int maxWidth = Scalable.scaleToInt( MAX_BUTTON_WIDTH );
        Dimension maxSize = new Dimension( maxWidth, maxHeight );
        
        listeners = new LinkedList<>();
        ActionListener listener = new ListenerAggregator( listeners );
        
        GridBagConstraints c = new GridBagConstraints();
        
        JPanel buttons = new JPanel( new GridBagLayout() );
        
        JButton start = new JButton( "Start" );
        Scalable.scaleFont( start );
        start.setActionCommand( START_COMMAND );
        start.addActionListener( listener );
        start.setMaximumSize( maxSize );
        
        
        JButton load = new JButton( "Load" );
        Scalable.scaleFont( load );
        load.setActionCommand( LOAD_COMMAND );
        load.addActionListener( listener );
        load.setMaximumSize( maxSize );
        
        JButton exit = new JButton( "Exit" );
        Scalable.scaleFont( exit );
        exit.setActionCommand( EXIT_COMMAND );
        exit.addActionListener( listener );
        exit.setMaximumSize( maxSize );
        
        int padding = Scalable.scaleToInt( BUTTON_PADDING );
        c.insets = new Insets( padding, 0, padding, 0 );
        c.gridx = 0;
        c.gridy = 0;
        buttons.add( start, c );
        
        c.insets = new Insets( 0, 0, padding, 0 );
        c.gridy = 1;
        buttons.add( load, c );
        
        c.gridx = 0;
        c.gridy = 2;
        buttons.add( exit, c );
        
        add( buttons, BorderLayout.CENTER );

    }
    
    /**
     * Adds a listener to be notified when one of the buttons in the panel is pressed.
     * 
     * @param l Listener to be registered.
     * @see ActionListener
     */
    public void addActionListener( ActionListener l ) {
        
        listeners.add( l );
        
    }
    
    /**
     * Removes a listener so that it is no longer notified when one of the buttons in the panel is pressed.
     * 
     * @param l Listener to be unregistered.
     * @see ActionListener
     */
    public void removeActionListener( ActionListener l ) {
        
        listeners.remove( l );
        
    }

}
