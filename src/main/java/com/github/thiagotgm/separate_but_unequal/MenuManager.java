package com.github.thiagotgm.separate_but_unequal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.thiagotgm.separate_but_unequal.gui.ButtonPanel;
import com.github.thiagotgm.separate_but_unequal.gui.GamePanel;
import com.github.thiagotgm.separate_but_unequal.gui.MainMenuPanel;
import com.github.thiagotgm.separate_but_unequal.gui.SettingsPanel;
import com.github.thiagotgm.separate_but_unequal.gui.StorySelector;
import com.github.thiagotgm.separate_but_unequal.gui.TextPanel;
import com.github.thiagotgm.separate_but_unequal.resource.Story;

/**
 * Manages the game menus, and controls what is displayed on the game window at any point.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-28
 */
public class MenuManager implements ActionListener {
    
    private static final Logger log = LoggerFactory.getLogger( MenuManager.class );
    
    private static final String HELP_FILE = "help.txt";
    private static final String ABOUT_FILE = "about.txt";
    
    /**
     * Action command that identifies that a button to return to the main menu
     * was pressed.
     * */
    public static final String BACK_COMMAND = "BACK";
    
    private final JFrame program;
    private final GamePanel game;
    private final GameManager gameManager;
    private final MainMenuPanel menu;
    
    private ButtonPanel current;

    /**
     * Creates a new manager that displays on the given window.
     * 
     * @param program Window to display the menus and the game on.
     */
    public MenuManager( JFrame program ) {

        this.program = program;
        game = new GamePanel();
        gameManager = new GameManager( game, this );
        menu = new MainMenuPanel();
        menu.addActionListener( this ); // Starts listening to main menu.
        
        program.add( menu );
        current = menu;
        program.pack();
        
    }

    /**
     * Identifies which menu button was pressed and performs the corresponding action.<br>
     * Will return to the main menu when the command {@value #BACK_COMMAND} is received.
     * 
     * @param e Event triggered by the button press.
     */
    @Override
    public void actionPerformed( ActionEvent e ) {

        switch ( e.getActionCommand() ) {
            
            case MainMenuPanel.EXIT_COMMAND: // Exit the program.
                program.dispatchEvent( new WindowEvent( program, WindowEvent.WINDOW_CLOSING ) );
                break;
                
            case MainMenuPanel.ACHIEVEMENTS_COMMAND: // Open Help.
                log.debug( "Opening Achievements panel." );
                // TODO
                break;
                
            case MainMenuPanel.SETTINGS_COMMAND: // Open settings.
                log.debug( "Opening Settings panel." );
                setWindow( new SettingsPanel() );
                break;
                
            case MainMenuPanel.HELP_COMMAND: // Open Help.
                log.debug( "Opening Help page." );
                InputStream file = getClass().getClassLoader().getResourceAsStream( HELP_FILE );
                if ( file != null ) {
                    setWindow( new TextPanel( file ) );
                } else { // Could not load file.
                    log.error( "Help file '" + HELP_FILE + "' not found." );
                    JOptionPane.showMessageDialog( current, "The Help menu could not be loaded.",
                            "Menu Error", JOptionPane.ERROR_MESSAGE );
                }
                break;
                
            case MainMenuPanel.ABOUT_COMMAND: // Open About.
                log.debug( "Opening About page." );
                file = getClass().getClassLoader().getResourceAsStream( ABOUT_FILE );
                if ( file != null ) {
                    setWindow( new TextPanel( file ) );
                } else { // Could not load file.
                    log.error( "About file '" + ABOUT_FILE + "' not found." );
                    JOptionPane.showMessageDialog( current, "The About menu could not be loaded.",
                            "Menu Error", JOptionPane.ERROR_MESSAGE );
                }
                break;
                
            case MainMenuPanel.START_COMMAND: // Start the game.
                setWindow( new StorySelector() );
                break;
                
            case MainMenuPanel.LOAD_COMMAND: // Load from previous save.
                setWindow( game );
                gameManager.load();
                break;
                
            case StorySelector.SELECT_STORY_COMMAND: // A story was selected.
                StorySelector selector = (StorySelector) current;
                Story selected = selector.getChoice();
                String startID = selected.getStart();
                char code = selected.getCode();
                log.info( "Selected story with code '" + code + "'" );
                try {
                    log.info( "Starting game with Scene ID '" + startID + "', code '" + code + "'." );
                    gameManager.start( startID, code );
                    setWindow( game ); // Start the game.
                } catch ( IllegalArgumentException ex ) {
                    log.warn( "Invalid starting Scene '" + startID + "'", ex );
                    JOptionPane.showMessageDialog( current, "The story selected "
                            + "does not have a valid starting Scene.", // Invalid start.
                            "Start Error", JOptionPane.ERROR_MESSAGE );
                }
                break;
                
            case GamePanel.SAVE_COMMAND: // The game was saved.
                menu.setLoadButtonEnabled( true );
                break;
                
            case SettingsPanel.CLEAR_SAVE_COMMAND:
                menu.setLoadButtonEnabled( false );
                game.setLoadButtonEnabled( false );
                break;
                
            case MenuManager.BACK_COMMAND: // Back to main menu.
                setWindow( menu );
            
        }
        
    }
    
    /**
     * Sets which panel is to be displayed in the game window.
     * 
     * @param panel Panel to be displayed.
     */
    private void setWindow( ButtonPanel panel ) {
        
        current.removeActionListener( this ); // Stop listening to old panel.
        program.remove( current );
        program.add( panel );
        current = panel;
        program.pack();
        program.revalidate(); // Display new panel.
        program.repaint();
        panel.addActionListener( this ); // Starts listening to new panel.
        
    }
    
    /**
     * Records that the game was halted.
     * 
     * @param endCode Code that the game ended with. If game was stopped before reaching an end, should be 0. Else,
     *                should be the code of the ending that was achived.
     */
    protected void gameEnd( int endCode ) {
        
        if ( endCode != 0 ) {
            char storyCode = gameManager.getStoryCode();
            log.info( "Game reached ending '" + storyCode + "-" + endCode + "'." );
        } else {
            log.debug( "Game halted." );
        }
        setWindow( menu );
        
    }

}
