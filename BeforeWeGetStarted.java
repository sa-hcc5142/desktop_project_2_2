package com.example.login;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BeforeWeGetStarted implements Initializable {

    @FXML
    private Label lblGoAhead;;

    @FXML
    private TextArea taHowAreYouFeelingNow;
    
    private final String welcomeMessage=STR."""
                Hi I am just a prototype trying to help
                you with some common categories of 
                feelings that are defined in me. Maybe 
                I can't give you accurate results now
                but I am in a growing stage and I will
                try to help you my level best!""";
    private String currentUserEmail;


    
    // Method to close the current window
    private void closeCurrentWindow(@NotNull MouseEvent event) {
        // Close the current stage
        Stage currentStage = (Stage) ((Label) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    // Method to open the new WhatKindOfHelpDoYouNeed window
    private void openHowAreYouFeelingNow(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/login/HowAreYouFeelingNowPos.fxml"));
            Parent root = fxmlLoader.load(); // Load the FXML first

//            SharedModel.getInstance().setCurrentUserEmail(currentUserEmail);
//            System.out.println("Email set in SharedModel openHAre: " + SharedModel.getInstance().getCurrentUserEmail());
//

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();  // This will log the error in the console
            System.err.println("Could not load the FXML file.");
        }
    }

    // Displays text with a typing animation
    private void displayMessageWithTypingEffect(String message) {
        Timeline timeline = new Timeline();
        final int[] index = {0};

        KeyFrame keyFrame = new KeyFrame(Duration.millis(50), event -> {
            if (index[0] < message.length()) {
                taHowAreYouFeelingNow.appendText(String.valueOf(message.charAt(index[0])));
                index[0]++;
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(message.length());
        timeline.play();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String email = SharedModel.getInstance().getCurrentUserEmail();
        System.out.println("Email set in SharedModel BeforeWeGetStarted: " + email);

        // Disable text editing in the TextArea
        taHowAreYouFeelingNow.setEditable(false);

        // Display the welcome message with typing animation
        displayMessageWithTypingEffect(welcomeMessage);

        lblGoAhead.setOnMouseClicked(event -> {
                 SharedModel.getInstance().setCurrentUserEmail(email);
                openHowAreYouFeelingNow(event);
                closeCurrentWindow(event);

        });
    }

}
