package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Factory that constructs instances of the Story resource type.<br>
 * The  is required, but other elements are optional.
 *
 * @version 2.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public class StoryFactory extends ResourceFactory {
    
    /** Identifier for the "Code" element. */
    public static final String CODE_ELEMENT = "code";
    /** Identifier for the "Name" element. */
    public static final String NAME_ELEMENT = "name";
    /** Identifier for the "Description" element. */
    public static final String DESCRIPTION_ELEMENT = "description";
    /** Identifier for the "Start" element. */
    public static final String START_ELEMENT = "start";
    /** Identifier for the "Graphic" element. */
    public static final String GRAPHIC_ELEMENT = "graphic";
    
    private static final char DEFAULT_CODE = '\0';
    
    private char code;
    private String name;
    private String description;
    private String start;
    private String graphic;

    /**
     * Creates a new Story factory with a given Resource ID.
     * 
     * @param id ID of the Story resource to be constructed.
     * @throws NullPointerException if the ID given is null.
     */
    protected StoryFactory( String id ) throws NullPointerException {
        
        super( id );
        code = DEFAULT_CODE;

    }

    @Override
    public ResourceFactory withElement( String element, Object value ) throws IllegalArgumentException {

        try {
            switch ( element ) { // Identifies the element to insert.
                
                case CODE_ELEMENT:
                    withCode( (char) value );
                    break; 
                case NAME_ELEMENT:
                    withName( (String) value );
                    break;                   
                case DESCRIPTION_ELEMENT:
                    withDescription( (String) value );
                    break;                    
                case START_ELEMENT:
                    withStart( (String) value );
                    break;                   
                case GRAPHIC_ELEMENT:
                    withGraphic( (String) value );
                    break;                 
                default:
                    throw new IllegalArgumentException( "Scene does not have element '"
                            + element + "'." );
                
            }
        } catch ( ClassCastException e ) {
            throw new IllegalArgumentException( String.format( ResourceFactory.INVALID_VALUE_TYPE, element ), e );
        }
        return this;
        
    }
    
    /**
     * Specifies the story code to be used in the constructed Story object.<p>
     * Code must be in the range {@value Story#MIN_CODE} to {@value Story#MAX_CODE}, inclusive.
     * 
     * @param code Story code to be used.
     * @return The calling instance.
     * @throws IllegalArgumentException if the code is out of the acceptable range.
     */
    public StoryFactory withCode( char code ) {
        
        if ( ( code < Story.MIN_CODE ) || ( code > Story.MAX_CODE ) ) {
            throw new IllegalArgumentException( Story.CODE_OOB );
        }
        this.code = code;
        return this;
        
    }
    
    /**
     * Specifies the name to be used in the built Scene.
     *
     * @param name Name of the storyline.
     * @return The calling instance.
     */
    public StoryFactory withName( String name ) {
        
        this.name = name;
        return this;
        
    }
    
    /**
     * Specifies the description to be used in the built Scene.
     *
     * @param description Description of the storyline.
     * @return The calling instance.
     */
    public StoryFactory withDescription( String description ) {
        
        this.description = description;
        return this;
        
    }
    
    /**
     * Specifies the start ID to be used in the built Scene.
     *
     * @param description Resource ID of the first Scene in the storyline.
     * @return The calling instance.
     */
    public StoryFactory withStart( String start ) {
        
        this.start = start;
        return this;
        
    }
    
    /**
     * Specifies the graphic to be used in the built Story.<br>
     * Optional.
     *
     * @param graphic Filename of the Scene story.
     * @return The calling instance.
     */
    public StoryFactory withGraphic( String graphic ) {
        
        this.graphic = graphic;
        return this;
        
    }

    @Override
    public Resource build() throws IllegalStateException {

        if ( code == DEFAULT_CODE ) {
            throw new IllegalStateException( "A valid Story Code is required to build a Story instance." );
        }
        if ( name == null ) {
            throw new IllegalStateException( "Name is required for Story construction but wasn't specified!" );
        }
        if ( description == null ) {
            throw new IllegalStateException( "Description is required for Story construction but wasn't specified!" );
        }
        if ( start == null ) {
            throw new IllegalStateException( "Start is required for Story construction but wasn't specified!" );
        }
        
        return new Story( id, code, name, description, start, graphic );
        
    }

}
