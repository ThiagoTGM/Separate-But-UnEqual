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
    
    private EndScene def;
    private EndScene scene;
    private EndScene alt;

    @Before
    public void setUp() throws Exception {
        
        EndSceneFactory factory = (EndSceneFactory) ResourceFactory.newInstance( ResourceType.END_SCENE, "test" );
        def = (EndScene) factory.withPath( PATH ).build();
        scene = (EndScene) factory.withCode( 42 ).withPath( PATH ).build();
        
        ResourceFactory superFactory = ResourceFactory.newInstance( ResourceType.END_SCENE, "Alternate" );
        superFactory.withElement( SceneFactory.PATH_ELEMENT, PATH );
        superFactory.withElement( EndSceneFactory.CODE_ELEMENT, 1337 );
        alt = (EndScene) superFactory.build();

    }

    @Test
    public void testCode() {

        assertEquals( "Incorrect end code retrieved.", 0, def.getCode() );
        assertEquals( "Incorrect end code retrieved.", 42, scene.getCode() );
        assertEquals( "Incorrect end code retrieved.", 1337, alt.getCode() );

    }

}
