package com.example.login;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HowAreYouFeelingNowPosController extends BaseController {

    @FXML
    private Slider slider1, slider2, slider3;
    @FXML
    private ProgressBar progressBarPos;
    @FXML
    private Label errorMessageLabelPos;
    @FXML
    private Button btnPosQues;
    @FXML
    private Button btnNoPos;

    @FXML
    private Button btnYesPos;

    // Boolean properties to track slider interactions
    private final BooleanProperty slider1Moved = new SimpleBooleanProperty(false);
    private final BooleanProperty slider2Moved = new SimpleBooleanProperty(false);
    private final BooleanProperty slider3Moved = new SimpleBooleanProperty(false);
   // private String currentUserEmail;

//    public void setUserEmail(String email) {
//        this.currentUserEmail = email;
//        System.out.println("User Email in PosController: " + email); // Optional for debugging
//    }


    @FXML
    public void initialize() {
        // Add listeners to each slider
        initializeSlider(slider1, slider1Moved, "Slider 1");
        initializeSlider(slider2, slider2Moved, "Slider 2");
        initializeSlider(slider3, slider3Moved, "Slider 3");

    }

    private void initializeSlider(Slider slider, BooleanProperty moved,String sliderName) {
        if (slider == null) {
            System.err.println(sliderName + " is null. Please check the FXML file.");
            return;
        }
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!moved.get() && !oldVal.equals(newVal)) {
                moved.set(true); // Mark the slider as interacted
                updateProgressBar(); // Update the progress bar
            }
        });
    }

    private void updateProgressBar() {
        double progress = 0;
        if (slider1Moved.get()) progress += 0.1;
        if (slider2Moved.get()) progress += 0.1;
        if (slider3Moved.get()) progress += 0.1;
        progressBarPos.setProgress(progress);
    }

    private boolean areAllSlidersMoved() {
        return slider1Moved.get() && slider2Moved.get() && slider3Moved.get();
    }

    @FXML
    public void onBtnPosQuesClicked(ActionEvent event) {
        if (areAllSlidersMoved() && progressBarPos.getProgress() >= 0.3) {
            double sum = slider1.getValue() + slider2.getValue() + slider3.getValue() ;
            if (model != null) {
                model.setS1(sum);
                System.out.println("Sumpos: " + model.getS1());
            } else {
                System.err.println("Model is not initialized!");
            }
            // model.setS1(sum);

            resetInteractedSliders();

            redirectTo("HowAreYouFeelingNowNeg.fxml", event);
        } else {
            errorMessageLabelPos.setText("                    You must answer all the questions to move forward!");
        }
    }
//    @FXML
//    private void onBtnPosQuesClicked() {
//        if (areAllSlidersMoved() && progressBarPos.getProgress() >= 0.3) {
//            // Calculate S1
//            double sum = slider1.getValue() + slider2.getValue() + slider3.getValue();
//
//            setS1(sum); // Pass value to BaseController
//            System.out.println("Sumpos: " + getS1());
//            // Reset slider states for future interactions
//            resetInteractedSliders();
//
//            // Redirect to HowAreYouFeelingNowNeg.fxml
//            redirectTo("HowAreYouFeelingNowNeg.fxml");
//        } else {
//            // Show error message if progress is insufficient
//            errorMessageLabelPos.setText("                    You must answer all the questions to move forward!");
//        }
//    }
//    private void redirectTo(String fxmlFile) {
//        try {
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
//            Parent root = loader.load();
//
//
//            Stage currentStage = (Stage) btnPosQues.getScene().getWindow();
//            currentStage.close();
//
//            // Open the next stage
//            Stage stage = new Stage();
//            stage.initStyle(StageStyle.UNDECORATED);
//            stage.setScene(new Scene(root));
//            stage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void resetInteractedSliders() {
        slider1Moved.set(false);
        slider2Moved.set(false);
        slider3Moved.set(false);
        progressBarPos.setProgress(0); // Reset progress bar if required
    }


}
