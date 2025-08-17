package com.exemplo.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class ScreenManager {

    public static void mudarTela(String fxmlPath, Node nodeAtual) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) nodeAtual.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela: " + fxmlPath);
        }
    }

    public static void mudarTelaComDados(String fxmlPath, Node nodeAtual, String dados) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Se o controller implementar uma interface para receber dados
            Object controller = loader.getController();
            if (controller instanceof com.exemplo.controllers.ExerciciosController) {
                ((com.exemplo.controllers.ExerciciosController) controller).setTipoExercicio(dados);
            }

            Stage stage = (Stage) nodeAtual.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela: " + fxmlPath);
        }
    }
}