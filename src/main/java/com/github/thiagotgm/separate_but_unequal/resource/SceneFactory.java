package com.github.thiagotgm.separate_but_unequal.resource;

import java.util.List;

/**
 * Factory that constructs instances of the Scene resource type.
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
    public void withElement( String element, Object value ) throws IllegalArgumentException {

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

    }
    
    /**
     * Specifies the filename to be used in the built Scene.
     *
     * @param filename Filename of the Scene text.
     */
    public void withFilename( String filename ) {
        
        this.filename = filename;
        
    }
    
    /**
     * Specifies the transition to be used before the built Scene.<br>
     * Optional.
     *
     * @param transition Filename of the Scene transition.
     */
    public void withTransition( String transition ) {
        
        this.transition = transition;
        
    }
    
    /**
     * Specifies the graphic to be used in the built Scene.<br>
     * Optional.
     *
     * @param graphic Filename of the Scene graphic.
     */
    public void withGraphic( String graphic ) {
        
        this.graphic = graphic;
        
    }
    
    /**
     * Specifies the audio to be used in the built Scene.<br>
     * Optional.
     *
     * @param audio Filename of the Scene audio.
     */
    public void withAudio( String audio ) {
        
        this.audio = audio;
        
    }
    
    /**
     * Specifies the list of player choices to be used in the built Scene.
     *
     * @param options List of choices to be in the Scene.
     */
    public void withOptions( List<Choice> options ) {
        
        this.options = options;
        
    }

    @Override
    public Resource build() throws IllegalStateException {

        if ( id == null ) {
            throw new IllegalStateException( "The Resource ID is required for Resource construction but wasn't specified!" );
        }
        if ( filename == null ) {
            throw new IllegalStateException( "The filename is required for Scene construction but wasn't specified!" );
        }
        if ( options == null ) {
            throw new IllegalStateException( "The option list is required for Scene construction but wasn't specified!" );
        }
        return new Scene( id, filename, transition, graphic, audio, options );
        
    }

}
