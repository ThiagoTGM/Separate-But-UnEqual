package com.github.thiagotgm.separate_but_unequal.resource;

import java.util.List;

/**
 * Factory that constructs instances of the Scene resource type.<br>
 * The {@link #withFilename(String) filename} and a non-empty {@link #withOptions(List) option list} are required,
 * but other elements are optional.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public class SceneFactory extends ResourceFactory {

    private String filename;
    private String transition;
    private String graphic;
    private String audio;
    private List<Choice> options;
    
    /**
     * Creates a new Scene factory with a given Resource ID.
     * 
     * @param id ID of the Scene resource to be constructed.
     */
    protected SceneFactory( String id ) {
        
        super( id );
        
    }
    
    @SuppressWarnings( "unchecked" )
    @Override
    public ResourceFactory withElement( String element, Object value ) throws IllegalArgumentException {

        try {
            switch( element ) { // Identifies the element to insert.
                
                case "filename":
                    withFilename( (String) value );
                    break;
                case "transition":
                    withTransition( (String) value );
                    break;
                case "graphic":
                    withGraphic( (String) value );
                    break;
                case "audio":
                    withAudio( (String) value );
                    break;
                case "options":
                    withOptions( (List<Choice>) value );
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
     * Specifies the filename to be used in the built Scene.
     *
     * @param filename Filename of the Scene text.
     */
    public SceneFactory withFilename( String filename ) {
        
        this.filename = filename;
        return this;
        
    }
    
    /**
     * Specifies the transition to be used before the built Scene.<br>
     * Optional.
     *
     * @param transition Filename of the Scene transition.
     */
    public SceneFactory withTransition( String transition ) {
        
        this.transition = transition;
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
    
    /**
     * Specifies the list of player choices to be used in the built Scene.
     *
     * @param options List of choices to be in the Scene.
     */
    public SceneFactory withOptions( List<Choice> options ) {
        
        this.options = options;
        return this;
        
    }

    @Override
    public Resource build() throws IllegalStateException {

        if ( filename == null ) {
            throw new IllegalStateException( "The filename is required for Scene construction but wasn't specified!" );
        }
        if ( options == null ) {
            throw new IllegalStateException( "The option list is required for Scene construction but wasn't specified!" );
        }
        if ( options.isEmpty() ) {
            throw new IllegalStateException( "The option list must have at least one Choice!" );
        }
        return new Scene( id, filename, transition, graphic, audio, options );
        
    }

}
