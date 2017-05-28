package com.github.thiagotgm.separate_but_unequal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.thiagotgm.separate_but_unequal.gui.GamePanel;
import com.github.thiagotgm.separate_but_unequal.resource.Choice;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceManager;
import com.github.thiagotgm.separate_but_unequal.resource.Scene;
import com.github.thiagotgm.separate_but_unequal.resource.ChoiceScene;

/**
 * Manages the game flow during normal gameplay.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-25
 */
public class GameManager implements ActionListener, Runnable {
    
    private static final Logger log = LoggerFactory.getLogger( GameManager.class );
    
    /** Thread name to be used for objects of this type. */
    public static final String THREAD_NAME = "Game Manager";
    
    private final GamePanel panel;
    private final SceneDisplayer sceneDisplayer;
    private final ChoiceDisplayer choiceDisplayer;
    
    private List<Choice> currentOptions;
    private Loader buffer;
    private Thread textThread;
    private Thread bufferThread;
    
    private LoadedScene nextScene;
    private Thread managerThread;

    /**
     * Starts a new Manager that displays the game on the given panel.
     * 
     * @param panel The panel where the game is to be displayed.
     */
    public GameManager( GamePanel panel ) {

        this.panel = panel;
        sceneDisplayer = new SceneDisplayer( panel.getSceneArea() );
        choiceDisplayer = new ChoiceDisplayer( panel.getOptionsArea() );
        panel.addActionListener( this );
        
    }
    
    /**
     * Executes actions corresponding to certain buttons in the game panel.
     *
     * @param e Triggered event.
     */
    @Override
    public void actionPerformed( ActionEvent e ) {

        String command = e.getActionCommand();
        log.trace( "Command received: " + command );
        int current;
        switch ( command ) {
            
            case GamePanel.SKIP_COMMAND: // Skip text displaying process (just print full text).
                textThread.interrupt();
                sceneDisplayer.skip();
                break;
            case GamePanel.UP_COMMAND: // Move one choice up.
                current = choiceDisplayer.getSelected();
                if ( current > 0 ) {
                    choiceDisplayer.setSelected( current - 1 );
                }
                break;
            case GamePanel.DOWN_COMMAND: // Move one choice down.
                current = choiceDisplayer.getSelected();
                if ( current < currentOptions.size() - 1 ) {
                    choiceDisplayer.setSelected( current + 1 );
                }
                break;
            case GamePanel.SELECT_COMMAND: // Select current choice.
                current = choiceDisplayer.getSelected();
                try {
                    bufferThread.join();
                } catch ( InterruptedException e1 ) {
                    log.warn( "Buffer thread was interrupted." );
                }
                LoadedScene next = buffer.getLoadedScenes().get( current );
                if ( next != null ) {
                    nextScene = next;
                    runNext();
                } else {
                    JOptionPane.showMessageDialog( panel, "The target specified by this option is invalid.",
                            "Target Error", JOptionPane.ERROR_MESSAGE );
                }
            
        }
        
    }
    
    /**
     * Starts the game on a specified Scene.
     * 
     * @param startSceneID The Resource ID of the first scene to be shown.
     */
    public void start( String startSceneID ) {
        
        Scene target = ( Scene ) ResourceManager.getResource( startSceneID );
        nextScene = new Loader().load( target );
        runNext();
        
    }
    
    /**
     * Runs the next scene on a separate thread.<br>
     * Cleans up any old threads beforehand.
     */
    public void runNext() {
        
        if ( managerThread != null ) {
            managerThread.interrupt();
        }
        if ( textThread != null ) {
            textThread.interrupt();
        }
        if ( bufferThread != null ) {
            bufferThread.interrupt();
        }
        managerThread = new Thread( this, THREAD_NAME );
        managerThread.start();
        
    }
    
    /**
     * Runs the next scene.<br>
     * Includes displaying the text+graphic, buffering the next scenes, and starting the user choice process.<br>
     * Don't run this manually, call {@link #runNext()} instead as it performs cleanup for old threads that might be
     * still running.
     * 
     * @param scene Scene to be ran next.
     */
    @Override
    public void run() {

        LoadedScene scene = nextScene;
        /* Display scene */
        panel.setOptionButtonsEnabled( false );
        panel.setSkipButtonEnabled( true );
        choiceDisplayer.clear();
        sceneDisplayer.showScene( scene.getText() );
        textThread = new Thread( sceneDisplayer, SceneDisplayer.THREAD_NAME );
        textThread.start();
        currentOptions = ( (ChoiceScene) scene.getScene() ).getOptions();
        bufferNextScenes( currentOptions );
        try {
            textThread.join();
        } catch ( InterruptedException e ) {
            // Normal
        }
        
        /* Scene displayed. Get player choice. */
        panel.setSkipButtonEnabled( false );
        panel.setOptionButtonsEnabled( true );
        choiceDisplayer.showOptions( currentOptions );
        
        /* End of thread */
        managerThread = null;
        
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
            if ( target == null ) {
                log.warn( "Invalid target: " + possible.getTarget() );
            }
            targets.add( target );
            
        }
        buffer = new Loader( targets );
        bufferThread = new Thread( buffer, Loader.THREAD_NAME );
        bufferThread.start();

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
                
                if ( target == null ) {
                    product.add( null );
                } else {
                    product.add( load( target ) );
                }
                
            }
            this.product = product; // Stores finished result.
            log.debug( "Done buffering files." );
            
        }
        
        
        /**
         * Retrieves the loaded Scenes once they are fully loaded.<br>
         * If this Loader was given no targets, returns the empty list.
         * 
         * @return All the Scenes with all associated resources loaded.
         * @throws IllegalStateException if used before the loading process completed.
         */
        public List<LoadedScene> getLoadedScenes() throws IllegalStateException {
            
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
            return builder.toString().trim(); // Finished reading file.
            
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
