/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phoneentry.api;

import java.util.ArrayList;

/**
 *
 * @author Piroli
 */
public interface DataConnection
{
    PhoneEntry getPhoneEntry( String number );
    Iterable<PhoneEntry> getPhoneEntries();
    boolean savePhoneEntry( PhoneEntry entry );
    boolean savePhoneEntries( ArrayList<PhoneEntry> entrys );
    boolean editPhoneEntry( PhoneEntry oldEntry,  PhoneEntry newEntry );
    boolean deletePhoneEntry( String number );
    boolean deleteAllPhoneEntries();
    Iterable<PhoneEntry> orderByFirstName( boolean desc );
    Iterable<PhoneEntry> orderbyLastName( boolean desc );
}
