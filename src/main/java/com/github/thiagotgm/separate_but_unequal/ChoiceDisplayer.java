package com.github.thiagotgm.separate_but_unequal;

import java.util.List;

import javax.swing.JTextArea;

import com.github.thiagotgm.separate_but_unequal.resource.Choice;

/**
 * Manages the displaying of a list of choices (and what the currently selected one
 * is) to a TextArea.
 *
 * @version 1.0
 * @author ThiagoTGM
 * @since 2017-05-27
 */
public class ChoiceDisplayer {
    
    private static final char SELECTOR = '>';
    
    private final JTextArea optionDisplay;
    private List<Choice> options;
    private int current;

    /**
     * Intializes a new Displayer that displays a list of options onto a given text area.
     *
     * @param optionDisplay TextArea to display the options on.
     */
    public ChoiceDisplayer( JTextArea optionDisplay ) {

        this.optionDisplay = optionDisplay;
        
    }
    
    /**
     * Displays a list of options.<br>
     * The first option will be selected by default.
     *
     * @param options List of options to be displayed.
     */
    public void showOptions( List<Choice> options ) {
        
        this.options = options;
        setSelected( 0 );
        
    }
    
    /**
     * Sets the currently selected option and updates the display.
     *
     * @param selected Index of the option to be selected.
     */
    public void setSelected( int selected ) {
        
        this.current = selected;
        StringBuilder builder = new StringBuilder();
        for ( int i = 0; i < options.size(); i++ ) {
            
            if ( i != 0 ) { // If not first option, add a line of padding on top.
                builder.append( '\n' );
            }
            builder.append( ( i == selected ) ? " " + SELECTOR + " " : "   " ); // Current option has a selector
            builder.append( options.get( i ) );                                 // indicating it.
            builder.append( '\n' );
            
        }
        optionDisplay.setText( builder.toString() );
        
    }
    
    /**
     * Clears the text display area.
     */
    public void clear() {
        
        optionDisplay.setText( null );
        
    }
    
    /**
     * Retrieves the currently selected option.
     *
     * @return The index of the currently selected option.
     */
    public int getSelected() {
        
        return current;
        
    }

}
