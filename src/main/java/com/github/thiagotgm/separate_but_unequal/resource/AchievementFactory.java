package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Factory that constructs instances of the Achievement resource type.<br>
 * All elements except the graphic are required.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-29
 */
public class AchievementFactory extends ResourceFactory {
    
    /** Identifier for the "Story Code" element. */
    public static final String STORY_CODE_ELEMENT = "storyCode";
    /** Identifier for the "End Code" element. */
    public static final String END_CODE_ELEMENT = "endCode";
    /** Identifier for the "Title" element. */
    public static final String TITLE_ELEMENT = "title";
    /** Identifier for the "Text" element. */
    public static final String TEXT_ELEMENT = "text";
    /** Identifier for the "Graphic" element. */
    public static final String GRAPHIC_ELEMENT = "graphic";
    
    private static final char DEFAULT_STORY_CODE = '\0';
    private static final int DEFAULT_END_CODE = 0;
    
    private char storyCode;
    private int endCode;
    private String title;
    private String text;
    private String graphic;

    /**
     * Creates a new Achievement factory with a given Resource ID.
     * 
     * @param id ID of the Achievement resource to be constructed.
     * @throws NullPointerException if the ID given is null.
     */
    protected AchievementFactory( String id ) throws NullPointerException {
        
        super( id );
        this.storyCode = DEFAULT_STORY_CODE;
        this.endCode = DEFAULT_END_CODE;

    }

    @Override
    public ResourceFactory withElement( String element, Object value ) throws IllegalArgumentException {

        try {
            switch ( element ) { // Identifies the element to insert.
                
                case STORY_CODE_ELEMENT:
                    withStoryCode( (char) value );
                    break;
                case END_CODE_ELEMENT:
                    withEndCode( (int) value );
                    break;
                case TITLE_ELEMENT:
                    withTitle( (String) value );
                    break;
                case TEXT_ELEMENT:
                    withText( (String) value );
                    break;
                case GRAPHIC_ELEMENT:
                    withGraphic( (String) value );
                    break;
                default:
                    throw new IllegalArgumentException( "Achievement does not have element '"
                            + element + "'." );
                
            }
        } catch ( ClassCastException e ) {
            throw new IllegalArgumentException( String.format( ResourceFactory.INVALID_VALUE_TYPE, element ), e );
        }
        return this;

    }
    
    /**
     * Specifies the story code to be used in the constructed Achievement object.<p>
     * Code must be in the range {@value Story#MIN_CODE} to {@value Story#MAX_CODE}, inclusive.
     * 
     * @param storyCode Story code to be used.
     * @return The calling instance.
     * @throws IllegalArgumentException if the code is out of the acceptable range.
     */
    public AchievementFactory withStoryCode( char storyCode ) throws IllegalArgumentException  {
        
        if ( ( storyCode < Story.MIN_CODE ) || ( storyCode > Story.MAX_CODE ) ) {
            throw new IllegalArgumentException( Story.CODE_OOB );
        }
        this.storyCode = storyCode;
        return this;
        
    }
    
    /**
     * Specifies the end code to be used in the constructed Achievement object.<p>
     * Code must be in the range {@value EndScene#MIN_CODE} to {@value EndScene#MAX_CODE}, inclusive.
     * 
     * @param endCode End code to be used.
     * @return The calling instance.
     * @throws IllegalArgumentException if the code is out of the acceptable range.
     */
    public AchievementFactory withEndCode( int endCode ) throws IllegalArgumentException  {
        
        if ( ( endCode < EndScene.MIN_CODE ) || ( endCode > EndScene.MAX_CODE ) ) {
            throw new IllegalArgumentException( EndScene.CODE_OOB_ERROR );
        }
        this.endCode = endCode;
        return this;
        
    }
    
    /**
     * Specifies the title to be used for the constructed Achievement object.
     * 
     * @param title Title to be used.
     * @return The calling instance.
     */
    public AchievementFactory withTitle( String title ) {
        
        this.title = title;
        return this;
        
    }
    
    /**
     * Specifies the text to be used for the constructed Achievement object.
     * 
     * @param text Text to be used.
     * @return The calling instance.
     */
    public AchievementFactory withText( String text ) {
        
        this.text = text;
        return this;
        
    }
    
    /**
     * Specifies the Resource ID of the Graphic to be associated with the constructed Achievement object.
     * 
     * @param graphic The ID of the graphic.
     * @return The calling instance.
     */
    public AchievementFactory withGraphic( String graphic ) {
        
        this.graphic = graphic;
        return this;
        
    }

    @Override
    public Resource build() throws IllegalStateException {

        if ( storyCode == DEFAULT_STORY_CODE ) {
            throw new IllegalStateException( "A valid Story Code is required to build an Achivement instance." );
        }
        if ( endCode == DEFAULT_END_CODE ) {
            throw new IllegalStateException( "A valid End Code is required to build an Achivement instance." );
        }
        if ( title == null ) {
            throw new IllegalStateException( "Title is required for Achivement construction but wasn't specified!" );
        }
        if ( text == null ) {
            throw new IllegalStateException( "Text is required for Achivement construction but wasn't specified!" );
        }
        
        return new Achievement( id, storyCode, endCode, title, text, graphic );
        
    }

}
