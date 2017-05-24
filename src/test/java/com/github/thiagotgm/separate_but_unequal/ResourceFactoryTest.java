package com.github.thiagotgm.separate_but_unequal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.thiagotgm.separate_but_unequal.resource.ResourceFactory;
import com.github.thiagotgm.separate_but_unequal.resource.Resource.ResourceType;


public class ResourceFactoryTest {

    @SuppressWarnings( "unused" )
    @Test
    public void testNewFactory() {

        ResourceFactory factory;
        factory = ResourceFactory.newInstance( ResourceType.SCENE, "ID" );
        
    }
    
    @SuppressWarnings( "unused" )
    @Test
    public void testNewFactoryExceptions() {
        
        ResourceFactory factory;
        try {
            factory = ResourceFactory.newInstance( ResourceType.NONE, "ID" );
            fail( "Should throw exception for invalid resource type." );
        } catch ( UnsupportedOperationException e ) {
            assertEquals( "Unexpected exception when no factory available.", 
                    "No factory available for the requested resource type.",
                    e.getMessage() );
        }
        try {
            factory = ResourceFactory.newInstance( ResourceType.SCENE, null );
            fail( "Should throw exception for null ID." );
        } catch ( NullPointerException e ) {
            assertEquals( "Unexpected exception when null ID.", 
                    "Resource ID cannot be null.",
                    e.getMessage() );
        }
        try {
            factory = ResourceFactory.newInstance( ResourceType.NONE, null );
            fail( "Should throw exception for invalid resource type and null ID." );
        } catch ( Exception e ) {
            // Normal
        }
        
    }

}
