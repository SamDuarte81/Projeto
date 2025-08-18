package com.exemplo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Carregar a tela de login primeiro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("Sistema de Exercicios - Login");
            primaryStage.setScene(new Scene(root, 900, 700));
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erro ao carregar a aplicações: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}