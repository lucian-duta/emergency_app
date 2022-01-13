# **Emergency Application Description**

The emergency application is made to process a collection of emergency calls having various functions like: add an emergency call, display calls, delete a call, modify certain aspects of a call or perform queries.

### The application has 8 classes:

1. EmergCall class – the emergency call object which dictates what information an emergency call holds
2. EmergCallList class – the collection class that stores the emergency calls objects in an array list, the class is also designed to perform some queries on the list.
3. Dept class – creates the model for the department object, used to store the information of the department called for an emergency
4. DeptList class – is a collection class that will hold the list of departments called for each emergency call
5. Victim class – used to store the details of the victim of an incident
6. VictimList class – is a collection class that holds a list of victims for each emergency call
7. Mainmenu class – is the class that provides the functionality of the menu driven interface, it also helps with some final touches for the queries.
8. EasyScanner class – used to easily ready the keyboard input

### Approach

There were a lot of challenges faced while building the application, the first thing I thought about was that the app had to gather as much information as possible from the operator in the shortest time possible. That is why I used switches a lot, the user just must choose an option, this way is way more efficient than writing repetitive and standard information, for example, the gender of the victim or the service needed. This approach also serves as input validation since the user can’t make a typing mistake that will mess with the whole system. Storing the time of the call was another problem that I thought a lot about because some people use the 24-hour standard and others the 12-hour one, also there was the risk for human error. So for the time, I used the LocalTime class which was already included in the java package, using a simple method of the class I was able to precisely and automatically get the time of the system that the app runs on. All the input validation is done while getting the input from the user in such a way that if the user does a mistake it’s immediately asked for different input, it is more efficient in this use case since the user does not lose all the information previously entered.
For storing the information I only used array lists, the main list is the emergency call list that holds the calls, for each call, there are two more lists created, one list for the departments and another one for the victims of the incident. Each call can be tailored for any emergency scenario. The call can have no victims if the number of victims is entered as 0 and it can have only one, two or three departments called if it is necessary.
The application is also able to hold permanent records, after the user closes the application all the objects are serialized and the emergency call list is stored in a file.
