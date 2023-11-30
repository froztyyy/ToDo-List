/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package todolist;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author jcarl
 */
public class MaximizeDisplayListCardController implements Initializable {

    @FXML
    private TextField txtDescription;
    @FXML
    private TextArea txtDetails;
    @FXML
    private TextField txtDueDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private DisplayListController displayListController;

    public void setDisplayListController(DisplayListController displayListController) {
        this.displayListController = displayListController;
    }

    public void setData(toDoListData todoData) {
        txtDescription.setText(todoData.getDescription());
        txtDetails.setText(todoData.getDetails());
        txtDueDate.setText(todoData.getDue_date());
        // You may set other fields as needed
    }

    private java.sql.Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    // Call this method to retrieve data from the database based on description
    public void retrieveDataFromDatabase(String description) {

        try {
            // Establish a connection
            connect = database.getConnection();

            // SQL query to retrieve data based on description
            String query = "SELECT * FROM task WHERE description = ?";

            // Prepare the statement
            prepare = connect.prepareStatement(query);
            prepare.setString(1, description);

            // Execute the query
            result = prepare.executeQuery();

            // Check if there is a result
            if (result.next()) {
                // Retrieve data from the result set
                String retrievedDescription = result.getString("description");
                String retrievedDetails = result.getString("details");
                String retrievedDueDate = result.getString("due_date");

                // Set the retrieved data to the UI elements
                txtDescription.setText(retrievedDescription);
                txtDetails.setText(retrievedDetails);
                txtDueDate.setText(retrievedDueDate);
            } else {
                // Handle the case where no data is found for the given description
                System.out.println("No data found for the description: " + description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        } finally {
            // Close resources in a finally block
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
