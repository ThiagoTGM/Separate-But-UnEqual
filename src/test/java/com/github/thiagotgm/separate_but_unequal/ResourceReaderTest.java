package com.github.thiagotgm.separate_but_unequal;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Test;


public class ResourceReaderTest {
    
    private static final Path BASE_PATH = Paths.get( "src", "test", "resources" );

    @Test
    public void test() {

        System.out.println( Arrays.toString( new File( BASE_PATH.toString() ).list() ) );
    }

}
