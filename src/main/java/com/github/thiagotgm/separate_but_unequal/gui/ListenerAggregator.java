package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Class that listens to all the buttons in the panel and passes any event fired by them to the listeners on the
 * overall panel
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-25
 */
class ListenerAggregator implements ActionListener {

    List<ActionListener> listeners;
    
    /**
     * Creates an aggregator that repasses events to a given list of listeners.<br>
     * Updates in the list will still affect the aggregator.
     * 
     * @param listeners The list of listeners to repass events to.
     */
    public ListenerAggregator( List<ActionListener> listeners ) {
        
        this.listeners = listeners;
        
    }
    
    /**
     * Sends the received ActionEvent to all the listeners currently in the listener list.
     * 
     * @param e ActionEvent fired.
     */
    @Override
    public void actionPerformed( ActionEvent e ) {

        for ( ActionListener listener : listeners ) {
            
            listener.actionPerformed( e ); // Notifies all listeners that a button event was fired.
            
        }
        
    }
    
}