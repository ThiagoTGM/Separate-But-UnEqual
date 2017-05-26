package com.github.thiagotgm.separate_but_unequal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.thiagotgm.separate_but_unequal.gui.GamePanel;
import com.github.thiagotgm.separate_but_unequal.resource.Choice;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;
import com.github.thiagotgm.separate_but_unequal.resource.Scene;

public class GameManager implements ActionListener {
    
    private final GamePanel panel;
    private final SceneDisplayer sceneDisplayer;
    private List<Choice> currentOptions;
    private Loader buffer;
    private Thread textThread;
    private Thread bufferThread;

    public GameManager( GamePanel panel ) {

        this.panel = panel;
        sceneDisplayer = new SceneDisplayer( panel.getSceneArea() );
        panel.addActionListener( this );
        
    }
    

    @Override
    public void actionPerformed( ActionEvent e ) {

        String command = e.getActionCommand();
        switch ( command ) {
            
            case GamePanel.SKIP_COMMAND:
                textThread.interrupt();
                sceneDisplayer.skip();
                break;
            
        }
        
    }
    
    public void start( String sceneID ) {
        
        Scene target = ( Scene ) ResourceManager.getResource( sceneID );
        LoadedScene first = new Loader().load( target );
        next( first );
        
    }
    
    private void next( LoadedScene scene ) {
        
        sceneDisplayer.showScene( scene.getText() );
        textThread = new Thread( sceneDisplayer );
        textThread.start();
        currentOptions = scene.getScene().getOptions();
        bufferNextScenes( currentOptions );
        
    }
    
    
    /**
     * Starts loading the next possible scenes from disk.
     * 
     * @param choices The list of choices that marks the next possible scenes.
     */
    private void bufferNextScenes( List<Choice> choices ) {
        
        List<Scene> targets = new ArrayList<>( choices.size() );
        for ( Choice possible : choices ) {
            
            Scene target = ( Scene ) ResourceManager.getResource( possible.getTarget() );
            targets.add( target );
            
        }
        buffer = new Loader( targets );
        bufferThread = new Thread( buffer, Loader.THREAD_NAME );

    }
    
    /**
     * Class that loads a specified list of Scenes from disk, with the possibility of being used in a separate thread.<br>
     * Can also load a single Scene (without using other threads) if the {@link #load(Scene) load()} method is used.
     *
     * @version 1.0
     * @author Thiago
     * @since 2017-05-25
     */
    private class Loader implements Runnable {
        
        private static final String THREAD_NAME = "Scene Resource Loader";
        
        private final List<Scene> targets;
        private List<LoadedScene> product;
        
        /**
         * Constructs a loader with no target.
         */
        public Loader() {
            
            targets = new ArrayList<Scene>();
            
        }
        
        /**
         * Constructs a Loader to load the specified Scene.
         * 
         * @param scene The Scene to load onto memory.
         */
        public Loader( List<Scene> scenes ) {
            
            this.targets = scenes;
            
        }

        @Override
        public void run() {
            
            List<LoadedScene> product = new ArrayList<>( targets.size() );
            for ( Scene target : targets ) { // Loads each target.
                
                product.add( load( target ) );
                
            }
            this.product = product; // Stores finished result.
            
        }
        
        
        /**
         * Retrieves the loaded Scenes once they are fully loaded.<br>
         * If this Loader was given no targets, returns the empty list.
         * 
         * @return All the Scenes with all associated resources loaded.
         * @throws IllegalStateException if used before the loading process completed.
         */
        public List<LoadedScene> getLoadedScene() throws IllegalStateException {
            
            if ( product != null ) {
                return product;
            } else {
                throw new IllegalStateException( "Attempted to retrieve loaded Scene before the Loader finished"
                        + " loading it." );
            }
            
        }

        /**
         * Loads the resources of a single scene.
         * 
         * @param target Scene to load.
         * @return Scene and its resources loaded into memory.
         */
        public LoadedScene load( Scene target ) {
            
            String text = loadSceneText( target );
            return new LoadedScene( target, text );
            
        }
        
        
        /**
         * Loads the Scene's text from disk.
         * 
         * @param scene The scene to load.
         * @return The text of that scene.
         */
        private String loadSceneText( Scene scene ) {
            
            StringBuilder builder = new StringBuilder();
            InputStream in = scene.getPath().getInputStream();
            if ( in == null ) {
                return null; // Couldn't load file.
            }
            Scanner reader = new Scanner( in );
            while ( reader.hasNext() ) { // Read every line from the file.
                
                builder.append( reader.nextLine() );
                builder.append( '\n' );
                
            }
            reader.close();
            return builder.toString(); // Finished reading file.
            
        }
        
        // Load graphic
        // Load audio
        
    }
    
    /**
     * Class that encapsulates a Scene and the associated Resources, after loading them from disk.
     *
     * @version 1.0
     * @author Thiago
     * @since 2017-05-25
     */
    private class LoadedScene {
        
        private final Scene scene;
        private final String text;
        // Graphic
        // Audio
        
        /**
         * Records a Scene and associated loaded resources.
         * 
         * @param scene Scene to be loaded.
         */
        public LoadedScene( Scene scene, String text ) {
            
            this.scene = scene;
            this.text = text;
            
        }
        
        /**
         * Retrieves the Scene represented by this object.
         * 
         * @return The Scene that this represents.
         */
        public Scene getScene() {
            
            return scene;
            
        }
        
        /**
         * The text to be displayed for this scene (already loaded).
         * 
         * @return The scene text.
         */
        public String getText() {
            
            return text;
            
        }
        
    }

}
