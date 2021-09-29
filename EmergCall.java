import java.io.Serializable;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/** Class used to record the details of an emergency call
 * @author Lucian Duta
 * @version 1.0
 * @date 20 March 2020
 */

public class EmergCall implements Serializable
{
    private long phoneNo;
    private String callerName;
    private String callLocation;
    private String emergencyDesc;
    private VictimList victimList;
    private DeptList deptReq;
    private LocalTime time;
    private String status;

    /** Constructor creates the call based on the information given by the caller
     * @param phoneNoIn: The phone number of the caller
     * @param callerNameIn: The name of the caller
     * @param callLocationIn: The location of the caller
     * @param emergencyDescIn: A brief description of the emergency
     * @param nOfVictims: the number of victims of an incident
     */
    public EmergCall(long phoneNoIn, String callerNameIn, String callLocationIn, String emergencyDescIn, int nOfVictims)
    {
        phoneNo = phoneNoIn;
        callerName = callerNameIn;
        callLocation = callLocationIn;
        emergencyDesc = emergencyDescIn;
        victimList = new VictimList(nOfVictims);
        deptReq = new DeptList(3);
        time = LocalTime.now(); //setting the time of the call as the local time
        status = "Received"; //declaring the status of the emergency as just received
    }

    /** Adds a department required for the emergency
     * @param deptReqIn: The department required
     */
    public boolean addDept(Dept deptReqIn) {
        if(deptReq.search(deptReqIn.getType()) != null)
        {
            return false;
        }
        deptReq.addDept(deptReqIn);
        return true;

    } //calling the DeptList method

    /** Adds a victim of the incident to the list
     * @param victimIn: The victim to be added
     */
    public void addVictim(Victim victimIn) { victimList.addVictim(victimIn); } //calling the VictimList

    /** Reads the phone number of the caller
     * @return: Returns the phone number of the caller
     */
    public double getPhoneNo() { return phoneNo; }

    /** Reads the name of the caller
     * @return: Returns the name of the caller
     */
    public String getName() { return callerName; }

    /** Reads the location of the call
     * @return: Returns the location of the call
     */
    public String getLocation() { return callLocation; }

    /** Reads the description of the emergency
     * @return: Returns the brief description of the emergency
     */
    public String getDescription() { return emergencyDesc; }

    /** Reads the list of the victims
     * @return: the victims of the incident
     */
    public VictimList getVictimList() { return victimList; }

    /** Reads the departments required for the emergency
     * @return: Returns the departments required
     */
    public DeptList getDepartments() { return deptReq; }

    /** Reads the time of the call
     * @return: Returns the time of the call
     */
    public LocalTime getTime() { return time;}

    /** Reads the status of the call
     * @return: Returns the status of the emergency
     */
    public String getStatus() { return status; }

    /** Change the status of the emergency
     * @param statusIn: The new status
     * @return: Returns true if the status was changed successfully or false if the status was the same as the new one
     */
    public void setStatus(String statusIn) { status = statusIn; }

    @Override
    public String toString() { return "\nPhone number: " + phoneNo + "\nCaller name: " + callerName + "\nCall location: "
            + callLocation + "\nEmergency description: " + emergencyDesc+ "\n"+ victimList.toString() + "\n" + deptReq.toString() + "\nTime: " + time.truncatedTo(ChronoUnit.SECONDS) + "\nStatus: " + status + "\n"; }


}
