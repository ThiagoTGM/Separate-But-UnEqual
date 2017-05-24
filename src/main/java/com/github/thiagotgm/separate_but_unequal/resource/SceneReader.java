package com.github.thiagotgm.separate_but_unequal.resource;

import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class SceneReader extends ResourceReader {

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

}
