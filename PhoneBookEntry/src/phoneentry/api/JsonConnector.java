package phoneentry.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Piroli
 */
public class JsonConnector implements DataConnection
{
    
    private ArrayList<PhoneEntry> phoneEntries;
    private String filePath;
    private static final java.lang.reflect.Type PHONE_ENTRY_TYPE = new TypeToken<List<PhoneEntry>>() {}.getType();
    private Object phoneEntry;
    

    // create the object string for json and with .json
    public JsonConnector( String path ) throws FileNotFoundException, IOException
    {
        
        // here check if the path was null
        if( path == null || path.isEmpty() )
            throw new IllegalArgumentException( "The object path cannot be null or empty" );
        
        // here check for the path that have  .json
        // example c:\folder1\skedar.json
     
        if( path.contains( ".json" ) == false )
            throw new IllegalArgumentException( "The file path must contain .json file name" );
        
        filePath = path;
        
        readFromFile();
    }
    
    // this method check if exist file .json
    // if exist read the file and add to the arraylist
    private void readFromFile() throws FileNotFoundException, IOException
    {
        
        File jsonFile = new File( filePath );
        
        // check if exist and for the correct path
        if( jsonFile.exists() && jsonFile.isDirectory() == false )
        {
            
            // here use the library gson for read and write json file
            Gson gson = new Gson();
            try (JsonReader reader = new JsonReader( new FileReader( filePath ) ))
            {
                phoneEntries = gson.fromJson(reader, PHONE_ENTRY_TYPE);
                reader.close();
            }
        }
        else
        {   
            // if not exist initialise empty array
            phoneEntries = new ArrayList<>();
        }
    }
    
    
    // this method use the gson library for save the file
  
    private void saveToFile() throws IOException
    {
        try (Writer writer = new FileWriter( filePath ))
        {
            Gson gson = new GsonBuilder().create();
            gson.toJson( phoneEntries ,writer); 
            writer.close();
        }
    }

    @Override
    public PhoneEntry getPhoneEntry(String number)
    {   
        // check if the parmeter was null 
        
       if( phoneEntries == null  ) return null;
       
       for( PhoneEntry entry : phoneEntries )
       {
           if( entry.getNumber().equals( number ) )
               return entry;
       }
       
       return null;
       
    }
    
    // return all the arraylist
    @Override
    public Iterable<PhoneEntry> getPhoneEntries()
    {
       return phoneEntries;
    }
  
    
    // save the new element and save with json file
    @Override
    public boolean savePhoneEntry(PhoneEntry entry)
    {   
        // check if objeck was null
        if( entry == null )
            throw new IllegalArgumentException( "Entry must not be null" );
        
      //  check for the number if was in arraylist
        for( PhoneEntry iterator : phoneEntries )
        {
            if( iterator.getNumber().equalsIgnoreCase( entry.getNumber() ) )
                throw new IllegalArgumentException( "The number: " + entry.getNumber() + " exists. Number must be unique" );
        }

        boolean toReturn = false;
        
        try
        {   
            phoneEntries.add(entry); // first save in arraylist 
            saveToFile(); // arraylist save in the file json
            toReturn = true;
        } catch (IOException ex)
        {
            Logger.getLogger(JsonConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return toReturn;
    }
    

    @Override
    public boolean savePhoneEntries(ArrayList<PhoneEntry> entrys)
    {
       if( entrys == null )
           throw new IllegalArgumentException( "Entrys must not be null" );
       
       
       for( PhoneEntry actualEntry : phoneEntries )
       {
           for( PhoneEntry newEntry: entrys )
           {
               if( actualEntry.getNumber().equalsIgnoreCase( newEntry.getNumber() ))
                   throw new IllegalArgumentException( "The number: " + newEntry.getNumber() + " exists. \n Number must be unique" );
           }    
       }
       
       boolean toReturn = false;
       
        try
        {   
            phoneEntries.addAll(entrys);
            saveToFile();
            toReturn = true;
        } catch (IOException ex)
        {
            Logger.getLogger(JsonConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
       return toReturn;
    }
    
    //here are the method for edit the file
    @Override
    public boolean editPhoneEntry(PhoneEntry oldEntry, PhoneEntry newEntry )
    {
       if( oldEntry == null )
            throw new IllegalArgumentException( " oldEntry must not be null" );
       
       if( newEntry == null )
           throw new IllegalArgumentException( " newEntry must not be null" );
       
       boolean found = false;
       
       // check for the old object
       for( int i = 0; i < phoneEntries.size(); i++ )
       {    
           // if was
           if( phoneEntries.get(i).equals( oldEntry ) )
           {    
               // replace with new object
               phoneEntries.set(i, newEntry);
               found = true;
               break;
           }
       }
       
       // if was not
       if( !found )
       {
          throw new IllegalArgumentException( " oldEntry must be in the collection to edit" );
       }
               
        try
        {
            saveToFile();
            found = true;
        } catch (IOException ex)
        {
            Logger.getLogger(JsonConnector.class.getName()).log(Level.SEVERE, null, ex);
            found = false;
        }
        
        return found;
    }

    @Override
    public boolean deletePhoneEntry(String number)
    {
        if( number == null || number.isEmpty() )
            throw new IllegalArgumentException( " number must not be null or empty" );
        
        boolean toReturn = false;
        
        for( int i = 0; i < phoneEntries.size(); i++ )
        {
            if( phoneEntries.get(i).getNumber().equals(number))
            {
                phoneEntries.remove(i);
                toReturn = true;
                break;
            }
        }
        
        if( ! toReturn )
        {
            throw new IllegalArgumentException( " number must be in the collection" );
        }
        
        try
        {   
            saveToFile();
            toReturn = true;
        } catch (IOException ex)
        {
            Logger.getLogger(JsonConnector.class.getName()).log(Level.SEVERE, null, ex);
            toReturn = false;
        }
     
        return toReturn;
        
    }

    @Override
    public boolean deleteAllPhoneEntries()
    {   
        boolean toReturn = false;
        
        phoneEntries.clear();
        
        try
        {
            saveToFile();
            toReturn = true;
        } catch (IOException ex)
        {
            Logger.getLogger(JsonConnector.class.getName()).log(Level.SEVERE, null, ex);
            toReturn = false;
        }
        
        return toReturn;
    }


    @Override
    public Iterable<PhoneEntry> orderByFirstName(boolean desc) {
        
      if( phoneEntries == null || phoneEntries.size() == 1 ) return phoneEntries;
      
      List<PhoneEntry> listOfPhoneEntries =  phoneEntries ;
      
      if( desc )
      {
           listOfPhoneEntries.sort( Comparator.comparing( PhoneEntry::getFirstName).reversed() );
      }
      else
      {
           listOfPhoneEntries.sort( Comparator.comparing( PhoneEntry::getFirstName) );
      }
      
      return listOfPhoneEntries;
    }

    @Override
    public Iterable<PhoneEntry> orderbyLastName(boolean desc) {
        
      if( phoneEntries == null || phoneEntries.size() == 1 ) return phoneEntries;
      
      List<PhoneEntry> listOfPhoneEntries =  phoneEntries ;
      
      if( desc )
      {
           listOfPhoneEntries.sort( Comparator.comparing( PhoneEntry::getLastName).reversed() );
      }
      else
      {
           listOfPhoneEntries.sort( Comparator.comparing( PhoneEntry::getLastName) );
      }
      
      return listOfPhoneEntries;
    }
}
