package com.github.thiagotgm.separate_but_unequal.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Type of scene that allows the user to make a choice, that will define the next Scene to be displayed.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-27
 */
public class ChoiceScene extends Scene {

    private final List<Choice> options;
    
    /**
     * Creates a new ChoiceScene instance with the given Resource ID.<p>
     * The id, path, and option list are required, but the other arguments are optional and
     * should be set as null if not used.<p>
     * Option list must have at least one element.
     *
     * @param id Resource ID of this Scene object.
     * @param path Path to the text file that contains scene text.
     * @param graphic Graphic to be displayed with the Scene. If null, no graphic is displayed.
     * @param audio Audio to be displayed with the Scene. If null, no audio is displayed.
     * @param options List of options that can be chosen in this Scene.
     * @throws NullPointerException if the id, filename, or option list received was null.
     * @throws IllegalArgumentException if the option list received was empty.
     */
    protected ChoiceScene( String id, ResourcePath path, String graphic, String audio, List<Choice> options ) 
            throws NullPointerException, IllegalArgumentException {
        
        super( id, path, graphic, audio );
        
        if ( options == null ) {
            throw new NullPointerException( "The options in a ChoiceScene can't be null." );
        }
        if ( options.isEmpty() ) {
            throw new IllegalArgumentException( "A ChoiceScene must have at least one Choice." );
        }
        
        this.options = Collections.unmodifiableList( new ArrayList<Choice>( options ) );
                
    }
    
    /**
     * Creates a new ChoiceScene from a given, already existing Scene, adding a given list of choices.
     * 
     * @param scene Base scene.
     * @param options List of options that can be chosen in this Scene.
     * @throws NullPointerException if the scene or option list received was null.
     * @throws IllegalArgumentException if the option list received was empty.
     */
    protected ChoiceScene( Scene scene, List<Choice> options ) throws NullPointerException, IllegalArgumentException {
        
        super( scene.getID(), scene.getPath(), scene.getGraphic(), scene.getAudio() );
        if ( options == null ) {
            throw new NullPointerException( "The options in a ChoiceScene can't be null." );
        }
        if ( options.isEmpty() ) {
            throw new IllegalArgumentException( "A ChoiceScene must have at least one Choice." );
        }
        
        this.options = Collections.unmodifiableList( new ArrayList<Choice>( options ) );
        
    }
    
    @Override
    public ResourceType getType() {

        return ResourceType.CHOICE_SCENE;
        
    }
    
    /**
     * Retrieves the list of options that can be picked in this scene.<br>
     * The list is unmodifiable.
     *
     * @return The list of choices in this scene.
     */
    public List<Choice> getOptions() {
    
        return options;
        
    }

}
