package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Class that encapsulates a storyline that can be played through in the game.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-28
 */
public class Story extends Resource implements Comparable<Story> {
    
    public static final char MIN_CODE = 'A';
    public static final char MAX_CODE = 'Z';
    
    public static final String CODE_OOB = "Story code given is out of range.";
    public static final String NULL_NAME = "The name of a Story cannot be null.";
    public static final String NULL_DESCRIPTION = "The description of a Story cannot be null.";
    public static final String NULL_START = "The start of a Story cannot be null.";
    
    private final char code;
    private final String name;
    private final String description;
    private final String start;
    private final String graphic;

    /**
     * Creates a new Story with the given resources.<br>
     * ID, name, description, and start are required and cannot be null, but graphic is optional.<br>
     * Code must be in the range {@value #MIN_CODE} to {@value #MAX_CODE}, inclusive.
     * 
     * @param id Resource ID of this Scene.
     * @param code Code that identifies this storyline.
     * @param name Name of this storyline.
     * @param description Description of this storyline.
     * @param start Starting Scene of this storyline.
     * @param graphic Graphic that represents this storyline.
     * @throws NullPointerException If ID, name, description, or start are null.
     * @throws IllegalArgumentException If the code is not in the acceptable range.
     */
    protected Story( String id, char code, String name, String description, String start, String graphic )
            throws NullPointerException, IllegalArgumentException {
        
        super( id );

        if ( ( code < MIN_CODE ) || ( code > MAX_CODE ) ) {
            throw new IllegalArgumentException( CODE_OOB );
        }
        this.code = code;
        
        if ( name == null ) {
            throw new NullPointerException( NULL_NAME );
        }
        this.name = name;
        
        if ( description == null ) {
            throw new NullPointerException( NULL_DESCRIPTION );
        }
        this.description = description;
        
        if ( start == null ) {
            throw new NullPointerException( NULL_START );
        }
        this.start = description;
        
        this.graphic = graphic;
        
    }
    
    /**
     * Retrieves the code that identifies this storyline.
     * 
     * @return The code of this Story
     */
    public char getCode() {
        
        return code;
        
    }
    
    /**
     * Retrieves the name of this storyline.
     * 
     * @return The name of this Story.
     */
    public String getName() {
        
        return name;
        
    }
    
    /**
     * Retrieves the description of this storyline.
     * 
     * @return The description of this Story.
     */
    public String getDescription() {
        
        return description;
        
    }
    
    /**
     * Retrieves the ID of the initial Scene of this storyline.
     * 
     * @return The ID of the start of this Story.
     */
    public String getStart() {
        
        return start;
        
    }
    
    /**
     * Retrieves the ID of the graphic associated with this storyline.
     * 
     * @return The graphic of this Story.
     */
    public String getGraphic() {
        
        return graphic;
        
    }

    @Override
    public ResourceType getType() {

        return ResourceType.STORY;
        
    }

    @Override
    public int compareTo( Story o ) {

        return this.code - o.code;
        
    }

}
