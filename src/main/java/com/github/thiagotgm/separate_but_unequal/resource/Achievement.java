package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Class that encapsulates a Resource of the Achievment type. Includes the codes of the Story and End that unlock the
 * Achievement, as well as the title and text of the Achievement.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-29
 */
public class Achievement extends Resource {
    
    private static final String NULL_TITLE = "Achievement title cannot be null.";
    private static final String NULL_TEXT = "Achievement text cannot be null.";

    private final char storyCode;
    private final int endCode;
    private final String title;
    private final String text;
    
    /**
     * Instantiates an Achievement with the given ID, story and end codes, title, and text.
     * The ID, title, and text cannot be null, the story code must be in the range {@value Story#MIN_CODE} to
     * {@value Story#MAX_CODE} (inclusive), and the end code must be in the range {@value EndScene#MIN_CODE} to
     * {@value EndScene#MAX_CODE} (inclusive).
     * 
     * @param id The Resource ID of the Achievement.
     * @param storyCode The code of the Story that triggers the Achievement.
     * @param endCode The code of the End that triggers the Achievement.
     * @param title The title of the Achievement.
     * @param text The text/description of the Achievement.
     * @throws NullPointerException if the ID, title, or text are null.
     * @throws IllegalArgumentException if the story or end codes are out of acceptable range.
     */
    protected Achievement( String id, char storyCode, int endCode, String title, String text )
            throws NullPointerException, IllegalArgumentException {
        
        super( id );
        
        if ( ( storyCode < Story.MIN_CODE ) || ( storyCode > Story.MAX_CODE ) ) {
            throw new IllegalArgumentException( Story.CODE_OOB );
        }
        this.storyCode = storyCode;
        
        if ( ( endCode < EndScene.MIN_CODE ) || ( endCode > EndScene.MAX_CODE ) ) {
            throw new IllegalArgumentException( EndScene.CODE_OOB_ERROR );
        }
        this.endCode = endCode;
        
        if ( title == null ) {
            throw new NullPointerException( NULL_TITLE );
        }
        this.title = title;
        
        if ( text == null ) {
            throw new NullPointerException( NULL_TEXT );
        }
        this.text = text;
        
    }
    
    /**
     * Retrieves the code of the Story that triggers this Achievement.
     * 
     * @return The code of the Story.
     */
    public char getStoryCode() {
        
        return storyCode;
        
    }
    
    /**
     * Retrieves the code of the End that triggers this Achievement.
     * 
     * @return The code of the End.
     */
    public int getEndCode() {
        
        return endCode;
        
    }
    
    /**
     * Retrieves the title of this Achievement.
     * 
     * @return The title.
     */
    public String getTitle() {
        
        return title;
        
    }
    
    /**
     * Retrieves the text of this Achievement.
     * 
     * @return The text.
     */
    public String getText() {
        
        return text;
        
    }

    @Override
    public ResourceType getType() {

        return ResourceType.ACHIEVEMENT;
        
    }

}
