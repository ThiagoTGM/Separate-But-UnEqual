package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;

/**
 * Panel that contains the UI for the main menu, including labels and buttons.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-25
 */
public class MainMenuPanel extends ButtonPanel {
    
    /** Serial ID that represents this class. */
    private static final long serialVersionUID = 2874269025005392973L;
    
    public static final String TITLE = "Separate But UnEqual";
    private static final float TITLE_FONT_SIZE = 50f;
    
    private static final String BOTTOM_TEXT = "Author: Thiago Marback             Version: ";
    
    /** Action command that identifies that the "Start" button was pressed. */
    public static final String START_COMMAND = "START";
    /** Action command that identifies that the "Load" button on the main menu was pressed. */
    public static final String LOAD_COMMAND = "MENU_" + GamePanel.LOAD_COMMAND;
    /** Action command that identifies that the "Settings" button was pressed. */
    public static final String SETTINGS_COMMAND = "SETTINGS";
    /** Action command that identifies that the "Help" button was pressed. */
    public static final String HELP_COMMAND = "HELP";
    /** Action command that identifies that the "About" button was pressed. */
    public static final String ABOUT_COMMAND = "About";
    /** Action command that identifies that the "Exit" button was pressed. */
    public static final String EXIT_COMMAND = "EXIT";
    
    private static final double MAX_BUTTON_HEIGHT = 1;
    private static final double MAX_BUTTON_WIDTH = 5;
    private static final double BUTTON_PADDING = 0.2;
    private static final double BUTTON_PANEL_PADDING = 1;
    private static final double WINDOW_PADDING = 0.3;
    
    private final JButton loadButton;
    
    /**
     * Initializes a double-buffered MainMenu.
     * 
     * @see #MainMenuPanel(boolean)
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
        
        super( isDoubleBuffered );
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        
        /* Create top labels */
        JLabel title = new JLabel( TITLE ); // Creates title bar.
        title.setFont( title.getFont().deriveFont( TITLE_FONT_SIZE ) );
        Scalable.scaleFont( title );
        title.setHorizontalAlignment( SwingConstants.CENTER );
        title.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( title );
        
        /* Create buttons */
        int maxHeight = Scalable.scaleToInt( MAX_BUTTON_HEIGHT ); // Calculates max dimensions for the buttons.
        int maxWidth = Scalable.scaleToInt( MAX_BUTTON_WIDTH );
        Dimension maxSize = new Dimension( maxWidth, maxHeight );
        
        ActionListener listener = new ListenerAggregator( listeners ); // Starts aggregator.
        
        JButton startButton = new JButton( "Start" ); // Creates button to start game.
        Scalable.scaleFont( startButton );
        startButton.setActionCommand( START_COMMAND );
        startButton.addActionListener( listener );
        startButton.setMaximumSize( maxSize );
        startButton.setHorizontalAlignment( SwingConstants.CENTER );
        
        loadButton = new JButton( "Load" ); // Creates button to load save.
        Scalable.scaleFont( loadButton );
        loadButton.setActionCommand( LOAD_COMMAND );
        loadButton.addActionListener( listener );
        loadButton.setMaximumSize( maxSize );
        loadButton.setHorizontalAlignment( SwingConstants.CENTER );
        loadButton.setEnabled( ResourceManager.getInstance().hasSave() );
        
        JButton settingsButton = new JButton( "Settings" ); // Creates button to open settings menu.
        Scalable.scaleFont( settingsButton );
        settingsButton.setActionCommand( SETTINGS_COMMAND );
        settingsButton.addActionListener( listener );
        settingsButton.setMaximumSize( maxSize );
        settingsButton.setHorizontalAlignment( SwingConstants.CENTER );
        
        JButton helpButton = new JButton( "Help" ); // Creates button to open help page.
        Scalable.scaleFont( helpButton );
        helpButton.setActionCommand( HELP_COMMAND );
        helpButton.addActionListener( listener );
        helpButton.setMaximumSize( maxSize );
        helpButton.setHorizontalAlignment( SwingConstants.CENTER );
        
        JButton aboutButton = new JButton( "About" ); // Creates button to open about page.
        Scalable.scaleFont( aboutButton );
        aboutButton.setActionCommand( ABOUT_COMMAND );
        aboutButton.addActionListener( listener );
        aboutButton.setMaximumSize( maxSize );
        aboutButton.setHorizontalAlignment( SwingConstants.CENTER );
        
        JButton exitButton = new JButton( "Exit" ); // Creates button to exit to desktop.
        Scalable.scaleFont( exitButton );
        exitButton.setActionCommand( EXIT_COMMAND );
        exitButton.addActionListener( listener );
        exitButton.setMaximumSize( maxSize );
        exitButton.setHorizontalAlignment( SwingConstants.CENTER );
        
        /* Add buttons to panel */
        Dimension padding = new Dimension( 0, Scalable.scaleToInt( BUTTON_PADDING ) );
        Dimension panelPadding = new Dimension( 0, Scalable.scaleToInt( BUTTON_PANEL_PADDING ) );
        
        add( Box.createVerticalGlue() ); // Border between top labels and buttons.
        add( Box.createRigidArea( panelPadding ) );
        
        startButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( startButton ); // Insert start button to panel.
        
        add( Box.createRigidArea( padding ) );
        
        loadButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( loadButton ); // Insert load button to panel.
        
        add( Box.createRigidArea( padding ) );
        
        settingsButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( settingsButton ); // Insert settings button to panel.
        
        add( Box.createRigidArea( padding ) );
        
        helpButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( helpButton ); // Insert help button to panel.
        
        add( Box.createRigidArea( padding ) );
        
        aboutButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( aboutButton ); // Insert about button to panel.
        
        add( Box.createRigidArea( padding ) );
        
        exitButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( exitButton ); // Insert exit button to panel.
        
        add( Box.createRigidArea( panelPadding ) );
        add( Box.createVerticalGlue() ); // Border between buttons and bottom labels.
        
        /* Create bottom labels */
        JLabel bottom = new JLabel( BOTTOM_TEXT + getClass().getPackage().getImplementationVersion() );
        Scalable.scaleFont( bottom ); // Creates bottom bar.
        bottom.setHorizontalAlignment( SwingConstants.CENTER );
        bottom.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( bottom );
        
        /* Create panel border */
        int paddingSize = Scalable.scaleToInt( WINDOW_PADDING );
        Border windowPadding = BorderFactory.createEmptyBorder( paddingSize, paddingSize, paddingSize, paddingSize );
        setBorder( windowPadding );

    }
    
    /**
     * Sets whether the load button is enabled.
     * 
     * @param enabled If true, the button will be enabled. If false, the button will be disabled.
     */
    public void setLoadButtonEnabled( boolean enabled ) {
        
        loadButton.setEnabled( enabled );
        
    }

}
