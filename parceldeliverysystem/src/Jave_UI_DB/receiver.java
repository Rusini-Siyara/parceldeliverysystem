package Jave_UI_DB;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

public class receiver extends Application {


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome To Parcel Delivery System");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Add Receiver");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);


        Label firstNameLabel = new Label("First Name:");
        grid.add(firstNameLabel, 0, 2);

        TextField firstNameTextField = new TextField();
        grid.add(firstNameTextField, 1, 2);

        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 3);

        TextField lastNameTextField = new TextField();
        grid.add(lastNameTextField, 1, 3);

        Label AddressLabel = new Label("Address :");
        grid.add(AddressLabel, 0, 4);

        TextField AddressTextField = new TextField();
        grid.add(AddressTextField, 1, 4);

        Label EmailLabel = new Label("Email :");
        grid.add(EmailLabel, 0, 5);

        TextField EmailTextField = new TextField();
        grid.add(EmailTextField, 1, 5);

        Label TelLabel = new Label("Tel :");
        grid.add(TelLabel, 0, 6);

        TextField TelTextField = new TextField();
        grid.add(TelTextField, 1, 6);

        Button saveButton = new Button("Save");
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(saveButton);
        grid.add(hBox, 1, 8);

        Label savedReceiverLabel = new Label("Saved Receiver:");
        grid.add(savedReceiverLabel, 0, 9);

        Label savedReceiver = new Label("");
        grid.add(savedReceiver, 1, 9);

        //button to play a match
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {


                        String lastName  = lastNameTextField.getText().trim();
                        String firstName = firstNameTextField.getText().trim();
                        String Address   = AddressTextField.getText().trim();
                        String Email     = EmailTextField.getText().trim();
                        String Tel       = TelTextField.getText().trim();

                        try {

                            int RECEIVERID = saveReceiver(firstName,lastName,Address,Email,Tel);
                            String savedReceiverName = getReceiver(RECEIVERID);
                            savedReceiver.setText(savedReceiverName);

                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                        }

                    }
                });


        Scene scene = new Scene(grid, 500, 475);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public int saveReceiver(String firstName, String lastName, String Address,String Email, String Tel) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);

            String query = "INSERT INTO receiver(RFIRSTNAME,RLASTNAME,RADDRESS,REMAIL,RTEL) VALUES( ?, ?,?,?,?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, Address);
            statement.setString(4, Email);
            statement.setString(5, Tel);

            statement.executeUpdate();
            connection.commit();
            resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        } finally {
            connection.close();

        }

        return 0;
    }


    public String getReceiver(int RECEIVERID) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String receiver = "";

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT RFIRSTNAME,  RLASTNAME, RADDRESS,REMAIL,RTEL FROM receiver WHERE RECEIVERID = ?";
            statement = connection.prepareStatement(query);

            statement.setInt(1,RECEIVERID );
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                receiver = resultSet.getString(1) + " " + resultSet.getString(2);
            }

            return receiver;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        } finally {
            connection.close();
        }

        return receiver;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
