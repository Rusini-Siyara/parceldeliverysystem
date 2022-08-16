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

public class bank extends Application {

    @Override
    public void start(Stage primaryStage) {
    primaryStage.setTitle("Welcome to Parcel Delivery System");

    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    Text scenetitle = new Text("Add Bank");
    scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(scenetitle, 0, 0, 2, 1);


    Label BankNameLabel = new Label("Bank Name:");
    grid.add(BankNameLabel, 0, 2);

    TextField BankNameTextField = new TextField();
    grid.add(BankNameTextField, 1, 2);

    Label BankCityLabel = new Label("Bank City:");
    grid.add(BankCityLabel, 0, 3);

    TextField BankCityTextField = new TextField();
    grid.add(BankCityTextField, 1, 3);

    Label BankCountryLabel = new Label("Bank Country:");
    grid.add(BankCountryLabel, 0, 4);

    TextField BankCountryTextField = new TextField();
    grid.add(BankCountryTextField, 1, 4);


    Button saveButton = new Button("Save");
    HBox hBox = new HBox(10);
    hBox.setAlignment(Pos.BOTTOM_RIGHT);
    hBox.getChildren().add(saveButton);
    grid.add(hBox, 1, 8);

    Label savedBankLabel = new Label("Saved Bank:");
    grid.add(savedBankLabel, 0, 9);

    Label savedBank = new Label("");
    grid.add(savedBank, 1, 9);

    //button to play a match
    saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {

                    String BankName    = BankNameTextField.getText().trim();
                    String BankCity    = BankCityTextField.getText().trim();
                    String BankCountry = BankCountryTextField.getText().trim();

                    try {

                        int BANKID = saveBank(BankName, BankCity,BankCountry);
                        String savedBankName = getbank(BANKID);
                        savedBank.setText(savedBankName);

                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                    }

                }
            });


    Scene scene = new Scene(grid, 500, 475);
    primaryStage.setScene(scene);

    primaryStage.show();
}


    public int saveBank(String BankName , String BankCity , String BankCountry) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);

            String query = "INSERT INTO bank(BANKNAME,BANKCITY,BANKCOUNTRY) VALUES( ?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, BankName);
            statement.setString(2, BankCity);
            statement.setString(3, BankCountry);


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


    public String getbank(int BANKID) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String bank = "";

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT BANKNAME,BANKCITY,BANKCOUNTRY FROM bank WHERE  BANKID =  ?";
            statement = connection.prepareStatement(query);

            statement.setInt(1, BANKID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bank = resultSet.getString(1) + " " + resultSet.getString(2);
            }

            return bank;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        } finally {
            connection.close();
        }

        return bank;
    }

    public static void main(String[] args) {
        launch(args);
    }

}

