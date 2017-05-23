package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Factory that constructs instances of the Scene resource type.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public class SceneFactory extends ResourceFactory {

    /**
     * Creates a new Scene factory with a given Resource ID.
     * 
     * @param id ID of the Scene resource to be constructed.
     */
    protected SceneFactory( String id ) {
        
        super( id );
        
    }
    
    @Override
    public void withElement( String element, Object value ) throws IllegalArgumentException {

        // TODO Auto-generated method stub

    }

    @Override
    public Resource build() throws IllegalStateException {

        if ( id == null ) {
            throw new IllegalStateException( "The Resource ID is required for Resource construction but wasn't specified!" );
        }
        return null;
    }

}
