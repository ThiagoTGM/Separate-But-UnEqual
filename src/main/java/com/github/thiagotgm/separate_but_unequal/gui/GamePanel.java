package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;

/**
 * Panel that contains the UI for the game, including text boxes and buttons.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-25
 */
public class GamePanel extends JPanel {
    
    /** Serial ID that represents this class. */
    private static final long serialVersionUID = -3770146955575152229L;
    
    private static final double WIDTH = 10;
    private static final double HEIGHT = 10;
    
    private static final double SIDE_BORDER_PADDING = 0.2;
    private static final double TOP_BORDER_PADDING = 0.3;
    private static final double BOTTOM_BORDER_PADDING = 0.3;
    
    private static final double TEXT_PADDING = 0.2;
    private static final double TEXT_MARGIN = 0.1;
    
    private static final double FLOW_BUTTON_PADDING = 3;
    private static final double BUTTON_PADDING = 0.2;
    
    /** Action command that identifies that the "Skip" button was pressed. */
    public static final String SKIP_COMMAND = "SKIP";
    /** Action command that identifies that the "Menu" button was pressed. */
    public static final String MENU_COMMAND = "MENU";
    /** Action command that identifies that the "Save" button was pressed. */
    public static final String SAVE_COMMAND = "SAVE";
    /** Action command that identifies that the "Load" button was pressed. */
    public static final String LOAD_COMMAND = "LOAD";
    /** Action command that identifies that the "Up" button was pressed. */
    public static final String UP_COMMAND = "UP";
    /** Action command that identifies that the "Select" button was pressed. */
    public static final String SELECT_COMMAND = "SELECT";
    /** Action command that identifies that the "Down" button was pressed. */
    public static final String DOWN_COMMAND = "DOWN";
    
    private final JButton skipButton;
    private final JButton loadButton;
    private final JButton upButton;
    private final JButton selectButton;
    private final JButton downButton;
    
    private final JTextArea scene;
    private final JTextArea options;
    private final JPanel graphic;
    
    private final List<ActionListener> listeners;

    /**
     * Initializes a double-buffered GamePanel.
     * 
     * @see #GamePanel(boolean)
     */
    public GamePanel() {
        
        this( true );
        
    }

