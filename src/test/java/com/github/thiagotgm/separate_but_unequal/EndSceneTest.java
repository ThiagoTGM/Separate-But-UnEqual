package com.github.thiagotgm.separate_but_unequal;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.github.thiagotgm.separate_but_unequal.resource.EndScene;
import com.github.thiagotgm.separate_but_unequal.resource.EndSceneFactory;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceFactory;
import com.github.thiagotgm.separate_but_unequal.resource.ResourcePath;
import com.github.thiagotgm.separate_but_unequal.resource.SceneFactory;
import com.github.thiagotgm.separate_but_unequal.resource.Resource.ResourceType;


public class EndSceneTest {
    
    private static final ResourcePath PATH =  new ResourcePath( Paths.get( "res", "file.txt" ), false );
    
    private EndScene low;
    private EndScene scene;
    private EndScene high;
    private EndScene alt;

    @Before
    public void setUp() throws Exception {
        
        EndSceneFactory factory = (EndSceneFactory) ResourceFactory.newInstance( ResourceType.END_SCENE, "test" );
        low = (EndScene) factory.withCode( 1 ).withPath( PATH ).build();
        scene = (EndScene) factory.withCode( 42 ).withPath( PATH ).build();
        high = (EndScene) factory.withCode( 64 ).withPath( PATH ).build();
        
        ResourceFactory superFactory = ResourceFactory.newInstance( ResourceType.END_SCENE, "Alternate" );
        superFactory.withElement( SceneFactory.PATH_ELEMENT, PATH );
        superFactory.withElement( EndSceneFactory.CODE_ELEMENT, 50 );
        alt = (EndScene) superFactory.build();

    }

    @Test
    public void testCode() {

        assertEquals( "Incorrect end code retrieved.", 1, low.getCode() );
        assertEquals( "Incorrect end code retrieved.", 42, scene.getCode() );
        assertEquals( "Incorrect end code retrieved.", 64, high.getCode() );
        assertEquals( "Incorrect end code retrieved.", 50, alt.getCode() );

    }
    
    @Test
    public void testExceptions() {
        
        EndSceneFactory factory = (EndSceneFactory) ResourceFactory.newInstance( ResourceType.END_SCENE, "test" );
        factory.withPath( PATH );
        
        /* Test invalid end codes. */
        try {
            factory.withCode( 0 );
            fail( "End codes lower than limit should throw an exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception encountered when giving an invalid end code value.",
                    EndScene.CODE_OOB_ERROR, e.getMessage() );
        }
        try {
            factory.withCode( -10 );
            fail( "End codes lower than limit should throw an exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception encountered when giving an invalid end code value.",
                    EndScene.CODE_OOB_ERROR, e.getMessage() );
        }
        try {
            factory.withCode( 65 );
            fail( "End codes higher than limit should throw an exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception encountered when giving an invalid end code value.",
                    EndScene.CODE_OOB_ERROR, e.getMessage() );
        }
        try {
            factory.withCode( 100 );
            fail( "End codes higher than limit should throw an exception." );
        } catch ( IllegalArgumentException e ) {
            assertEquals( "Unexpected exception encountered when giving an invalid end code value.",
                    EndScene.CODE_OOB_ERROR, e.getMessage() );
        }
        
        /* Test building without and end code. */
        try {
            factory.build();
            fail( "Building EndScene without end code should throw an exception." );
        } catch ( IllegalStateException e ) {
            assertEquals( "Unexpected exception encountered when building without an end code.",
                    "A valid End Code is required to build a EndScene instance.",
                    e.getMessage() );
        }
        
    }

}
