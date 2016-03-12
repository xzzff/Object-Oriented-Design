/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrlmwcunzipper;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.WindowEvent;
import ui.UIScene;

/**
 * FXML Controller class
 *
 * @author joe
 */
public class MainUIScene extends UIScene
{
//    @FXML
//    private Label percentageCompleteLabel;
//   
//    @FXML
//    private ProgressBar progressBar;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }
    
    @Override
    public void onClose(WindowEvent we)
    {
    }
    
    /**
     * What happens when the start button is pressed, i.e. when the user wants
     * to begin the file extraction.
     */
    @FXML
    public void start(ActionEvent event)
    {
        System.out.println("Clicked the start button!");
    }
    
    /**
     * What happens when the stop button is pressed, i.e. when the user
     * wants to interrupt/stop the file extraction.
     * TODO: support resuming easily with start
     */
    @FXML
    public void stop(ActionEvent event)
    {
        System.out.println("Clicked the stop button!");
    } 
}
