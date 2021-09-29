import java.io.Serializable;
import java.util.ArrayList;

/** Collection class to hold a list of victims of an incident
 * @author Lucian Duta
 * @version 1.0
 * @date 08 April 2021
 */

public class VictimList implements Serializable
{
    private ArrayList<Victim> vList;
    public final int MAX;

    /** Constructor creates the empty list of victims and registers the number of victims
     * @param maxIn: The number of victims of an incident
     */
    public VictimList(int maxIn) {
        vList = new ArrayList<>();
        MAX = maxIn;
    }

    /** Adds a new victim to the list
     * @param vIn: is the victim to add
     * @return: returns true if the victim was added successfully or false if it was not
     */
    public boolean addVictim(Victim vIn) {
        if (!isFull()) {
            vList.add(vIn);
            return true;
        } else {
            return false;
        }
    }

    /** Tells if the list is empty
     * @return: returns true if the list is empty or false otherwise
     */
    public boolean isEmpty() {
        return vList.isEmpty();
    }

    /** Tells if the list is full
     * @return: returns true if the list is full or false otherwise
     */
    public boolean isFull() {
        return vList.size() == MAX;
    }

    /** Gets the total number of victims in the list
     * @return: returns the number of victims in the list
     */
    public int getTotal() {
        return vList.size();
    }

    @Override
    public String toString() {
        return "\nVictims: " + vList.toString();
    }

}
