package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.thiagotgm.separate_but_unequal.MenuManager;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;


public class SettingsPanel extends ButtonPanel implements ActionListener {
    
    private static final Logger log = LoggerFactory.getLogger( SettingsPanel.class );
    
    /** Action command that identifies that the "Decrease" button for text speed was pressed. */
    public static final String DECREASE_SPEED_COMMAND = "DECREASE SPEED";
    /** Action command that identifies that the "Increase" button for text speed was pressed. */
    public static final String INCREASE_SPEED_COMMAND = "INCREASE SPEED";
    
    private static final double PANEL_PADDING = 1;
    private static final double LABEL_PADDING = 0.2;
    private static final double BUTTON_PADDING = 0.1;
    
    private static final int MIN_TEXT_SPEED = 1;
    private static final int MAX_TEXT_SPEED = 16;
    private static final int TEXT_SPEED_CHANGE = 1;
    
    private final JLabel textSpeedValue;

    /**
     * Initializes a double-buffered SettingsPanel.
     * 
     * @see #SettingsPanel(boolean)
     */
    public SettingsPanel() {

        this( true );
        
    }

    /**
     * Initializes a SettingsPanel with a specified buffering strategy.
     * 
     * @param isDoubleBuffered A boolean, true for double-buffering, which uses additional memory space to achieve fast,
     *                         flicker-free updates
     * @see JPanel#JPanel(boolean)
     */
    public SettingsPanel( boolean isDoubleBuffered ) {
        
        super( isDoubleBuffered );
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        
        ActionListener listener = new ListenerAggregator( listeners ); // Initialize listener aggregator.
        
        Dimension labelPadding = Scalable.scale( LABEL_PADDING, 0 );
        Dimension buttonPadding = Scalable.scale( BUTTON_PADDING, 0 );
        
        JPanel textSpeed = new JPanel( isDoubleBuffered );
        textSpeed.setLayout( new BoxLayout( textSpeed, BoxLayout.X_AXIS ) );
        
        JLabel textSpeedLabel = new JLabel( "Text Speed:" );
        Scalable.scaleFont( textSpeedLabel );
        textSpeed.add( textSpeedLabel );
        
        textSpeed.add( Box.createRigidArea( labelPadding ) );
        
        JButton minusButton = new JButton( "-" );
        minusButton.setActionCommand( DECREASE_SPEED_COMMAND );
        Scalable.scaleFont( minusButton );
        minusButton.addActionListener( listener );
        textSpeed.add( minusButton );
        
        textSpeed.add( Box.createRigidArea( buttonPadding ) );
        
        textSpeedValue = new JLabel( ResourceManager.getInstance().getTextSpeedMultiplier() + "x" );
        Scalable.scaleFont( textSpeedValue );
        textSpeed.add( textSpeedValue );
        
        textSpeed.add( Box.createRigidArea( buttonPadding ) );
        
        JButton plusButton = new JButton( "+" );
        plusButton.setActionCommand( INCREASE_SPEED_COMMAND );
        Scalable.scaleFont( plusButton );
        plusButton.addActionListener( listener );
        textSpeed.add( plusButton );
        
        textSpeed.add( Box.createHorizontalGlue() );
        add( textSpeed );
        
        add( Box.createVerticalGlue() );
        add( Box.createRigidArea( Scalable.scale( 0, PANEL_PADDING ) ) );
        
        JButton backButton = new JButton( "Back" );
        backButton.setActionCommand( MenuManager.BACK_COMMAND );
        Scalable.scaleFont( backButton );
        backButton.addActionListener( listener );
        backButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( backButton );
        
        int panelBorderSize = Scalable.scaleToInt( PANEL_PADDING );
        Border panelBorder = BorderFactory.createEmptyBorder( panelBorderSize, panelBorderSize, panelBorderSize,
                panelBorderSize );
        this.setBorder( panelBorder );
        
        addActionListener( this );

    }
    
    /**
     * Changes the text speed multiplier by a certain offset.<br>
     * The change will only be made if the new value is within the range {@value #MIN_TEXT_SPEED} to
     * {@value #MAX_TEXT_SPEED}, inclusive. If it is not, no change is made.
     * 
     * @param change Offset to change the multiplier by.
     */
    private void changeTextSpeed( int change ) {
        
        int newSpeed = ResourceManager.getInstance().getTextSpeedMultiplier() + change;
        if ( ( newSpeed >= MIN_TEXT_SPEED ) && ( newSpeed <= MAX_TEXT_SPEED ) ) {
            ResourceManager.getInstance().setTextSpeedMultiplier( newSpeed );
            textSpeedValue.setText( newSpeed + "x" );
            log.debug( "Changed text speed multiplier to " + newSpeed + "." );
        } else {
            log.trace( "Text speed multiplier " + newSpeed + " out of bounds. Change refused." );
        }
        
    }

    @Override
    public void actionPerformed( ActionEvent e ) {

        switch ( e.getActionCommand() ) {
            
            case DECREASE_SPEED_COMMAND: // Reduce text speed multiplier.
                changeTextSpeed( -TEXT_SPEED_CHANGE );
                break;
                
            case INCREASE_SPEED_COMMAND: // Increase text speed multiplier.
                changeTextSpeed( TEXT_SPEED_CHANGE );
                break;
            
        }
        
    }

}
