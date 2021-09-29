import java.io.*;

/** The menu driven interface of the Emergency application
 * @author Lucian Duta
 * @version 09.04.2021
 */

public class Mainmenu {

    public static void main(String[] args) {
        char choice;
        EmergCallList emergList;

        System.out.println("*** EMERGENCY APP *** \n");

        emergList = new EmergCallList();
        emergList = readFromFile(emergList);
        do {
            System.out.println();
            System.out.println("[1] Add a new call");
            System.out.println("[2] Display calls");
            System.out.println("[3] Change status of an emergency (or delete call)");
            System.out.println("[4] Departments status");
            System.out.println("[5] Number of units deployed");
            System.out.println("[6] Search for an emergency call");
            System.out.println("[7] Exit");

            choice = EasyScanner.nextChar();
            switch (choice) {
                case '1':
                    addEmergencyCall(emergList);
                    break;
                case '2':
                    System.out.println(emergList.toString());
                    break;
                case '3': emergList = changeStatus(emergList);
                    break;
                case '4': departmentsStatus(emergList);
                    break;
                case '5': unitsDeployed(emergList);
                    break;
                case '6': searchECall(emergList);
                    break;
                case '7': {
                    System.out.println("Goodbye !");
                    saveToFile(emergList);
                }
                    break;
                default: System.out.println("ERROR: Only 1 to 7");

            }
        } while (choice != '7');

    }

    /** Reads all the details of a emergency call from the user and creates a new emergency call to be added to the list
     * @param emListIn: the emergency call list where the new call will be stored
     */
    public static void addEmergencyCall(EmergCallList emListIn) {
        char c;
        long phoneNo = 0;
        boolean ok;
        do { //looping for input validation, making use of the error thrown when the wrong value is assigned to long
            System.out.print("Phone Number: ");
            try {
                phoneNo = EasyScanner.nextLong();
                ok = phoneNo >= 0;
            } catch (Exception e) {
                System.out.println("INVALID PHONE NUMBER FORMAT !");
                ok = false;
            }
        } while (!ok);

        System.out.print("Caller Name: ");
        String callerName = EasyScanner.nextString(); //reading the caller name from the user

        System.out.print("Caller Location: ");
        String callLocation = EasyScanner.nextString(); //reading the call location from the user

        System.out.print("Emergency Description: ");
        String emergencyDesc = EasyScanner.nextString(); //reading the emergency description from the user
        int nOfVictims;
        do { //Reading the number of victims of an incident and looping for input validation
            System.out.println("How many victims? ");
            nOfVictims = EasyScanner.nextInt();
            if (nOfVictims < 0)
                System.out.println("ERROR: Number must be positive");
            if (nOfVictims > 15)
                System.out.println("ERROR: Too many victims");
        } while (nOfVictims < 0 || nOfVictims > 15);
        int k = 1;

        EmergCall e = new EmergCall(phoneNo, callerName, callLocation, emergencyDesc, nOfVictims); //calling the EmergCall method to create a new emergency call

        if (nOfVictims != 0)
            do { //looping until all victims were added
                System.out.println("Add victim number " + k);
                e.addVictim(readVictim()); //calling the addvictim method to add a new victim with the output recieved from the readVictim method
                k++;
            } while (k <= nOfVictims);

        int count = 0; // creating a count for departments added
        do { //looping a switch to find out if the user wants to add a department to the emergency
            System.out.println("Do you want to add a department?");
            System.out.println("[1] Yes");
            System.out.println("[2] No");
            System.out.println("Enter 1 or 2");
            c = EasyScanner.nextChar();
            switch (c) {
                case '1': {
                    count++;
                    if (!e.addDept(readDeptReq())) { //attempting to add the department and testing if it was already added
                        System.out.println("DEPARTMENT ALREADY ADDED !");
                    }
                }
                break;
                case '2': {
                    System.out.println("Department list updated"); //exiting the loop
                    if(count < 1) // if no department is added the user is asked again to enter a department
                        System.out.println("ERROR: AT LEAST ONE DEPARTMENT IS REQUIRED !");
                }
                break;
                default:
                    System.out.println("ERROR: Only 1 or 2 \n");
            }
        } while (c != '2' || count < 1);

        emListIn.addEmergCall(e); //adding the emergency call with all it's details to the Emergency call list
    }

