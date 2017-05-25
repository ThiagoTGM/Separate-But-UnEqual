package com.github.thiagotgm.separate_but_unequal.resource;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.github.thiagotgm.separate_but_unequal.resource.Resource.ResourceType;

/**
 * Class that provides a method to read Resource data from an XML Stream.
 * Subclasses must be provided that implement reading data for particular resource types.
 *
 * @version 1.0
 * @author ThiagoTGM
 * @since 2017-05-24
 */
public abstract class ResourceReader {
    
    /* Strings used for exception messages */
    // Element errors.
    protected static final String MISSING_ELEMENTS = "<%s> element missing required subelements.";
    
    // Unexpected events.
    protected static final String UNEXPECTED_ELEMENT = "Unexpected element encountered.";
    protected static final String UNEXPECTED_CLOSING_TAG = "Unexpected closing tag.";
    protected static final String UNEXPECTED_EOF = "Unexpected EOF encountered.";
    /***************************************/

    private static final String ROOT = "resource";
    
    /**
     * Reads resource information from the given input string.
     *
     * @param input The input stream to read from.
     * @param inJar Whether the resource file is stored in a Jar (if it is not, it is in the normal filesystem).
     * @return The resource described in the stream.
     * @throws XMLStreamException if a parsing error occurred.
     */
    public static Resource readResource( InputStream input, boolean inJar ) throws XMLStreamException {

        String id = null;
        ResourceFactory factory = null;
        XMLEventReader reader = XMLInputFactory.newFactory().createXMLEventReader( input );
        while ( reader.hasNext() ) { // Reads each event in the stream.
            
            XMLEvent event = reader.nextEvent();
            String name;
            switch ( event.getEventType() ) {
                
                /* Opening tag */
                case XMLStreamConstants.START_ELEMENT:
                    StartElement start = event.asStartElement();
                    name = start.getName().getLocalPart();
                    /* Root element */
                    if ( id == null ) {
                        
                        if ( !name.equals( ROOT ) ) { // Checks if correct root element name.
                            throw new XMLStreamException( "Invalid root element." );
                        }
                        @SuppressWarnings( "unchecked" )
                        Iterator<Attribute> attributes = start.getAttributes();
                        if ( !attributes.hasNext() ) { // Retrieves Resource ID from attribute.
                            throw new XMLStreamException( "Missing Resource ID attribute in root element." );
                        }
                        id = attributes.next().getValue();
                        
                    /* Type element */
                    } else if ( factory != null ) { // Checks if the resource type was already found previously.
                        throw new XMLStreamException( "Extra type element found." );
                    } else {
                        ResourceType type;
                        try { // Identifies resource type.
                            type = ResourceType.valueOf( name.toUpperCase() );
                        } catch ( IllegalArgumentException e ) {
                            throw new XMLStreamException( "Invalid Resource type <" + name + ">." );
                        }
                        factory = ResourceFactory.newInstance( type, id ); // Reads type-specific values.
                        readType( reader, factory, inJar, type );
                        
                    }
                    break;
                    
                /* Closing tag */
                case XMLStreamConstants.END_ELEMENT:
                    EndElement end = event.asEndElement();
                    name = end.getName().getLocalPart();
                    if ( name.equals( ROOT ) && ( id != null ) ) { // Found end of root element (and it was opened).
                        if ( factory != null ) { // Type element was found.
                            try { // Done reading resource.
                                return factory.build();
                            } catch ( IllegalStateException e ) { // A required element was missing.
                                throw new XMLStreamException( "Missing required element: " + e.getMessage() );
                            }
                        } else { // Type element not found.
                            throw new XMLStreamException( "Missing specific resource type element." );
                        }
                    } else { // Closing tag that is not for the root element.
                        throw new XMLStreamException( UNEXPECTED_CLOSING_TAG );
                    }
                
            }
            
        }
        // If reached here, then the root element was not found (obs: if the closing tag is missing it is not recognized).
        throw new XMLStreamException( "Missing root element." );
        
    }
    
    /**
     * Reads the type-specific information.
     *
     * @param reader Reader going through the resource file stream.
     * @param factory Factory that is constructing the Resource.
     * @param inJar Whether the resource file is stored in a Jar.
     * @param type The type of the resource.
     * @throws XMLStreamException if a parsing error occurred.
     */
    private static void readType( XMLEventReader reader, ResourceFactory factory, boolean inJar, ResourceType type ) throws XMLStreamException {
        
        ResourceReader resReader;
        /* Gets the appropriate Reader for the given type */
        switch ( type ) {
            
            case SCENE:
                resReader = new SceneReader();
                break;
            default: // Type does not have a Reader.
                throw new IllegalArgumentException( "No reader available for the given Resource type." );
            
        }
        resReader.read( reader, factory, inJar ); // Read type-specific elements.
        
    }
    
    /**
     * Reads type-specific information from a stream being read by an EventReader, placing the data
     * in the given ResourceFactory.
     *
     * @param reader Reader going through the resource file stream. Its last return should have been
     *               the opening tag of the resource-type element.
     * @param factory The factory constructing the Resource.
     * @param inJar Whether the resource file is stored in a Jar.
     * @throws XMLStreamException if a parsing error occurred.
     */
    protected abstract void read( XMLEventReader reader, ResourceFactory factory, boolean inJar ) throws XMLStreamException, IllegalArgumentException;

}
