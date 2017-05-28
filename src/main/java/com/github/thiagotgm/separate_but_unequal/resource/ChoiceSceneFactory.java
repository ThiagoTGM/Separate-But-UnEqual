package com.github.thiagotgm.separate_but_unequal.resource;

import java.util.List;

/**
 * Factory that constructs instances of the ChoiceScene resource type, a subtype of the Scene type.<br>
 * Beyond the requirements of a SceneFactory, this also requires a non-empty {@link #withOptions(List) option list}.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public class ChoiceSceneFactory extends SceneFactory {

    /** Identifier for the "Options" element. */
    public static final String OPTIONS_ELEMENT = "options";
    
    private List<Choice> options;
    
    /**
     * Creates a new ChoiceScene factory with a given Resource ID.
     * 
     * @param id ID of the Scene resource to be constructed.
     * @throws NullPointerException if the ID given is null.
     */
    protected ChoiceSceneFactory( String id ) {
        
        super( id );
        
    }
    
    @SuppressWarnings( "unchecked" )
    @Override
    public ResourceFactory withElement( String element, Object value ) throws IllegalArgumentException {

        try {
            switch( element ) { // Identifies the element to insert.
                
                case OPTIONS_ELEMENT:
                    withOptions( (List<Choice>) value );
                    break;
                default:
                    super.withElement( element, value ); // Checks if superclass has that element.
                
            }
        } catch ( ClassCastException e ) {
            throw new IllegalArgumentException( "Value given for element '" + element +
                    "' is of the wrong type.", e );
        }
        return this;

    }
    
    /**
     * Specifies the list of player choices to be used in the built Scene.
     *
     * @param options List of choices to be in the Scene.
     * @return The calling instance.
     */
    public ChoiceSceneFactory withOptions( List<Choice> options ) {
        
        this.options = options;
        return this;
        
    }
    
    @Override
    public Resource build() throws IllegalStateException {

        Scene scene = (Scene) super.build();
        if ( options == null ) {
            throw new IllegalStateException( "The option list is required for ChoiceScene construction but wasn't specified!" );
        }
        if ( options.isEmpty() ) {
            throw new IllegalStateException( "The option list must have at least one Choice!" );
        }
        return new ChoiceScene( scene, options );
        
    }

}
