package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Extension of JPanel that has buttons, and supports passing through the events of those buttons to a set of
 * registered listeners.<br>
 * The extending class is responsible for redirecting the ActionEvents from its buttons to the
 * {@link #listeners listener list}.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-29
 */
public abstract class ButtonPanel extends JPanel {
    
    /** Serial ID that represents this class. */
    private static final long serialVersionUID = 3709160307809783062L;
    
    /** List of registered listeners. */
    protected final List<ActionListener> listeners;
    
    {
        listeners = new LinkedList<>(); // Initialize listener list.
    }

    /**
     * @see JPanel#JPanel()
     */
    public ButtonPanel() {
        
        super();
        
    }
    
    /**
     * @see JPanel#JPanel(boolean)
     */
    public ButtonPanel( boolean isDoubleBuffered ) {
        
        super( isDoubleBuffered );
        
    }
    
    /**
     * @see JPanel#JPanel(LayoutManager)
     */
    public ButtonPanel( LayoutManager layout ) {
        
        super( layout );
        
    }
    
    /**
     * @see JPanel#JPanel(LayoutManager, boolean)
     */
    public ButtonPanel( LayoutManager layout, boolean isDoubleBuffered ) {
        
        super( layout, isDoubleBuffered );
        
    }
    
    /**
     * Adds a listener to be notified when one of the buttons in the panel is pressed.
     * 
     * @param l Listener to be registered.
     * @see ActionListener
     */
    public void addActionListener( ActionListener l ) {
        
        listeners.add( l );
        
    }
    
    /**
     * Removes a listener so that it is no longer notified when one of the buttons in the panel is pressed.
     * 
     * @param l Listener to be unregistered.
     * @see ActionListener
     */
    public void removeActionListener( ActionListener l ) {
        
        listeners.remove( l );
        
    }

}