    /**
     * Initializes a GamePanel with a specified buffering strategy.
     * 
     * @param isDoubleBuffered A boolean, true for double-buffering, which uses additional memory space to achieve fast,
     *                         flicker-free updates
     * @see JPanel#JPanel(boolean)
     */
    public GamePanel( boolean isDoubleBuffered ) {
        
        super( new BorderLayout(), isDoubleBuffered );
        
        setPreferredSize( Scalable.scale( WIDTH, HEIGHT ) );
        
        int sidePadding = Scalable.scaleToInt( SIDE_BORDER_PADDING ); // Calculates padding for the button panel.
        int topPadding = Scalable.scaleToInt( TOP_BORDER_PADDING );
        int bottomPadding = Scalable.scaleToInt( BOTTOM_BORDER_PADDING );
    
        listeners = new LinkedList<>();
        ActionListener listener = new ListenerAggregator( listeners );
        
        GridBagConstraints c = new GridBagConstraints();
        
        /* Creates the control panel */
        JPanel controlPanel = new JPanel( new GridBagLayout(), isDoubleBuffered );
        
        skipButton = new JButton( "Skip" ); // Create skip button.
        Scalable.scaleFont( skipButton );
        skipButton.setActionCommand( SKIP_COMMAND );
        skipButton.addActionListener( listener );
        
        JButton menuButton = new JButton( "Menu" );
        Scalable.scaleFont( menuButton );
        menuButton.setActionCommand( MENU_COMMAND );
        menuButton.addActionListener( listener );
        
        JButton saveButton = new JButton( "Save" );
        Scalable.scaleFont( saveButton );
        saveButton.setActionCommand( SAVE_COMMAND );
        saveButton.addActionListener( listener );
        
        loadButton = new JButton( "Load" );
        Scalable.scaleFont( loadButton );
        loadButton.setActionCommand( LOAD_COMMAND );
        loadButton.addActionListener( listener );
        loadButton.setEnabled( ResourceManager.getInstance().hasSave() );
        
        /* Buttons related to game flow */
        int buttonPadding = Scalable.scaleToInt( FLOW_BUTTON_PADDING );
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets( topPadding, sidePadding, buttonPadding, sidePadding );
        c.gridx = 0;
        c.gridy = 0;
        controlPanel.add( skipButton, c ); // Insert skip button in panel.
        
        /* Buttons related to game state */
        buttonPadding = Scalable.scaleToInt( BUTTON_PADDING );
        
        c.insets = new Insets( 0, sidePadding, buttonPadding, sidePadding );
        c.gridy = 1;
        controlPanel.add( menuButton, c ); // Insert menu button in panel.
        
        c.gridy = 2;
        controlPanel.add( saveButton, c ); // Insert save button in panel.
        
        c.gridy = 3;
        controlPanel.add( loadButton, c ); // Insert load button in panel.
        
        /* Creates the choice selector */
        JPanel choicePanel = new JPanel( new GridBagLayout(), isDoubleBuffered );
        
        upButton = new JButton( "/\\" ); // Create arrows and select buttons.
        Scalable.scaleFont( upButton );
        upButton.setActionCommand( UP_COMMAND );
        upButton.addActionListener( listener );
        selectButton = new JButton( "O" );
        Scalable.scaleFont( selectButton );
        selectButton.setActionCommand( SELECT_COMMAND );
        selectButton.addActionListener( listener );
        downButton = new JButton( "\\/" );
        Scalable.scaleFont( downButton );
        downButton.setActionCommand( DOWN_COMMAND );
        downButton.addActionListener( listener );
        
        Dimension size = selectButton.getPreferredSize(); // Sets choice buttons to be squares of the same size.
        size.setSize( size.getWidth(), size.getWidth() );
        downButton.setPreferredSize( size );
        selectButton.setPreferredSize( size );
        upButton.setPreferredSize( size );
        
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets( 0, sidePadding, 0, sidePadding ); // Insert up button in panel.
        c.gridx = 0;
        c.gridy = 0;
        choicePanel.add( upButton, c );
        
        c.gridy = 1; // Insert select button in panel.
        choicePanel.add( selectButton, c );
        
        c.insets = new Insets( 0, sidePadding, bottomPadding, sidePadding ); // Insert down button in panel.
        c.gridy = 2;
        choicePanel.add( downButton, c );
        
        /* Joins control and choice selectors into button panel */
        JPanel buttonPanel = new JPanel( new BorderLayout(), isDoubleBuffered );
        buttonPanel.add( controlPanel, BorderLayout.NORTH );
        buttonPanel.add( choicePanel, BorderLayout.SOUTH );
        
        int padding = Scalable.scaleToInt( TEXT_PADDING ); // Calculates padding and margins for text boxes.
        int margin = Scalable.scaleToInt( TEXT_MARGIN );
        
        /* Creates the text and image boxes */
        JPanel gameArea = new JPanel( new GridBagLayout(), true );
        
        scene = new JTextArea(); // Create and configure area for scene text.
        Scalable.scaleFont( scene );
        scene.setEditable( false );
        scene.setMargin( new Insets( margin, margin, margin, margin ) );
        scene.setLineWrap( true );
        JScrollPane scenePane = new JScrollPane( scene, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        Border border = BorderFactory.createLineBorder( Color.BLACK );
        scenePane.setBorder( border );
        
        options = new JTextArea(); // Create and configure area for options.
        Scalable.scaleFont( options );
        options.setEditable( false );
        options.setMargin( new Insets( margin, margin, margin, margin ) );
        options.setLineWrap( true );
        JScrollPane optionsPane = new JScrollPane( options, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        border = BorderFactory.createLineBorder( Color.BLACK );
        optionsPane.setBorder( border );
        
        graphic = new JPanel( isDoubleBuffered ); // Create and configure area for graphic content.
        border = BorderFactory.createLineBorder( Color.BLACK );
        graphic.setBorder( border );
        graphic.setMinimumSize( new Dimension( 0, 0 ) );
        
        c.fill = GridBagConstraints.BOTH; // Insert scene text pane.
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets( padding, padding, padding, 0 );
        c.gridx = 0;
        c.gridy = 0;
        gameArea.add( scenePane, c );
        
        c.gridy = 2; // Insert options pane.
        gameArea.add( optionsPane, c );
        
        c.gridy = 1; // Insert graphic panel.
        c.weighty = 2;
        c.insets = new Insets( 0, padding, 0, 0 );
        gameArea.add( graphic, c );
        
        /* Adds all areas to the panel */
        add( buttonPanel, BorderLayout.EAST );
        add( gameArea, BorderLayout.CENTER );
        
    }
    
    /**
     * Retrieves the TextArea where the scene text should be displayed.
     * 
     * @return The scene text area.
     */
    public JTextArea getSceneArea() {
        
        return scene;
        
    }
    
    /**
     * Retrieves the TextArea where the scene options should be displayed.
     * 
     * @return The options text area.
     */
    public JTextArea getOptionsArea() {
        
        return options;
        
    }
    
    /**
     * Retrieves the Panel where the scene graphics should be displayed.
     * 
     * @return The graphics panel.
     */
    public JPanel getGraphicArea() {
        
        return graphic;
        
    }
    
    /**
     * Sets whether the buttons of the option selector are enabled.
     * 
     * @param enabled If true, the buttons will be enabled. If false, the buttons will be disabled.
     */
    public void setOptionButtonsEnabled( boolean enabled ) {
        
        upButton.setEnabled( enabled );
        selectButton.setEnabled( enabled );
        downButton.setEnabled( enabled );
        
    }
    
    /**
     * Sets whether the skip button is enabled.
     * 
     * @param enabled If true, the button will be enabled. If false, the button will be disabled.
     */
    public void setSkipButtonEnabled( boolean enabled ) {
        
        skipButton.setEnabled( enabled );
        
    }
    
    /**
     * Sets whether the load button is enabled.
     * 
     * @param enabled If true, the button will be enabled. If false, the button will be disabled.
     */
    public void setLoadButtonEnabled( boolean enabled ) {
        
        loadButton.setEnabled( enabled );
        
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
