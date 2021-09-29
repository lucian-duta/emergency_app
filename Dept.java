import java.io.Serializable;

/** Class used to store the details of a department
 * @author Lucian Duta
 * @version 22.03.2021
 */
public class Dept implements Serializable
{

    private String type;
    private int units;
    private String unitType;

    /** Constructor initialising the type of emergency department, how many units and type of unit
     * @param typeIn: The type of department(Ambulance, Police, Fire)
     * @param unitsIn: How many units are required
     * @param unitTypeIn: What kind of unit is required i.e. Chemical danger fire truck
     */
    public Dept(String typeIn, int unitsIn, String unitTypeIn)
    {
        type = typeIn;
        units = unitsIn;
        unitType = unitTypeIn;
    }

    /** Reads the type of department
     * @return: the department type(Ambulance, Police or Fire)
     */
    public String getType() { return type; }

    /** Tells how many units were called
     * @return: The number of units called for an emergency
     */
    public int getUnits() { return units; }

    /** Tells the type of the units called
     * @return: The type of the units called
     */
    public String getUnitType() { return unitType; }

    @Override
    public String toString() { return "\n\nService: " + type + "\nNumber of units: " + units + "\nUnit type: " + unitType; }


}
