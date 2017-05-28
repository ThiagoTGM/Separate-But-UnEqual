package com.github.thiagotgm.separate_but_unequal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.thiagotgm.separate_but_unequal.resource.Choice;


public class ChoiceTest {

    private Choice choice1;
    private Choice choice2;
    private Choice choice3;
    private Choice rev1;
    
    @Before
    public void setUp() {
        
        choice1 = new Choice( "fire", "target" );
        choice2 = new Choice( "horse away!", "gallop" );
        choice3 = new Choice( "Go home", "bed" );
        rev1 = new Choice( "target", "fire" );
        
    }
    
    @Test
    public void testToString() {

        assertEquals( "Incorrect String returned.", "fire", choice1.toString() );
        assertEquals( "Incorrect String returned.", "horse away!", choice2.toString() );
        assertEquals( "Incorrect String returned.", "Go home", choice3.toString() );
        assertEquals( "Incorrect String returned.", "target", rev1.toString() );
        
        
    }
    
    @Test
    public void testTarget() {
        
        assertEquals( "Incorrect target returned.", "target", choice1.getTarget() );
        assertEquals( "Incorrect target returned.", "gallop", choice2.getTarget() );
        assertEquals( "Incorrect target returned.", "bed", choice3.getTarget() );
        assertEquals( "Incorrect target returned.", "fire", rev1.getTarget() );
        
    }
    
    @Test
    public void testEquals() {
        
        Choice choice = new Choice( "fire", "target" );
        assertTrue( choice1.equals( choice ) );
        assertFalse( choice2.equals( choice ) );
        assertFalse( choice3.equals( choice ) );
        assertFalse( rev1.equals( choice ) );
        
        choice = new Choice( "target", "fire" );
        assertFalse( choice1.equals( choice ) );
        assertFalse( choice2.equals( choice ) );
        assertFalse( choice3.equals( choice ) );
        assertTrue( rev1.equals( choice ) );
        
        choice = new Choice( "Go home", "bed" );
        assertFalse( choice1.equals( choice ) );
        assertFalse( choice2.equals( choice ) );
        assertTrue( choice3.equals( choice ) );
        assertFalse( rev1.equals( choice ) );
        
    }

}
