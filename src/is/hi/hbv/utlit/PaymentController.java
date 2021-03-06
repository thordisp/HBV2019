package is.hi.hbv.utlit;

import is.hi.hbv.vinnsla.Hotel;
import is.hi.hbv.vinnsla.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;


import javax.lang.model.SourceVersion;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PaymentController<fxmlLoader> implements  Initializable{

    @FXML
    private TextField getPhone;         // User inserts a phone number
    @FXML
    private TextField getCVC;           // User inserts a CVC for his card
    @FXML
    private TextField getFirstname;     // User inserts his first name
    @FXML
    private TextField getLastname;      // User inserts his last name
    @FXML
    private TextField getKennitala;     // User inserts his kennitala
    @FXML
    private TextField getExpirydate;    // User inserts the expiry date of his card
    @FXML
    private TextField getEmail;         // User inserts his email
    @FXML
    private TextField getAddress;       // User inserts his address
    @FXML
    private Button nextButton;          // The payment information is confirmed and next window shown
    @FXML
    private Button backButton;          // Shows the last window (services) again
    @FXML
    private Label totalscost;           // Shows the totalcost of the payment
    @FXML
    private Label totallist;            // Shows a list of the room and services ordered
    @FXML
    private TextField getCardnumber;    // User inserts his cardnumber

    // The values acquired from the search
    private Hotel chosenHotel;
    private long daycountvalue;
    private LocalDate arrivalchoicevalue;
    private LocalDate departurechoicevalue;
    private int guestnumbervalue;
    private Room chosenRoom;
    private int totalPrice;
    private int servicePrice;

    /*
    * This function checks if the payment information are valid, and if so, loads the next window
    * in the program
     */
    @FXML
    void Next(ActionEvent actionEvent) {
        if (validateTextfield() == false ){
            System.out.println("hello");
        } else if (         validateFirstName() && validateLastName()
                && validateEmaill() && validatePhone()
                && validateKennitala((getKennitala.getText())) && validateCardNumber()
                && validateCardExpiryDate() && validateCVC() == true)
        {

            Stage stage123 = (Stage) nextButton.getScene().getWindow();
            stage123.close();
            String Firsname_text = getFirstname.getText();
            String Lastname_text = getLastname.getText();
            String Email_text = getEmail.getText();
            String Phone_text = getPhone.getText();
            String Address_text = getAddress.getText();
            String Kennitala_text = getKennitala.getText();
            String CardNumber_text = getCardnumber.getText();
            String List = totallist.getText();

            FXMLLoader Loader = new FXMLLoader();
            Loader.setLocation(getClass().getResource("confirm.fxml"));
            try {
                Loader.load();
            } catch (IOException ex) {
                Logger.getLogger(servicesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            confirmController display = Loader.getController();
            display.setText123(Firsname_text,Lastname_text,Email_text,Phone_text,Address_text,Kennitala_text,CardNumber_text,totalPrice, servicePrice);
            display.setText12(chosenHotel, daycountvalue, arrivalchoicevalue, departurechoicevalue, guestnumbervalue);
            Parent p = Loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            stage.show();
        }
    }

    // Goes back to choosing a room in a chosen hotel
    @FXML
    void Back (ActionEvent actionEvent) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("herbergi.fxml"));
        try {
            Loader.load();
        } catch (IOException ex) {
            Logger.getLogger(servicesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        RoomController herbergi = Loader.getController();
        herbergi.setValues(chosenHotel, daycountvalue, arrivalchoicevalue, departurechoicevalue, guestnumbervalue);

        Parent herbergi_parent = Loader.getRoot();
        Scene herbergi_scene = new Scene(herbergi_parent);
        Stage main_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        main_stage.setScene(herbergi_scene);
        main_stage.show();
    }

    // Unused function
    private boolean isInt(TextField input, String message) {
        try {
            int number = Integer.parseInt(getCardnumber.getText());
            System.out.println("Number is valid: "+ number);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error: "+message + " is invalid");
            return false;
        }
    }

    // Checks if the cardnumber is valid
    private boolean validateCardNumber(){
        Pattern p = Pattern.compile("^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
                "(?<mastercard>5[1-5][0-9]{14})|" +
                "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
                "(?<amex>3[47][0-9]{13})|" +
                "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|" +
                "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$");
        Matcher m = p.matcher(getCardnumber.getText());
        if(m.find() && m.group().equals(getCardnumber.getText())){
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Card-number");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Card-number");
            alert.showAndWait();
            return false;
        }
    }

    // Checks if the expiry date exists
    private boolean validateCardExpiryDate() {
        Pattern p = Pattern.compile("(?:0[1-9]|1[0-2])/[0-9]{2}");
        Matcher m = p.matcher(getExpirydate.getText());
        if(m.find() && m.group().equals(getExpirydate.getText())){
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Expiry date");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Expiry date");
            alert.showAndWait();
            return false;
        }
    }

    // Checks if CVC is in the correct form
    private boolean validateCVC() {
        Pattern p = Pattern.compile("[1-9][1-9][1-9]");
        Matcher m = p.matcher(getCVC.getText());
        if(m.find() && m.group().equals(getCVC.getText())){
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate CVC");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid CVC");
            alert.showAndWait();
            return false;
        }
    }

    // Checks if email is in the correct form
    private boolean validateEmaill(){
        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher m = p.matcher(getEmail.getText());
        if(m.find() && m.group().equals(getEmail.getText())){
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Email");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Email");
            alert.showAndWait();
            return false;
        }
    }

    // Checks if first name makes sens
    private boolean validateFirstName(){
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(getFirstname.getText());
        if(m.find() && m.group().equals(getFirstname.getText())){
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate First Name");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid First Name");
            alert.showAndWait();
            return false;
        }
    }

    //  Checks if last name makes sens
    private boolean validateLastName(){
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(getLastname.getText());
        if(m.find() && m.group().equals(getLastname.getText())){
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Last Name");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Last Name");
            alert.showAndWait();

            return false;
        }
    }

    // Checks if kennitala is valid
    private boolean validateKennitala(final String a){
        if (a == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Kennitala");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Kennitala");
            alert.showAndWait();
            return false;
        }

        final String kennitalaClean = cleanKennitala(a);

        if (kennitalaClean.length() != 10) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Kennitala");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Kennitala");
            alert.showAndWait();
            return false;
        }

        int sum =
                (Integer.parseInt(kennitalaClean.substring(0, 1)) * 3)
                        + (Integer.parseInt(kennitalaClean.substring(1, 2)) * 2)
                        + (Integer.parseInt(kennitalaClean.substring(2, 3)) * 7)
                        + (Integer.parseInt(kennitalaClean.substring(3, 4)) * 6)
                        + (Integer.parseInt(kennitalaClean.substring(4, 5)) * 5)
                        + (Integer.parseInt(kennitalaClean.substring(5, 6)) * 4)
                        + (Integer.parseInt(kennitalaClean.substring(6, 7)) * 3)
                        + (Integer.parseInt(kennitalaClean.substring(7, 8)) * 2);

        int num = 11 - (sum % 11);
        num = (num == 11) ? 0 : num;
        if (num == Integer.parseInt(kennitalaClean.substring(8, 9))){
            return true;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Kennitala");
        alert.setHeaderText(null);
        alert.setContentText("Please Enter Valid Kennitala");
        alert.showAndWait();
        return false;
    }
    public static String cleanKennitala(final String a) {
        return a.replaceAll("[^0-9]", "");
    }

    // checks if phone number is valid
    private boolean validatePhone(){
        Pattern p = Pattern.compile("(00354|354)?[5-8][0-9][0-9][0-9][0-9][0-9][0-9]");
        Matcher m = p.matcher(getPhone.getText());
        if(m.find() && m.group().equals(getPhone.getText())){
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Mobile Number");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Mobile Number");
            alert.showAndWait();
            return false;
        }
    }

    // checks if all text fields have been filled
    private boolean validateTextfield(){
        if(     getFirstname.getText() == null | getLastname.getText() == null|
                getEmail.getText() == null | getPhone.getText() == null |
                getKennitala.getText() == null | getCardnumber.getText() == null |
                getExpirydate.getText() == null | getCVC.getText() == null
                )
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Into The Fields");
            alert.showAndWait();
            return false;
        }
        return true;
    }


    /*
    * Shows the total cost of the order together with room cost and service cost separated
    * and the main information about the hotel
     */
    public void setText12 (String totalslist) {
        int roomPrice = totalPrice - servicePrice;
        totalscost.setText("Total cost : " + totalPrice +" ISK");
        totallist.setText("----Order details----\n"
                + chosenHotel.getName() + "\n"
                + chosenHotel.getStars() + " stars" + "\n"
                + chosenHotel.getHotelAddress() + "\n"
                + "Room number " + chosenRoom.getRoomNr() + "\n"
                + String.valueOf(roomPrice) + " ISK for " + daycountvalue + " night(s)" + "\n"
                + "----Additional Services----\n"+ totalslist + "\n");
    }

    /*
    * Sets the values of the chosen hotel from search
     */
    public void setValues(Hotel hotel, Room room, int price, int addedPrice, long daycount, LocalDate arrival, LocalDate departure, int guests) {
        chosenHotel = hotel;
        daycountvalue = daycount;
        arrivalchoicevalue = arrival;
        departurechoicevalue = departure;
        guestnumbervalue = guests;
        chosenRoom = room;
        totalPrice = price;
        servicePrice = addedPrice;
    }

    /*
    * Sets the payment information if the user has chosen to save it after having ordered a hotel before
     */
    public void setSaveInfo(String Firstname, String Lastname, String Email, String Phone, String Address, String Kennitala, String Card) {
        this.getFirstname.setText(Firstname);
        this.getLastname.setText(Lastname);
        this.getEmail.setText(Email);
        this.getPhone.setText(Phone);
        this.getAddress.setText(Address);
        this.getKennitala.setText(Kennitala);
        this.getCardnumber.setText(Card);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getFirstname.setTooltip(new Tooltip("Please enter your first name"));
        getLastname.setTooltip(new Tooltip("Please enter your last name"));
        getKennitala.setTooltip(new Tooltip("Please enter your icelandic ID without symbol"));
        getEmail.setTooltip(new Tooltip("Please enter your email"));
        getPhone.setTooltip(new Tooltip("Please enter your phone number without symbol"));
        getAddress.setTooltip(new Tooltip("Please enter your address"));
        getCardnumber.setTooltip(new Tooltip("Please enter your credit card number"));
        getExpirydate.setTooltip(new Tooltip("Please enter your expiry date with symbol ex. mm/yy"));
        getCVC.setTooltip(new Tooltip("Please enter your CVC"));
        nextButton.setTooltip(new Tooltip("Finish payment"));
        backButton.setTooltip(new Tooltip("Go back to search"));
    }
}