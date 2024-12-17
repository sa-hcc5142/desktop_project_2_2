package com.example.login;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Delete_Account {

    @FXML
    private TextField tfKuet_email;

    @FXML
    private TextField pfPasswordText;

    @FXML
    private Label errorMessageLabelDelAccount;

    @FXML
    private Button btnDeleteAccount;

    @FXML
    private CheckBox chkShowPassword;

    @FXML
    private PasswordField pfPasswordHidden;

    @FXML
    private void initialize() {
        // Initially hide the plain text password field and show the masked password field
        pfPasswordHidden.setVisible(true);
        pfPasswordText.setVisible(false);

        if (chkShowPassword != null) {
            chkShowPassword.setOnAction(event -> {
                if (chkShowPassword.isSelected()) {
                    // Show password as plain text
                    pfPasswordText.setText(pfPasswordHidden.getText());
                    pfPasswordText.setVisible(true);
                    pfPasswordHidden.setVisible(false);
                } else {
                    // Hide password, show it as masked
                    pfPasswordHidden.setText(pfPasswordText.getText());
                    pfPasswordHidden.setVisible(true);
                    pfPasswordText.setVisible(false);
                }
            });
        }

        // Initialize Delete Account button
        btnDeleteAccount.setOnAction(event -> {
            String email = tfKuet_email.getText();
            String password = pfPasswordHidden.isVisible() ? pfPasswordHidden.getText() : pfPasswordText.getText();

            if (isDeleteFieldAlright(email, password)) {
                // Proceed to delete account
                deleteAccount(email, (Stage) btnDeleteAccount.getScene().getWindow());
            }
        });
    }

    private boolean isDeleteFieldAlright(String email, String password) {
        if (email.isEmpty()) {
            errorMessageLabelDelAccount.setText("Email is empty!");
            return false;
        }
        if (!email.contains("@stud.kuet.ac.bd")) {
            errorMessageLabelDelAccount.setText("Invalid email address!");
            return false;
        }
        if (password.isEmpty()) {
            errorMessageLabelDelAccount.setText("Password is empty!");
            return false;
        }
        if (password.length() < 8 || !password.contains("#")) {
            errorMessageLabelDelAccount.setText("Password must contain at least 8 characters and a '#'!");
            return false;
        }
        if (!isValidCredentials(email, password)) {
            return false; // Error message already displayed in isValidCredentials
        }
        return true;
    }

    private boolean isValidCredentials(String email, String password) {
        String query = "SELECT password FROM signup WHERE email = ?";
        String DB_URL = "jdbc:mysql://localhost:3306/mentalhealth";
        String DB_USERNAME = "root";
        String DB_PASSWORD = "1234";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (!storedPassword.equals(password)) {
                    errorMessageLabelDelAccount.setText("Incorrect Password!");
                    return false;
                }
                return true;
            } else {
                errorMessageLabelDelAccount.setText("Wrong Email Address!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessageLabelDelAccount.setText("Database Error!");
            return false;
        }
    }

    private void deleteAccount(String email, Stage currentStage) {
        String query = "DELETE FROM signup WHERE email = ?";
        String DB_URL = "jdbc:mysql://localhost:3306/mentalhealth";
        String DB_USERNAME = "root";
        String DB_PASSWORD = "1234";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            int rowsDeleted = stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Deletion");
            alert.setHeaderText(null);

            if (rowsDeleted > 0) {
                alert.setContentText("Account deleted successfully!");
            } else {
                alert.setContentText("Error deleting account!");
            }

            // Show the alert
            alert.show();

            // Create a Timeline to close the alert after 1.5 seconds
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1.5), e -> alert.close())
            );
            timeline.setCycleCount(1);
            timeline.play();

            // After the alert closes, load the signUp.fxml window
            timeline.setOnFinished(e -> {
                try {
                    // Load the signUp.fxml file
                    Parent root = FXMLLoader.load(getClass().getResource("signUp.fxml"));

                    // Create a new Stage for the sign-up window
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.show();

                    // Close the current Delete_Account window
                    if (currentStage != null) {
                        currentStage.close();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while connecting to the database.");
            alert.showAndWait();
        }
    }




}
