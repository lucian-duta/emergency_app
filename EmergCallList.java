import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/** Collection class used to hold a list of the emergency calls
 * @author Lucian Duta
 * @version 20 March 2021
 */

public class EmergCallList implements Serializable {

    private ArrayList<EmergCall> eList;

    /** Constructor creates the empty list of calls
     */
    public EmergCallList()
    {
        eList = new ArrayList<>();
    }

    /** Adds a new emergency call to the list
     * @param ecIn: The new emergency call to add
     */
    public void addEmergCall(EmergCall ecIn)
    {
            eList.add(ecIn);
    }

    /** Removes the emergency call for the given phone number
     * @param phoneNoIn: The phone number for the call to remove
     * @return: Returns 1 if the call was deleted successfully, 0 if the call does not exist and 2 if the emergency is still in progress or just received
     */
    public boolean removeEmergCall(long phoneNoIn)
    {
        EmergCall findEC = search(phoneNoIn);
        if(findEC != null)
        {
                eList.remove(findEC); //remove call
                return true;
        }
        else
        {
            return false;
        }
    }

    /** Searches for an emergency call for the given phone number
     * @param phoneNoIn: The phone number used to search the call
     * @return: Returns the emergency call or null if it was not found
     */
    public EmergCall search(long phoneNoIn)
    {
        for(EmergCall currentEmergCall: eList)
        {
            if(currentEmergCall.getPhoneNo() == phoneNoIn)
            {
                return currentEmergCall;
            }
        }
        return null;
    }

    /** Changes the status of an emergency
     * @param phoneNoIn: The phone number for searching the emergency
     * @param statusIn: The new status of the emergency
     * @return: true if the status was changed successfully or false if it was not
     */
    public boolean changeStatus(long phoneNoIn, String statusIn)
    {
        boolean success = false;
        for(EmergCall currentEmergCall: eList) //accessing each emergency call
        {
            if(currentEmergCall.getPhoneNo() == phoneNoIn) //finding the one with the specific phone number
            {
                if(!currentEmergCall.getStatus().equals(statusIn)) //testing if the new status was already declared
                {
                    currentEmergCall.setStatus(statusIn);
                    success = true;
                }
                else
                {
                    success = false;
                }

            }
        }
        return success;
    }

    /** Counts the units deployed for a specific type
     * @param departmentIn: Type of department
     * @return: the number of units counted
     */
    public int countUnit(String departmentIn)
    {
        int count = 0;
        for(EmergCall currentEmergCall: eList)
            if(currentEmergCall.getDepartments().search(departmentIn) != null)
            {
                count =count + currentEmergCall.getDepartments().search(departmentIn).getUnits();
            }
        return count;

    }

    /** Makes a list of calls for a department
     * @param department: the department to be searched
     * @return: the list of emergency calls for a specific department
     */
    public ArrayList depStats(String department)
    {
        ArrayList<EmergCall> resList = new ArrayList<>(); //creating a new array list to hold the calls

        for(EmergCall currentEmergCall: eList) //searching for emergencies that required the department and adding them to the list
        {
            if(currentEmergCall.getDepartments().toString().contains(department))
            {
                resList.add(currentEmergCall);
            }
        }
        resList.sort(Comparator.comparing(EmergCall::getName)); //sort the list on caller name
        return resList;
    }

    /** Tells if the list is empty
     * @return Returns true if the list is empty or false if it's not
     */
    public boolean isEmpty(){return eList.isEmpty();}

    /** Gets the total number of emergency calls
     * @return Returns the total number of of emergency calls
     */
    public int getTotal() {return eList.size();}

    @Override
    public String toString()
    {return eList.toString(); }


}
