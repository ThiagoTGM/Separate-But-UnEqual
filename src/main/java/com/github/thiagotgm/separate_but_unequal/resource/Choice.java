package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Class that encapsulates an option within a set of choices.
 *
 * @version 1.0
 * @author ThiagoTGM
 * @since 2017-05-23
 */
public class Choice {
    
    private final String text;
    private final String target;
    
    /**
     * Instantiates a new Choice with a given text and target.
     *
     * @param text Text to be displayed for this choice.
     * @param target ID of the target of this choice.
     */
    public Choice( String text, String target ) {
        
        this.text = text;
        this.target = target;
        
    }
    
    /**
     * Represents this Choice as a String.
     *
     * @return The string that represents this choice, which is the text to be displayed.
     */
    @Override
    public String toString() {
        
        return text;
        
    }
    
    /**
     * Retrieves the target ID of this choice.
     *
     * @return The ID of the target.
     */
    public String getTarget() {
        
        return target;
        
    }

}