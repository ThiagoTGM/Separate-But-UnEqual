package com.github.thiagotgm.separate_but_unequal.resource;

public class EndScene extends Scene {
    
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
     * @param endCode Code that represents this ending.
     * @throws NullPointerException if the id or filename received was null.
     */
    protected EndScene( String id, ResourcePath path, String graphic, String audio, int endCode )
            throws NullPointerException {
        
        super( id, path, graphic, audio );
        this.endCode = endCode;
        
    }
    
    /**
     * Creates a new EndScene from a given, already existing Scene, adding a endcode.
     * 
     * @param scene Base scene.
     * @param endCode Code that represents this ending.
     * @throws NullPointerException if the scene received was null.
     */
    protected EndScene( Scene scene, int endCode ) throws NullPointerException {
        
        super( scene.getID(), scene.getPath(), scene.getGraphic(), scene.getAudio() );
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
