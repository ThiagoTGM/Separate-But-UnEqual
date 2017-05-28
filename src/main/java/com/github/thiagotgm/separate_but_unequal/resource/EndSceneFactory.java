package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Factory that constructs instances of the EndScene resource type, a subtype of the Scene type.<br>
 * Beyond the requirements of a SceneFactory, this also requires a {@link #withCode(int) ending code} in the
 * appropriate range.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public class EndSceneFactory extends SceneFactory {

    /** Identifier for the "End Code" element. */
    public static final String CODE_ELEMENT = "code";
    
    private static final int DEFAULT_CODE = 0; // Out of range - marks unset code.
    
    private int endCode;
    
    /**
     * Creates a new EndScene factory with a given Resource ID.
     * 
     * @param id ID of the Scene resource to be constructed.
     * @throws NullPointerException if the ID given is null.
     */
    protected EndSceneFactory( String id ) throws NullPointerException {
        
        super( id );
        endCode = DEFAULT_CODE;

    }
    
    @Override
    public ResourceFactory withElement( String element, Object value ) throws IllegalArgumentException {

        try {
            switch( element ) { // Identifies the element to insert.
                
                case CODE_ELEMENT:
                    withCode( (int) value );
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
     * Specifies the ending code to be used in the constructed EndScene object.<p>
     * Code must be in the range {@value EndScene#MIN_CODE} to {@value EndScene#MAX_CODE}, inclusive.
     * 
     * @param endCode Ending code to be used.
     * @return The calling instance.
     * @throws IllegalArgumentException if the code is out of the acceptable range.
     */
    public EndSceneFactory withCode( int endCode ) throws IllegalArgumentException {
        
        if ( ( endCode < EndScene.MIN_CODE ) || ( endCode > EndScene.MAX_CODE ) ) {
            throw new IllegalArgumentException( EndScene.CODE_OOB_ERROR );
        }
        this.endCode = endCode;
        return this;
        
    }
    
    @Override
    public Resource build() throws IllegalStateException {

        Scene scene = (Scene) super.build();
        if ( endCode == DEFAULT_CODE ) {
            throw new IllegalStateException( "A valid End Code is required to build a EndScene instance." );
        }
        return new EndScene( scene, endCode );
        
    }

}
