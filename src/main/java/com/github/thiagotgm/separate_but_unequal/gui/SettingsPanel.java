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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.thiagotgm.separate_but_unequal.CompletionManager;
import com.github.thiagotgm.separate_but_unequal.MenuManager;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;

/**
 * Panel that displays the game's options and allows the user to change them.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-29
 */
public class SettingsPanel extends ButtonPanel implements ActionListener {
    
    /** Serial ID that represents this class. */
    private static final long serialVersionUID = 1649752456283539079L;

    private static final Logger log = LoggerFactory.getLogger( SettingsPanel.class );
    
    /** Action command that identifies that the "Decrease" button for text speed was pressed. */
    public static final String DECREASE_SPEED_COMMAND = "DECREASE SPEED";
    /** Action command that identifies that the "Increase" button for text speed was pressed. */
    public static final String INCREASE_SPEED_COMMAND = "INCREASE SPEED";
    /** Action command that identifies that the "Clear Save" was pressed. */
    public static final String CLEAR_SAVE_COMMAND = "CLEAR SAVE";
    /** Action command that identifies that the "Clear Progress" was pressed. */
    public static final String CLEAR_PROGRESS_COMMAND = "CLEAR PROGRESS";
    
    private static final double PANEL_PADDING = 1;
    private static final double LABEL_PADDING = 0.2;
    private static final double BUTTON_PADDING = 0.1;
    
    private static final int MIN_TEXT_SPEED = 1;
    private static final int MAX_TEXT_SPEED = 16;
    private static final int TEXT_SPEED_CHANGE = 1;
    
    private final JLabel textSpeedValue;
    private final JButton clearSaveButton;
    private final ActionListener listener;
    
    private boolean bypassConfirm;

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
        
        listener = new ListenerAggregator( listeners ); // Initialize listener aggregator.
        
        Dimension labelPadding = Scalable.scale( LABEL_PADDING, 0 );
        Dimension buttonPadding = Scalable.scale( BUTTON_PADDING, 0 );
        
        /* Add text speed multiplier setting */
        
        JPanel textSpeed = new JPanel( isDoubleBuffered );
        textSpeed.setLayout( new BoxLayout( textSpeed, BoxLayout.X_AXIS ) );
        
        JLabel textSpeedLabel = new JLabel( "Text Speed:" ); // Label.
        Scalable.scaleFont( textSpeedLabel );
        textSpeed.add( textSpeedLabel );
        
        textSpeed.add( Box.createRigidArea( labelPadding ) );
        
        JButton minusButton = new JButton( "-" ); // Decrease button.
        minusButton.setActionCommand( DECREASE_SPEED_COMMAND );
        Scalable.scaleFont( minusButton );
        minusButton.addActionListener( this );
        textSpeed.add( minusButton );
        
        textSpeed.add( Box.createRigidArea( buttonPadding ) );
        
        textSpeedValue = new JLabel( ResourceManager.getInstance().getTextSpeedMultiplier() + "x" );
        Scalable.scaleFont( textSpeedValue ); // Current value.
        textSpeed.add( textSpeedValue );
        
        textSpeed.add( Box.createRigidArea( buttonPadding ) );
        
        JButton plusButton = new JButton( "+" ); // Increase button.
        plusButton.setActionCommand( INCREASE_SPEED_COMMAND );
        Scalable.scaleFont( plusButton );
        plusButton.addActionListener( this );
        textSpeed.add( plusButton );
        
        textSpeed.add( Box.createHorizontalGlue() );
        add( textSpeed );
        
        /* Add Clear Save button */
        add( Box.createRigidArea( Scalable.scale( 0, LABEL_PADDING ) ) );
        
        clearSaveButton = new JButton( "Clear Save" );
        clearSaveButton.setActionCommand( CLEAR_SAVE_COMMAND );
        Scalable.scaleFont( clearSaveButton );
        clearSaveButton.addActionListener( this );
        clearSaveButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        clearSaveButton.setEnabled( ResourceManager.getInstance().hasSave() );
        add( clearSaveButton );
        
        /* Add Clear Progress button */
        add( Box.createRigidArea( Scalable.scale( 0, LABEL_PADDING ) ) );
        
        JButton clearProgressButton = new JButton( "Clear Progress" );
        clearProgressButton.setActionCommand( CLEAR_PROGRESS_COMMAND );
        Scalable.scaleFont( clearProgressButton );
        clearProgressButton.addActionListener( this );
        clearProgressButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( clearProgressButton );
        
        /* Add button to go back to menu */
        add( Box.createVerticalGlue() );
        add( Box.createRigidArea( Scalable.scale( 0, PANEL_PADDING ) ) );
        
        JButton backButton = new JButton( "Back" );
        backButton.setActionCommand( MenuManager.BACK_COMMAND );
        Scalable.scaleFont( backButton );
        backButton.addActionListener( this );
        backButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( backButton );
        
        int panelBorderSize = Scalable.scaleToInt( PANEL_PADDING ); // Add border padding.
        Border panelBorder = BorderFactory.createEmptyBorder( panelBorderSize, panelBorderSize, panelBorderSize,
                panelBorderSize );
        this.setBorder( panelBorder );
        
        this.bypassConfirm = false;

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
                
            case CLEAR_SAVE_COMMAND: // Clear game save.
                log.debug( "Clear save selected." );
                int choice = ( bypassConfirm ) ? JOptionPane.YES_OPTION : JOptionPane.showConfirmDialog( this,
                        "This will delete your currently saved game. Are you sure?", "Clear Save",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE ); // Ask to confirm unless clear
                if ( choice == JOptionPane.YES_OPTION ) {                         // progress was the command.
                    ResourceManager.getInstance().setSave( null );
                    clearSaveButton.setEnabled( false );
                    log.info( "Save cleared." );
                    listener.actionPerformed( e );
                } else {
                    log.debug( "Clear Save cancelled." );
                }
                this.bypassConfirm = false;
                break;
                
            case CLEAR_PROGRESS_COMMAND:
                log.debug( "Clear save selected." );
                choice = JOptionPane.showConfirmDialog( this,
                        "This will delete your currently saved game and reset your achievements and progress. Are you"
                        + " sure?", "Clear Progress",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE );
                if ( choice == JOptionPane.YES_OPTION ) {
                    CompletionManager.getInstance().clearProgress();
                    log.info( "Progress cleared." );
                    this.bypassConfirm = true; // Delete save without asking for confirmation.
                    clearSaveButton.doClick();
                } else {
                    log.debug( "Clear Progress cancelled." );
                }
                break;
                
            default:
                listener.actionPerformed( e ); // Pass on to registered listeners.
            
        }
        
    }

}
