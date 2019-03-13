/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.andre.maven.install.ui;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.andre.maven.install.ui.Organization;
import de.andre.maven.install.ui.Project;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author obinna.asuzu
 */
public class UiController implements Initializable {

    @FXML
    private TextField nameInput;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField groupIdInput;
    @FXML
    private Label groupIdLabel;
    @FXML
    private TextField artifactIdInput;
    @FXML
    private Label artifactIdLabel;
    @FXML
    private TextField versionInput;
    @FXML
    private Label versionLabel;
    @FXML
    private TextField packagingInput;
    @FXML
    private Label packagingLabel;
    @FXML
    private TextField browseField;
    @FXML
    private Button browseButton;
    @FXML
    private Label artifactLabel;
    @FXML
    private TextField organisationInput;
    @FXML
    private TextField organisationUrl;
    @FXML
    private TextArea descriptionInput;
    @FXML
    private Button installButton;

    private BooleanProperty validated = new SimpleBooleanProperty(false);

    private File artifactFile;

    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initInputs();
    }
    
    public void setStage(Stage stage){
        this.stage = stage;
    }

    void initInputs() {
        nameInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validateAll();
            }
        });

        groupIdInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validateAll();
            }
        });

        artifactIdInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validateAll();
            }
        });

        versionInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validateAll();
            }
        });

        browseField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validateAll();
            }
        });
        
        validated.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                installButton.setDisable(!newValue);
            }
        });

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Artifacts", "*.jar", "*.war"));
        fileChooser.setTitle("Select Artifact");
        browseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    browseField.setText(file.getAbsolutePath());
                    artifactFile = file;
                }
            }
        });
        
        installButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                install();
            }
        });
    }

    boolean validateInput(TextField input, Label label) {
        return !(input.getText() == null || input.getText().trim().isEmpty());
    }

    void validateAll() {
        validated.set(false);
        validated.set(validateInput(nameInput, nameLabel));
        if (!validated.get()) {
            return;
        }
        validated.set(validateInput(groupIdInput, groupIdLabel));
        if (!validated.get()) {
            return;
        }
        validated.set(validateInput(artifactIdInput, artifactIdLabel));
        if (!validated.get()) {
            return;
        }
        validated.set(validateInput(versionInput, versionLabel));
        if (!validated.get()) {
            return;
        }
        validated.set(validateInput(browseField, artifactLabel));
        if (!validated.get()) {
            return;
        }
    }

    public void install() {
        String rootPath = System.getProperty("user.home") + "/.m2/repository";
        File rootFolder = new File(rootPath);

        if (!rootFolder.exists()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Maven root directory not found!");
            alert.showAndWait();
            clearAll();
            Platform.exit();
            return;
        };

        String packaging = (packagingInput.getText() == null || packagingInput.getText().trim().isEmpty()) ? "jar" : packagingInput.getText();

        List<String> packs = Arrays.asList("pom", "jar", "maven-plugin", "ejb", "war", "ear", "rar", "par");

        if (!packs.contains(packaging)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid packaging, valid options: \"pom\", \"jar\", \"maven-plugin\", \"ejb\", \"war\", \"ear\", \"rar\", \"par\"");
            alert.showAndWait();
        }
        String path = rootPath + "/" + groupIdInput.getText() + "/" + artifactIdInput.getText() + "/" + versionInput.getText();
        File artifactFolder = new File(path);
        if (artifactFolder.exists()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("There is an alread existing artifact");
            alert.showAndWait();
        }
        
        artifactFolder.mkdirs();

        Path copied = Paths.get(path + "/" + artifactIdInput.getText() + "-" + versionInput.getText() + "." + packaging);
        Path originalPath = artifactFile.toPath();
        
        
        Path pomPath = Paths.get(path + "/" + artifactIdInput.getText() + "-" + versionInput.getText() + ".pom");
        Project project = new Project();
        project.setName(nameInput.getText());
        project.setGroupId(groupIdInput.getText());
        project.setArtifactId(artifactIdInput.getText());
        project.setModelVersion("4.0");
        project.setPackaging(packagingInput.getText());
        project.setVersion(versionInput.getText());
        project.setDescription(descriptionInput.getText());
        Organization organization = new Organization();
        organization.setName(organisationInput.getText());
        organization.setUrl(organisationUrl.getText());
        project.setOrganization(organization);
        XmlMapper xmlMapper = new XmlMapper();
        
        
        
        try {
            String pom = xmlMapper.writeValueAsString(project);
            Files.copy(originalPath, copied);
            Files.write(pomPath, pom.getBytes());
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Installation complete");
            alert.setHeaderText(null);
            alert.setContentText("Good news!, Your artifact has been installed");
            alert.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(UiController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Installation failed");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }

        clearAll();
    }

    private void clearAll() {
        nameInput.clear();
        groupIdInput.clear();
        artifactIdInput.clear();
        versionInput.clear();
        packagingInput.clear();
        browseField.clear();
        organisationInput.clear();
        organisationUrl.clear();
        descriptionInput.clear();
        installButton.setDisable(true);
        validated.set(false);
        artifactFile = null;
    }

}
