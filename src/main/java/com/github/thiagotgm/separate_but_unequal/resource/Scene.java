package com.github.thiagotgm.separate_but_unequal.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represents a single scene in the game story.
 *
 * @version 1.0
 * @author ThiagoTGM
 * @since 2017-05-23
 */
public class Scene extends Resource {

    private final String filename;
    private final String transition;
    private final String graphic;
    private final String audio;
    private final List<Choice> options;
    
    /**
     * Creates a new Scene instance with the given Resource ID.<p>
     * The id, filename, and options are required, but the other arguments are optional and
     * should be set as null if not used.<p>
     * The option list must have at least one Choice.
     *
     * @param id Resource ID of this Scene object.
     * @throws NullPointerException if the filename or option list received was null.
     * @throws IllegalArgumentException if the option list received was empty.
     */
    protected Scene( String id, String filename, String transition, String graphic,
            String audio, List<Choice> options ) {
        
        super( id );
        
        if ( ( filename == null ) || ( options == null ) ) {
            throw new NullPointerException( "The filename and options in a Scene " +
                    "can't be null." );
        }
        if ( options.isEmpty() ) {
            throw new IllegalArgumentException( "A Scene must have at least one Choice." );
        }
        
        this.filename = filename;
        this.transition = transition;
        this.graphic = graphic;
        this.audio = audio;
        this.options = Collections.unmodifiableList( new ArrayList<Choice>( options ) );
        
    }
    
    @Override
    public ResourceType getType() {

        return ResourceType.SCENE;
        
    }
    
    /**
     * Retrieves the filename of the text of this Scene.
     *
     * @return The filename of the scene text.
     */
    public String getFilename() {
        
        return filename;
        
    }

    /**
     * Retrieves the ID of the transition to be displayed before this scene, if any.
     *
     * @return The ID of the transition, or null if none.
     */
    public String getTransition() {
    
        return transition;
        
    }

    /**
     * Retrieves the ID of the graphic to be displayed with this scene, if any.
     *
     * @return The ID of the graphic, or null if none.
     */
    public String getGraphic() {
    
        return graphic;
        
    }

    /**
     * Retrieves the ID of the audio to be played with this scene, if any.
     *
     * @return The ID of the audio, or null if none.
     */
    public String getAudio() {
    
        return audio;
        
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
