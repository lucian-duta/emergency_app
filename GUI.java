import java.io.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.*;
import javafx.scene.layout.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;

/** Graphical User Interface (GUI) for the Emergency Call Application
 * @author Lucian Duta
 * @version 03 May 2021
 */
public class GUI extends Application {
    //initial declarations
    EmergCallList ecallList = new EmergCallList();
    EmergCall emCall;
    // initialising the border pane layout that will hold different visual components
    BorderPane layout = new BorderPane();

    // initialising the menu that will hold the buttons
    VBox menu = new VBox();
    private Stage mainWindow;
    @Override
    /** Initialises the main window on the screen
        @param window - the stage of the scene
     */
    public void start(Stage window)
    {
        layout.setMinSize(800,500); // setting the size of the main window

        this.mainWindow = window; // declaring the main window

        window.setOnCloseRequest(closeWindowConfirmation); // handle the attempt to close the window

        ecallList = readFromFile(ecallList); // reading the list from the save file

        Text sceneTitle = new Text("Emergency Call Application Menu"); //setting the title
        sceneTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sceneTitle.setFill(Color.WHITE);
        sceneTitle.setStrokeWidth(2);

        // setting the visual parameters of the top of the window
        HBox layoutTop = new HBox();
        layoutTop.setPadding(new Insets(15, 12, 15, 12));
        layoutTop.setStyle("-fx-background-color: #336699;");
        layoutTop.getChildren().add(sceneTitle);
        layoutTop.setAlignment(Pos.CENTER);
        layout.setTop(layoutTop);

        // creating all the buttons for the menu
        Button addCall = new Button("Add a call");
        Button displayCalls = new Button("Display calls");
        Button changeStatus = new Button("Change status");
        Button deptCalls = new Button("Dept Calls");
        Button nUDepl = new Button("Units Deployed");
        Button search = new Button("Search");
        Button exit = new Button("Exit");
        // adding a stile to each button
        Stream.of(addCall, displayCalls,changeStatus,deptCalls,nUDepl,search,exit).forEach(button ->
        {   button.setStyle("-fx-background-color: #61d3ff;" +
                                "-fx-text-fill: black;" +
                                "-fx-border-color: black;" +
                                "-fx-border-style: solid;" +
                                "-fx-border-width: 1.5px;");
            button.setMinWidth(100);
        });

        // stylizing the menu box
        menu.setSpacing(10);
        menu.setPadding(new Insets(10));
        // making the menu title and costumize the font
        Text menuTitle = new Text("MENU");
        menuTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        //adding all the buttons to the menu box and adding the manu to the main window
        menu.getChildren().addAll(menuTitle,addCall,displayCalls,changeStatus,deptCalls,nUDepl,search,exit);
        layout.setLeft(menu);

        //calling the addCallHandler method when the add button is pressed
        addCall.setOnAction(e -> {
            addCallHandler();
            menu.setDisable(true);
        });

        //calling the displayAllCalls method when display button is pressed
        displayCalls.setOnAction(e -> displayAllCalls());

        //calling the search method when the search button is pressed
        search.setOnAction(e -> search());

        //calling the saveToFile method and closing the window when the exit button is pressed
        exit.setOnAction(e ->
        {
            saveToFile(ecallList);
            window.close();
        });

        //calling the changeStatus method when the change status button is pressed
        changeStatus.setOnAction(e -> changeStatus());

        //calling the unitsDeployed method when the units deployed button is pressed
        nUDepl.setOnAction(e -> unitsDeployed());

        //calling the deptCalls method when the dept calls button is pressed
        deptCalls.setOnAction(e -> deptCalls());


        Scene scene = new Scene(layout); // creating a new scene containing the layout
        window.setScene(scene); //setting the scene into the window stage
        window.setTitle("EMCALL APP"); // creating the title of the window
        window.show(); // displaying the window on the screen

    }

