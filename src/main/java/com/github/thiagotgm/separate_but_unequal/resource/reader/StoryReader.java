package com.github.thiagotgm.separate_but_unequal.resource.reader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.github.thiagotgm.separate_but_unequal.resource.ResourceFactory;
import com.github.thiagotgm.separate_but_unequal.resource.ResourcePath;
import com.github.thiagotgm.separate_but_unequal.resource.StoryFactory;

/**
 * Reader that extracts specific information about a Story resource object from a resource XML stream.
 *
 * @version 1.0
 * @author ThiagoTGM
 * @since 2017-05-23
 */
public class StoryReader extends ResourceReader {
    
    private static final String STORY_TAG = "story";
    private static final String CODE_TAG = StoryFactory.CODE_ELEMENT;
    private static final String NAME_TAG = StoryFactory.NAME_ELEMENT;
    private static final String DESCRIPTION_TAG = StoryFactory.DESCRIPTION_ELEMENT;
    private static final String START_TAG = StoryFactory.START_ELEMENT;
    private static final String GRAPHIC_TAG = StoryFactory.GRAPHIC_ELEMENT;

    /**
     * Constructs a new StoryReader.
     */
    protected StoryReader() {
        // Nothing to initialize.
    }

    @Override
    protected void read( XMLEventReader reader, ResourcePath path, ResourceFactory factory )
            throws XMLStreamException, IllegalArgumentException {

        StoryFactory sFactory = (StoryFactory) factory;
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

                        case CODE_TAG:
                        case NAME_TAG:
                        case DESCRIPTION_TAG:
                        case START_TAG:
                        case GRAPHIC_TAG:
                            currentTag = name;
                            break;
                            
                        default: // Element not recognized.
                            throw new XMLStreamException( UNEXPECTED_ELEMENT );
                        
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
                    if ( ( currentTag == null ) && name.equals( STORY_TAG ) ) {
                        return; // Finished reading Scene element.
                    }
                    if ( !name.equals( currentTag ) ) { // Does not match element currently being read.
                        throw new XMLStreamException( UNEXPECTED_CLOSING_TAG );
                    }
                    switch ( name ) { // Identifies what element was being read and records value appropriately.
                        
                        case CODE_TAG:
                            if ( value == null ) {
                                throw new XMLStreamException( MISSING_VALUE );
                            }
                            if ( value.length() > 1 ) { // Code not a single char.
                                throw new XMLStreamException( INVALID_VALUE );
                            }
                            char code = value.charAt( 0 );
                            try {
                                sFactory.withCode( code );
                            } catch ( IllegalArgumentException e ) {
                                throw new XMLStreamException( INVALID_VALUE, e );
                            }
                            break;
                            
                        default:
                            sFactory.withElement( currentTag, value ); // Elements that don't need extra parsing.
                        
                    }
                    currentTag = null; // Reset temporary storage.
                    value = null;
                    
            }
            
        }
        // If nothing else to read, EOF was found before the element was closed.
        throw new XMLStreamException( UNEXPECTED_EOF );
        
    }

}
