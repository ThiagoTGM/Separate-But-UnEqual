package com.github.thiagotgm.separate_but_unequal.resource;

import com.github.thiagotgm.separate_but_unequal.resource.Resource.ResourceType;

/**
 * Factory class that creates new Resource instances with given type and elements.<br>
 * All subclasses must call the {@link #ResourceFactory(String) super(String)} constructor
 * to provide the Resource ID.
 *
 * @version 1.2
 * @author Thiago Marback
 * @since 2017-05-23
 */
public abstract class ResourceFactory {
    
    protected static final String INVALID_VALUE_TYPE = "Value given for element '%s' is of the wrong type.";

    protected final String id;
    
    /**
     * Constructs a new factory for the given resource type with a given ID.
     * 
     * @param type Type of the resource to be constructed.
     * @param id ID of the resource to be constructed.
     * @return A factory that constructs a Resource of the given type.
     * @throws UnsupportedOperationException if no factory for the given type is available.
     * @throws NullPointerException if the ID received was null.
     */
    public static ResourceFactory newInstance( ResourceType type, String id )
            throws UnsupportedOperationException, NullPointerException {
        
        if ( id == null ) {
            throw new NullPointerException( "Resource ID cannot be null." );
        }
        switch ( type ) {
            
            case SCENE:
                return new SceneFactory( id );
            case CHOICE_SCENE:
                return new ChoiceSceneFactory( id );
            case END_SCENE:
                return new EndSceneFactory( id );
            case STORY:
                return new StoryFactory( id );
            case ACHIEVEMENT:
                return new AchievementFactory( id );
            default:
                throw new UnsupportedOperationException( "No factory available for the requested resource type." );
            
        }
        
    }
    
    /**
     * Creates a new factory with a given Resource ID.
     * 
     * @param id ID of the resource to be constructed.
     * @throws NullPointerException if the ID given is null.
     */
    protected ResourceFactory( String id ) throws NullPointerException {
        
        if ( id == null ) {
            throw new NullPointerException( "Must specify a non-null ID for the constructed Resource." );
        }
        this.id = id;
        
    }
    
    /**
     * Sets an element of the Resource being constructed, identified by a given string, to a particular value.
     * 
     * @param element Name of the element to be set.
     * @param value Value of the element.
     * @return The calling instance.
     * @throws IllegalArgumentException if the type being constructed does not have an element with this identifier, or
     *                                  if the value is not valid.
     */
    public abstract ResourceFactory withElement( String element, Object value ) throws IllegalArgumentException;
    
    /**
     * Creates the Resource instance being built, using the element values set.
     * 
     * @return The build Resource instance.
     * @throws IllegalStateException if not all required elements were set.
     */
    public abstract Resource build() throws IllegalStateException;
    
}
