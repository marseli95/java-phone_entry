package phoneentry.api;

import java.util.Objects;

/**
 *
 * @author Piroli
 */
public class PhoneEntry
{
    private String firstName;
    private String lastName;
    private Type type;
    private String number;

    public PhoneEntry(String firstName, String lastName, Type type, String number)
    {

        if( firstName == null || firstName.isEmpty() )
            throw new IllegalArgumentException( "The object firstName cannot be null or empty" );

        if( lastName == null || lastName.isEmpty() )
            throw new IllegalArgumentException( "The object lastName cannot be null or empty" );

        if( number == null || number.isEmpty() )
            throw new IllegalArgumentException( "The object number cannot be null or empty" );

        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.number = number;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        if( firstName == null || firstName.isEmpty() )
            throw new IllegalArgumentException( "The object firstName cannot be null or empty" );

        if( this.firstName.equals( firstName ) == false )
            this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        if( lastName == null || lastName.isEmpty() )
            throw new IllegalArgumentException( "The object lastName cannot be null or empty" );

        if( this.lastName.equals( lastName ) == false )
             this.lastName = lastName;
    }


    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        if(this.type != type )
            this.type = type;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        if(this.number.equals(number) == false )
            this.number = number;
    }

    @Override
    public String toString()
    {
        return "PhoneEntry{" + "firstName=" + firstName + ", lastName=" + lastName + ", type=" + type + ", number=" + number + '}';
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null ) return false;

        if( !( obj instanceof PhoneEntry ) ) return false;

        PhoneEntry entry = (PhoneEntry)obj;
       return this.firstName.equals( entry.firstName ) && this.lastName.equals( entry.lastName ) && this.number.equals( entry.number) && this.type == entry.type;
    }

//    @Override
//    public int hashCode()
//    {
//        int hash = 7;
//        hash = 47 * hash + Objects.hashCode(this.firstName);
//        hash = 47 * hash + Objects.hashCode(this.lastName);
//        hash = 47 * hash + Objects.hashCode(this.type);
//        hash = 47 * hash + Objects.hashCode(this.number);
//        return hash;
//    }

}
