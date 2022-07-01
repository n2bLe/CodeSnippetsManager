package com.nobble.codesnippets;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SnippetApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SnippetApplication.class.getResource("codeSnippets.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Code Snippets");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {launch();
    }
}