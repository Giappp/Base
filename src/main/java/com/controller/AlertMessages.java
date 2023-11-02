package com.controller;

import javafx.scene.control.Alert;

import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AlertMessages {

    public void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void successMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void confirmationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void warningMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void handleSendMail(String message, String subject, List<String> customerMails) {
        // variable for gmail
        String host = "smtp.gmail.com";
        String from = "projecttest213@gmail.com";

        // get the system properties
        Properties properties = System.getProperties();

        // setting important info to properties object
        // host set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // step 1: to get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("projecttest213@gmail.com", "pnnzreerwjctwjmn");
            }
        });

        // step 2: compose the message [text,multimedia]
        MimeMessage m = new MimeMessage(session);

        try {
            // from email
            m.setFrom(new InternetAddress(from));

            for (String email : customerMails) {
                m.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }

            // adding subject to message
            m.setSubject(subject);

            m.setContent(message, "text/html");

            // step 3: send the message using Transport class
            Transport.send(m);

            System.out.println("sent success........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
