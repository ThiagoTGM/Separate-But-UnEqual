package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Factory that constructs instances of the Scene resource type.<br>
 * The {@link #withPath(ResourcePath) path} is required, but other elements are optional.
 *
 * @version 2.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public class SceneFactory extends ResourceFactory {
    
    /** Identifier for the "Path" element. */
    public static final String PATH_ELEMENT = "path";
    /** Identifier for the "Graphic" element. */
    public static final String GRAPHIC_ELEMENT = "graphic";
    /** Identifier for the "Audio" element. */
    public static final String AUDIO_ELEMENT = "audio";

    private ResourcePath path;
    private String graphic;
    private String audio;
    
    /**
     * Creates a new Scene factory with a given Resource ID.
     * 
     * @param id ID of the Scene resource to be constructed.
     * @throws NullPointerException if the ID given is null.
     */
    protected SceneFactory( String id ) throws NullPointerException {
        
        super( id );
        
    }
    
    @Override
    public ResourceFactory withElement( String element, Object value ) throws IllegalArgumentException {

        try {
            switch( element ) { // Identifies the element to insert.
                
                case PATH_ELEMENT:
                    withPath( (ResourcePath) value );
                    break;
                case GRAPHIC_ELEMENT:
                    withGraphic( (String) value );
                    break;
                case AUDIO_ELEMENT:
                    withAudio( (String) value );
                    break;
                default:
                    throw new IllegalArgumentException( "Scene does not have element '"
                            + element + "'." );
                
            }
        } catch ( ClassCastException e ) {
            throw new IllegalArgumentException( "Value given for element '" + element +
                    "' is of the wrong type.", e );
        }
        return this;

    }
    
    /**
     * Specifies the path of the file to be used in the built Scene.
     *
     * @param path Path of the Scene text file.
     */
    public SceneFactory withPath( ResourcePath path ) {
        
        this.path = path;
        return this;
        
    }
    
    /**
     * Specifies the graphic to be used in the built Scene.<br>
     * Optional.
     *
     * @param graphic Filename of the Scene graphic.
     */
    public SceneFactory withGraphic( String graphic ) {
        
        this.graphic = graphic;
        return this;
        
    }
    
    /**
     * Specifies the audio to be used in the built Scene.<br>
     * Optional.
     *
     * @param audio Filename of the Scene audio.
     */
    public SceneFactory withAudio( String audio ) {
        
        this.audio = audio;
        return this;
        
    }

    @Override
    public Resource build() throws IllegalStateException {

        if ( path == null ) {
            throw new IllegalStateException( "The path is required for Scene construction but wasn't specified!" );
        }
        return new Scene( id, path, graphic, audio );
        
    }

}
