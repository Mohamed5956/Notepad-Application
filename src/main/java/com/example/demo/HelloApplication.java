package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import javafx.scene.control.UndoManager;
import java.io.*;
import java.util.Optional;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
//        UndoManager undoManager = new UndoManager();
        TextArea ta = new TextArea();
        MenuBar bar = new MenuBar();
        TextArea bottom = new TextArea("Copyright @Mohammed_Adel");
        bottom.setMaxHeight(50);
//        first menu for file
        Menu file = new Menu("File");
        MenuItem Inew = new MenuItem("New");
        Inew.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        Inew.setOnAction(event -> ta.clear());
        MenuItem Iopen = new MenuItem("Open");
        Iopen.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        Iopen.setOnAction(event -> OpenDialog(ta));
        MenuItem Isave = new MenuItem("Save");
        Isave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        Isave.setOnAction(event -> saveDialog(ta));
        MenuItem Iexit = new MenuItem("Exit");
        Iexit.setOnAction(event->exitText(ta));
        file.getItems().addAll(Inew,Iopen,Isave,Iexit);
        SeparatorMenuItem sep1 = new SeparatorMenuItem();
        file.getItems().add(3,sep1);
//         second menu for edit
        Menu edit = new Menu("Edit");
        MenuItem Iundo = new MenuItem("Undo");
        MenuItem Icut = new MenuItem("Cut");
        Icut.setOnAction(event->cutText(ta));
        MenuItem Icopy = new MenuItem("Copy");
        Icopy.setOnAction(event->copyText(ta));
        MenuItem Ipaste = new MenuItem("Paste");
        Ipaste.setOnAction(event->pasteText(ta));
        MenuItem Idelete = new MenuItem("Delete");
        Idelete.setOnAction(event->deleteText(ta));
        MenuItem IselectAll = new MenuItem("Select All");
        IselectAll.setOnAction(event-> ta.selectAll());
        edit.getItems().addAll(Iundo,Icut,Icopy,Ipaste,Idelete,IselectAll);
        SeparatorMenuItem sep2 = new SeparatorMenuItem();
        SeparatorMenuItem sep3 = new SeparatorMenuItem();
        edit.getItems().add(1, sep2);
        edit.getItems().add(6, sep3);
//          third menu for help
        Menu help = new Menu("Help");
        MenuItem Ihelp = new MenuItem("About notepad");
        help.getItems().addAll(Ihelp);
        help.setOnAction(even->openHelpDialog());
//        add all menus to the bar
        bar.getMenus().addAll(file,edit,help);
        BorderPane borderpane = new BorderPane();
        borderpane.setTop(bar);
        borderpane.setCenter(ta);
        borderpane.setBottom(bottom);
        Scene scene = new Scene(borderpane,500,500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private void exitText(TextArea ta) {
        if (ta.getText().equals(ta)) {
            System.exit(0);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save changes?");
            ButtonType saveButton = new ButtonType("Save");
            ButtonType dontSaveButton = new ButtonType("Don't Save");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(saveButton, dontSaveButton, cancelButton);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == saveButton) {
                saveDialog(ta);
                System.exit(0);
            } else if (result.get() == dontSaveButton) {
                System.exit(0);
            }
        }
    }

    private void openHelpDialog() {
        Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle("Help");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText("This is My Notepad Program :) ");
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
    }

    private void deleteText(TextArea ta) {
        String selectedText = ta.getSelectedText();
        ta.setText(ta.getText().replace(selectedText,""));
    }

    private void cutText(TextArea ta) {
        String selectedText = ta.getSelectedText();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(selectedText);
        clipboard.setContent(content);
        ta.deleteText(ta.getSelection());
    }

    private void pasteText(TextArea ta) {
        Clipboard cli = Clipboard.getSystemClipboard();
        if(cli.hasString()){
            ta.paste();
        }
    }

    private void copyText(TextArea ta) {
        Clipboard cli = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(ta.getSelectedText());
        cli.setContent(content);
    }
    private void saveDialog(TextArea ta) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("Java", "*.java")
        );
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try {
                FileWriter writer = new FileWriter(selectedFile);
                writer.write(ta.getText());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("File saved successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("No file selected!");
            alert.showAndWait();
        }
    }
    private void OpenDialog(TextArea ta){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text","*.txt"),
                new FileChooser.ExtensionFilter("Java","*.java")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile!=null){
            try{
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                String line;
                while ((line = reader.readLine()) !=null){
                    ta.appendText(line + "\n");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            System.out.println(selectedFile.getPath());
        }
    }
    public static void main(String[] args) {
        launch();
    }
}