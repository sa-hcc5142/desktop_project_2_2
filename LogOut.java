package com.example.login;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LogOut {
    @FXML
    private Button btnLogOut;

    @FXML
    private Label lblDeleteAccount;

    @FXML
    private void initialize() {
        btnLogOut.setOnAction(event -> {
            // Display alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You have been logged out of your account successfully!");
            alert.setHeaderText(null);
            alert.show();

            // Close alert after 1.5 seconds
            new Thread(() -> {
                try {
                    // Sleep for 1.5 seconds
                    Thread.sleep(1500);

                    // Update UI on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        if (alert.isShowing()) {
                            alert.hide(); // Close the alert
                        }

                        try {
                            // Close LogOut.fxml
                            Stage currentStage = (Stage) btnLogOut.getScene().getWindow();
                            currentStage.close();

                            // Open sample.fxml
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/login/sample.fxml"));
                            Stage stage = new Stage();
                            stage.setScene(new Scene(loader.load()));
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        lblDeleteAccount.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/login/Delete_Account.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.initStyle(StageStyle.UNDECORATED);
                //stage.setTitle("Delete Account");
                stage.show();

                // Close current window
                Stage currentStage = (Stage) lblDeleteAccount.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
