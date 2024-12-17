package com.example.login;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ChatPage {


        @FXML private Button SendText;
        @FXML private TextField InputText;
        @FXML private ScrollPane Scrollmsg;
        @FXML private VBox ShowMsg;
        @FXML private Button closeButton;

        // Database Connection Parameters
        private static final String DB_URL = "jdbc:mysql://localhost:3306/mentalhealth";
        private static final String DB_USER = "root";
        private static final String DB_PASSWORD = "1234";

        // Current user's email (should be set during login)
        private String currentUserEmail;

       // Group chat ID (instead of recipient email, we use a group ID)
        private String groupChatId;
//        // Recipient's email (should be set when opening chat)
//        private String recipientEmail;

        @FXML
        public void initialize() {

            // Setup send message functionality
            SendText.setOnAction(event -> sendMessage());

            // Ensure scrollpane always scrolls to bottom
            ShowMsg.heightProperty().addListener((obs, oldVal, newVal) ->
                    Scrollmsg.setVvalue((Double) newVal));

            // Load previous messages when chat is opened
            loadPreviousMessages();
        }

        /**
         * Set the current user's email
         * @param email Current user's email
         */
        public void setCurrentUserEmail(String email) {
            this.currentUserEmail = email;
            System.out.println("Email in ChatPage: " + email);
        }


//        public void setRecipientEmail(String email) {
//            this.recipientEmail = email;
//        }
        public void setGroupChatId(String groupId) {
            this.groupChatId = groupId;
        }

        /**
         * Send a message to the database
         */
        private void sendMessage() {
            String messageText = InputText.getText().trim();
            if (messageText.isEmpty()) return;

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String insertQuery = "INSERT INTO messages " +
                        "(sender_email, group_chat_id, message_text, timestamp) " +
                        "VALUES (?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, currentUserEmail);
                    pstmt.setString(2, groupChatId); // Store the group chat ID
                    pstmt.setString(3, messageText);
                    pstmt.setObject(4, LocalDateTime.now());

                    pstmt.executeUpdate();
                }

                // Display message in UI
                displayMessage(messageText, true);

                // Clear input field
                InputText.clear();
            } catch (SQLException e) {
                e.printStackTrace();
                displayErrorMessage("Failed to send message");
            }
        }

    /**
     * Load previous messages for the group chat
     */
    private void loadPreviousMessages() {
        if (currentUserEmail == null || groupChatId == null) return;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String selectQuery = "SELECT sender_email, message_text, timestamp " +
                    "FROM messages " +
                    "WHERE group_chat_id = ? " +
                    "ORDER BY timestamp";

            try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                pstmt.setString(1, groupChatId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String senderEmail = rs.getString("sender_email");
                        String messageText = rs.getString("message_text");

                        // Determine if message is from current user
                        boolean isCurrentUser = senderEmail.equals(currentUserEmail);
                        displayMessage(messageText, isCurrentUser);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayErrorMessage("Failed to load messages");
        }
    }

    /**
     * Display a message in the chat UI
     * @param message Message text
     * @param isCurrentUser Whether the message is from current user
     */
    private void displayMessage(String message, boolean isCurrentUser) {
        Platform.runLater(() -> {
            Label messageLabel = new Label(message);
            messageLabel.getStyleClass().add(isCurrentUser ? "sent-message" : "received-message");
            ShowMsg.getChildren().add(messageLabel);
        });
    }

    /**
     * Display error message
     * @param errorText Error message to display
     */
    private void displayErrorMessage(String errorText) {
        Platform.runLater(() -> {
            Label errorLabel = new Label(errorText);
            errorLabel.getStyleClass().add("error-message");
            ShowMsg.getChildren().add(errorLabel);
        });
    }

    /**
     * Close the current window
     */
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

