package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;
import com.github.thiagotgm.separate_but_unequal.resource.Story;


public class StorySelector extends JPanel {
    
    /** 
     * Action command that identifies that the button selecting one of the stories was pressed.<br>
     * The code of the story is appended to the beginning of the string.
     */
    public static final String SELECT_STORY_COMMAND = "SELECT STORY";
    
    private static final double SMALL_PADDING = 0.1;
    private static final double BIG_PADDING = 0.3;
    private static final double TEXT_MARGIN = 0.1;
    
    private final List<ActionListener> listeners;
    
    private Story choice;

    /**
     * Initializes a double-buffered StorySelector.
     * 
     * @see #StorySelector(boolean)
     */
    public StorySelector() {
        
        this( true );
        
    }

    /**
     * Initializes a StorySelector with a specified buffering strategy.
     * 
     * @param isDoubleBuffered A boolean, true for double-buffering, which uses additional memory space to achieve fast,
     *                         flicker-free updates
     * @see JPanel#JPanel(boolean)
     */
    public StorySelector( boolean isDoubleBuffered ) {
        
        super( isDoubleBuffered );
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        
        listeners = new LinkedList<>();
        ActionListener listener = new ListenerAggregator( listeners ) {
            
            /**
             * If the button pressed was a choice, identifies the choice from the command string, then removes the
             * Story code from the command string before passing it on.
             * 
             * @param e Event triggered.
             */
            @Override
            public void actionPerformed( ActionEvent e ) {
                
                if ( e.getActionCommand().contains( SELECT_STORY_COMMAND ) ) { // A choice was selected.
                    char code = e.getActionCommand().charAt( 0 );
                    for ( Story story : ResourceManager.getInstance().getStories() ) { // Identifies what choice it was.
                        
                        if ( story.getCode() == code ) {
                            choice = story;
                            break;
                        }
                        
                    }
                    e = new ActionEvent( e.getSource(), e.getID(), e.getActionCommand().substring( 1 ), e.getWhen(),
                            e.getModifiers() ); // Removes the Story code from the command string.
                }
                super.actionPerformed( e );
                
            }
            
        };
        
        Dimension smallPadding = Scalable.scale( SMALL_PADDING, SMALL_PADDING ); 
        int margin = Scalable.scaleToInt( TEXT_MARGIN );
        List<JPanel> stories = new LinkedList<>();
        for ( Story story : ResourceManager.getInstance().getStories() ) {

            JPanel panel = new JPanel( isDoubleBuffered );
            panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
            
            JLabel name = new JLabel( story.getName() );
            Scalable.scaleFont( name );
            name.setHorizontalAlignment( SwingConstants.CENTER );
            name.setAlignmentX( Component.CENTER_ALIGNMENT );
            
            JTextArea description = new JTextArea( story.getDescription() );
            description.setEditable( false );
            description.setMargin( new Insets( margin, margin, margin, margin ) );
            Scalable.scaleFont( description );
            description.setAlignmentX( Component.CENTER_ALIGNMENT );
            
            JButton button = new JButton( "Select" );
            Scalable.scaleFont( button );
            button.setAlignmentX( Component.CENTER_ALIGNMENT );
            button.setActionCommand( story.getCode() + SELECT_STORY_COMMAND );
            button.addActionListener( listener );
            
            panel.add( Box.createRigidArea( smallPadding ) );
            panel.add( name );
            panel.add( Box.createRigidArea( smallPadding ) );
            panel.add( description );
            panel.add( Box.createRigidArea( smallPadding ) );
            panel.add( button );
            panel.add( Box.createRigidArea( smallPadding ) );
            
            Border border = BorderFactory.createBevelBorder( BevelBorder.LOWERED, Color.BLACK, Color.GRAY );
            panel.setBorder( border );
            
            stories.add( panel );
            
        }
        
        Dimension bigPadding = Scalable.scale( BIG_PADDING, BIG_PADDING );
        JPanel leftChoices = new JPanel( isDoubleBuffered );
        leftChoices.setLayout( new BoxLayout( leftChoices, BoxLayout.Y_AXIS ) );
        leftChoices.add( Box.createRigidArea( bigPadding ) );
        JPanel rightChoices = new JPanel( isDoubleBuffered );
        rightChoices.setLayout( new BoxLayout( rightChoices, BoxLayout.Y_AXIS ) );
        rightChoices.add( Box.createRigidArea( bigPadding ) );
        
        boolean left = true;
        for ( JPanel panel : stories ) {
            
            JPanel dest = ( left ) ? leftChoices : rightChoices;
            dest.add( panel );
            dest.add( Box.createRigidArea( bigPadding ) );
            left = !left;
            
        }
        
        JPanel choices = new JPanel( isDoubleBuffered );
        choices.setLayout( new BoxLayout( choices, BoxLayout.X_AXIS ) );
        choices.add( Box.createRigidArea( bigPadding ) );
        choices.add( leftChoices );
        choices.add( Box.createRigidArea( bigPadding ) );
        choices.add( rightChoices );
        choices.add( Box.createRigidArea( bigPadding ) );
        
        JScrollPane scroll = new JScrollPane( choices, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        add( scroll );
        
    }
    
    /**
     * Retrieves the user's choice.
     * 
     * @return The Story chosen by the user.
     */
    public Story getChoice() {
        
        return choice;
        
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
