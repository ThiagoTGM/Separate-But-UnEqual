package com.github.thiagotgm.separate_but_unequal.resource;

import java.util.LinkedList;
import java.util.List;

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
public class SceneReader extends ResourceReader {
    
    private static final String SCENE_TAG = "scene";
    private static final String FILENAME_TAG = "filename";
    private static final String TRANSITION_TAG = "transition";
    private static final String GRAPHIC_TAG = "graphic";
    private static final String AUDIO_TAG = "audio";
    private static final String OPTIONS_TAG = "options";
    private static final String CHOICE_TAG = "option";
    private static final String CHOICE_TEXT_TAG = "text";
    private static final String CHOICE_TARGET_TAG = "target";

    @Override
    protected void read( XMLEventReader reader, ResourceFactory factory, boolean inJar ) throws XMLStreamException  {
        
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
                        case TRANSITION_TAG:                            
                        case GRAPHIC_TAG:                            
                        case AUDIO_TAG:
                            currentTag = name;
                            break;
                            
                        case OPTIONS_TAG:
                            sFactory.withOptions( readOptions( reader ) );
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
                    if ( ( currentTag == null ) && name.equals( SCENE_TAG ) ) {
                        return; // Finished reading Scene element.
                    }
                    if ( !name.equals( currentTag ) ) { // Does not match element currently being read.
                        throw new XMLStreamException( UNEXPECTED_CLOSING_TAG );
                    }
                    switch ( name ) { // Identifies what element was being read and records value appropriately.
                        
                        case FILENAME_TAG:
                            sFactory.withFilename( value );
                            break;
                            
                        case TRANSITION_TAG:
                            sFactory.withTransition( value );
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
     * Reads the options list described by a {@value #OPTIONS_TAG} element.
     *
     * @param reader Reader going through the XML document. Its last return should have been
     *               the opening tag of the {@value #OPTIONS_TAG} element.
     * @return The list of Choices described by that element.
     * @throws XMLStreamException If a format error was encountered in the element.
     */
    private List<Choice> readOptions( XMLEventReader reader ) throws XMLStreamException {
        
        List<Choice> options = new LinkedList<>();
        while ( reader.hasNext() ) {
            
            XMLEvent event = reader.nextEvent();
            String name;
            switch ( event.getEventType() ) {
                
                /* Opening tag */
                case XMLStreamConstants.START_ELEMENT:
                    StartElement start = event.asStartElement();
                    name = start.getName().getLocalPart();
                    if ( name.equals( CHOICE_TAG ) ) { // Found a Choice.
                        options.add( readChoice( reader ) );
                    } else { // Unrecognized subelement.
                        throw new XMLStreamException( UNEXPECTED_ELEMENT );
                    }
                    break;
                    
                /* Closing tag */
                case XMLStreamConstants.END_ELEMENT:
                    EndElement end = event.asEndElement();
                    name = end.getName().getLocalPart();
                    if ( name.equals( OPTIONS_TAG ) ) { // Finished element.
                        return options;
                    } else { // Closing tag not recognized.
                        throw new XMLStreamException( UNEXPECTED_CLOSING_TAG );
                    }
                
            }
            
        }
        // If nothing else to read, EOF was found before the element was closed.
        throw new XMLStreamException( UNEXPECTED_EOF );
        
    }
    
    /**
     * Reads a Choice from a {@value #CHOICE_TAG} element from the given EventReader.
     *
     * @param reader Reader going through the XML document. Its last return should have been
     *               the opening tag of the {@value #CHOICE_TAG} element.
     * @return The choice object representing that element.
     * @throws XMLStreamException If a format error was encountered in the element.
     */
    private Choice readChoice( XMLEventReader reader ) throws XMLStreamException {
        
        String text = null;
        boolean isText = false;
        String target = null;
        boolean isTarget = false;
        while ( reader.hasNext() ) {
            
            XMLEvent event = reader.nextEvent();
            String name;
            switch ( event.getEventType() ) {
                
                /* Opening tag */
                case XMLStreamConstants.START_ELEMENT:
                    if ( isText || isTarget ) { // <text> and <target> shouldn't have subelements.
                        throw new XMLStreamException( UNEXPECTED_ELEMENT );
                    }
                    StartElement start = event.asStartElement();
                    name = start.getName().getLocalPart();
                    if ( name.equals( CHOICE_TEXT_TAG ) ) { // <text> encountered.
                        isText = true;
                    } else if ( name.equals( CHOICE_TARGET_TAG ) ) { // <target> encountered.
                        isTarget = true;
                    } else { // tag not recognized.
                        throw new XMLStreamException( UNEXPECTED_ELEMENT );
                    }
                    break;
                    
                /* Text */
                case XMLStreamConstants.CHARACTERS:
                    String value = event.asCharacters().getData().trim();
                    if ( value.isEmpty() ) {
                        value = null; // Empty text is not valid.
                    }
                    if ( isText ) { // Text in <text>.
                        text = value;
                    } else if ( isTarget ) { // Text in <target>.
                        target = value;
                    }
                    break;
                    
                /* Closing tag */
                case XMLStreamConstants.END_ELEMENT:
                    EndElement end = event.asEndElement();
                    name = end.getName().getLocalPart();
                    if ( isText && name.equals( CHOICE_TEXT_TAG ) ) { // Was reading <text> and found </text>.
                        isText = false;
                    } else if ( isTarget && name.equals( CHOICE_TARGET_TAG ) ) { // Was reading <target> and found </target>.
                        isTarget = false;
                    } else if ( !( isText || isTarget ) && name.equals( CHOICE_TAG ) ) { // Wasn't reading <text> or <target>
                        if ( ( text != null ) && ( target != null ) ) {                  // and found end of choice element.
                            return new Choice( text, target ); // All elements present, build and return.
                        } else { // Missing text or target.
                            throw new XMLStreamException( String.format( MISSING_ELEMENTS, "choice" ) );
                        }
                    } else { // Tag not recognized.
                        throw new XMLStreamException( UNEXPECTED_CLOSING_TAG );
                    }
                    
            }
            
        }
        // If nothing else to read, EOF was found before the element was closed.
        throw new XMLStreamException( UNEXPECTED_EOF );
        
    }

}
