/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phoneentry.api;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Piroli
 */
public class XmlConnector implements DataConnection {

    private ArrayList<PhoneEntry> phoneEntries;
    private String filePath;

    public XmlConnector(String path) throws FileNotFoundException, IOException {
        // check if the path was null and do  exception
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("The object path cannot be null or empty");
        }

        // check if path have  .xml
        // example c:\folder1\skedar.xml
        // if dont have do exeption
        if (path.contains(".xml") == false) {
            throw new IllegalArgumentException("The file path must contain .xml file name");
        }

        filePath = path;

        readFromFile();
    }

    private void readFromFile() throws FileNotFoundException, IOException {
        File xmlFile = new File(filePath);

        // check if exists and the path is right
        if (xmlFile.exists() && xmlFile.isDirectory() == false) {
            FileReader fileReader = new FileReader(filePath);  // load our xml file 
            XStream xstream = new XStream();
            xstream.alias("phoneEntry", PhoneEntry.class);
            phoneEntries = (ArrayList<PhoneEntry>) xstream.fromXML(fileReader);
            fileReader.close();
        } else {

            // inicialise arraylist empty
            phoneEntries = new ArrayList<>();
        }

          }

    private void saveToFile() {
        FileOutputStream fos = null;
        String xml = "";
        XStream xstream = new XStream();
        
        for(PhoneEntry entry : phoneEntries )
        {
            xml +=  System.getProperty("line.separator") + xstream.toXML( entry );
        }
        
        try {
            fos = new FileOutputStream(filePath);
            fos.write("<?xml version=\"1.0\"?>".getBytes("UTF-8")); //write XML header, as XStream doesn't do that for us
            byte[] bytes = xml.getBytes("UTF-8");
            fos.write(bytes);

        } catch (Exception e) {
            e.printStackTrace(); // this obviously needs to be refined.
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace(); // this obviously needs to be refined.
                }
            }
        }
    }

    @Override
    public PhoneEntry getPhoneEntry(String number) {
    
        if (phoneEntries == null) {
            return null;
        }

        for (PhoneEntry entry : phoneEntries) {
            if (entry.getNumber().equals(number)) {
                return entry;
            }
        }

        return null;
    }

    // return all the arraylisten
    @Override
    public Iterable<PhoneEntry> getPhoneEntries() {
        return phoneEntries;
    }

    @Override
    public boolean savePhoneEntry(PhoneEntry entry) {
        // check if the object was null
        if (entry == null) {
            throw new IllegalArgumentException("Entry must not be null");
        }

        // check for the number if was in the arraylist
        for (PhoneEntry iterator : phoneEntries) {
            if (iterator.getNumber().equalsIgnoreCase(entry.getNumber())) {
                throw new IllegalArgumentException("The number: " + entry.getNumber() + " exists. Number must be unique");
            }
        }

        boolean toReturn = false;

        phoneEntries.add(entry);
        saveToFile();
        toReturn = true;

        return toReturn;
    }

    @Override
    public boolean savePhoneEntries(ArrayList<PhoneEntry> entrys) {

        if (entrys == null) {
            throw new IllegalArgumentException("Entrys must not be null");
        }

        for (PhoneEntry actualEntry : phoneEntries) {
            for (PhoneEntry newEntry : entrys) {
                if (actualEntry.getNumber().equalsIgnoreCase(newEntry.getNumber())) {
                    throw new IllegalArgumentException("The number: " + newEntry.getNumber() + " exists. \n Number must be unique");
                }
            }
        }

        boolean toReturn = false;

        phoneEntries.addAll(entrys);
        saveToFile();
        toReturn = true;
        return toReturn;
    }

    //edit the object take the old value and the new 

    @Override
    public boolean editPhoneEntry(PhoneEntry oldEntry, PhoneEntry newEntry) {
        if (oldEntry == null) {
            throw new IllegalArgumentException(" oldEntry must not be null");
        }

        if (newEntry == null) {
            throw new IllegalArgumentException(" newEntry must not be null");
        }

        boolean found = false;

        //  check if the old objeck was in the arraylist
        for (int i = 0; i < phoneEntries.size(); i++) {
            // if was 
            if (phoneEntries.get(i).equals(oldEntry)) {
                // replace with the new object
                phoneEntries.set(i, newEntry);
                found = true;
                break;
            }
        }

        // if not found
        if (!found) {
            throw new IllegalArgumentException(" oldEntry must be in the collection to edit");
        }

        saveToFile();
        found = true;

        return found;
    }

    @Override
    public boolean deletePhoneEntry(String number) {
      
        if (number == null || number.isEmpty()) {
            throw new IllegalArgumentException(" number must not be null or empty");
        }

        boolean toReturn = false;

        for (int i = 0; i < phoneEntries.size(); i++) {
            if (phoneEntries.get(i).getNumber().equals(number)) {
                phoneEntries.remove(i);
                toReturn = true;
                break;
            }
        }

        if (!toReturn) {
            throw new IllegalArgumentException(" number must be in the collection");
        }

        saveToFile();
        toReturn = true;

        return toReturn;
    }

    @Override
    public boolean deleteAllPhoneEntries() {
        boolean toReturn = false;

        phoneEntries.clear();

        saveToFile();
        toReturn = true;

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
