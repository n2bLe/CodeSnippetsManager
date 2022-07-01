package com.nobble.codesnippets;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SnippetController implements Initializable {
    public HashMap<String,Integer> indexMap = new HashMap<>();
    public HashMap<String,String> typeMap = new HashMap<>();
    @FXML
    private Button addBtn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField langField;
    @FXML
    private TextField typeField;
    @FXML
    private TextArea newCode;

    public static HashMap<String,String> codeMap = new HashMap<>();

    public static ArrayList<String> types = new ArrayList<>(
            Arrays.asList("I/O",
                    "Sockets",
                    "Databases",
                    "Strings",
                    "Multithreading",
                    "Algorithms",
                    "Data Structures",
                    "OOP",
                    "Arrays",
                    "Classes",
                    "Exception Handling",
                    "Build Tools",
                    "Math")

    );

    public static List<String> names = new ArrayList<>();
    @FXML
    private TextField searchBar;
    @FXML
    private TextArea codeArea;
    @FXML
    private ListView typesList = new ListView();
    @FXML
    private ListView namesList = new ListView();
    private final String url = "jdbc:mysql://localhost:3306/codesnips";
    private final String username = "root";
    private final String password = "bullet";
    private final String select_query = "SELECT * FROM snips";
    private final String insert_query = "INSERT INTO snips (lang, type, name, code) VALUES (?,?,?,?)";

    public void connectToInsert(String lang,String type,String name,String code){
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(insert_query);
            preparedStatement.setString(1,lang);
            preparedStatement.setString(2,type);
            preparedStatement.setString(3,name);
            preparedStatement.setString(4,code);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void connectToGet(String Url,String Username,String Password,String query){
        try{
            Connection connection = DriverManager.getConnection(Url,Username,Password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Integer index = 0;
            while(resultSet.next()){
                String lang = resultSet.getString("lang");
                String type = resultSet.getString("type");
                String name = resultSet.getString("name");
                String code = resultSet.getString("code");
                index++;
                codeMap.put(name,code);
                typeMap.put(name,type);
                indexMap.put(type,index);
                names.add(name);
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        connectToGet(url,username,password,select_query);
        typesList.getSelectionModel().selectFirst();
        typesList.getItems().addAll(types);
    }
    //edit the style

    public void search(ActionEvent e){
        if(searchBar.getText() != null){
            String searchWord = searchBar.getText();
            namesList.getItems().clear();
            namesList.getItems().addAll(searchList(searchWord,names));
            String type;
            int index;
            for(String s : searchList(searchWord,names)){
                type = typeMap.get(s);
                index = indexMap.get(type);
                typesList.getSelectionModel().select(index);
            }

        }
        else if(searchBar.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information on snippet");
            alert.setHeaderText("Searched Word");
            alert.setContentText("No Function or class with the entered name were found");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }
    private List<String> searchList(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }
    public void showCode(){
        if(namesList.getSelectionModel().selectedItemProperty() != null){
            String item = String.valueOf(namesList.getSelectionModel().getSelectedItem());
            item = item.replace("[","");
            item = item.replace("]","");
            String code = codeMap.get(item);
            codeArea.setText(code);
        }
    }
    public void showAddWindow(ActionEvent event){
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/secondWindow.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Add snippet");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(addBtn.getScene().getWindow());
            stage.show();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public void addSnippet(ActionEvent e){
        String lang = langField.getText();
        String type = typeField.getText();
        String name = nameField.getText();
        String code = newCode.getText();
        connectToInsert(lang,type,name,code);
        System.out.println("New snippet was added");
    }


    public void onTypeSelect(javafx.scene.input.MouseEvent mouseEvent) {
        ArrayList<String> items = new ArrayList<>();
        if(typesList.getSelectionModel().selectedItemProperty() != null){
            String selected = String.valueOf(typesList.getSelectionModel().getSelectedItem());
            String query = "SELECT lang, type, name, code FROM snips WHERE type = '" + selected + "'";
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while(resultSet.next()){
                    String lang = resultSet.getString("lang");
                    String type = resultSet.getString("type");
                    String name = resultSet.getString("name");
                    String code = resultSet.getString("code");

                    items.add(name);
                    codeMap.put(name,code);
                }
                namesList.getItems().clear();
                namesList.getItems().addAll(items);
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }

    }
    //Override the original function to use on initialization
    public void onTypeSelect(String item) {
        ArrayList<String> items = new ArrayList<>();
        if(typesList.getSelectionModel().selectedItemProperty() != null){
            String selected = String.valueOf(typesList.getSelectionModel().getSelectedItem());
            String query = "SELECT lang, type, name, code FROM snips WHERE type = '" + selected + "'";
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while(resultSet.next()){
                    String lang = resultSet.getString("lang");
                    String type = resultSet.getString("type");
                    String name = resultSet.getString("name");
                    String code = resultSet.getString("code");

                    items.add(name);
                    codeMap.put(name,code);
                }
                namesList.getItems().clear();
                namesList.getItems().addAll(items);
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }

    }

    //Todo: fix the search
    //Todo: optimize the code
}