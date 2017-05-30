package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Class that represents any game resource. Specific types of resources should be subtyped from this with their
 * specific functionality.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public abstract class Resource {
    
    /** Enums used to identify what kind of resource a subclass represents. */
    public enum ResourceType { 
        
        /**
         * Not a resource. Used mostly for error testing.
         */
        NONE,
        
        /**
         * Single scene in the story. Includes text of the scene and identifies other resources.
         */
        SCENE,
        
        /**
         * Single scene in the story. Includes text of the scene, identifies other resources, and specifies
         * branch options.
         */
        CHOICE_SCENE,
        
        /**
         * Ending scene in the story. Includes text of the scene, identifies other resources, and specifies end code.
         */
        END_SCENE,
        
        /**
         * A single storyline in the game. Has the name of the stoy, gives a description, and identifies the starting
         * scene of the story and other resources.
         */
        STORY,
        
        /**
         * An achievement that represents that the user encountered one of the endings in the game.
         */
        ACHIEVEMENT
        
    };
    
    private final String id;
    
    /**
     * Creates a new Resource object with a given ID.<p>
     * The ID cannot be null.
     *
     * @param id ID of the resource.
     * @throws NullPointerException if the ID received is null.
     */
    protected Resource( String id ) throws NullPointerException {
        
        if ( id == null ) {
            throw new NullPointerException( "Resource ID cannot be null." );
        }
        this.id = id;
        
    }
    
    /**
     * Retrieves the ID of the resource.
     * 
     * @return The ID of the resource.
     */
    public String getID() {
        
        return id;
        
    }
    
    /**
     * Retrieves the specific resource type represented by this Resource object.
     * 
     * @return The specific type of this resource.
     */
    public abstract ResourceType getType();

}
