package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.github.thiagotgm.separate_but_unequal.CompletionManager;
import com.github.thiagotgm.separate_but_unequal.MenuManager;
import com.github.thiagotgm.separate_but_unequal.resource.Achievement;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;


public class AchievementPanel extends ButtonPanel {
    
    private static final String CODE_STRING = "Unlocked for reaching ending %c-%d";
    
    private static final String LOCKED_ITEM = "Locked";
    private static final double PANEL_BORDER = 0.25;
    private static final double DISPLAY_PADDING = 0.1;
    private static final double TEXT_MARGIN = 0.1;
    private static final double WIDTH = 7;
    private static final double HEIGHT = 5;
    
    private final JLabel title;
    private final JLabel code;
    private final JTextArea text;

    /**
     * Initializes a double-buffered AchievementPanel.
     * 
     * @see #AchievementPanel(boolean)
     */
    public AchievementPanel() {
        
        this( true );
        
    }

    /**
     * Initializes a AchievementPanel with a specified buffering strategy.
     * 
     * @param isDoubleBuffered A boolean, true for double-buffering, which uses additional memory space to achieve fast,
     *                         flicker-free updates
     * @see JPanel#JPanel(boolean)
     */
    public AchievementPanel( boolean isDoubleBuffered ) {
        
        super( isDoubleBuffered );
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        
        List<Achievement> achievements = ResourceManager.getInstance().getAchievements();
        String[] items = new String[achievements.size()];
        CompletionManager completion = CompletionManager.getInstance();
        Iterator<Achievement> iter = achievements.iterator();
        for ( int i = 0; i < achievements.size(); i++ ) { // Create the achievement list, hiding locked ones.
            
            Achievement current = iter.next();
            if ( completion.isReached( current.getStoryCode(), current.getEndCode() ) ) {
                items[i] = current.getTitle(); // Achievement is unlocked.
            } else {
                items[i] = LOCKED_ITEM; // Achievement is locked.
            }
            
        }
        
        /* Create achievement list */
        JList<String> achievementList = new JList<String>( items );
        achievementList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        achievementList.setVisibleRowCount( -1 );
        Scalable.scaleFont( achievementList );
        
        achievementList.addListSelectionListener( ( e ) -> {
            
            int index = achievementList.getSelectedIndex();
            if ( index != -1 ) { // Display selected achievement.
                Achievement selected = achievements.get( index );
                AchievementPanel.this.setDisplay( selected );
            }
            
        });
        
        JScrollPane scroll = new JScrollPane( achievementList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        double width = ( WIDTH - 3 * Scalable.scaleToInt( DISPLAY_PADDING ) ) / 3;
        scroll.setPreferredSize( Scalable.scale( width, HEIGHT ) );
        
        /* Create selected-achievement display */
        JPanel display = new JPanel( isDoubleBuffered );
        display.setLayout( new BoxLayout( display, BoxLayout.Y_AXIS ) );
        Dimension padding = Scalable.scale( 0, DISPLAY_PADDING );
        
        title = new JLabel(); // Achievement title.
        Scalable.scaleFont( title );
        title.setText( "-----------------------------" );
        title.setAlignmentX( Component.CENTER_ALIGNMENT );
        display.add( title );
        
        display.add( Box.createRigidArea( padding ) );
        
        code = new JLabel(); // Achievement code.
        Scalable.scaleFont( code );
        code.setText( "-" );
        code.setAlignmentX( Component.CENTER_ALIGNMENT );
        display.add( code );
        
        display.add( Box.createRigidArea( padding ) );
        
        text = new JTextArea(); // Achievement text.
        Scalable.scaleFont( text );
        int margin = Scalable.scaleToInt( TEXT_MARGIN );
        text.setMargin( new Insets( margin, margin, margin, margin ) );
        text.setEditable( false );
        text.setLineWrap( true );
        text.setText( "Select an item from the menu." );
        text.setAlignmentX( Component.CENTER_ALIGNMENT );
        display.add( text );
        
        display.setPreferredSize( Scalable.scale( width * 2, HEIGHT ) );
        
        /* Add list and display to full display area */
        JPanel achievementDisplay = new JPanel( new GridBagLayout(), isDoubleBuffered );
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 2;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        achievementDisplay.add( display, c );
        c.weightx = 1;
        c.gridx = 1;
        c.insets = new Insets( 0, Scalable.scaleToInt( PANEL_BORDER ), 0, 0 );
        achievementDisplay.add( scroll, c );
        
        achievementDisplay.setPreferredSize( Scalable.scale( WIDTH, HEIGHT ) );
        add( achievementDisplay );
        
        /* Make button to go back to menu */
        int borderSize = Scalable.scaleToInt( PANEL_BORDER );
        add( Box.createRigidArea( new Dimension( borderSize, borderSize ) ) );
        
        ActionListener listener = new ListenerAggregator( listeners );
        JButton menuButton = new JButton( "Back" );
        Scalable.scaleFont( menuButton );
        menuButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        menuButton.setActionCommand( MenuManager.BACK_COMMAND );
        menuButton.addActionListener( listener );
        
        JPanel buttonPanel = new JPanel( isDoubleBuffered );
        buttonPanel.add( menuButton );
        add( menuButton );
        
        /* Border padding areound the panel */
        Border border = BorderFactory.createEmptyBorder( borderSize, borderSize, borderSize, borderSize );
        setBorder( border );

    }
    
    /**
     * Sets the achievement display panel to display a given Achievement.
     * 
     * @param achievement The achievement to display.
     */
    private void setDisplay( Achievement achievement ) {
        
        title.setText( achievement.getTitle() );
        code.setText( String.format( CODE_STRING, achievement.getStoryCode(), achievement.getEndCode() ) );
        text.setText( achievement.getText() );
        
    }

}
