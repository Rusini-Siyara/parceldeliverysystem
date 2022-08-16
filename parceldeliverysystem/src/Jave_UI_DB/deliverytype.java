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

public class deliverytype extends Application {


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome to Parcel Delivery System");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Add Delivery Type");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);


        Label DeliveryTypeNameLabel = new Label("Delivery type Name:");
        grid.add(DeliveryTypeNameLabel, 0, 2);

        TextField DeliveryTypeNameTextField = new TextField();
        grid.add(DeliveryTypeNameTextField, 1, 2);


        Button saveButton = new Button("Save");
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(saveButton);
        grid.add(hBox, 1, 5);

        Label savedDeliveryTypeLabel = new Label("Saved Delivery Type:");
        grid.add(savedDeliveryTypeLabel, 0, 6);

        Label savedDeliveryType = new Label("");
        grid.add(savedDeliveryType, 1, 6);

        //button to play a match
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {

                        String DeliveryTypeName    = DeliveryTypeNameTextField.getText().trim();


                        try {

                            int DELIVERYTYPEID = saveDeliveryType(DeliveryTypeName);
                            String savedDeliveryTypeName = getdeliveryType(DELIVERYTYPEID);
                            savedDeliveryType.setText(savedDeliveryTypeName);

                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                        }

                    }
                });


        Scene scene = new Scene(grid, 500, 475);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public int saveDeliveryType(String DeliveryTypeName) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);

            String query = "INSERT INTO deliverytype(DELIVERYTYPENAME) VALUES(?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, DeliveryTypeName);



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


    public String getdeliveryType(int DELIVERYTYPEID) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String deliverytype = "";

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT DELIVERYTYPENAME FROM deliverytype WHERE  DELIVERYTYPEID =  ?";
            statement = connection.prepareStatement(query);

            statement.setInt(1, DELIVERYTYPEID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                deliverytype = resultSet.getString(1) ;
            }

            return deliverytype;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        } finally {
            connection.close();
        }

        return deliverytype;
    }

    public static void main(String[] args) {
        launch(args);
    }

}



