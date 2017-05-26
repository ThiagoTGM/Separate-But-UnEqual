package com.github.thiagotgm.separate_but_unequal;

import java.util.List;

import javax.swing.JTextArea;

import com.github.thiagotgm.separate_but_unequal.resource.Choice;

public class ChoiceDisplayer {
    
    private static final char SELECTOR = '>';
    
    private final JTextArea optionDisplay;
    private List<Choice> options;
    private int current;

    public ChoiceDisplayer( JTextArea optionDisplay ) {

        this.optionDisplay = optionDisplay;
        
    }
    
    public void showOptions( List<Choice> options ) {
        
        this.options = options;
        setSelected( 0 );
        
    }
    
    public void setSelected( int selected ) {
        
        this.current = selected;
        StringBuilder builder = new StringBuilder();
        for ( int i = 0; i < options.size(); i++ ) {
            
            if ( i != 0 ) { // If not first option, add a line of padding on top.
                builder.append( '\n' );
            }
            builder.append( ( i == selected ) ? " " + SELECTOR + " " : "   " );
            builder.append( options.get( i ) );
            builder.append( '\n' );
            
        }
        optionDisplay.setText( builder.toString() );
        
    }
    
    public void clear() {
        
        optionDisplay.setText( null );
        
    }
    
    public int getSelected() {
        
        return current;
        
    }

}
