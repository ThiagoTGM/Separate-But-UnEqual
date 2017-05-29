package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.github.thiagotgm.separate_but_unequal.MenuManager;

/**
 * Panel that displays text it reads from an InputStream.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-29
 */
public class TextPanel extends ButtonPanel {
    
    /** Serial ID that represents this class. */
    private static final long serialVersionUID = 597334332763617401L;
    private static final double PANEL_PADDING = 0.3;
    private static final double TEXT_MARGIN = 0.1;
    
    private static final double HEIGHT = 6;
    private static final double WIDTH = 6;

    /**
     * Initializes a double-buffered TextPanel that displays the content of a given InputStream.
     * 
     * @param input Stream that the text to be displayed should be read from.
     * @see #TextPanel(boolean,InputStream)
     */
    public TextPanel( InputStream input ) {
        
        this( true, input );
        
    }

    /**
     * Initializes a MainMenuPanel with a specified buffering strategy that displays the content of a given InputStream.
     * 
     * @param isDoubleBuffered A boolean, true for double-buffering, which uses additional memory space to achieve fast,
     *                         flicker-free updates
     * @param input Stream that the text to be displayed should be read from.
     * @see JPanel#JPanel(boolean)
     */
    public TextPanel( boolean isDoubleBuffered, InputStream input ) {
        
        super( isDoubleBuffered );
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        
        ActionListener listener = new ListenerAggregator( listeners );
        
        /* Obtain text to display. */
        Scanner scan = new Scanner( input );
        scan.useDelimiter( "\\A" );
        String content = ( scan.hasNext() ) ? scan.next() : "";
        scan.close();
        
        /* Create text display panel */
        JTextArea text = new JTextArea( content );
        Scalable.scaleFont( text );
        text.setEditable( false );
        text.setLineWrap( true );
        int margin = Scalable.scaleToInt( TEXT_MARGIN );
        text.setMargin( new Insets( margin, margin, margin, margin ) );
        
        JScrollPane scroll = new JScrollPane( text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, // Scrollbar in case of
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );                                     // long texts.
        
        Border border = BorderFactory.createBevelBorder( BevelBorder.RAISED, Color.BLACK, Color.GRAY );
        scroll.setBorder( border ); // Make a border for the text.
        scroll.setPreferredSize( Scalable.scale( WIDTH, HEIGHT ) );
        
        add( scroll ); // Add text+scroll to panel.
        
        /* Add button to go back to menu */
        add( Box.createVerticalGlue() );
        add( Box.createRigidArea( Scalable.scale( 0, PANEL_PADDING ) ) );
        
        JButton backButton = new JButton( "Back" );
        backButton.setActionCommand( MenuManager.BACK_COMMAND );
        Scalable.scaleFont( backButton );
        backButton.addActionListener( listener );
        backButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        add( backButton );
        
        int panelBorderSize = Scalable.scaleToInt( PANEL_PADDING ); // Add border padding.
        Border panelBorder = BorderFactory.createEmptyBorder( panelBorderSize, panelBorderSize, panelBorderSize,
                panelBorderSize );
        setBorder( panelBorder );

    }

}
