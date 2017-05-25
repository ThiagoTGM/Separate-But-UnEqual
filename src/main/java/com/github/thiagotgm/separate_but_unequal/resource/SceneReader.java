package com.github.thiagotgm.separate_but_unequal.resource;

import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class SceneReader extends ResourceReader {
    
    private static final String CHOICE_TAG = "choice";
    private static final String CHOICE_TEXT_TAG = "text";
    private static final String CHOICE_TARGET_TAG = "target";

    protected void read( XMLEventReader reader, ResourceFactory factory, boolean inJar ) throws XMLStreamException  {
        
        SceneFactory sFactory = (SceneFactory) factory;
        while ( reader.hasNext() ) {
            
            XMLEvent event = reader.nextEvent();
            switch ( event.getEventType() ) {
                
                case XMLStreamConstants.START_ELEMENT:
                    StartElement start = event.asStartElement();
                    String name = start.getName().getLocalPart();
                    switch ( name ) {
                        
                        case "filename":
                            event = reader.nextEvent();
                            if ( !event.isCharacters() ) {
                                throw new XMLStreamException( "Format error - <filename> has invalid content" );
                            }
                            sFactory.withFilename( event.asCharacters().getData().trim() );
                            break;
                            
                        case "transition":
                            event = reader.nextEvent();
                            if ( !event.isCharacters() ) {
                                throw new XMLStreamException( "Format error - <transition> has invalid content" );
                            }
                            sFactory.withTransition( event.asCharacters().getData().trim() );
                            break;
                            
                        case "graphic":
                            event = reader.nextEvent();
                            if ( !event.isCharacters() ) {
                                throw new XMLStreamException( "Format error - <graphic> has invalid content" );
                            }
                            sFactory.withGraphic( event.asCharacters().getData().trim() );
                            break;
                            
                        case "audio":
                            event = reader.nextEvent();
                            if ( !event.isCharacters() ) {
                                throw new XMLStreamException( "Format error - <audio> has invalid content" );
                            }
                            sFactory.withAudio( event.asCharacters().getData().trim() );
                            break;
                            
                        case "options":
                            List<Choice> options = new LinkedList<Choice>();
                            event = reader.nextEvent();
                            while ( event.isCharacters() ) {
                                event = reader.nextEvent();
                            }
                            while ( event.isStartElement() ) {
                                
                                start = event.asStartElement();
                                if ( !start.getName().getLocalPart().equals( "option" ) ) {
                                    throw new XMLStreamException( "Format error - Unsupported tag inside <options> block" );
                                }
                                String text = null;
                                String target = null;
                                for ( int i = 0; i < 2; i++ ) {
                                    
                                    event = reader.nextEvent();
                                    while ( event.isCharacters() ) {
                                        event = reader.nextEvent();
                                    }
                                    if ( !event.isStartElement() ) {
                                        throw new XMLStreamException( "Format error - Invalid member of <option> block" );
                                    }
                                    String innerName = event.asStartElement().getName().getLocalPart();
                                    switch ( innerName ) {
                                        
                                        case "text":
                                            if ( text != null ) {
                                                throw new XMLStreamException( "Format error - Duplicate <text> in <option> block" );
                                            }
                                            event = reader.nextEvent();
                                            if ( !event.isCharacters() ) {
                                                throw new XMLStreamException( "Format error - <text> has invalid content" );
                                            }
                                            text = event.asCharacters().getData().trim();
                                            break;
                                            
                                        case "target":
                                            if ( target != null ) {
                                                throw new XMLStreamException( "Format error - Duplicate <target> in <option> block" );
                                            }
                                            event = reader.nextEvent();
                                            if ( !event.isCharacters() ) {
                                                throw new XMLStreamException( "Format error - <target> has invalid content" );
                                            }
                                            target = event.asCharacters().getData().trim();
                                            break;
                                            
                                        default:
                                            throw new XMLStreamException( "Format error - invalid tag" );
                                            
                                    }
                                    event = reader.nextEvent();
                                    if ( !event.isEndElement() || !event.asEndElement().getName().getLocalPart().equals( innerName ) ) {
                                        throw new XMLStreamException( "Format error - missing closing tag" );
                                    }
                                            
                                    
                                }
                                options.add( new Choice( text, target ) );
                                event = reader.nextEvent();
                                while ( event.isCharacters() ) {
                                    event = reader.nextEvent();
                                }
                                if ( !event.isEndElement() || !event.asEndElement().getName().getLocalPart().equals( "option" ) ) {
                                    throw new XMLStreamException( "Format error - missing closing tag" );
                                }
                                
                                
                            }
                            sFactory.withOptions( options );
                            break;
                            
                        default:
                            //throw new XMLStreamException( "Format error - invalid tag" );
                        
                    }
                    event = reader.nextEvent();
                    while ( event.isCharacters() ) {
                        event = reader.nextEvent();
                    }
                    if ( !event.isEndElement() || !event.asEndElement().getName().getLocalPart().equals( name ) ) {
                        //throw new XMLStreamException( "Format error - missing closing tag" );
                    }
                    break;
                    
                case XMLStreamConstants.END_ELEMENT:
                    if ( event.asEndElement().getName().getLocalPart().equals( "scene" ) ) {
                        return;
                    }
                
            }
            
        }
        
    }
    
    /**
     * Reads a "choice" element from the given EventReader.
     *
     * @param reader Reader going through the XML document. Its last return should have been
     *               the opening tag of the "choice" element.
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
                        throw new XMLStreamException( UNEXPECTED_SUBELEMENT );
                    }
                    StartElement start = event.asStartElement();
                    name = start.getName().getLocalPart();
                    if ( name.equals( CHOICE_TEXT_TAG ) ) { // <text> encountered.
                        isText = true;
                    } else if ( name.equals( CHOICE_TARGET_TAG ) ) { // <target> encountered.
                        isTarget = true;
                    } else { // tag not recognized.
                        throw new XMLStreamException( UNEXPECTED_SUBELEMENT );
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
                    } else { // Text that was not in <text> or <target>.
                        throw new XMLStreamException( UNEXPECTED_TEXT );
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
        // If nothing else to read, EOF was found before the element ended.
        throw new XMLStreamException( UNEXPECTED_EOF );
        
    }

}
