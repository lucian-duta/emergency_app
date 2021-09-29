import java.io.Serializable;

/** Class used to store the details of a victim
 * @author Lucian Duta
 * @version  20 March 2021
 */

public class Victim implements Serializable
{
    private boolean knownID;
    private String name;
    private String gender;
    private int age;
    private String cCond;

    /** Constructor creates the victim with all it's information
     * @param knownIDin: States if the identity of the victim is known or not
     * @param nameIn: Name of the victim if it is known
     * @param genderIn: Sex of the victim
     * @param ageIn: Age of the victim
     * @param cCondIn: The current condition of the victim
     */
    public Victim(boolean knownIDin, String nameIn, String genderIn, int ageIn, String cCondIn)
    {
        knownID = knownIDin;
        name = nameIn;
        gender = genderIn;
        age = ageIn;
        cCond = cCondIn;
    }

    /** Reads if the identity of the victim is known
     * @return: true if it is known or false if it's not
     */
    public boolean getknownID() { return knownID;}

    /** Reads the name of the victim
     * @return: the name of the victim
     */
    public String getName() { return name;}

    /** Reads the sex of the victim
     * @return: returns the sex of the victim
     */
    public String getGender() { return gender;}

    /** Reads the age of the victim
     * @return: returns the age of the victim
     */
    public int getAge() { return age;}

    /** Reads the current condition of the victim
     * @return: returns the current condition of the victim
     */
    public String getCCond() { return cCond;}

    @Override
    public String toString() { return  "\n\nName: "+ name + "\nGender: " + gender + "\nAge: " + age + "\nCurrent condition: " + cCond; }


}
