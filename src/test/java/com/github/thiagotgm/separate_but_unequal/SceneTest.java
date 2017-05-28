package com.github.thiagotgm.separate_but_unequal;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;

import com.github.thiagotgm.separate_but_unequal.resource.Resource;
import com.github.thiagotgm.separate_but_unequal.resource.Resource.ResourceType;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceFactory;
import com.github.thiagotgm.separate_but_unequal.resource.ResourcePath;
import com.github.thiagotgm.separate_but_unequal.resource.Scene;
import com.github.thiagotgm.separate_but_unequal.resource.SceneFactory;


public class SceneTest {

    private static final Path NONE_PATH = Paths.get( "res", "none.txt" );
    private static final Path ALL_PATH = Paths.get( "res", "none.txt" );
    private static final Path GRAPHIC_PATH = Paths.get( "res", "none.txt" );
    private static final Path AUDIO_PATH = Paths.get( "res", "none.txt" );
    private static final Path ALT_PATH = Paths.get( "res", "alt.txt" );
    
    private Scene noOpts;
    private Scene allOpts;
    private Scene wGraphic;
    private Scene wAudio;
    private Scene alt;
    
    @Before
    public void setUp() throws Exception {
        
        SceneFactory factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "No Opts" );
        noOpts = (Scene) factory.withPath( new ResourcePath( NONE_PATH, false ) ).build();
        factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "All Opts" );
        allOpts = (Scene) factory.withPath( new ResourcePath( ALL_PATH, false ) ).withGraphic( "graphic" )
                .withAudio( "audio" ).build();
        factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "With Graphic" );
        wGraphic = (Scene) factory.withPath( new ResourcePath( GRAPHIC_PATH, false ) ).withGraphic( "graphic" ).build();
        factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "With Audio" );
        wAudio = (Scene) factory.withPath( new ResourcePath( AUDIO_PATH, false ) ).withAudio( "audio" ).build();
        
        /* Builds a scene using superclass method. */
        ResourceFactory superFactory = ResourceFactory.newInstance( ResourceType.SCENE, "Alternate" );
        superFactory.withElement( "path", new ResourcePath( ALT_PATH, false ) );
        superFactory.withElement( "graphic", "graphicID" );
        superFactory.withElement( "audio", "audioID" );
        alt = (Scene) superFactory.build();
        
    }

    @Test
    public void testID() {

        assertEquals( "Incorrect ID.", "No Opts", noOpts.getID() );
        assertEquals( "Incorrect ID.", "All Opts", allOpts.getID() );
        assertEquals( "Incorrect ID.", "With Graphic", wGraphic.getID() );
        assertEquals( "Incorrect ID.", "With Audio", wAudio.getID() );
        assertEquals( "Incorrect ID.", "Alternate", alt.getID() );
        
    }
    
    @Test
    public void testPath() {
        
        assertEquals( "Incorrect path.", NONE_PATH, noOpts.getPath().getPath() );
        assertFalse( "Incorrect path type.", noOpts.getPath().inJar() );
        assertEquals( "Incorrect path.", ALL_PATH, allOpts.getPath().getPath() );
        assertFalse( "Incorrect path type.", allOpts.getPath().inJar() );
        assertEquals( "Incorrect path.", GRAPHIC_PATH, wGraphic.getPath().getPath() );
        assertFalse( "Incorrect path type.", wGraphic.getPath().inJar() );
        assertEquals( "Incorrect path.", AUDIO_PATH, wAudio.getPath().getPath() );
        assertFalse( "Incorrect path type.", wAudio.getPath().inJar() );
        assertEquals( "Incorrect path.", ALT_PATH, alt.getPath().getPath() );
        assertFalse( "Incorrect path type.", alt.getPath().inJar() );
        
    }
    
    @Test
    public void testGraphic() {
        
        assertNull( "Shouldn't have a graphic.", noOpts.getGraphic() );
        assertEquals( "Incorrect graphic ID.", "graphic", allOpts.getGraphic() );
        assertEquals( "Incorrect graphic ID.", "graphic", wGraphic.getGraphic() );
        assertNull( "Shouldn't have a graphic.", wAudio.getGraphic() );
        assertEquals( "Incorrect graphic ID.", "graphicID", alt.getGraphic() );
        
    }
    
    @Test
    public void testAudio() {
        
        assertNull( "Shouldn't have an audio.", noOpts.getAudio() );
        assertEquals( "Incorrect audio ID.", "audio", allOpts.getAudio() );
        assertNull( "Shouldn't have an audio.", wGraphic.getAudio() );
        assertEquals( "Incorrect audio ID.", "audio", wAudio.getAudio() );
        assertEquals( "Incorrect audio ID.", "audioID", alt.getAudio() );
        
    }
    
    @SuppressWarnings( "unused" )
    @Test
    public void testExceptions() {
        
        SceneFactory factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "Test" );
        Resource res;
        
        /* Test can't build without path. */
        try {
            res = factory.build();
            fail( "Building Scene without a path should throw exception." );
        } catch ( IllegalStateException e ) {
            assertEquals( "Unexpected exception when building Scene without path.", 
                    "The path is required for Scene construction but wasn't specified!",
                    e.getMessage() );
        }
        
        /* Test can build after providing what was missing. */
        factory.withPath( new ResourcePath( Paths.get( "file.txt" ), false ) );
        try {
            res = factory.build();
            // Normal
        } catch ( IllegalStateException e ) {
            fail( "Scene should be built once path is provided." );
        }
        
    }
    
    @Test
    public void testAlternateExceptions() {
        
        ResourceFactory factory = ResourceFactory.newInstance( ResourceType.SCENE, "Test" );
        
        /* Test invalid element. */
        try {
            factory.withElement( "invalid", "test" );
            fail( "Invalid element should throw exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception when setting invalid element.", 
                    "Scene does not have element 'invalid'.",
                    e.getMessage() );
        }
        
        /* Test invalid values. */
        try {
            factory.withElement( "path", new Integer(0) );
            fail( "Value of invalid type should throw exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception when setting invalid element value.", 
                    "Value given for element 'path' is of the wrong type.",
                    e.getMessage() );
        }
        
        try {
            factory.withElement( "graphic", new Integer(0) );
            fail( "Value of invalid type should throw exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception when setting invalid element value.", 
                    "Value given for element 'graphic' is of the wrong type.",
                    e.getMessage() );
        }
        
        try {
            factory.withElement( "audio", new Integer(0) );
            fail( "Value of invalid type should throw exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception when setting invalid element value.", 
                    "Value given for element 'audio' is of the wrong type.",
                    e.getMessage() );
        }
        
    }

}
