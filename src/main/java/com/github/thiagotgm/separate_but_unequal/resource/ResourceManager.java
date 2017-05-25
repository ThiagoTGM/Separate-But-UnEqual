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
import java.util.stream.Stream;

import javax.xml.stream.XMLStreamException;

/**
 * Class that manages the resource library.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public class ResourceManager {
    
    private static final String RESOURCE_IDENTIFIER = "resource.xml";
    private static final String RESOURCE_ROOT = "resources";
    private static final int MAX_DEPTH = 5;

    private static Hashtable<String, Resource> resources = new Hashtable<>();
    
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
     * Loads the resource library.
     */
    public static void load() {
        
        List<ResourcePath> files = getResourceFiles();
        for ( ResourcePath file : files ) {
            
            System.out.println( "**************[ File " + file.getPath() + " ]**************" );
            try {
                Scene sc;
                sc = (Scene) ResourceReader.readResource( file );
                System.out.println( sc.getID() );
                System.out.println( sc.getPath().getPath() );
                System.out.println( sc.getPath().inJar() );
                System.out.println( (sc.getTransition() == null) ? "No transition." : sc.getTransition() );
                System.out.println( (sc.getGraphic() == null) ? "No graphic." : sc.getGraphic() );
                System.out.println( (sc.getAudio() == null) ? "No audio." : sc.getAudio() );
                for ( Choice c : sc.getOptions() ) {
                    
                    System.out.println( "Choice: " + c + " ===> " + c.getTarget() );
                    
                }
            } catch ( XMLStreamException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
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
            e.printStackTrace();
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
            System.err.println( "Error encountered while identifying resource files." );
            e.printStackTrace();
            found = null;
        } finally { // Closes jar filesystem (if used) and pathwalker.
            if ( fileSystem != null ) {
                try {
                    fileSystem.close();
                } catch ( IOException e ) {
                    System.err.println( "Failed to close FileSystem." );
                    e.printStackTrace();
                }
            }
            if ( walk != null ) {
                walk.close();
            }
        }
        
        return found;
        
    }

}
