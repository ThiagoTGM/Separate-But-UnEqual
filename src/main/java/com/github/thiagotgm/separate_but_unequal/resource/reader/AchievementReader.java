package com.github.thiagotgm.separate_but_unequal.resource.reader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.github.thiagotgm.separate_but_unequal.resource.AchievementFactory;
import com.github.thiagotgm.separate_but_unequal.resource.ResourceFactory;
import com.github.thiagotgm.separate_but_unequal.resource.ResourcePath;

/**
 * Reader that extracts specific information about an Achievement resource object from a resource XML stream.
 *
 * @version 1.0
 * @author Thiago
 * @since 2017-05-28
 */
public class AchievementReader extends ResourceReader {
    
    private static final String ACHIEVEMENT_TAG = "achievement";
    private static final String STORY_CODE_TAG = AchievementFactory.STORY_CODE_ELEMENT;
    private static final String END_CODE_TAG = AchievementFactory.END_CODE_ELEMENT;
    private static final String TITLE_TAG = AchievementFactory.TITLE_ELEMENT;
    private static final String TEXT_TAG = AchievementFactory.TEXT_ELEMENT;
    private static final String GRAPHIC_TAG = AchievementFactory.GRAPHIC_ELEMENT;

    /**
     * Constructs a new AchievementReader.
     */
    protected AchievementReader() {
        // Nothing to initialize.
    }

    @Override
    protected void read( XMLEventReader reader, ResourcePath path, ResourceFactory factory ) throws XMLStreamException {

        AchievementFactory aFactory = (AchievementFactory) factory;
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

                        case STORY_CODE_TAG:
                        case END_CODE_TAG:
                        case TITLE_TAG:
                        case TEXT_TAG:
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
                    if ( ( currentTag == null ) && name.equals( ACHIEVEMENT_TAG ) ) {
                        return; // Finished reading Scene element.
                    }
                    if ( !name.equals( currentTag ) ) { // Does not match element currently being read.
                        throw new XMLStreamException( UNEXPECTED_CLOSING_TAG );
                    }
                    switch ( name ) { // Identifies what element was being read and records value appropriately.
                        
                        case STORY_CODE_TAG:
                            if ( value == null ) {
                                throw new XMLStreamException( MISSING_VALUE );
                            }
                            if ( value.length() > 1 ) { // Code not a single char.
                                throw new XMLStreamException( INVALID_VALUE );
                            }
                            char storyCode = value.charAt( 0 );
                            try {
                                aFactory.withStoryCode( storyCode );
                            } catch ( IllegalArgumentException e ) {
                                throw new XMLStreamException( INVALID_VALUE, e );
                            }
                            break;
                            
                        case END_CODE_TAG:
                            int endCode;
                            try {
                                endCode = Integer.valueOf( value );
                            } catch ( NumberFormatException e ) {
                                throw new XMLStreamException( INVALID_VALUE, e );
                            }
                            try {
                                aFactory.withEndCode( endCode );
                            } catch ( IllegalArgumentException e ) {
                                throw new XMLStreamException( INVALID_VALUE, e );
                            }
                            break;
                            
                        default:
                            aFactory.withElement( currentTag, value ); // Elements that don't need extra parsing.
                        
                    }
                    currentTag = null; // Reset temporary storage.
                    value = null;
                    
            }
            
        }
        // If nothing else to read, EOF was found before the element was closed.
        throw new XMLStreamException( UNEXPECTED_EOF );

    }

}