    /** Method used to read the details of a victim
     *
     * @return: Returns the Victim object with all the details
     */
    public static Victim readVictim() {
        char choice;
        String name = null;
        boolean knownID = false;
        do {
            // menu for finding out if the identity of the victim is known
            System.out.println("Is the victim identity known? ");
            System.out.println();
            System.out.println("[1] YES");
            System.out.println("[2] NO");

            choice = EasyScanner.nextChar();
            //switch to choose if the identity is known

            switch (choice) {
                case '1': { //if the identity is known the name is read from the user
                    knownID = true;
                    System.out.print("Name: ");
                    name = EasyScanner.nextString();
                }
                break;
                case '2': { //if the identity is not known the name will automatically be "UNKNOWN"
                    knownID = false;
                    name = "UNKNOWN";
                }
                break;
                default:
                    System.out.print("1 or 2 only \n");
            }
        } while (name == null);

        String gender = null;
        do {
            // menu for reading the gender of the victim
            System.out.print("Gender: ");
            System.out.println();
            System.out.println("[1] MALE");
            System.out.println("[2] FEMALE");
            System.out.println("[3] UNKNOWN");

            choice = EasyScanner.nextChar();

            switch (choice) { //switch to choose the gender
                case '1': {
                    gender = "MALE";
                }
                break;
                case '2': {
                    gender = "FEMALE";
                }
                break;
                case '3': {
                    gender = "UNKNOWN";
                }
                break;
                default:
                    System.out.print("1 - 3 only \n");
            }
        } while (gender == null);

        int age;
        do { //Reading the age with input validation
            System.out.print("Age: ");
            age = EasyScanner.nextInt();
            if (age < 0)
                System.out.println("ERROR: Age must be positive");
            if (age > 120)
                System.out.println("ERROR: Invalid age");
        } while (age < 0 || age > 120);

        System.out.print("Current condition: ");
        String cCond = EasyScanner.nextString(); //reading the current condition of the victim

        return new Victim(knownID, name, gender, age, cCond); //creates the victim and returns it

    }

    /**
     *Creates a department based on the information gathered from the user
     *
     * @return: the new Dept object
     */
    public static Dept readDeptReq() {
        char choice;
        String type = null;
        do {
            // menu for choosing a service
            System.out.println("Choose which emergency service you need !");
            System.out.println();
            System.out.println("[1] Ambulance");
            System.out.println("[2] Police");
            System.out.println("[3] Fire");

            choice = EasyScanner.nextChar();
            //switch to choose between departments

            switch (choice) {
                case '1': {
                    type = "Ambulance";
                }
                break;
                case '2': {
                    type = "Police";
                }
                break;
                case '3': {
                    type = "Fire";
                }
                break;
                default:
                    System.out.print("1-3 only \n");
            }
        } while (type == null);
        //check if the department was already called

        System.out.print("Enter number of units: ");
        int units = EasyScanner.nextInt(); // reading the number of units required

        System.out.print("Enter unit type: ");
        String unitType = EasyScanner.nextString(); //reading the unit type

        return new Dept(type, units, unitType); // create new department object from input

    }

    /** Method used to change the status of an emergency
     * @param emergCallListIn: The emergency list that contains the call to be changed
     * @return: Returns the updated list
     */
    public static EmergCallList changeStatus(EmergCallList emergCallListIn) {
        EmergCallList eList;
        eList = emergCallListIn;
        String status = null;
        long phoneNo = 0;
        boolean ok;
        do { //looping for input validation
            System.out.print("Phone Number: ");
            try {
                phoneNo = EasyScanner.nextLong();
                ok = phoneNo >= 0;
            } catch (Exception e) {
                System.out.println("INVALID PHONE NUMBER FORMAT !");
                ok = false;
            }
        } while (!ok);

        char c;
         //menu to find out the new status the user wants to declare
            System.out.println("Status you want to declare: ");
            System.out.println("[1] In Progress");
            System.out.println("[2] Dealt With (WARNING: THIS OPTION WILL DELETE THE RECORD OF THE EMERGENCY CALL)");
            System.out.println("Enter 1 or 2");
            c = EasyScanner.nextChar();
            switch (c) //switch to choose the status
            {
                case '1':
                    status = "In Progress";
                    break;
                case '2': { //additionally when the emergency is dealt with it will be deleted from the list
                    status = "Dealt With";
                    if(eList.removeEmergCall(phoneNo))
                        System.out.println("Call deleted successfully");
                    else
                        System.out.println("There was an error deleting the call");
                }
                break;
                default:
                    System.out.println("ONLY 1 or 2");
            }


        eList.changeStatus(phoneNo, status); //calling the changeStatus method of EmergCallList to change the status

        return eList;
    }

