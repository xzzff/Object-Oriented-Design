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
import javafx.scene.control.TextArea;
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
    
    @FXML
    private TextArea extractPaths;
    
    private Unzipper unzipper;

    /**
     * Instead of having to deal with managing lifetime and scope of the Unzipper
     * object before start button is pressed, and whether user selects the
     * zip file or directory first, we just start off our scene with a fresh
     * Unzipper. Life is good.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        unzipper = new Unzipper();
    }
    
    /**
     * What happens when the window gets closed.
     * @param we 
     */
    @Override
    public void onClose(WindowEvent we)
    {
        if (unzipper != null)
        {
            unzipper.interrupt();
        }
    }
    
    /**
     * Allows the user to select the ZIP file that is to be unzipped.
     * @param event 
     */
    @FXML
    public void selectFileToUnzip(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
 
        // Set extension filter
        final FileChooser.ExtensionFilter extensionFilter = 
            new FileChooser.ExtensionFilter("Zip files (*.zip)", "*.zip");
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);
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
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(null);
        if (dir == null)
        {
            throw new NullPointerException("No directory selected!");
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
        restartIfNecessary();
       
        progressBar.setProgress(0);
        
        unzipper.setOnNotification(
            (double percentComplete, String extractPath, Status status) ->
        {   
            final int prettyPercent = (int) Math.floor(percentComplete * 100);
            percentageCompleteLabel.setText(Integer.toString(prettyPercent));
            
            statusLabel.setText(status.toString());
            extractPaths.appendText(extractPath + "\n");
            
            // This needs to be in the range of (0.0, 1.0).
            progressBar.setProgress(percentComplete);
        });    
        // Do work in another thread
        unzipper.start(); // implicitly calls unzipper.run()
    }
    
    /**
     * This is a bit of a hacky way to get around making it easy to resume
     * after interrupting the thread/stopping. We just create a new Unzipper,
     * i.e. a new thread. There are certainly fancier ways to implement this
     * "restart" feature.
     */
    private void restartIfNecessary()
    {       
        // Our thread got interrupted. We need to resume swiftfully.
        if (unzipper.getStatus() == Status.INTERRUPTED)
        {
            if (!isTextAreaEmpty())
            { 
                extractPaths.setText("");
            }
            // Interrupted previously, starting over now
            unzipper = new Unzipper(
                unzipper.getSource(), unzipper.getDestinationPath());
        }
        // Finished already, but we want to unzip some more!
        if (unzipper.getStatus() == Status.FINISHED)
        {
            if (!isTextAreaEmpty())
            {
                extractPaths.setText("");
            }
            unzipper = new Unzipper(
                unzipper.getSource(), unzipper.getDestinationPath());
        }
    }
    
    private boolean isTextAreaEmpty()
    {
        return extractPaths.getText().trim().isEmpty();
    }
    
    /**
     * What happens when the stop button is pressed, i.e. when the user
     * wants to interrupt/stop the file extraction.
     * @param event
     */
    @FXML
    public void stop(ActionEvent event)
    {
        unzipper.setStatus(Status.INTERRUPTED); // part of the glorious hack
        statusLabel.setText(Status.INTERRUPTED.toString());
        extractPaths.setText("");
        unzipper.interrupt();
    } 
}
