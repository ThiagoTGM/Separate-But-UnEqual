package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Class that represents a single scene in the game story.
 *
 * @version 2.0
 * @author ThiagoTGM
 * @since 2017-05-23
 */
public class Scene extends Resource {

    private final ResourcePath path;
    private final String graphic;
    private final String audio;
    
    /**
     * Creates a new Scene instance with the given Resource ID.<p>
     * The id and path are required, but the other arguments are optional and
     * should be set as null if not used.
     *
     * @param id Resource ID of this Scene object.
     * @param path Path to the text file that contains scene text.
     * @param graphic Graphic to be displayed with the Scene. If null, no graphic is displayed.
     * @param audio Audio to be displayed with the Scene. If null, no audio is displayed.
     * @throws NullPointerException if the id or filename received was null.
     */
    protected Scene( String id, ResourcePath path, String graphic, String audio ) throws NullPointerException {
        
        super( id );
        
        if ( path == null ) {
            throw new NullPointerException( "The path in a Scene can't be null." );
        }

        this.path = path;
        this.graphic = graphic;
        this.audio = audio;
        
    }
    
    /**
     * Retrieves the filename of the text of this Scene.
     *
     * @return The filename of the scene text.
     */
    public ResourcePath getPath() {
        
        return path;
        
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

    @Override
    public ResourceType getType() {

        return ResourceType.SCENE;
        
    }

}
