import java.io.Serializable;
import java.util.ArrayList;

/** Collection class to hold a list of the departments required for an emergency
 * @author Lucian Duta
 * @version 20 March 2021
 */
public class DeptList implements Serializable
{
    private ArrayList<Dept> deptList;
    public final int MAX;

    /** Constructor creates the empty department list required for the emergency and sets the maximum number of
     *  departments that can be called
     * @param maxIn: The maximum number of departments in the list
     */
    public DeptList(int maxIn)
    {
        deptList = new ArrayList<>();
        MAX = maxIn;
    }

    /** Checks if the department list is full
     * @return Returns true if the list is full and false if it's not
     */
    public boolean isFull() { return deptList.size() == MAX; }

    /** Checks if the department list is empty
     * @return Returns true if it's empty or false if it's not
     */
    public boolean isEmpty() { return deptList.size() == 0; }

    /** Gets the total number of departments in the list
     * @return Returns the total number of departments in the list
     */
    public int getTotal() { return deptList.size(); }


    /** Adds a new department to the list
     *
     * @param deptIn: The department to add
     * @return Returns true in the department was added successfully and false otherwise
     */
    public boolean addDept(Dept deptIn)
    {
        if(!isFull())
        {
            deptList.add(deptIn);
            return true;
        }
        else
        {
            return false;
        }
    }

    /** Searches for a department called for the emergency
     * @param typeIn: The type of the department to search for
     * @return: Returns the department with the type specified or null if not found
     */
    public Dept search (String typeIn)
    {
        for(Dept currentDept: deptList) //accessing each department in the list
        {
            if(currentDept.getType().equals(typeIn)) //testing for the specific type of department
            {
                return currentDept;
            }
        }

        return null;
    }

    @Override
    public String toString() { return "\nDepartments:" + deptList.toString(); }



}
