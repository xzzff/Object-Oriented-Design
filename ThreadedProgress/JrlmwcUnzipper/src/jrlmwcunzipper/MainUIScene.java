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
        if (!unzipper.isInterrupted())
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
        if (file == null)
        {
            throw new NullPointerException("Must select a file in the File Dialog.");
        }
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
     * If the current thread was interrupted (stopped) or it finished and we
     * want to keep unzipping files, we need to start a new thread to do so.
     */
    private void restartIfNecessary()
    {
        /**
         * Currently there are two scenarios:
         * 1) Our original thread got interrupted/stopped. We want to just
         *    start over -- note we will not try to resume from where we left
         *    off in the new execution.
         * 2) Our original thread finished its execution just fine (i.e. finished
         *    extracting all of the files), but now we want to unzip more things.
         */
        final Status currentStatus = unzipper.getStatus();
        if (currentStatus == Status.INTERRUPTED || 
            currentStatus == Status.FINISHED)
        {
            // Since we're starting a new execution, we should tidy up.
            if (isTextAreaEmpty())
            {
                clearTextArea();
            }
            unzipper = new Unzipper(unzipper.getSource(), 
                                    unzipper.getDestinationPath());
        }
    }
    
    private boolean isTextAreaEmpty()
    {
        return extractPaths.getText().trim().isEmpty();
    }
    
    private void clearTextArea()
    {
        extractPaths.setText("");
    }
    
    /**
     * What happens when the stop button is pressed, i.e. when the user
     * wants to interrupt/stop the file extraction.
     * @param event
     */
    @FXML
    public void stop(ActionEvent event)
    {
        unzipper.setStatus(Status.INTERRUPTED);
        statusLabel.setText(Status.INTERRUPTED.toString());
        clearTextArea();
        unzipper.interrupt();
    } 
}
