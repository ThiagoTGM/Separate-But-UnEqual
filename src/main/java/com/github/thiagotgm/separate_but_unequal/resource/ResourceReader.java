package com.github.thiagotgm.separate_but_unequal.resource;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.github.thiagotgm.separate_but_unequal.resource.Resource.ResourceType;

public class ResourceReader {

    public static Resource readResource( InputStream input ) throws XMLStreamException {

        String id = null;
        ResourceFactory factory = null;
        XMLEventReader reader = XMLInputFactory.newFactory().createXMLEventReader( input );
        while ( reader.hasNext() ) {
            
            XMLEvent event = reader.nextEvent();
            switch ( event.getEventType() ) {
                
                case XMLStreamConstants.START_ELEMENT:
                    StartElement start = event.asStartElement();
                    String name = start.getName().getLocalPart();
                    if ( name.equals( "resource" ) ) {
                        if ( id != null ) {
                            throw new XMLStreamException( "Format error - Duplicate tag: <resource>" );
                        }
                        @SuppressWarnings( "unchecked" )
                        Iterator<Attribute> attributes = start.getAttributes();
                        if ( !attributes.hasNext() ) {
                            throw new XMLStreamException( "Format error - <resource> missing 'id' attribute" );
                        }
                        id = attributes.next().getValue();
                    } else if ( factory != null ) {
                        throw new XMLStreamException( "Format error - Extra type tag" );
                    } else {
                        if ( id == null ) {
                            throw new XMLStreamException( "Format error - <resource> was not root tag" );
                        }
                        ResourceType type;
                        try {
                            type = ResourceType.valueOf( name.toUpperCase() );
                        } catch ( IllegalArgumentException e ) {
                            throw new XMLStreamException( "Format error - invalid resource type <" + name + ">" );
                        }
                        factory = ResourceFactory.newInstance( type, id );
                        readType( reader, factory, type );
                        
                    }
                
            }
            
        }
        return factory.build();
        
    }
    
    private static void readType( XMLEventReader reader, ResourceFactory factory, ResourceType type ) throws XMLStreamException {
        
        switch ( type ) {
            
            case SCENE:
                SceneReader.read( reader, factory );
            
        }
        
    }

}
