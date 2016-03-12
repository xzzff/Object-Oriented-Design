/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author joe
 */
public class UIStage
{
    private final Stage stage;
    private UIScene currentUIScene;
    private boolean shown = false;
    
    
    public UIStage(Stage stage)
    {
        this.stage = stage;
    }
    
    public void displayScene(UIScene uiScene)
    {
        if (uiScene == null) return;
        
        UIScene previousUIScene = currentUIScene;
        
        stage.setScene(uiScene.getScene());
        currentUIScene = uiScene;
        
        if (previousUIScene != null)
        {
            previousUIScene.onWillBeRemovedFromStage();
        }
        uiScene.onWillBeAddedToStage();
        
        if (shown == false)
        {
            shown = true;
            stage.show();
        }
        
        if (previousUIScene != null) previousUIScene.removedFromStage();
        currentUIScene.addedToStage(this);
    }
    
    public Stage getStage()
    {
        return stage;
    }
    
    public UIScene loadScene(String name, URL resource) throws Exception
    {
        UIScene uiScene = null;
        try
        {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = (Parent)loader.load();
            uiScene = (UIScene)loader.getController();
            uiScene.resource = resource;
            uiScene.name = name;
            uiScene.scene = new Scene(root);
            uiScene.root = root;
            uiScene.uiStage = this;
            uiScene.createEventHandlers();
        }
        catch (Exception ex)
        {
            throw ex;
        }
        
        return uiScene;
    }
    
}