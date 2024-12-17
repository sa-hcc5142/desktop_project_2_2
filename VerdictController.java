package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class VerdictController extends BaseController {
    @FXML
    private Button btnYes, btnNo;


    @FXML
    private void onBtnYesClicked(ActionEvent event) {

        redirectTo("WhatKindOfHelpDoYouNeed.fxml", event);
    }

    @FXML
    private void onBtnNoClicked(ActionEvent event) {

        redirectTo("HowAreYouFeelingNowPos.fxml",event);
    }


}
