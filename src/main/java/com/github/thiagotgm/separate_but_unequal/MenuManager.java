package com.github.thiagotgm.separate_but_unequal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.thiagotgm.separate_but_unequal.gui.GamePanel;
import com.github.thiagotgm.separate_but_unequal.gui.MainMenuPanel;

public class MenuManager implements ActionListener {
    
    private final JFrame program;
    private final GamePanel game;
    private final GameManager gameManager;
    private final MainMenuPanel menu;
    
    private JPanel current;

    public MenuManager( JFrame program ) {

        this.program = program;
        game = new GamePanel();
        game.addActionListener( this );
        gameManager = new GameManager( game, this );
        menu = new MainMenuPanel();
        menu.addActionListener( this );
        
        program.add( menu );
        current = menu;
        
    }

    @Override
    public void actionPerformed( ActionEvent e ) {

        switch ( e.getActionCommand() ) {
            
            case MainMenuPanel.EXIT_COMMAND:
                System.exit( 0 );
                
            case MainMenuPanel.START_COMMAND:
                setWindow( game );
                gameManager.start( "Char 1 Start" );
                break;
            
        }
        
    }
    
    private void setWindow( JPanel panel ) {
        
        program.remove( current );
        program.add( panel );
        current = panel;
        program.revalidate();
        program.repaint();
        
    }
    
    void gameEnd( int endCode ) {
        
        System.out.println( endCode );
        setWindow( menu );
        
    }

}
