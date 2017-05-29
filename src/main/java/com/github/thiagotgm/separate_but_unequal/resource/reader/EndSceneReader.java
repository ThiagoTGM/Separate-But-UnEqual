package com.github.thiagotgm.separate_but_unequal.resource.reader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;

import com.github.thiagotgm.separate_but_unequal.resource.EndSceneFactory;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceFactory;

/**
 * Implementation of SceneReader that reads a Scene of specific type EndScene.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-27
 */
public class EndSceneReader extends SceneReader {
    
    private static final String END_CODE_TAG = EndSceneFactory.CODE_ELEMENT;

    /**
     * Constructs a new EndSceneReader.
     */
    protected EndSceneReader() {
        // Nothing to initialize.
    }

    @Override
    public void readSpecificElement( XMLEventReader reader, ResourceFactory factory, String element )
            throws XMLStreamException {

        EndSceneFactory esFactory = (EndSceneFactory) factory;
        switch ( element ) {
            
            case END_CODE_TAG:
                int code = readCode( reader );
                try {
                    esFactory.withCode( code );
                } catch ( IllegalArgumentException e ) {
                    throw new XMLStreamException( INVALID_VALUE, e );
                }
                break;
                
            default: // Element not recognized.
                throw new XMLStreamException( UNEXPECTED_ELEMENT );
            
        }

    }
    
    /**
     * Reads the code value described by a {@value #END_CODE_TAG} element.
     * 
     * @param reader Reader going through the XML document. Its last return should have been
     *               the opening tag of the {@value #END_CODE_TAG} element.
     * @return The integer value of the {@value #END_CODE_TAG} element.
     * @throws XMLStreamException If a format error was encountered in the element.
     */
    private int readCode( XMLEventReader reader ) throws XMLStreamException {
        
        int code = 0;
        boolean found = false;
        while ( reader.hasNext() ) {
            
            XMLEvent event = reader.nextEvent();
            String name;
            switch ( event.getEventType() ) {
                
                /* Opening tag */
                case XMLStreamConstants.START_ELEMENT:
                    throw new XMLStreamException( UNEXPECTED_ELEMENT );
                    
                /* Text */
                case XMLStreamConstants.CHARACTERS:
                    String data = event.asCharacters().getData();
                    try {
                        code = Integer.valueOf( data );
                    } catch ( NumberFormatException e ) { // Also catches format exceptions.
                        throw new XMLStreamException( INVALID_VALUE, e );
                    }
                    found = true;
                    break;
                    
                    /* Closing tag */
                case XMLStreamConstants.END_ELEMENT:
                    EndElement end = event.asEndElement();
                    name = end.getName().getLocalPart();
                    if ( name.equals( END_CODE_TAG ) ) {
                        if ( found ) {
                            return code;
                        } else {
                            throw new XMLStreamException( MISSING_VALUE );
                        }
                    } else {
                        throw new XMLStreamException( UNEXPECTED_CLOSING_TAG );
                    }
                    
            }
            
        }
        // If nothing else to read, EOF was found before the element was closed.
        throw new XMLStreamException( UNEXPECTED_EOF );
        
    }

}
