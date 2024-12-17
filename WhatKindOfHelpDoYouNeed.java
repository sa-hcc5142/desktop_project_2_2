package com.example.login;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.RadioButton;
import java.util.ResourceBundle;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WhatKindOfHelpDoYouNeed implements Initializable {

    @FXML
    private Button btnNextInterface;

    @FXML
    private RadioButton btnPaidHelp;

    @FXML
    private Button btnPeerGroup;

    @FXML
    private RadioButton btnUnpaidHelp;

    // Declare currentUserEmail as an instance variable
    private String currentUserEmail;
    public void initializeCustomLogic() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        currentUserEmail = SharedModel.getInstance().getCurrentUserEmail();
        System.out.println("Email in WhatKindOfHelpDoYouNeed: " + currentUserEmail);

        // Set up the event handler for the btnPaidHelp click
        btnUnpaidHelp.setOnAction(event -> {
            if (btnUnpaidHelp.isSelected()) {
                btnPaidHelp.setSelected(false); // Deselect PaidHelp RadioButton
                loadUnpaidHelpWindow(); // Load Unpaid_Help.fxml
            }
        });

        // Set up event handler for btnPaidHelp
        btnPaidHelp.setOnAction(event -> {
            if (btnPaidHelp.isSelected()) {
                btnUnpaidHelp.setSelected(false); // Deselect UnpaidHelp RadioButton
                displayDoctorProfiles(); // Load dr_1.fxml
            }
        });
        if (btnNextInterface != null) {
            btnNextInterface.setOnAction(event -> {
                // Run UI updates on the JavaFX Application Thread
                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/login/LogOut.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(loader.load()));
                        //stage.setTitle("Log Out");
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.show();

                        // Close current window
                        Stage currentStage = (Stage) btnNextInterface.getScene().getWindow();
                        currentStage.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });

        } else {
            System.err.println("btnNextInterface is null! Make sure it is correctly linked in the FXML file.");
        }

        // Set up action for btnPeerGroup
        btnPeerGroup.setOnAction(event -> openChatWindow());

    }
    private void loadUnpaidHelpWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/login/Unpaid_Help.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Unpaid Help");

            // Set up controller logic for Unpaid_Help.fxml
            UnpaidHelpController controller = loader.getController();
            controller.initializeLabels();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openChatWindow() {
        try {
            // Load the Chat UI FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat_UI.fxml"));
            Parent chatRoot = loader.load();

            // Pass email to ChatPage
            ChatPage chatController = loader.getController();
            chatController.setCurrentUserEmail(currentUserEmail);

            // Create a new stage for the chat window
            Stage chatStage = new Stage();
            //chatStage.setTitle("Group Chat");
            chatStage.setScene(new Scene(chatRoot, 900, 600));

            // Optional: You can set this stage to close when the main stage closes
            // Get the current stage
            Stage currentStage = (Stage) btnPeerGroup.getScene().getWindow();
            chatStage.initOwner(currentStage);
            chatStage.initStyle(StageStyle.UNDECORATED);
            // Show the chat window
            chatStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, show an error dialog to the user
            // You could use Alert or a custom error handling method
        }
    }

    private void displayDoctorProfiles() {
        String url = "https://jsonhost.com/json/ca498465caf723b264f358c69b6265f2"; // Replace with your JSON URL
        List<DoctorProfile> profiles = fetchProfiles(url);

        if (profiles != null && profiles.size() >= 5) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/login/dr_1.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("Doctor Profiles");

                DoctorProfilesController controller = loader.getController();
                controller.setDoctorProfiles(profiles);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<DoctorProfile> fetchProfiles(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parseProfiles(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<DoctorProfile> parseProfiles(String json) {
        List<DoctorProfile> profiles = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        Gson gson = new Gson();

        for (int i = 0; i < jsonArray.size(); i++) {
            DoctorProfile profile = gson.fromJson(jsonArray.get(i), DoctorProfile.class);
            System.out.println("Parsed profile: " + profile.getName() + ", Contact: " + profile.getContactNumber());
            profiles.add(profile);
        }

        return profiles;
    }

}