    /** Creates a list for each department to find out which emergency calls were assigned to a specific department
     * @param emergListIn: The list to search in for a department
     */
    public static void departmentsStatus(EmergCallList emergListIn) {

        char choice;
        String type = null;
        do {
            // menu for choosing a service
            System.out.println("Choose an emergency service !");
            System.out.println();
            System.out.println("[1] Ambulance");
            System.out.println("[2] Police");
            System.out.println("[3] Fire");

            choice = EasyScanner.nextChar();
            //switch to choose between departments

            switch (choice) {
                case '1': {
                    type = "Ambulance";
                }
                break;
                case '2': {
                    type = "Police";
                }
                break;
                case '3': {
                    type = "Fire";
                }
                break;
                default:
                    System.out.print("1-3 only \n");
            }
        } while (type == null);

        System.out.println(emergListIn.depStats(type).toString()); //printing the output of the depStats method
    }

    /** Method use to write the emergency call list to a file for keeping permanent records
     * @param emergListIn: the emergency call list to be written
     */
    public static void saveToFile(EmergCallList emergListIn) {
        try (
                //creating the FileOutputStream object name eCall
                FileOutputStream eCallFile = new FileOutputStream("eCall.ser");
                //creating the ObjectOutputStream object to wrap the eCallFile
                ObjectOutputStream eCallStream = new ObjectOutputStream(eCallFile)
        ) {
            eCallStream.writeObject(emergListIn); // writing the object to the file
            eCallStream.flush(); // making sure to write any buffered data to the file
            eCallStream.close(); // closing the stream to make sure there is no error while writing
        } catch (IOException e) { //catching any error what the process might throw
            System.out.println("THERE WAS A PROBLEM WRITING THE FILE");
            e.printStackTrace();
        }
    }

    /** Read the emergency call list from a file
     * @param eCallListIn: eCallList to be updated
     * @return: the updated call list
     */
    public static EmergCallList readFromFile(EmergCallList eCallListIn) {
        try (
                //creating the FileInputStream object name eCall
                FileInputStream eCallFile = new FileInputStream("eCall.ser");
                //creating the ObjectInputStream object to wrap the eCallFile
                ObjectInputStream eCallStream = new ObjectInputStream(eCallFile)
        ) {
            eCallListIn = (EmergCallList) eCallStream.readObject(); // reading the list from the file
            eCallStream.close(); // closing the stream to avoid problems
        } catch (FileNotFoundException e) { // making the user aware that there is no file to read from
            System.out.println("\n No file was read");
        } catch (ClassNotFoundException e) { // error thrown in case there was a data corruption or a different object was written
            System.out.println("\n Trying to read an object of an unknown class");
        } catch (StreamCorruptedException e) { // another case of data corruption or file format problem
            System.out.println("\n Unreadable file format");
        } catch (IOException e) { // catching any other error
            System.out.println("There was a problem reading the file");
            e.printStackTrace();
        }
        return eCallListIn;
    }

    /** Display the number of units deployed for each service
     * @param eCallListIn: the list to count from
     */
    public static void unitsDeployed(EmergCallList eCallListIn)
    {
        System.out.println("Number of ambulances deployed: " + eCallListIn.countUnit("Ambulance"));
        System.out.println("Number of police units deployed: " + eCallListIn.countUnit("Police"));
        System.out.println("Number of fire units: " + eCallListIn.countUnit("Fire"));
    }

    /** Search for an emergency call based on the phone number
     * @param eCallListIn: The phone number of the emergency call to find
     */
    public static void searchECall(EmergCallList eCallListIn)
    {
        boolean ok;
        long phoneNo = 0;
        do { //looping for input validation
            System.out.print("Phone Number: ");
            try {
                phoneNo = EasyScanner.nextLong();
                ok = phoneNo >= 0;
            } catch (Exception e) {
                System.out.println("INVALID PHONE NUMBER FORMAT !");
                ok = false;
            }
        } while (!ok);

        if(eCallListIn.search(phoneNo) != null)
            System.out.println(eCallListIn.search(phoneNo).toString());
        else
            System.out.println("Emergency call not found");
    }

}
