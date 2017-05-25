package com.github.thiagotgm.separate_but_unequal;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.thiagotgm.separate_but_unequal.resource.Choice;
import com.github.thiagotgm.separate_but_unequal.resource.Resource;
import com.github.thiagotgm.separate_but_unequal.resource.Resource.ResourceType;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceFactory;
import com.github.thiagotgm.separate_but_unequal.resource.ResourcePath;
import com.github.thiagotgm.separate_but_unequal.resource.Scene;
import com.github.thiagotgm.separate_but_unequal.resource.SceneFactory;


public class SceneTest {

    private static final Choice[] CHOICES = { 
            new Choice( "Test things!", "Test" ),
            new Choice( "Don't tell me what to do!", "Choice" ),
            new Choice( "Okay...", "Obey" ) };
    private static final Path NONE_PATH = Paths.get( "res", "none.txt" );
    private static final Path ALL_PATH = Paths.get( "res", "none.txt" );
    private static final Path TRANSITION_PATH = Paths.get( "res", "none.txt" );
    private static final Path GRAPHIC_PATH = Paths.get( "res", "none.txt" );
    private static final Path AUDIO_PATH = Paths.get( "res", "none.txt" );
    private static final Path ALT_PATH = Paths.get( "res", "alt.txt" );
    
    private Scene noOpts;
    private Scene allOpts;
    private Scene wTransition;
    private Scene wGraphic;
    private Scene wAudio;
    private Scene alt;
    
