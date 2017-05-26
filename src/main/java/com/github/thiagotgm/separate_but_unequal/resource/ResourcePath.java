package com.github.thiagotgm.separate_but_unequal.resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Class that encapsulates a path to a resource and whether that path is in a jar or the regular
 * filesystem.
 *
 * @version 1.0
 * @author Thiago Marback
 * @since 2017-05-23
 */
public class ResourcePath {
    
    private final Path path;
    private final boolean inJar;
    
    /**
     * Creates a new ResourcePath with given path.
     * 
     * @param path Path of the resource.
     * @param inJar Whether the resource is in a jar (true) or in the normal filesystem (false).
     */
    public ResourcePath( Path path, boolean inJar ) {
        
        this.path = path;
        this.inJar = inJar;
        
    }
    
    /**
     * Identifies whether this Resource file is contained in a Jar or the filesystem.
     *
     * @return true if the resource file is in a jar, false if it is in the normal filesystem.
     */
    public boolean inJar() {
        
        return inJar;
        
    }
    
    /**
     * Retrieves the path of this resource.
     * 
     * @return The path to the resource file.
     */
    public Path getPath() {
        
        return path;
        
    }
    
    /**
     * Retrieves the resource file (resource.xml) as an input string.<br>
     * Takes into account where the file is stored (jar or filesystem).
     * 
     * @return The resource file as an InputStream, or null if it was not found.
     */
    public InputStream getInputStream() {
        
        if ( inJar ) {
            return ResourceManager.class.getResourceAsStream( path.toString() );
        } else {
            try {
                return new FileInputStream( path.toString() );
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
                return null;
            }
        }
        
    }
    
}