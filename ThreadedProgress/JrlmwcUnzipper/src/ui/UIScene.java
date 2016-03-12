/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dalemusser
 * https://blog.idrsolutions.com/2012/11/adding-a-window-resize-listener-to-javafx-scene/
 */
public class UIScene implements Initializable
{    
    protected String name;
    protected URL resource;
    protected Scene scene;
    protected Parent root;
    protected UIStage uiStage;
    private EventHandler<WindowEvent> windowCloseEventHandler;
    private ChangeListener<Number> widthChangeListener;
    private ChangeListener<Number> heightChangeListener;
    
    protected void createEventHandlers()
    {
        windowCloseEventHandler = (WindowEvent we) -> this.onClose(we);

        widthChangeListener = (ObservableValue<? extends Number> ov, Number oldWidth, Number newWidth) ->
            this.onWidthChange((double)oldWidth, (double)newWidth);
        
        heightChangeListener = (ObservableValue<? extends Number> ov, Number oldHeight, Number newHeight) ->
            this.onHeightChange((double)oldHeight, (double)newHeight);
    }
    
    private void addEventHandlers()
    {
        if (uiStage == null) return;
        Stage stage = uiStage.getStage();
        
        stage.setOnCloseRequest(windowCloseEventHandler);
        stage.widthProperty().addListener(widthChangeListener);
        stage.heightProperty().addListener(heightChangeListener);
    }
    
    private void removeEventHandlers()
    {
        if (uiStage == null) return;
        Stage stage = uiStage.getStage();
        
        stage.setOnCloseRequest(null);
        stage.widthProperty().removeListener(widthChangeListener);
        stage.heightProperty().removeListener(heightChangeListener);        
    }
    
    public void addedToStage(UIStage uiStage)
    {
        addEventHandlers();
        this.onAddedToStage();
    }
    
    public void removedFromStage()
    {
        removeEventHandlers();
        this.onRemovedFromStage();
    }
    
    public UIStage getUIStage()
    {
        return uiStage;
    }
    
    public Scene getScene()
    {
        return scene;
    }
    
    public Parent getRoot()
    {
        return root;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { };
    
    public void onWillBeAddedToStage() { }
    public void onAddedToStage() { }
    public void onWillBeRemovedFromStage() { }
    public void onRemovedFromStage() { }
    public void onClose(WindowEvent we) { }
    public void onWidthChange(double oldWidth, double newWidth) { }
    public void onHeightChange(double oldWidth, double newWidth) { }
}
