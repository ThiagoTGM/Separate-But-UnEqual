package com.github.thiagotgm.separate_but_unequal.resource;

/**
 * Type of scene that marks a possible ending of a storyline.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-27
 */
public class EndScene extends Scene {
    
    public static final String CODE_OOB_ERROR = "End code out of acceptable range.";
    
    public static final int MAX_CODE = 64;
    public static final int MIN_CODE = 1;
    
    private final int endCode;

    /**
     * Creates a new EndScene instance with the given Resource ID.<p>
     * The id, path, and endCode are required, but the other arguments are optional and
     * should be set as null if not used.
     *
     * @param id Resource ID of this Scene object.
     * @param path Path to the text file that contains scene text.
     * @param graphic Graphic to be displayed with the Scene. If null, no graphic is displayed.
     * @param audio Audio to be displayed with the Scene. If null, no audio is displayed.
     * @param endCode Code that represents this ending. Must be in the range {@value #MIN_CODE} to {@value #MAX_CODE}
     *                (inclusive).
     * @throws NullPointerException if the id or filename received was null.
     * @throws IllegalArgumentException if the endCode given is out of the acceptable range.
     */
    protected EndScene( String id, ResourcePath path, String graphic, String audio, int endCode )
            throws NullPointerException {
        
        super( id, path, graphic, audio );
        if ( ( endCode < MIN_CODE ) || ( endCode > MAX_CODE ) ) {
            throw new IllegalArgumentException( CODE_OOB_ERROR );
        }
        this.endCode = endCode;
        
    }
    
    /**
     * Creates a new EndScene from a given, already existing Scene, adding a endcode.
     * 
     * @param scene Base scene.
     * @param endCode Code that represents this ending. Must be in the range {@value #MIN_CODE} to {@value #MAX_CODE}
     *                (inclusive).
     * @throws NullPointerException if the scene received was null.
     * @throws IllegalArgumentException if the endCode given is out of the acceptable range.
     */
    protected EndScene( Scene scene, int endCode ) throws NullPointerException {
        
        super( scene.getID(), scene.getPath(), scene.getGraphic(), scene.getAudio() );
        if ( ( endCode < MIN_CODE ) || ( endCode > MAX_CODE ) ) {
            throw new IllegalArgumentException( CODE_OOB_ERROR );
        }
        this.endCode = endCode;
        
    }
    
    @Override
    public ResourceType getType() {

        return ResourceType.END_SCENE;
        
    }
    
    /**
     * Retrieves the code that identifies this ending.
     * 
     * @return Code of this ending.
     */
    public int getCode() {
        
        return endCode;
        
    }

}
