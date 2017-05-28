package com.github.thiagotgm.separate_but_unequal;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.thiagotgm.separate_but_unequal.resource.Choice;
import com.github.thiagotgm.separate_but_unequal.resource.ChoiceScene;
import com.github.thiagotgm.separate_but_unequal.resource.ChoiceSceneFactory;
import com.github.thiagotgm.separate_but_unequal.resource.Resource;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceFactory;
import com.github.thiagotgm.separate_but_unequal.resource.ResourcePath;
import com.github.thiagotgm.separate_but_unequal.resource.SceneFactory;
import com.github.thiagotgm.separate_but_unequal.resource.Resource.ResourceType;


public class ChoiceSceneTest {
    
    private static final Choice[] CHOICES1 = { 
            new Choice( "Test things!", "Test" ),
            new Choice( "Don't tell me what to do!", "Choice" ),
            new Choice( "Okay...", "Obey" ) };
    private static final Choice[] CHOICES2 = { 
            new Choice( "Ayyy", "Lmao" ),
            new Choice( "*shoots*", "Bullseye!" ) };
    private static final ResourcePath PATH =  new ResourcePath( Paths.get( "res", "file.txt" ), false );
    
    private ChoiceScene scene1;
    private ChoiceScene scene2;
    private ChoiceScene alt;
    
    @Before
    public void setUp() throws Exception {
        
        ChoiceSceneFactory factory = (ChoiceSceneFactory) ResourceFactory.newInstance( ResourceType.CHOICE_SCENE, "Scene1" );
        scene1 = (ChoiceScene) factory.withOptions( Arrays.asList( CHOICES1 ) ).withPath( PATH ).build();
        factory = (ChoiceSceneFactory) ResourceFactory.newInstance( ResourceType.CHOICE_SCENE, "Scene2" );
        scene2 = (ChoiceScene) factory.withOptions( Arrays.asList( CHOICES2 ) ).withPath( PATH ).build();
        
        /* Builds a scene using superclass method. */
        ResourceFactory superFactory = ResourceFactory.newInstance( ResourceType.CHOICE_SCENE, "Alternate" );
        superFactory.withElement( SceneFactory.PATH_ELEMENT, PATH );
        superFactory.withElement( ChoiceSceneFactory.OPTIONS_ELEMENT, Arrays.asList( CHOICES1 ) );
        alt = (ChoiceScene) superFactory.build();
        
    }

    @Test
    public void testOptions() {
        
        assertEquals( "Incorrect choice array", Arrays.asList( CHOICES1 ), scene1.getOptions() );
        assertEquals( "Incorrect choice array", Arrays.asList( CHOICES2 ), scene2.getOptions() );
        assertEquals( "Incorrect choice array", Arrays.asList( CHOICES1 ), alt.getOptions() );
        
        /* Test if Scene made a copy of given option list. */
        List<Choice> original = new ArrayList<>();
        original.add( new Choice( "Testing 1", "Test1" ) );
        original.add( new Choice( "Testing 1", "Test1" ) );
        original.add( new Choice( "Testing 1", "Test1" ) );
        ChoiceScene scene = (ChoiceScene) ( (ChoiceSceneFactory) ResourceFactory.newInstance( ResourceType.CHOICE_SCENE, "L" ) )
                .withOptions( original ).withPath( PATH ).build();
        List<Choice> returned = scene.getOptions();
        assertNotSame( "Scene should have a copy of the option list, not use the same List instance.",
                original, returned );
        
    }
    
    @SuppressWarnings( "unused" )
    @Test
    public void testExceptions() {
        
        /* Tests returned option list is unmodifiable. */
        try {
            scene1.getOptions().remove( 0 );
            fail( "Option list should be unmodifiable." );
        } catch ( UnsupportedOperationException e ) {
            // Normal
        }
        
        Resource res;
        List<Choice> options = Arrays.asList( CHOICES1 );
        ChoiceSceneFactory factory = (ChoiceSceneFactory) ResourceFactory.newInstance( ResourceType.CHOICE_SCENE, "Test" );
        
        /* Test can't build without option list. */
        factory.withPath( PATH );
        factory.withOptions( null );
        try {
            res = factory.build();
            fail( "Building Scene without an option list should throw exception." );
        } catch ( IllegalStateException e ) {
            assertEquals( "Unexpected exception when building ChoiceScene without option list.", 
                    "The option list is required for ChoiceScene construction but wasn't specified!",
                    e.getMessage() );
        }
        
        /* Test can't build without path and option list. */
        factory.withPath( null );
        try {
            res = factory.build();
            fail( "Building Scene without path and option list should throw exception." );
        } catch ( IllegalStateException e ) {
            // Normal
        }
        
        /* Test can build after providing what was missing. */
        factory.withPath( PATH );
        factory.withOptions( options );
        try {
            res = factory.build();
            // Normal
        } catch ( IllegalStateException e ) {
            fail( "Scene should be built once path and options are provided." );
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
        
        ResourceFactory factory = ResourceFactory.newInstance( ResourceType.CHOICE_SCENE, "Test" );
        
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
            factory.withElement( ChoiceSceneFactory.OPTIONS_ELEMENT, new Integer(0) );
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