    /** A method that will carry the process of adding a new call
     *  It will take all the input from the user as well as handling the visual part
     */
    public void addCallHandler()
    {
        // creating a text that will be used for error messages
        Text invalid = new Text("ERROR: The information entered is invalid !");
        invalid.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        invalid.setFill(Color.RED);
        invalid.setStrokeWidth(2);

        // creating a grid that will hold the call adding form
        GridPane addGrid = new GridPane();
        addGrid.setAlignment(Pos.CENTER);
        addGrid.setHgap(10);
        addGrid.setVgap(10);
        addGrid.setPadding(new Insets(25, 25, 25, 25));

        Text addTitle = new Text("Add a new emergency call");
        addGrid.add(addTitle,0,0,2,1);

        /* The following code is for creating the form labels and text boxes
            and adding them to the grid in their respective columns and rows.
            For each text field where will be a listener that will detect when
            new values are added or if the box is empty and will change the
            color of the text field border accordingly
        */
        Label phoneNo = new Label("Phone number: ");
        addGrid.add(phoneNo,0,1);

        TextField phoneNoIn = new TextField();
        addGrid.add(phoneNoIn,1,1);
        phoneNoIn.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(!isPhoneNo(phoneNoIn).getValue()){ //checking for phone number validity
                        phoneNoIn.setStyle("-fx-text-box-border: #ff0000;"); //making the border red if the value is not a phone number

                    }
                }
                else {
                    phoneNoIn.setStyle(null); //changing it back to normal if the value is accepted

                }
        });

        Label callerName = new Label("Caller name: ");
        addGrid.add(callerName,0,2);

        TextField callerNameIn = new TextField();
        addGrid.add(callerNameIn,1,2);
        callerNameIn.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if(!newValue){
                if(callerNameIn.getText().isEmpty()){
                    callerNameIn.setStyle("-fx-text-box-border: #ff0000;");

                }
            }
            else {
                callerNameIn.setStyle(null);

            }
        });

        Label callLoc = new Label("Caller location: ");
        addGrid.add(callLoc,0,3);

        TextField callLocIn = new TextField();
        addGrid.add(callLocIn,1,3);
        callLocIn.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if(!newValue){
                if(callLocIn.getText().isEmpty()){
                    callLocIn.setStyle("-fx-text-box-border: #ff0000;");


                }
            }
            else {
                callLocIn.setStyle(null);

            }
        });

        Label emergDesc = new Label("Emergency description: ");
        addGrid.add(emergDesc,0,4);

        TextField emergDescIn = new TextField();
        addGrid.add(emergDescIn,1,4);
        emergDescIn.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if(!newValue){
                if(emergDescIn.getText().isEmpty()){
                    emergDescIn.setStyle("-fx-text-box-border: #ff0000;");

                }
            }
            else {
                emergDescIn.setStyle(null);

            }
        });
        Label nofVictims = new Label("Number of victims: ");
        addGrid.add(nofVictims,0,5);
        Spinner<Integer> victimsSpinner = new Spinner<>(0,10,0); // getting number input for the number of victims with a spinner
        addGrid.add(victimsSpinner,1,5);
        Button proceed = new Button("Proceed to adding departments");
        victimsSpinner.valueProperty().addListener(e -> { // actively changing the text of the button according to the number of victims
            if(victimsSpinner.getValue() != 0)
            {
                proceed.setText("Proceed to adding victims"); // if there are victims the user will be directed to add the details
            }else{
                proceed.setText("Proceed to adding departments"); // if there are no victims the user will add the departments details
            }
        });

        addGrid.add(proceed,1,6);

        BooleanBinding booleanBind = callerNameIn.textProperty().isEmpty() //binding the value of a boolean to the text fileds empty status
                .or(callLocIn.textProperty().isEmpty())
                .or(emergDescIn.textProperty().isEmpty()).or(phoneNoIn.textProperty().isEmpty());

        //disabling the button if the value of the boolean is true(the fields are empty
        //in such a way that if the fields are empty the user cannot proceed to the next step
        proceed.disableProperty().bind(booleanBind);

        // if the proceed button is pressed the following lambda expression will be executed
        proceed.setOnAction(e -> {
            //making sure that the fields are not empty and the phone number is correct
            if(isPhoneNo(phoneNoIn).getValue() && !callerNameIn.getText().isEmpty() && !callLocIn.getText().isEmpty() &&
                    !emergDescIn.getText().isEmpty())
            {
                //creating a new emergency call with the entered data
                emCall = new EmergCall(Long.parseLong(phoneNoIn.getText()), callerNameIn.getText(), callLocIn.getText(), emergDescIn.getText(), victimsSpinner.getValue());
                if(victimsSpinner.getValue() != 0) // if the are victims the add victim method will be called, if not the user will be directed to add the departments
                {
                    addVictim(victimsSpinner.getValue()-1);
                }else{
                    addDepartment();
                }
            }
            else{
                //invalid message in case some information is not correct
                addGrid.add(invalid,0,7, 2, 1);
            }
        });
        //showing the form on the centre of the border pane
        layout.setCenter(addGrid);
    }

    /** Method that will carry the process of adding a victim to an emergency call
     * @param nofVictim: the number of victims to be added
     */
    private void addVictim(int nofVictim)
    {
        Text invalid = new Text("ERROR: The information entered is invalid !");
        invalid.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        invalid.setFill(Color.RED);
        invalid.setStrokeWidth(2);

        GridPane addVictimGrid = new GridPane(); // creating the form for adding the victim details
        addVictimGrid.setAlignment(Pos.CENTER);
        addVictimGrid.setHgap(10);
        addVictimGrid.setVgap(10);
        addVictimGrid.setPadding(new Insets(25, 25, 25, 25));
        Label vicTitle = new Label("Victim information");
        vicTitle.setStyle("-fx-font-family: 'Verdana';" + "-fx-font-size: 20;" + "-fx-alignment: center;");
        addVictimGrid.add(vicTitle,0,3, 2,1);

        /* The following code is for creating the form labels and text boxes
            and adding them to the grid in their respective columns and rows.
            For each text field where will be a listener that will detect when
            new values are added or if the box is empty and will change the
            color of the text field border accordingly
        */
        Label vname = new Label("Name");
        addVictimGrid.add(vname,0,4);

        TextField vNameIn = new TextField();
        addVictimGrid.add(vNameIn,1,4);
        vNameIn.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if(!newValue){
                if(vNameIn.getText().isEmpty()){
                    vNameIn.setStyle("-fx-text-box-border: #fffc04;");
                    vNameIn.setText("UNKNOWN");
                }
            }
            else {
                vNameIn.setStyle(null);
            }
        });

        Label vgender = new Label("Gender");
        addVictimGrid.add(vgender,0,5);

        ChoiceBox<String> gender = new ChoiceBox<>(); //asking the user to choose between the pre-defined genders
        gender.getItems().add("MALE");
        gender.getItems().add("FEMALE");
        gender.getItems().add("UNKNOWN");
        gender.setValue("UNKNOWN");
        addVictimGrid.add(gender,1,5);

        Label vage = new Label("Age: ");
        addVictimGrid.add(vage,0,6);

        TextField vageIn = new TextField();
        addVictimGrid.add(vageIn,1,6);
        vageIn.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if(!newValue){
                if(!isValidAge(vageIn)){
                    vageIn.setStyle("-fx-text-box-border: #ff0000;");
                }
            }
            else {
                vageIn.setStyle(null);
            }
        });

        Label vCond = new Label("Current condition: ");
        addVictimGrid.add(vCond,0,7);

        TextField vCondIn = new TextField();
        addVictimGrid.add(vCondIn,1,7);
        vCondIn.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if(!newValue){
                if(vCondIn.getText().isEmpty()){
                    vCondIn.setStyle("-fx-text-box-border: #fffc04;");
                    vCondIn.setText("UNKNOWN");
                }
            }
            else {
                vCondIn.setStyle(null);
            }
        });
        //creating the proceed button
        Button nextV = new Button("Proceed");
        BooleanBinding booleanBind = vNameIn.textProperty().isEmpty() //binding the boolean value to the field's empty value
                .or(vNameIn.textProperty().isEmpty())
                .or(vCondIn.textProperty().isEmpty()).or(vageIn.textProperty().isEmpty());

        //the button will be disabled if the fields are empty
        nextV.disableProperty().bind(booleanBind);
        addVictimGrid.add(nextV,1,8);

        // when the proceed button is pressed the following lambda expression will run
        nextV.setOnAction(e -> {
            if(!vNameIn.getText().isEmpty() && isValidAge(vageIn) && !vCondIn.getText().isEmpty()) // if the information in the fields is correct
            {
                // a new victim is created
                Victim v = new Victim(!vNameIn.getText().equals("UNKNOWN"), vNameIn.getText(), gender.getValue(), Integer.parseInt(vageIn.getText()), vCondIn.getText());
                emCall.addVictim(v); // the victim is added to the call
                if(nofVictim>0) // if there are more than one victims the addVictim method wil be called again
                    addVictim(nofVictim-1);
                else{
                    addDepartment(); // if there are no more victims the user will be directed to add departments
                }
            }
            else
            {
                //if the data in the fields is not valid the invalid message will be displayed
                addVictimGrid.add(invalid,0,10, 2, 1);
            }

        });
        layout.setCenter(addVictimGrid); // displaying the victim adding form

    }

    /** Method that will carry the process of adding departments to the emergency calls
     *
     */
    private void addDepartment()
    {
        Text invalid = new Text("");
        invalid.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        invalid.setFill(Color.RED);
        invalid.setStrokeWidth(2);

        // creating a vbox that will hold the title of the title of the view, the service buttons and copyright info
        VBox service = new VBox();
        service.maxHeight(50);
        service.maxWidth(50);
        // the title of the page
        Text addServ = new Text("SELECT A SERVICE");
        addServ.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        addServ.setFill(Color.BLACK);
        addServ.setStrokeWidth(2);
        // creating the text with the copyright information of the icons
        Text copyRight = new Text("Icons made by Freepik from www.flaticon.com");
        copyRight.setFont(Font.font("Arial", FontWeight.THIN, 10));
        copyRight.setFill(Color.GRAY);

        //Icons made by Freepik from www.flaticon.com

        //creating the buttons and adding a style to them
        Button ambulance = new Button();
        ambulance.setStyle("-fx-background-image: url(icons/ambulance.png);"+ // setting the background as a picture
                "-fx-background-size: 100px 100px;"+
                "-fx-background-repeat: no-repeat;"+
                "-fx-background-position: center;"+
                "-fx-min-height: 110px;"+
                "-fx-min-width: 110px;");
        Button police = new Button();
        police.setStyle("-fx-background-image: url(icons/police.png);"+
                "-fx-background-size: 100px 100px;"+
                "-fx-background-repeat: no-repeat;"+
                "-fx-background-position: center;"+
                "-fx-min-height: 110px;"+
                "-fx-min-width: 110px;");
        Button fire = new Button();
        fire.setStyle("-fx-background-image: url(icons/fire.png);"+
                "-fx-background-size: 100px 100px;"+
                "-fx-background-repeat: no-repeat;"+
                "-fx-background-position: center;"+
                "-fx-min-height: 110px;"+
                "-fx-min-width: 110px;");
        HBox servicesButtons = new HBox(); // HBox that will hold all the buttons
        servicesButtons.setPadding(new Insets(25, 25, 25, 25));
        servicesButtons.setAlignment(Pos.CENTER);
        servicesButtons.setSpacing(20);
        servicesButtons.getChildren().addAll(ambulance,police,fire);

        service.getChildren().addAll(addServ,servicesButtons,copyRight); // adding the title, buttons and copyright info to the service box
        service.setPadding(new Insets(25, 25, 25, 25));
        service.setAlignment(Pos.CENTER);
        layout.setCenter(service); //setting the box as the centre of the layout

        //creating a form for getting the department information
        GridPane addDGrid = new GridPane();
        addDGrid.setAlignment(Pos.CENTER);
        addDGrid.setHgap(10);
        addDGrid.setVgap(10);
        addDGrid.setPadding(new Insets(25, 25, 25, 25));
        Label vicTitle = new Label("Service information");
        vicTitle.setStyle("-fx-font-family: 'Verdana';" + "-fx-font-size: 20;" + "-fx-alignment: center;");
        addDGrid.add(vicTitle,0,3, 2,1);
        //getting the information form the user the same as before
        Label serviceName = new Label("Service name: ");
        addDGrid.add(serviceName,0,4);

        TextField serviceNameIn = new TextField();
        serviceNameIn.setEditable(false);
        serviceNameIn.setDisable(true);
        addDGrid.add(serviceNameIn,1,4);

        Label nOfUnits = new Label("Number of units: ");
        addDGrid.add(nOfUnits,0,5);

        Spinner<Integer> nOfUnitsIn = new Spinner<>(1,10,1);
        addDGrid.add(nOfUnitsIn,1,5);

        Label unitsType = new Label("Units type: ");
        addDGrid.add(unitsType,0,6);

        TextField unitsTypeIn = new TextField();
        addDGrid.add(unitsTypeIn,1,6);
        unitsTypeIn.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if(!newValue){
                if(unitsTypeIn.getText().isEmpty()){
                    unitsTypeIn.setStyle("-fx-text-box-border: #fffc04;");
                    unitsTypeIn.setText("Not specified"); // if no info is added the units will be not specified
                }
            }
            else {
                unitsTypeIn.setStyle(null);
            }
        });
        Button anotherService = new Button("Add another service");
        Button finish = new Button("Finish");
        BooleanBinding booleanBind = unitsTypeIn.textProperty().isEmpty();

        anotherService.disableProperty().bind(booleanBind);
        finish.disableProperty().bind(booleanBind);
        addDGrid.add(anotherService,0,8);
        addDGrid.add(finish,1,8);
        AtomicInteger sCount = new AtomicInteger(); //atomic integer to be used in anonymous inner classes for counting the services added
        //setting the actions perform when a button is pressed
        ambulance.setOnAction(e -> {
            layout.setCenter(addDGrid);
            serviceNameIn.setText("Ambulance");
            ambulance.setDisable(true); // disabling the button when it is pressed to avoid adding multiple units of the same type
            sCount.getAndIncrement(); //incrementing the counter
        });
        fire.setOnAction(e -> {
            layout.setCenter(addDGrid);
            serviceNameIn.setText("Fire");
            fire.setDisable(true);
            sCount.getAndIncrement();
        });
        police.setOnAction(e -> {
            layout.setCenter(addDGrid);
            serviceNameIn.setText("Police");
            police.setDisable(true);
            sCount.getAndIncrement();
        });
        anotherService.setOnAction(e ->{
            if(sCount.get() == 3){ //if 3 services were called
                layout.setCenter(null); //disabling the ability to add more services if all were added
                menu.setDisable(false);// enabling the menu buttons
                ecallList.addEmergCall(emCall); // adding the emergency call to the list
                saveToFile(ecallList); //saving the list in case of unexpected close of the program
                }
            else
                layout.setCenter(service);
            emCall.addDept(new Dept(serviceNameIn.getText(), nOfUnitsIn.getValue(), unitsTypeIn.getText()));
            unitsTypeIn.setText(""); //resetting the text field
            nOfUnitsIn.getValueFactory().setValue(1); //resetting the spinner
        });
        finish.setOnAction(e ->{ //finish the process of adding a new emergency call
            emCall.addDept(new Dept(serviceNameIn.getText(), nOfUnitsIn.getValue(), unitsTypeIn.getText()));
            layout.setCenter(null);
            menu.setDisable(false); // enabling the menu buttons
            ecallList.addEmergCall(emCall);
            System.out.println(ecallList.toString());
            saveToFile(ecallList);
        });
    }

    /** Method used to display all the calls on the screen
     */
    private void displayAllCalls(){
        VBox d = new VBox(); // creating the vbox that will hold the list of calls
        ListView<String> list = new ListView<>(); // creating the list for the calls
        ObservableList<String> items =FXCollections.observableArrayList (ecallList.toString()); // creating the items for calls
        list.setItems(items); // adding the itmes to the list
        d.getChildren().add(list); //adding the list to the vbox
        layout.setCenter(list); //displaying the list
    }

    /** Method that will carry the process of searching for a call
     */
    private void search(){

        VBox se = new VBox(); //creating the VBox that will hold the search form
        se.setMaxSize(350,200);
        se.setAlignment(Pos.BASELINE_CENTER);
        HBox spn = new HBox(); //creating horizontal box that will hold the label, text field and search button
        spn.setSpacing(10);
        Label phoneNo = new Label("Enter phone number:");
        TextField phoneNoSIn = new TextField();
        Button searchC = new Button("Search");
        spn.getChildren().addAll(phoneNo,phoneNoSIn,searchC); // adding the elements to the HBox
        phoneNoSIn.focusedProperty().addListener((arg0, oldValue, newValue) -> { // making the text field red if the phone number is wrong
            if(!newValue){
                if(!isPhoneNo(phoneNoSIn).getValue()){
                    phoneNoSIn.setStyle("-fx-text-box-border: #ff0000;");
                }
            }
            else {
                phoneNoSIn.setStyle(null);
            }
        });
        BooleanBinding booleanBind =  phoneNoSIn.textProperty().isEmpty(); //making sure the user cannot search with no info entered
        searchC.disableProperty().bind(booleanBind);
        searchC.setOnAction(e -> {// when the search button is pressed
            if (isPhoneNo(phoneNoSIn).getValue()) //we test the value of the phone number
            {
                //display the call on the screen
                ListView<String> lists = new ListView<>();
                ObservableList<String> items =FXCollections.observableArrayList (ecallList.search(Long.parseLong(phoneNoSIn.getText())).toString());
                lists.setItems(items);
                layout.setCenter(lists);
            }
        });
        se.setSpacing(20);
        se.getChildren().addAll(spn);
        layout.setCenter(se); //adding the search box to the screen
    }

    /** Method that will carry the process of changing the status of an emergency call
     */
    private void changeStatus(){

        Text warn = new Text(""); //creating an empty text node for eventual warnings
        warn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        warn.setStrokeWidth(2);


        GridPane changeStatusGrid = new GridPane(); //creating the form for getting the phone number and new status
        changeStatusGrid.setAlignment(Pos.CENTER);
        changeStatusGrid.setHgap(10);
        changeStatusGrid.setVgap(10);
        changeStatusGrid.setPadding(new Insets(25, 25, 25, 25));
        Label vicTitle = new Label("Change the status of a call");
        vicTitle.setStyle("-fx-font-family: 'Verdana';" + "-fx-font-size: 20;" + "-fx-alignment: center;");
        changeStatusGrid.add(vicTitle,0,3, 2,1);
        changeStatusGrid.add(warn,0,7,4,1);
        //getting the input for phone number
        Label phoneNo = new Label("Enter phone number:");
        changeStatusGrid.add(phoneNo,0,4);
        TextField phoneNoCIn = new TextField();
        changeStatusGrid.add(phoneNoCIn,1,4);
        phoneNoCIn.focusedProperty().addListener((arg0, oldValue, newValue) -> { // making the field red if the phone number is not good
            if(!newValue){
                if(!isPhoneNo(phoneNoCIn).getValue()){
                    phoneNoCIn.setStyle("-fx-text-box-border: #ff0000;");
                }
            }
            else {
                phoneNoCIn.setStyle(null);
            }
        });
        //getting the input for the new status
        Label statusLabel = new Label("Status:");
        changeStatusGrid.add(statusLabel,0,5);
        ChoiceBox<String> status = new ChoiceBox<>(); //giving two choices for the status
        status.getItems().add("In Progress");
        status.getItems().add("Dealt With");
        status.setValue("In Progress");
        changeStatusGrid.add(status,1,5);

        status.setOnAction(e ->
        { // when the desired status is changed
            if(status.getValue().equals("Dealt With")){ // if the status is dealt with the user will be warned that this action will delete the call
                warn.setText("WARNING: THIS OPTION WILL DELETE THE RECORD OF THE EMERGENCY CALL");
                warn.setFill(Color.RED);
            }
            else
            {
                warn.setText("");
            }
        });
        //creating the button for changing the status
        Button change = new Button("Change status");
        changeStatusGrid.add(change,1,6);
        //disabling the button if the phone field is empty
        BooleanBinding booleanBind =  phoneNoCIn.textProperty().isEmpty();
        change.disableProperty().bind(booleanBind);
        change.setOnAction(e -> { // when change button is pressed
            if (isPhoneNo(phoneNoCIn).getValue())
            {
                if(!ecallList.changeStatus(Long.parseLong(phoneNoCIn.getText()), status.getValue()))
                { // if the status was already declared or the call was not found the user will be informed
                    warn.setFill(Color.RED);
                    warn.setText("ERROR: Status already declared or call not found");
                }else{
                    warn.setFill(Color.GREEN); // informing the user that the call was updated
                    warn.setText("Status updated successfully");
                }

            }
            else
            {
                if(!ecallList.removeEmergCall(Long.parseLong(phoneNoCIn.getText()))) //attempting to delete the call
                {   // informing the user that the call could not be found
                    warn.setFill(Color.RED);
                    warn.setText("ERROR: No call found for the phone number " + phoneNoCIn.getText());
                }
                else
                {    // informing the user that the call was deleted succesfully
                    warn.setFill(Color.GREEN);
                    warn.setText("Call deleted successfully");
                }
            }
        });

        layout.setCenter(changeStatusGrid); // displaying the form on the screen

    }

    /** Method used to display the units deployment statistics
     *
     */
    private void unitsDeployed()
    {
        // initialising both axis and setting the labels
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Departments");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Units deployed");
        BarChart<String,Number> deployChart = new BarChart<>(xAxis,yAxis); // creating the bar chart

        XYChart.Series<String,Number> unitsData = new XYChart.Series<>(); // creating the series of data items for the chart
        unitsData.setName("Number of units");
        // adding the data to the series
        unitsData.getData().add(new XYChart.Data<>("Police", ecallList.countUnit("Police")));
        unitsData.getData().add(new XYChart.Data<>("Ambulance", ecallList.countUnit("Ambulance")));
        unitsData.getData().add(new XYChart.Data<>("Fire", ecallList.countUnit("Fire")));

        deployChart.getData().add(unitsData); // adding the data to the deployment chart

        VBox dis = new VBox(deployChart); //creating a vertical box for the chart
        layout.setCenter(dis); // displaying the chart on the screen

    }

    /** Method that will display the calls that were assigned to certain departments
     */
    private void deptCalls()
    {
        //creating the same box for selecting the department wanted
        VBox service = new VBox();
        service.maxHeight(50);
        service.maxWidth(50);
        Text addServ = new Text("SELECT A SERVICE");
        addServ.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        addServ.setFill(Color.BLACK);
        addServ.setStrokeWidth(2);

        Text copyRight = new Text("Icons made by Freepik from www.flaticon.com");
        copyRight.setFont(Font.font("Arial", FontWeight.THIN, 10));
        copyRight.setFill(Color.GRAY);

        Button sAmbulance = new Button();
        sAmbulance.setStyle("-fx-background-image: url(icons/ambulance.png);"+
                "-fx-background-size: 100px 100px;"+
                "-fx-background-repeat: no-repeat;"+
                "-fx-background-position: center;"+
                "-fx-min-height: 110px;"+
                "-fx-min-width: 110px;");
        Button sPolice = new Button();
        sPolice.setStyle("-fx-background-image: url(icons/police.png);"+
                "-fx-background-size: 100px 100px;"+
                "-fx-background-repeat: no-repeat;"+
                "-fx-background-position: center;"+
                "-fx-min-height: 110px;"+
                "-fx-min-width: 110px;");
        Button sFire = new Button();
        sFire.setStyle("-fx-background-image: url(icons/fire.png);"+
                "-fx-background-size: 100px 100px;"+
                "-fx-background-repeat: no-repeat;"+
                "-fx-background-position: center;"+
                "-fx-min-height: 110px;"+
                "-fx-min-width: 110px;");
        HBox servicesButtons = new HBox();
        servicesButtons.setPadding(new Insets(25, 25, 25, 25));
        servicesButtons.setAlignment(Pos.CENTER);
        servicesButtons.setSpacing(20);
        servicesButtons.getChildren().addAll(sAmbulance,sPolice,sFire);
        service.getChildren().addAll(addServ,servicesButtons,copyRight);
        service.setPadding(new Insets(25, 25, 25, 25));
        service.setAlignment(Pos.CENTER);
        layout.setCenter(service);

        ListView<String> list = new ListView<>(); //creating the list for the calls
        //creating items with calls from all services
        ObservableList<String> policeCalls =FXCollections.observableArrayList (ecallList.depStats("Police").toString());
        ObservableList<String> ambulanceCalls =FXCollections.observableArrayList (ecallList.depStats("Ambulance").toString());
        ObservableList<String> fireCalls =FXCollections.observableArrayList (ecallList.depStats("Fire").toString());

        // adding the wanted items to the list and displaying it according the which button was pressed
        sPolice.setOnAction(e ->
        {
            list.setItems(policeCalls);
            layout.setCenter(list);
        });

        sAmbulance.setOnAction(e ->
        {
            list.setItems(ambulanceCalls);
            layout.setCenter(list);
        });

        sFire.setOnAction(e ->
        {
            list.setItems(fireCalls);
            layout.setCenter(list);
        });
    }

    /** Event handler that will be triggered when the user attempts to close the window using the window commands
     */
    private final EventHandler<WindowEvent> closeWindowConfirmation = event -> {
        saveToFile(ecallList); // first of all saving the list
        Alert closeConfirmation = new Alert( // making a new alert to pop up
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit? \nAll data will be saved automatically."
        );
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton( // creating the exit button
                ButtonType.OK
        );
        exitButton.setText("Exit");
        closeConfirmation.setHeaderText("Confirm Exit - Emergency Call Application");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(mainWindow);

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait(); // getting the response from the user
        if (!ButtonType.OK.equals(closeResponse.get())) {
            event.consume();
        }
    };

    /** Method used to test the validity of a phone number
     * @param in: the phone number to be tested
     * @return: retunrs an obeservable boolean with the value true if it's a phone number or false if it's not
     */
    private ObservableBooleanValue isPhoneNo(TextField in)
    {
        SimpleBooleanProperty isPn = new SimpleBooleanProperty(); //creating the boolean property
        try{ //trying the parse the text from the text filed
            long num = Long.parseLong(in.getText());
            isPn.setValue(num > 0); // if the value of the next field is a number, it is tested to be greater than 0
        }catch(NumberFormatException e){ // if the value of the text filed is not a number false is returned
            isPn.setValue(false);
        }
        return isPn;
    }

    /** Method used to test if the content of a age text field is actually a proper age value
     * @param in: the contet of the age text field
     * @return: returns true if the value is a valid age or false if it's not
     */
    private boolean isValidAge(TextField in)
    {
        try{
            long num = Integer.parseInt(in.getText());
            return num >= 0 && num < 120;
        }catch(NumberFormatException e){
            return false;
        }
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

    public static void main(String[] args)
    {
        launch(args);
    }
}

