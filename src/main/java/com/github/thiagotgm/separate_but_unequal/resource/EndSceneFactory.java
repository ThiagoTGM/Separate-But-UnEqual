package com.github.thiagotgm.separate_but_unequal.resource;

public class EndSceneFactory extends SceneFactory {

    /** Identifier for the "End Code" element. */
    public static final String CODE_ELEMENT = "code";
    
    private static final int DEFAULT_CODE = 0;
    
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
     * If not specified, the default value used is {@value #DEFAULT_CODE}.
     * 
     * @param endCode Ending code to be used.
     * @return The calling instance.
     */
    public EndSceneFactory withCode( int endCode ) {
        
        this.endCode = endCode;
        return this;
        
    }
    
    @Override
    public Resource build() throws IllegalStateException {

        Scene scene = (Scene) super.build();
        return new EndScene( scene, endCode );
        
    }

}
