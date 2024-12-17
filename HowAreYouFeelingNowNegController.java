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

public class HowAreYouFeelingNowNegController extends BaseController {

    @FXML
    private Slider slider1, slider2, slider3, slider4;
    @FXML
    private ProgressBar progressBarNeg;
    @FXML
    private Label errorMessageLabelNeg;
    @FXML
    private Button btnNegQues;
    @FXML
    private Button btnNoNeg;
    @FXML
    private Button btnYesNeg;

    // Boolean properties to track slider interactions
    private final BooleanProperty slider1Moved = new SimpleBooleanProperty(false);
    private final BooleanProperty slider2Moved = new SimpleBooleanProperty(false);
    private final BooleanProperty slider3Moved = new SimpleBooleanProperty(false);
    private final BooleanProperty slider4Moved = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        // Initialize sliders and add listeners
        // Add listeners to each slider
        initializeSlider(slider1, slider1Moved, "Slider 1");
        initializeSlider(slider2, slider2Moved, "Slider 2");
        initializeSlider(slider3, slider3Moved, "Slider 3");
        initializeSlider(slider4, slider4Moved, "Slider 4");

    }



    private void initializeSlider(Slider slider, BooleanProperty moved,String sliderName) {
        if (slider == null) {
            System.err.println(sliderName + " is null. Please check the FXML file.");
            return;
        }
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!moved.get() && !oldVal.equals(newVal)) {
                moved.set(true); // Mark the slider as interacted
                updateProgressBar(); // Update progress bar
            }
        });
    }

    private void updateProgressBar() {
        double progress = 0.3; // Starting progress from PosController
        if (slider1Moved.get()) progress += 0.1; // Increment 10% for slider 1
        if (slider2Moved.get()) progress += 0.1; // Increment 10% for slider 2
        if (slider3Moved.get()) progress += 0.1; // Increment 10% for slider 3
        if (slider4Moved.get()) progress += 0.1; // Increment 10% for slider 4
        progressBarNeg.setProgress(progress); // Update progress bar
    }

    private boolean areAllSlidersMoved() {
        return slider1Moved.get() && slider2Moved.get() && slider3Moved.get() && slider4Moved.get();
    }

    @FXML
    public void onBtnNegQuesClicked(ActionEvent event) {
        if (areAllSlidersMoved() && progressBarNeg.getProgress() >= 0.7) {
            double sum = slider1.getValue() + slider2.getValue() + slider3.getValue() + slider4.getValue();
            if (model != null) {
                model.setS2(sum);
                System.out.println("Sumneg: " + model.getS2());
            } else {
                System.err.println("Model is not initialized!");
            }
            //model.setS2(sum);

            redirectTo("HowAreYouFeelingNowNeu.fxml",event);
        } else {
            errorMessageLabelNeg.setText("                            You must answer all the questions to move forward!");
        }
    }


}
