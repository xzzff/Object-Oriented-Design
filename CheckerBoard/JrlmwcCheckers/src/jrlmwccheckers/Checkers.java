/**
 * Joe Loser
 * 2/7/16
 * OOD CheckerBoard Challenge
 */
package jrlmwccheckers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author joe
 */
public class Checkers extends Application
{
    private final int numRows = 8;
    private final int numCols = 8;
    private final int boardWidthPixels = 600;
    private final int boardHeightPixels = 600;
    private final Color lightColor = Color.SKYBLUE;
    private final Color darkColor = Color.DARKBLUE;
    private CheckerBoard checkerBoard;
    
    @Override
    public void start(Stage primaryStage)
    {
        checkerBoard = new CheckerBoard(numRows, numCols, 
            boardWidthPixels, boardHeightPixels);
//        checkerBoard = new CheckerBoard(numRows, numCols, boardWidthPixels, 
//            boardHeightPixels, lightColor, darkColor);
        final GridPane board = checkerBoard.build();
        final Scene scene = new Scene(board, checkerBoard.getWidth(), checkerBoard.getHeight());
        
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