    @Before
    public void setUp() throws Exception {
        
        SceneFactory factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "No Opts" );
        noOpts = (Scene) factory.withPath( new ResourcePath( NONE_PATH, false ) ).withOptions( Arrays.asList( CHOICES ) ).build();
        factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "All Opts" );
        allOpts = (Scene) factory.withPath( new ResourcePath( ALL_PATH, false ) ).withTransition( "transition" )
                .withGraphic( "graphic" ).withAudio( "audio" ).withOptions( Arrays.asList( CHOICES ) ).build();
        factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "With Transition" );
        wTransition = (Scene) factory.withPath( new ResourcePath( TRANSITION_PATH, false ) ).withTransition( "transition" )
                .withOptions( Arrays.asList( CHOICES ) ).build();
        factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "With Graphic" );
        wGraphic = (Scene) factory.withPath( new ResourcePath( GRAPHIC_PATH, false ) ).withGraphic( "graphic" )
                .withOptions( Arrays.asList( CHOICES ) ).build();
        factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "With Audio" );
        wAudio = (Scene) factory.withPath( new ResourcePath( AUDIO_PATH, false ) ).withAudio( "audio" )
                .withOptions( Arrays.asList( CHOICES ) ).build();
        
        /* Builds a scene using superclass method. */
        ResourceFactory superFactory = ResourceFactory.newInstance( ResourceType.SCENE, "Alternate" );
        superFactory.withElement( "path", new ResourcePath( ALT_PATH, false ) );
        superFactory.withElement( "transition", "transitionID" );
        superFactory.withElement( "graphic", "graphicID" );
        superFactory.withElement( "audio", "audioID" );
        superFactory.withElement( "options", Arrays.asList( CHOICES ) );
        alt = (Scene) superFactory.build();
        
    }

    @Test
    public void testID() {

        assertEquals( "Incorrect ID.", "No Opts", noOpts.getID() );
        assertEquals( "Incorrect ID.", "All Opts", allOpts.getID() );
        assertEquals( "Incorrect ID.", "With Transition", wTransition.getID() );
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
        assertEquals( "Incorrect path.", TRANSITION_PATH, wTransition.getPath().getPath() );
        assertFalse( "Incorrect path type.", wTransition.getPath().inJar() );
        assertEquals( "Incorrect path.", GRAPHIC_PATH, wGraphic.getPath().getPath() );
        assertFalse( "Incorrect path type.", wGraphic.getPath().inJar() );
        assertEquals( "Incorrect path.", AUDIO_PATH, wAudio.getPath().getPath() );
        assertFalse( "Incorrect path type.", wAudio.getPath().inJar() );
        assertEquals( "Incorrect path.", ALT_PATH, alt.getPath().getPath() );
        assertFalse( "Incorrect path type.", alt.getPath().inJar() );
        
    }
    
    @Test
    public void testTransition() {
        
        assertNull( "Shouldn't have a transition.", noOpts.getTransition() );
        assertEquals( "Incorrect transition ID.", "transition", allOpts.getTransition() );
        assertEquals( "Incorrect transition ID.", "transition", wTransition.getTransition() );
        assertNull( "Shouldn't have a transition.", wGraphic.getTransition() );
        assertNull( "Shouldn't have a transition.", wAudio.getTransition() );
        assertEquals( "Incorrect transition ID.", "transitionID", alt.getTransition() );
        
    }
    
    @Test
    public void testGraphic() {
        
        assertNull( "Shouldn't have a graphic.", noOpts.getGraphic() );
        assertEquals( "Incorrect graphic ID.", "graphic", allOpts.getGraphic() );
        assertNull( "Shouldn't have a graphic.", wTransition.getGraphic() );
        assertEquals( "Incorrect graphic ID.", "graphic", wGraphic.getGraphic() );
        assertNull( "Shouldn't have a graphic.", wAudio.getGraphic() );
        assertEquals( "Incorrect graphic ID.", "graphicID", alt.getGraphic() );
        
    }
    
    @Test
    public void testAudio() {
        
        assertNull( "Shouldn't have an audio.", noOpts.getAudio() );
        assertEquals( "Incorrect audio ID.", "audio", allOpts.getAudio() );
        assertNull( "Shouldn't have an audio.", wTransition.getAudio() );
        assertNull( "Shouldn't have an audio.", wGraphic.getAudio() );
        assertEquals( "Incorrect audio ID.", "audio", wAudio.getAudio() );
        assertEquals( "Incorrect audio ID.", "audioID", alt.getAudio() );
        
    }
    
    @Test
    public void testOptions() {
        
        List<Choice> expected = Arrays.asList( CHOICES );
        
        assertEquals( "Incorrect choice array", expected, noOpts.getOptions() );
        assertEquals( "Incorrect choice array", expected, allOpts.getOptions() );
        assertEquals( "Incorrect choice array", expected, wTransition.getOptions() );
        assertEquals( "Incorrect choice array", expected, wGraphic.getOptions() );
        assertEquals( "Incorrect choice array", expected, wAudio.getOptions() );
        assertEquals( "Incorrect choice array", expected, alt.getOptions() );
        
        /* Test if Scene made a copy of given option list. */
        List<Choice> original = new ArrayList<>();
        original.add( new Choice( "Testing 1", "Test1" ) );
        original.add( new Choice( "Testing 1", "Test1" ) );
        original.add( new Choice( "Testing 1", "Test1" ) );
        Scene scene = (Scene) ( (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "L" ) )
                .withPath( new ResourcePath( Paths.get( "file.txt" ), false ) ).withOptions( original ).build();
        List<Choice> returned = scene.getOptions();
        assertNotSame( "Scene should have a copy of the option list, not use the same List instance.",
                original, returned );
        
    }
    
    @SuppressWarnings( "unused" )
    @Test
    public void testExceptions() {
        
        /* Tests returned option list is unmodifiable. */
        try {
            allOpts.getOptions().remove( 0 );
            fail( "Option list should be unmodifiable." );
        } catch ( UnsupportedOperationException e ) {
            // Normal
        }
        
        List<Choice> options = Arrays.asList( CHOICES );
        SceneFactory factory = (SceneFactory) ResourceFactory.newInstance( ResourceType.SCENE, "Test" );
        Resource res;
        
        /* Test can't build without filename. */
        factory.withOptions( options );
        try {
            res = factory.build();
            fail( "Building Scene without a filename should throw exception." );
        } catch ( IllegalStateException e ) {
            assertEquals( "Unexpected exception when building Scene without filename.", 
                    "The filename is required for Scene construction but wasn't specified!",
                    e.getMessage() );
        }
        
        /* Test can't build without option list. */
        factory.withPath( new ResourcePath( Paths.get( "file.txt" ), false ) );
        factory.withOptions( null );
        try {
            res = factory.build();
            fail( "Building Scene without an option list should throw exception." );
        } catch ( IllegalStateException e ) {
            assertEquals( "Unexpected exception when building Scene without option list.", 
                    "The option list is required for Scene construction but wasn't specified!",
                    e.getMessage() );
        }
        
        /* Test can't build without filename and option list. */
        factory.withPath( null );
        try {
            res = factory.build();
            fail( "Building Scene without filename and option list should throw exception." );
        } catch ( IllegalStateException e ) {
            // Normal
        }
        
        /* Test can build after providing what was missing. */
        factory.withPath( new ResourcePath( Paths.get( "file.txt" ), false ) );
        factory.withOptions( options );
        try {
            res = factory.build();
            // Normal
        } catch ( IllegalStateException e ) {
            fail( "Scene should be built once filename and options are provided." );
        }
        
        /* Test can't build with empty option list. */
        factory.withOptions( new ArrayList<Choice>() );
        try {
            res = factory.build();
            fail( "Building Scene with empty option list should throw exception." );
        } catch ( IllegalStateException e ) {
            assertEquals( "Unexpected exception when building Scene with empty option list.", 
                    "The option list must have at least one Choice!",
                    e.getMessage() );
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
            factory.withElement( "transition", new Integer(0) );
            fail( "Value of invalid type should throw exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception when setting invalid element value.", 
                    "Value given for element 'transition' is of the wrong type.",
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
        
        try {
            factory.withElement( "options", new Integer(0) );
            fail( "Value of invalid type should throw exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception when setting invalid element value.", 
                    "Value given for element 'options' is of the wrong type.",
                    e.getMessage() );
        }
        
        /*
        try {
            int[] nums = { 0, 1, 2, 3 };
            factory.withElement( "options", Arrays.asList( nums ) );
            fail( "Value of invalid type should throw exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception when setting invalid element value.", 
                    "Value given for element 'options' is of the wrong type.",
                    e.getMessage() );
        }
        */
        
    }

}
