package com.github.thiagotgm.separate_but_unequal.resource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.thiagotgm.separate_but_unequal.Launcher;

/**
 * Class that manages the resource library.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public class ResourceManager {
    
    private static final Logger log = LoggerFactory.getLogger( ResourceManager.class );
    
    private static final String RESOURCE_IDENTIFIER = "resource.xml";
    private static final String RESOURCE_ROOT = "resources";
    private static final int MAX_DEPTH = Integer.MAX_VALUE;
    
    private static final String DEFAULT_SETTINGS_FILE = "defaults.txt";
    private static final String TEXT_SPEED_MULTIPLIER = "textSpeedMultiplier";

    private static final Hashtable<String, Resource> resources = new Hashtable<>();
    private static final Properties defaultSettings = new Properties();
    private static Properties settings;
    
    /**
     * Retrieves the resource identified by the given ID.
     * 
     * @param id ID of the resource to be retrieved.
     * @return The resource in the library identified by the given ID.
     */
    public static Resource getResource( String id ) {
        
        return resources.get( id );
        
    }
    
    /**
     * Loads the resource library and the default properties.
     */
    public static void load() {
        
        try {
            defaultSettings.load( ResourceManager.class.getClassLoader().getResourceAsStream( DEFAULT_SETTINGS_FILE ) );
        } catch ( IOException e ) {
            log.error( "Failed to load default settings.", e );
            System.exit( Launcher.LOADING_ERROR_CODE );
        }
        resetSettings();
        
        log.info( "===================[ Loading Resource Database ]===================\n" );
        List<ResourcePath> files = getResourceFiles();
        if ( files == null ) {
            System.exit( Launcher.LOADING_ERROR_CODE );
        }
        for ( ResourcePath file : files ) {
            
            log.debug( "***[ Loading resource file '" + file.getPath() + "' ]***" );
            try {
                Resource res = ResourceReader.readResource( file );
                resources.put( res.getID(), res );
                log.info( "Loaded resource file '" + file.getPath() + "' successfully." );
            } catch ( XMLStreamException e ) {
                log.error( "Failed to load file '" + file.getPath() + "'.", e );
            }
            
        }
        log.info( "===================[ Database Loaded ]===================\n" );
        
    }
    
    /**
     * Obtains the path to each resource file present in the resource file tree.
     * 
     * @return The list of resource paths to each resource.xml file found in the resource file tree.
     */
    private static List<ResourcePath> getResourceFiles() {
        
        List<ResourcePath> found = new LinkedList<>();
        
        URI uri = null;
        try { // Obtains the URI of the root resource folder.
            uri = ResourceManager.class.getClassLoader().getResource( RESOURCE_ROOT ).toURI();
        } catch ( URISyntaxException e ) {
            log.error( "Failed to obtain resource folder.", e );
            return null;
        }
        
        Path myPath;
        FileSystem fileSystem = null;
        Stream<Path> walk = null;        
        try {
            boolean inJar;
            if ( uri.getScheme().equals( "jar" ) ) { // Resource file tree is in a jar
                fileSystem = FileSystems.newFileSystem( uri, Collections.<String, Object>emptyMap() ); // Opens filesystem
                myPath = fileSystem.getPath( RESOURCE_ROOT );                                          // based in the jar
                inJar = true;
            } else { // Resource file tree is in normal filesystem.
                myPath = Paths.get( uri );
                inJar = false;
            }
            
            walk = Files.walk( myPath, MAX_DEPTH ); // Goes through all files in the filesystem.
            for ( Iterator<Path> it = walk.iterator(); it.hasNext(); ){
                
                Path next = it.next();
                if ( next.getFileName().toString().equals( RESOURCE_IDENTIFIER ) ) {
                    found.add( new ResourcePath( next, inJar ) ); // Adds found resource.xml to the list.
                }
                
            }
        } catch ( IOException e ) {
            log.error( "Error encountered while identifying resource files.", e );
            found = null;
        } finally { // Closes jar filesystem (if used) and pathwalker.
            if ( walk != null ) {
                walk.close();
            }
            if ( fileSystem != null ) {
                try {
                    fileSystem.close();
                } catch ( IOException e ) {
                    log.warn( "Failed to close FileSystem.", e );
                }
            }
        }
        
        return found;
        
    }
    
    /**
     * Resets all settings to default.
     */
    public static void resetSettings() {
        
        settings = new Properties( defaultSettings );
        
    }
    
    /**
     * Retrieves the current value of the Text Speed Multiplier setting.
     * 
     * @return The current value of the setting.
     */
    public static int getTextSpeedMultiplier() {
        
        return Integer.valueOf( settings.getProperty( TEXT_SPEED_MULTIPLIER ) );
        
    }
    
    /**
     * Sets the value of the Text Speed Multiplier setting.
     * 
     * @param newValue the new value of the setting.
     */
    public static void setTextSpeedMultiplier( int newValue ) {
        
        settings.setProperty( TEXT_SPEED_MULTIPLIER, String.valueOf( newValue ) );
        
    }

}
