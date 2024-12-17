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

public class HowAreYouFeelingNowNeuController extends BaseController {

    @FXML
    private Slider slider1, slider2, slider3;
    @FXML
    private ProgressBar progressBarNeu;
    @FXML
    private Label errorMessageLabelNeu;
    @FXML
    private Button btnNeuQues;
    @FXML
    private Button btnNoNeu;
    @FXML
    private Button btnYesNeu;


    // Boolean properties to track slider interactions
    private final BooleanProperty slider1Moved = new SimpleBooleanProperty(false);
    private final BooleanProperty slider2Moved = new SimpleBooleanProperty(false);
    private final BooleanProperty slider3Moved = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        // Initialize sliders and add listeners
        initializeSlider(slider1, slider1Moved, "Slider 1");
        initializeSlider(slider2, slider2Moved, "Slider 2");
        initializeSlider(slider3, slider3Moved, "Slider 3");
    }


    private void initializeSlider(Slider slider, BooleanProperty moved, String sliderName) {
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
        double progress = 0.7; // Starting progress from Pos + Neg Controllers
        if (slider1Moved.get()) progress += 0.1; // Increment 10% for slider 1
        if (slider2Moved.get()) progress += 0.1; // Increment 10% for slider 2
        if (slider3Moved.get()) progress += 0.1; // Increment 10% for slider 3
        progressBarNeu.setProgress(progress); // Update progress bar
    }

    private boolean areAllSlidersMoved() {
        return slider1Moved.get() && slider2Moved.get() && slider3Moved.get();
    }

    @FXML
    public void onBtnNeuQuesClicked(ActionEvent event) {
        // Check if the model is initialized
        if (model == null) {
            System.err.println("Model is not initialized!");
            return; // Exit the method if the model is not set
        }

        if (areAllSlidersMoved() && progressBarNeu.getProgress() >= 0.9999999999999999) {
            double sum = slider1.getValue() + slider2.getValue() + slider3.getValue() ;

                model.setS3(sum);
                System.out.println("Sumneu: " + model.getS3());


           // model.setS3(sum);
           // System.out.println("Sumneu: " + model.getS3());
            System.out.println("Button clicked!");
            System.out.println("Progress: " + progressBarNeu.getProgress());

            double S1 = model.getS1();
            double S2 = model.getS2();
            double S3 = model.getS3();


            if (S1 >= S2 && S1 >= S3) {
                System.out.println("Redirecting to verdictpos.fxml");
                redirectTo("verdictpos.fxml", event);
            } else if (S2 >= S1 && S2 >= S3) {
                System.out.println("Redirecting to verdictneg.fxml");
                redirectTo("verdictneg.fxml", event);
            } else {
                System.out.println("Redirecting to verdictneu.fxml");
                redirectTo("verdictneu.fxml", event);
            }
        } else {
            errorMessageLabelNeu.setText("                             You must answer all the questions to move forward!");
        }
    }


}
