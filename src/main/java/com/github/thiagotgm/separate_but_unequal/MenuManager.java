package com.github.thiagotgm.separate_but_unequal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.thiagotgm.separate_but_unequal.gui.GamePanel;
import com.github.thiagotgm.separate_but_unequal.gui.MainMenuPanel;
import com.github.thiagotgm.separate_but_unequal.gui.StorySelector;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;
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
    
    /**
     * Action command that identifies that a button to return to the main menu
     * was pressed.
     * */
    public static final String BACK_COMMAND = "BACK";
    
    private final JFrame program;
    private final GamePanel game;
    private final GameManager gameManager;
    private final MainMenuPanel menu;
    
    private JPanel current;

    /**
     * Creates a new manager that displays on the given window.
     * 
     * @param program Window to display the menus and the game on.
     */
    public MenuManager( JFrame program ) {

        this.program = program;
        game = new GamePanel();
        game.addActionListener( this );
        gameManager = new GameManager( game, this );
        menu = new MainMenuPanel();
        menu.addActionListener( this );
        
        program.add( menu );
        current = menu;
        program.pack();
        
    }

    /**
     * Identifies which menu button was pressed and performs the corresponding action.
     * 
     * @param e Event triggered by the button press.
     */
    @Override
    public void actionPerformed( ActionEvent e ) {

        switch ( e.getActionCommand() ) {
            
            case MainMenuPanel.EXIT_COMMAND: // Exit the program.
                program.dispatchEvent( new WindowEvent( program, WindowEvent.WINDOW_CLOSING ) );
                break;
                
            case MainMenuPanel.START_COMMAND: // Start the game.
                StorySelector selector = new StorySelector();
                selector.addActionListener( this );
                setWindow( selector );
                break;
                
            case MainMenuPanel.LOAD_COMMAND: // Load from previous save.
                setWindow( game );
                gameManager.load();
                break;
                
            case StorySelector.SELECT_STORY_COMMAND: // A story was selected.
                selector = (StorySelector) current;
                Story selected = selector.getChoice();
                String startID = selected.getStart();
                log.info( "Selected story with start '" + startID + "'" );
                if ( !ResourceManager.getInstance().isResource( startID ) ) {
                    log.warn( "Invalid start ID '" + startID + "'" );
                    JOptionPane.showMessageDialog( current, "The story selected "
                            + "does not have a valid start.", // Invalid start.
                            "Start Error", JOptionPane.ERROR_MESSAGE );
                } else { // Valid start.
                    log.debug( "Starting game with Scene ID '" + startID + "'" );
                    selector.removeActionListener( this );
                    setWindow( game ); // Start the game.
                    gameManager.start( "Char 1 Start" );
                }
                break;
                
            case GamePanel.SAVE_COMMAND: // The game was saved.
                menu.setLoadButtonEnabled( true );
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
    private void setWindow( JPanel panel ) {
        
        program.remove( current );
        program.add( panel );
        current = panel;
        program.pack();
        program.revalidate();
        program.repaint();
        
    }
    
    /**
     * Records that the game was halted.
     * 
     * @param endCode Code that the game ended with. If game was stopped before reaching an end, should be 0. Else,
     *                should be the code of the ending that was achived.
     */
    protected void gameEnd( int endCode ) {
        
        System.out.println( endCode );
        setWindow( menu );
        
    }

}
