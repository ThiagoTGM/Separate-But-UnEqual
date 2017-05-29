package com.github.thiagotgm.separate_but_unequal.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import com.github.thiagotgm.separate_but_unequal.resource.reader.ResourceReader;

/**
 * Class that manages the resource library.<br>
 * Follows the Singleton pattern.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-23
 */
public class ResourceManager {
    
    private static final Logger log = LoggerFactory.getLogger( ResourceManager.class );
    
    private static final String RESOURCE_IDENTIFIER = "resource.xml";
    private static final String RESOURCE_ROOT = "resources";
    private static final int MAX_DEPTH = Integer.MAX_VALUE;
    
    private static final String DEFAULT_SETTINGS_FILE = "defaults.txt";
    private static final String TEXT_SPEED_MULTIPLIER = "textSpeedMultiplier";
    private static final String SAVE = "save";
    private static final String SAVE_FILE = "save.txt";
    private static final String SAVE_FILE_COMMENT = "Settings modified by the user, and information about the user's"
            + " progress in the game.";

    private final Hashtable<String, Resource> resources;
    private final Properties settings;
    
    private static ResourceManager instance;
    
    /**
     * Initializes a ResourceManager instance.
     */
    protected ResourceManager() {
        
        resources = new Hashtable<>();
        
        Properties defaultSettings = new Properties();
        log.info( "Loading default settings." );
        try {
            defaultSettings.load( ResourceManager.class.getClassLoader().getResourceAsStream( DEFAULT_SETTINGS_FILE ) );
            log.info( "Default settings loaded." );
        } catch ( IOException e ) {
            log.error( "Failed to load default settings.", e );
            System.exit( Launcher.LOADING_ERROR_CODE );
        }
        settings = new Properties( defaultSettings );
        
        load();
        
    }
    
    /**
     * Retrieves the currently existing instance of ResourceManager.<br>
     * If there isn't one, creates it.
     * 
     * @return The instance of ResourceManager.
     */
    public static ResourceManager getInstance() {
        
        if ( instance == null ) {
            instance = new ResourceManager();
        }
        return instance;
        
    }
    
    /**
     * Retrieves the resource identified by the given ID.
     * 
     * @param id ID of the resource to be retrieved.
     * @return The resource in the library identified by the given ID, or null if there is no Resource with this ID.
     */
    public Resource getResource( String id ) {
        
        return resources.get( id );
        
    }
    
    /**
     * Loads the resource library and the save file.
     */
    private void load() {
        
        if ( new File( SAVE_FILE ).exists() ) { // Load save file.
            log.info( "Loading save file." );
            try {
                settings.load( new FileInputStream( SAVE_FILE ) );
                log.info( "Save file loaded successfully." );
            } catch ( IOException e ) {
                log.error( "Could not load save file.", e );
            }
        } else {
            log.info( "No save file found." );
        }
        
        log.info( "===================[ Loading Resource Database ]===================" );
        List<ResourcePath> files = getResourceFiles();
        if ( files == null ) {
            System.exit( Launcher.LOADING_ERROR_CODE );
        }
        for ( ResourcePath file : files ) { // Load each Resource.
            
            log.debug( "***[ Loading resource file '" + file.getPath() + "' ]***" );
            try {
                Resource res = ResourceReader.readResource( file );
                resources.put( res.getID(), res );
                log.info( "Loaded resource file '" + file.getPath() + "' successfully." );
            } catch ( XMLStreamException e ) {
                log.error( "Failed to load file '" + file.getPath() + "'.", e );
            }
            
        }
        log.info( "===================[ Database Loaded ]===================" );
        
    }
    
    /**
     * Records the custom settings and save information to the save file.
     */
    public void save() {
        
        log.info( "Writing to save file." );
        try {
            OutputStream out = new FileOutputStream( SAVE_FILE );
            settings.store( out, SAVE_FILE_COMMENT );
            log.info( "Save file written successfully." );
        } catch ( IOException e ) {
            log.error( "Could not write save file.", e );
        }
        
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
     * Retrieves the current value of the Text Speed Multiplier setting.
     * 
     * @return The current value of the setting.
     */
    public int getTextSpeedMultiplier() {
        
        return Integer.valueOf( settings.getProperty( TEXT_SPEED_MULTIPLIER ) );
        
    }
    
    /**
     * Sets the value of the Text Speed Multiplier setting.
     * 
     * @param newValue the new value of the setting.
     */
    public void setTextSpeedMultiplier( int newValue ) {
        
        settings.setProperty( TEXT_SPEED_MULTIPLIER, String.valueOf( newValue ) );
        
    }
    
    /**
     * Retrieves the current value of the Save.
     * 
     * @return The current Save value. If there is no Save, returns null.
     */
    public String getSave() {
        
        return settings.getProperty( SAVE );
        
    }
    
    /**
     * Sets the value of the Save.
     * 
     * @param save The new value of the Save. If null, the Save is deleted.
     */
    public void setSave( String save ) {
        
        if ( save != null ) {
            settings.setProperty( SAVE, save );
        } else {
            settings.remove( SAVE );
        }
        
    }
    
    /**
     * Determines if a Save currently exists.
     * 
     * @return true if there is a Save, false otherwise.
     */
    public boolean hasSave() {
        
        return settings.containsKey( SAVE );
        
    }

}
