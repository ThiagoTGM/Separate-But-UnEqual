package com.github.thiagotgm.separate_but_unequal.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JComponent;

/**
 * Interface that provides methods to scale UI according to screen resolution.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-27
 */
public interface Scalable {
    
    public static final int RESOLUTION = Toolkit.getDefaultToolkit().getScreenResolution();
    public static final int FONT_DIVISOR = 70;
    
    public static final int FONT_MULTIPLIER = RESOLUTION / FONT_DIVISOR;
    
    /**
     * Scales a given dimension.<p>
     * Essentially assumes that the given Dimension is in inches and obtains the value in pixels.
     * 
     * @param dim Dimension to be scaled.
     * @return The scaled Dimension.
     */
    public static Dimension scale( Dimension dim ) {
        
        double newHeight = dim.getHeight() * RESOLUTION;
        double newWidth = dim.getWidth() * RESOLUTION;
        return new Dimension( (int) newWidth, (int) newHeight );
        
    }
   
    /**
     * Scales a given int value.<p>
     * Essentially assumes that the given value is in inches and obtains the value in pixels.
     * 
     * @param value Value to be scaled.
     * @return The scaled value.
     */
    public static int scale( int value ) {
        
        return value * RESOLUTION;
        
    }
    
    /**
     * Scales a given double value.<p>
     * Essentially assumes that the given value is in inches and obtains the value in pixels.
     * 
     * @param value Value to be scaled.
     * @return The scaled value.
     */
    public static double scale( double value ) {
        
        return value * RESOLUTION;
        
    }
    
    /**
     * Scales a given double value, but returns the result as an int.<p>
     * Essentially assumes that the given value is in inches and obtains the value in pixels.
     * 
     * @param value Value to be scaled.
     * @return The scaled value.
     */
    public static int scaleToInt( double value ) {
        
        return (int) scale( value );
        
    }
    
    /**
     * Scales a pair of dimension values into a Dimension.<p>
     * Essentially assumes that the given values are in inches and obtains the Dimension in pixels.
     * 
     * @param width Width to be scaled.
     * @param height Height to be scaled.
     * @return The scaled Dimension.
     */
    public static Dimension scale( int width, int height ) {
        
        return new Dimension( scale( width ), scale( height ) );
        
    }
    
    /**
     * Scales a pair of dimension values into a Dimension.<p>
     * Essentially assumes that the given values are in inches and obtains the Dimension in pixels.
     * 
     * @param width Width to be scaled.
     * @param height Height to be scaled.
     * @return The scaled Dimension.
     */
    public static Dimension scale( double width, double height ) {
        
        return new Dimension( scaleToInt( width ), scaleToInt( height ) );
        
    }
    
    /**
     * Sets the text font on the given component to be more appropriate to the resoultion.
     * 
     * @param c The component to have its font resized.
     */
    public static void scaleFont( JComponent c ) {
        
        Font font = c.getFont();
        font = font.deriveFont( font.getSize2D() * FONT_MULTIPLIER );
        c.setFont( font );
        
    }

}
