package phoneentry.api;

import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author Piroli
 */
public class GlobalConfig
{   
    // here save all the object that implements interfaceDataconnection
    // like jsonconnector, databaseconnector, xmlconnector etc
    private static final ArrayList<DataConnection> connections = new ArrayList<DataConnection>();
    
    public static void initializeConnections( boolean json, String jsonFilePath, boolean database, boolean xml, String xmlFilePath ) throws IOException
    {
        if( xml == true )
            connections.add( new XmlConnector( xmlFilePath ) );
        
        if( json == true )
            connections.add( new JsonConnector( jsonFilePath ) );

        if( database == true )
            connections.add( new DatabaseConnector() );
        
        
    }

    public static Iterable<DataConnection> getConnections()
    {
        return connections;
    }

    public static class getConnections {

        public getConnections() {
        }
    }


}
