package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.github.thiagotgm.separate_but_unequal.Launcher;

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
    
    private static final double SIDE_BORDER_PADDING = 0.2;
    private static final double TOP_BORDER_PADDING = 0.3;
    private static final double BOTTOM_BORDER_PADDING = 0.3;
    
    private static final double TEXT_PADDING = 0.2;
    private static final double TEXT_MARGIN = 0.1;
    
    /** Action command that identifies that the "Skip" button was pressed. */
    public static final String SKIP_COMMAND = "SKIP";
    /** Action command that identifies that the "Up" button was pressed. */
    public static final String UP_COMMAND = "UP";
    /** Action command that identifies that the "Select" button was pressed. */
    public static final String SELECT_COMMAND = "SELECT";
    /** Action command that identifies that the "Down" button was pressed. */
    public static final String DOWN_COMMAND = "DOWN";
    
    private final JButton skipButton;
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
        
        int sidePadding = (int) ( SIDE_BORDER_PADDING * Launcher.resolution ); // Calculates padding for the button panel.
        int topPadding = (int) ( TOP_BORDER_PADDING * Launcher.resolution );
        int bottomPadding = (int) ( BOTTOM_BORDER_PADDING * Launcher.resolution );
    
        ActionListener listener = new ListenerAggregator();
        GridBagConstraints c = new GridBagConstraints();
        
        /* Creates the control panel */
        JPanel controlPanel = new JPanel( new GridBagLayout(), isDoubleBuffered );
        
        skipButton = new JButton( "Skip" ); // Create skip button.
        setFont( skipButton );
        skipButton.setActionCommand( SKIP_COMMAND );
        skipButton.addActionListener( listener );
        
        c.weightx = 0; // Insert skip button in panel.
        c.weighty = 0;
        c.insets = new Insets( topPadding, sidePadding, 0, sidePadding );
        c.gridx = 0;
        c.gridy = 0;
        controlPanel.add( skipButton, c );
        
        /* Creates the choice selector */
        JPanel choicePanel = new JPanel( new GridBagLayout(), isDoubleBuffered );
        
        upButton = new JButton( "/\\" ); // Create arrows and select buttons.
        setFont( upButton );
        upButton.setActionCommand( UP_COMMAND );
        upButton.addActionListener( listener );
        selectButton = new JButton( "O" );
        setFont( selectButton );
        selectButton.setActionCommand( SELECT_COMMAND );
        selectButton.addActionListener( listener );
        downButton = new JButton( "\\/" );
        setFont( downButton );
        downButton.setActionCommand( DOWN_COMMAND );
        downButton.addActionListener( listener );
        
        Dimension size = selectButton.getPreferredSize(); // Sets choice buttons to be squares of the same size.
        size.setSize( size.getWidth(), size.getWidth() );
        downButton.setPreferredSize( size );
        selectButton.setPreferredSize( size );
        upButton.setPreferredSize( size );
        
        c.insets = new Insets( 0, sidePadding, 0, sidePadding ); // Insert up button in panel.
        c.gridx = 0;
        c.gridy = 0;
        choicePanel.add( upButton, c );
        
        c.gridx = 0; // Insert select button in panel.
        c.gridy = 1;
        choicePanel.add( selectButton, c );
        
        c.insets = new Insets( 0, sidePadding, bottomPadding, sidePadding ); // Insert down button in panel.
        c.gridx = 0;
        c.gridy = 2;
        choicePanel.add( downButton, c );
        
        /* Joins control and choice selectors into button panel */
        JPanel buttonPanel = new JPanel( new BorderLayout(), isDoubleBuffered );
        buttonPanel.add( controlPanel, BorderLayout.NORTH );
        buttonPanel.add( choicePanel, BorderLayout.SOUTH );
        
        int padding = (int) ( TEXT_PADDING * Launcher.resolution ); // Calculates padding and margins for text boxes.
        int margin = (int) ( TEXT_MARGIN * Launcher.resolution );
        
        /* Creates the text and image boxes */
        JPanel gameArea = new JPanel( new GridBagLayout(), true );
        
        scene = new JTextArea(); // Create and configure area for scene text.
        scene.setText( "Testing" );
        setFont( scene );
        scene.setEditable( false );
        scene.setMargin( new Insets( margin, margin, margin, margin ) );
        JScrollPane scenePane = new JScrollPane( scene );
        Border border = BorderFactory.createLineBorder( Color.BLACK );
        scenePane.setBorder( border );
        
        options = new JTextArea(); // Create and configure area for options.
        options.setText( " > DO THIS" );
        setFont( options );
        options.setEditable( false );
        options.setMargin( new Insets( margin, margin, margin, margin ) );
        JScrollPane optionsPane = new JScrollPane( options );
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
        
        listeners = new LinkedList<>();
        
    }
    
    /**
     * Sets the text font on the given component to be more appropriate to the resoultion.
     * 
     * @param c The component to have its font resized.
     */
    private void setFont( JComponent c ) {
        
        Font font = c.getFont();
        font = font.deriveFont( font.getSize2D() * Launcher.fontMultiplier );
        c.setFont( font );
        
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
    
    /**
     * Class that listens to all the buttons in the panel and passes any event fired by them to the listeners on the
     * overall panel
     *
     * @version 1.0
     * @author Thiago
     * @since 2017-05-25
     */
    private class ListenerAggregator implements ActionListener {

        @Override
        public void actionPerformed( ActionEvent e ) {

            for ( ActionListener listener : listeners ) {
                
                listener.actionPerformed( e ); // Notifies all listeners that a button event was fired.
                
            }
            
        }
        
    }

}
