package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

import com.github.thiagotgm.separate_but_unequal.CompletionManager;
import com.github.thiagotgm.separate_but_unequal.MenuManager;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;
import com.github.thiagotgm.separate_but_unequal.resource.Story;

/**
 * Panel that contains the UI for selecting one of the stories in the game.
 *
 * @version 1.0
 * @author ThiagoTGM
 * @since 2017-05-29
 */
public class StorySelector extends ButtonPanel {
    
    /** Serial ID that represents this class. */
    private static final long serialVersionUID = 8230505761611363794L;

    /** 
     * Action command that identifies that the button selecting one of the stories was pressed.<br>
     * The code of the story is appended to the beginning of the string.
     */
    public static final String SELECT_STORY_COMMAND = "SELECT STORY";
    
    private static final double SMALL_PADDING = 0.1;
    private static final double BIG_PADDING = 0.3;
    private static final double TEXT_MARGIN = 0.1;
    private static final double OPTION_WIDTH = 3;
    private static final double OPTION_HEIGHT = 2;
    private static final int MAX_LINES = 3;
    private static final int MAX_COLUMNS = 2;
    
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
        
        ActionListener listener = new ListenerAggregator( listeners ) { // Initialize listener aggregator.
            
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
        boolean unlocked = true;
        Story key = null;
        CompletionManager completion = CompletionManager.getInstance();
        for ( Story story : ResourceManager.getInstance().getStories() ) {
            
            /* Create a panel for each option */
            JPanel panel = new JPanel( isDoubleBuffered );
            panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
            
            JLabel name = new JLabel( ( unlocked ) ? story.getName() : "LOCKED" ); // Add story name.
            Scalable.scaleFont( name );
            name.setHorizontalAlignment( SwingConstants.CENTER );
            name.setAlignmentX( Component.CENTER_ALIGNMENT );
            
            JTextArea description = new JTextArea( ( unlocked ) ? story.getDescription() : "Reach any ending "
                    + "of '" + key.getName() + "' to unlock the next story!" );
            description.setEditable( false ); // Add story description.
            description.setMargin( new Insets( margin, margin, margin, margin ) );
            Scalable.scaleFont( description );
            description.setAlignmentX( Component.CENTER_ALIGNMENT );
            description.setLineWrap( true );
            
            JButton button = new JButton( "Select" );
            Scalable.scaleFont( button ); // Add select button.
            button.setAlignmentX( Component.CENTER_ALIGNMENT );
            button.setActionCommand( story.getCode() + SELECT_STORY_COMMAND );
            button.addActionListener( listener );
            button.setEnabled( unlocked );
            
            // Add all to panel.
            panel.add( Box.createRigidArea( smallPadding ) );
            panel.add( name );
            panel.add( Box.createRigidArea( smallPadding ) );
            panel.add( description );
            panel.add( Box.createRigidArea( smallPadding ) );
            panel.add( button );
            panel.add( Box.createRigidArea( smallPadding ) );
            
            Border border = BorderFactory.createBevelBorder( BevelBorder.RAISED, Color.BLACK, Color.GRAY );
            panel.setBorder( border ); // Make a border for this option.
            
            stories.add( panel );
            
            boolean isPlayed = completion.isPlayed( story.getCode() );
            if ( unlocked && !isPlayed ) {
                key = story;
            }
            unlocked = isPlayed;
            
        }
        
        Dimension bigPadding = Scalable.scale( BIG_PADDING, BIG_PADDING );
        List<JPanel> columns = new ArrayList<>( Math.min( MAX_COLUMNS, stories.size() ) );
        Dimension size = Scalable.scale( OPTION_WIDTH, OPTION_HEIGHT );
        for ( int i = 0; i < Math.min( MAX_COLUMNS, stories.size() ); i++ ) {
            // Create each column for displaying choices.
            JPanel column = new JPanel( isDoubleBuffered );
            column.setLayout( new BoxLayout( column, BoxLayout.Y_AXIS ) );
            columns.add( column );
            
        }
        
        int columnNum = 0;
        int lineCount = 0;
        for ( JPanel panel : stories ) {
            
            if ( columnNum == 0 ) {
                lineCount++; // Counts the amount of lines.
            }
            
            panel.setPreferredSize( size ); // Sets size of the option panel.
            panel.setMaximumSize( size );
            
            JPanel dest = columns.get( columnNum );
            dest.add( Box.createRigidArea( bigPadding ) );
            dest.add( panel );
            columnNum = ( columnNum < columns.size() - 1 ) ? columnNum + 1 : 0;
            
        }
        
        JPanel choices = new JPanel( isDoubleBuffered );
        choices.setLayout( new BoxLayout( choices, BoxLayout.X_AXIS ) );
        for ( JPanel column : columns ) {
            // Adds columns to total option panel.
            column.add( Box.createRigidArea( bigPadding ) );
            column.add( Box.createVerticalGlue() );
            choices.add( Box.createRigidArea( bigPadding ) );
            choices.add( column );
            
        }
        choices.add( Box.createRigidArea( bigPadding ) );
        choices.add( Box.createHorizontalGlue() );
        
        JScrollPane scroll = new JScrollPane( choices, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ); // Adds scroll bar to
        add( scroll );                                        // option panel.
        
        lineCount = Math.min( MAX_LINES, lineCount );
        int columnCount = columns.size();
        double panelWidth = columnCount * OPTION_WIDTH + 
                ( columnCount + 1 ) * BIG_PADDING + BIG_PADDING;
        double panelHeight = lineCount * OPTION_HEIGHT +
                ( lineCount + 1 ) * BIG_PADDING + BIG_PADDING;
        size = Scalable.scale( panelWidth, panelHeight );
        scroll.setPreferredSize( size ); // Set option panel to display at most the
        scroll.setMaximumSize( size );   // max # of choices.
        
        // Create button to go back to menu.
        JButton cancelButton = new JButton( "Cancel" );
        Scalable.scaleFont( cancelButton );
        cancelButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        cancelButton.setActionCommand( MenuManager.BACK_COMMAND );
        cancelButton.addActionListener( listener );
        add( cancelButton );
        
    }
    
    /**
     * Retrieves the user's choice.
     * 
     * @return The Story chosen by the user.
     */
    public Story getChoice() {
        
        return choice;
        
    }

}
