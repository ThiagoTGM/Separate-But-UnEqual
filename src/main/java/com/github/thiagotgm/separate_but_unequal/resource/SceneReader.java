package com.github.thiagotgm.separate_but_unequal.resource;

import java.nio.file.Path;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Reader that extracts specific information about a Scene resource object from a resource XML stream.
 *
 * @version 1.0
 * @author ThiagoTGM
 * @since 2017-05-23
 */
public abstract class SceneReader extends ResourceReader {
    
    private static final String SCENE_TAG = "scene";
    private static final String FILENAME_TAG = "filename";
    private static final String GRAPHIC_TAG = "graphic";
    private static final String AUDIO_TAG = "audio";
    
    /**
     * Constructs a new SceneReader.
     */
    protected SceneReader() {
        // Nothing to initialize.
    }

    @Override
    protected void read( XMLEventReader reader, ResourcePath path, ResourceFactory factory ) throws XMLStreamException  {
        
        SceneFactory sFactory = (SceneFactory) factory;
        String currentTag = null;
        String value = null;
        while ( reader.hasNext() ) {
            
            XMLEvent event = reader.nextEvent();
            switch ( event.getEventType() ) {
                
                /* Opening tag */
                case XMLStreamConstants.START_ELEMENT:
                    if ( currentTag != null ) {
                        throw new XMLStreamException( UNEXPECTED_ELEMENT );
                    }
                    StartElement start = event.asStartElement();
                    String name = start.getName().getLocalPart();
                    switch ( name ) {
                        
                        case FILENAME_TAG:                                                      
                        case GRAPHIC_TAG:                            
                        case AUDIO_TAG:
                            currentTag = name;
                            break;
                              
                        default: // Element not recognized.
                            readSpecificElement( reader, sFactory, name ); // Try subclass-specific elements.
                        
                    }
                    break;
                    
                /* Text */
                case XMLStreamConstants.CHARACTERS:
                    if ( currentTag != null ) { // If currently reading an element, reads the text.   
                        value = event.asCharacters().getData().trim();
                        if ( value.isEmpty() ) {
                            value = null; // Empty text is not valid.
                        }
                    }
                    break;
                    
                /* Closing tag */
                case XMLStreamConstants.END_ELEMENT:
                    EndElement end = event.asEndElement();
                    name = end.getName().getLocalPart();
                    if ( ( currentTag == null ) && name.equals( SCENE_TAG ) ) {
                        return; // Finished reading Scene element.
                    }
                    if ( !name.equals( currentTag ) ) { // Does not match element currently being read.
                        throw new XMLStreamException( UNEXPECTED_CLOSING_TAG );
                    }
                    switch ( name ) { // Identifies what element was being read and records value appropriately.
                        
                        case FILENAME_TAG:
                            Path filePath = path.getPath();
                            Path textPath = filePath.resolveSibling( value );
                            sFactory.withPath( new ResourcePath( textPath, path.inJar() ) );
                            break;
                            
                        case GRAPHIC_TAG:   
                            sFactory.withGraphic( value );
                            break;
                            
                        case AUDIO_TAG:
                            sFactory.withAudio( value );
                            break;
                        
                    }
                    currentTag = null; // Reset temporary storage.
                    value = null;
                
            }
            
        }
        // If nothing else to read, EOF was found before the element was closed.
        throw new XMLStreamException( UNEXPECTED_EOF );
        
    }
    
    /**
     * Reads an element specific to the subtype of Scene that the Reader extending this is reading.
     * 
     * @param reader Reader going through the XML document. Its last return should have been
     *               the opening tag of the subtype-specific element.
     * @param factory The factory constructing the Resource.
     * @param element The type of element to be read.
     * @throws XMLStreamException if a format error was encountered in the element, or if that element does not
     *                            exist in the subtype.
     */
    public abstract void readSpecificElement( XMLEventReader reader, ResourceFactory factory, String element )
            throws XMLStreamException;

}
