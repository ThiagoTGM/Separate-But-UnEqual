package com.github.thiagotgm.separate_but_unequal;

import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;

/**
 * Class that launches the game on startup.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-23
 */
public class Launcher {

    public static void main( String[] args ) {

        ResourceManager.load();

    }

}
