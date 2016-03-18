/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrlmwcunzipper;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import ui.UIScene;

/**
 * FXML Controller class
 *
 * @author joe
 */
public class MainUIScene extends UIScene
{
    @FXML
    private Label percentageCompleteLabel;
   
    @FXML
    private ProgressBar progressBar;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Label zipPathLabel;
    
    @FXML
    private Label destinationPathLabel;
    
    private Unzipper unzipper;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        unzipper = new Unzipper();
    }
    
    @Override
    public void onClose(WindowEvent we)
    {
        // Still some work left to do, just interrupting
        if (unzipper != null)
        {
            unzipper.interrupt();
        }
    }
    
    /**
     * Allows the user to select the ZIP file that is to be unzipped
     * using a File Open dialog.
     * TODO: Maybe use different dialog box?
     * @param event 
     */
    @FXML
    public void selectFileToUnzip(ActionEvent event)
    {
        System.out.println("Letting the user choose their zip file to unzip.");
        
        FileChooser fileChooser = new FileChooser();
 
        // Set extension filter
        final FileChooser.ExtensionFilter extensionFilter = 
            new FileChooser.ExtensionFilter("Zip files (*.zip)", "*.zip");
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        System.out.println("The zip path is: " + file.getPath());
        zipPathLabel.setText(file.getPath());
        
        final Path path = Paths.get(file.getPath());
        unzipper.setSourceZipPath(path);
    }
    
    /**
     * Allows the user to select a destination directory where the extracted
     * files are to be placed using a Directory Chooser dialog box.
     * @param event 
     */
    @FXML
    public void selectDestinationDirectory(ActionEvent event)
    {
        System.out.println("Letting the user choose their destination directory.");
        
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(null);
        if (dir == null)
        {
            System.err.println("No directory selected! "
                + "Please select a directory before starting the unzip process.");
        }
        else
        {
            destinationPathLabel.setText(dir.getPath());
            unzipper.setDestinationPath(dir.toPath());
        }
    }
    
    /**
     * What happens when the start button is pressed, i.e. when the user wants
     * to begin the file extraction.
     * @param event 
     */
    @FXML
    public void start(ActionEvent event)
    {
        System.out.println("Clicked the start button!");
        progressBar.setProgress(0);
        
        unzipper.setOnNotification(new Notification()
        {
            @Override
            public void handle(int percentComplete, Status status)
            {
                // Set text on label
                percentageCompleteLabel.setText(Integer.toString(percentComplete));
                statusLabel.setText(status.toString());
                progressBar.setProgress(percentComplete);
                
                // Done?
                if (status == Status.FINISHED || status == Status.INTERRUPTED)
                {
                    unzipper = null;
                }
            }
        });    
        // Do work in another thread
        unzipper.start(); // implicitly calls unzipper.run()
    }
    
    /**
     * What happens when the stop button is pressed, i.e. when the user
     * wants to interrupt/stop the file extraction.
     * TODO: support resuming easily with start
     * @param event
     */
    @FXML
    public void stop(ActionEvent event)
    {
        System.out.println("Clicked the stop button!");
        if (unzipper == null) return;
        unzipper.interrupt();
    } 
}
