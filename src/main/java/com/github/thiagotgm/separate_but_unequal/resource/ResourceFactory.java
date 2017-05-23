package com.github.thiagotgm.separate_but_unequal.resource;

import com.github.thiagotgm.separate_but_unequal.resource.Resource.ResourceType;

/**
 * Factory class that creates new Resource instances with given type and elements.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public abstract class ResourceFactory {

    /**
     * Constructs a new factory for the given resource type.
     * 
     * @param type Type of the resource to be constructed.
     * @return A factory that constructs a Resource of the given type.
     * @throws UnsupportedOperationException if no factory for the given type is available.
     */
    public static ResourceFactory newInstance( ResourceType type ) throws UnsupportedOperationException {
        
        switch ( type ) {
            
            case SCENE:
                return new SceneFactory();
            default:
                throw new UnsupportedOperationException( "No factory available for the requested resource type." );
            
        }
        
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
