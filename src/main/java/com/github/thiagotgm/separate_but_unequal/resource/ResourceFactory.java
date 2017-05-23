package com.github.thiagotgm.separate_but_unequal.resource;

import com.github.thiagotgm.separate_but_unequal.resource.Resource.ResourceType;

/**
 * Factory class that creates new Resource instances with given type and elements.<br>
 * All subclasses must call the {@link #ResourceFactory(String) super(String)} constructor to provide the Resource ID.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public abstract class ResourceFactory {

    protected final String id;
    
    /**
     * Constructs a new factory for the given resource type with a given ID.
     * 
     * @param type Type of the resource to be constructed.
     * @param id ID of the resource to be constructed.
     * @return A factory that constructs a Resource of the given type.
     * @throws UnsupportedOperationException if no factory for the given type is available.
     */
    public static ResourceFactory newInstance( ResourceType type, String id ) throws UnsupportedOperationException {
        
        switch ( type ) {
            
            case SCENE:
                return new SceneFactory( id );
            default:
                throw new UnsupportedOperationException( "No factory available for the requested resource type." );
            
        }
        
    }
    
    /**
     * Creates a new factory with a given Resource ID.
     * 
     * @param id ID of the resource to be constructed.
     */
    protected ResourceFactory( String id ) {
        
        this.id = id;
        
    }
    
    /**
     * Sets an element of the Resource being constructed, identified by a given string, to a particular value.
     * 
     * @param element Name of the element to be set.
     * @param value Value of the element.
     * @throws IllegalArgumentException if the type being constructed does not have an element with this identifier, or
     *                                  if the value is not valid.
     */
    public abstract void withElement( String element, Object value ) throws IllegalArgumentException;
    
    /**
     * Creates the Resource instance being built, using the element values set.
     * 
     * @return The build Resource instance.
     * @throws IllegalStateException if not all required elements were set.
     */
    public abstract Resource build() throws IllegalStateException;
    
}
