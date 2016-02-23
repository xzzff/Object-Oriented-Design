/**
 * Joe Loser
 * 2/6/16
 * OOD Grid Challenge
 */
package jrlmwcgrid;

import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author joe
 */
public class Grid extends Application
{
	final int GRID_ROWS = 10;
	final int GRID_COLUMNS = 10;
	final double GRID_WIDTH_PIXELS = 600;
	final double GRID_HEIGHT_PIXELS = 600;
	static final Color[] RECTANGLE_COLORS_TO_PICK_FROM = new Color[]
	{
            Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN,
            Color.BLACK, Color.ORANGE
	};
        
        /* To avoid keep constructing random objects when picking a random color,
           let's just use this one and seed it once. Sure, we could generate a new
           seed for each use. But, this is only used in a tight, quick loop where each
           iteration is <15 ms by far and hence the precision in number of bits used
           for the seed (since it uses default sys clock) will not be met. I could
           create a complex seed myself using GUIDs and Math.random and some fancy 
           ORing, but I will not bother.
        */
        private static Random randomNumberGenerator = new Random();
	
	private Color pickRandomColor()
	{
            final int randomIdx = randomNumberGenerator.nextInt(RECTANGLE_COLORS_TO_PICK_FROM.length);
            return RECTANGLE_COLORS_TO_PICK_FROM[randomIdx];
	}
	
	/**
	 * Generates a rectangle with a random color.
	 */
	private Rectangle createCell(final double width, final double height)
	{
            final Color color = pickRandomColor();
	    Rectangle rectangle = new Rectangle(width, height, color);
            return rectangle;
        }

	@Override
	public void start(Stage primaryStage)
	{
            GridPane grid = new GridPane();
            Scene scene = new Scene(grid, GRID_WIDTH_PIXELS, GRID_HEIGHT_PIXELS);
            final double width = GRID_WIDTH_PIXELS / GRID_ROWS;
            final double height = GRID_HEIGHT_PIXELS / GRID_COLUMNS;
            
            for (int i = 0; i < GRID_ROWS; ++i)
            {
                for (int j = 0; j < GRID_COLUMNS; ++j)
                {
                    Rectangle rectangle = createCell(width, height);
                    grid.add(rectangle, i, j);
                }
            }
            
            primaryStage.setTitle("Grid");
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
