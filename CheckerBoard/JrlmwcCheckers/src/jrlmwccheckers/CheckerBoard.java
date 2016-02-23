/**
 * Joe Loser
 * 2/7/16
 * OOD CheckerBoard Challenge
 */
package jrlmwccheckers;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author joe
 */
public class CheckerBoard
{
    private int numRows;
    private int numCols;
    private int boardWidthPixels;
    private int boardHeightPixels;
    private Color lightColor = Color.RED;
    private Color darkColor = Color.BLACK;
    private GridPane board;
    
    public CheckerBoard(int numRows, int numCols, int boardWidthPixels, 
        int boardHeightPixels)
    {
        this.numRows = numRows;
        this.numCols = numCols;
        this.boardWidthPixels = boardWidthPixels;
        this.boardHeightPixels = boardHeightPixels;
    }
    
    public CheckerBoard(int numRows, int numColumns, int boardWidthPixels,
        int boardHeightPixels, Color lightColor, Color darkColor)
    {
        this(numRows, numColumns, boardWidthPixels, boardHeightPixels);
        this.lightColor = lightColor;
        this.darkColor = darkColor;
    }
    
    /**
     * Efficiently determine which color is gotten for a particular
     * cell.
     * @param x - the x position on the board
     * @param y - the y position on the board
     * @return Color to color a particular square
     */
    private Color determineCellColor(final int x, final int y)
    {
        /**
         * I can't be certain the Java compiler knows to change mod of a 2^n power
         * to use bit shifting, though it certainly should since the mod value is
         * known at compile time.
         */
        if ((y & 1) != 0)
        {
            return ((x & 1) != 0) ? lightColor : darkColor;
        }
        else
        {
            return ((x & 1) != 0) ? darkColor : lightColor;
        }
    }
    
    public GridPane build()
    {
       GridPane grid = new GridPane();
       final int cellWidth = getSquareWidth();
       final int cellHeight = getSquareHeight();
       for (int i = 0; i < numRows; ++i)
       {
           for (int j = 0; j < numCols; ++j)
           {
               final Color color = determineCellColor(i, j);
               final Rectangle rec = new Rectangle(cellWidth, cellHeight, color);
               grid.add(rec, i, j);
           }
       }
       return grid;
    }
    
    public GridPane getBoard()
    {
        return board;
    }
    
    public int getNumRows()
    {
        return numRows;
    }
    
    public int getNumCols()
    {
        return numCols;
    }
    
    public int getWidth()
    {
        return boardWidthPixels;
    }
    
    public int getHeight()
    {
        return boardHeightPixels;
    }
    
    public Color getLightColor()
    {
        return lightColor;
    }
    
    public Color getDarkColor()
    {
        return darkColor;
    }
    
    /**
     * Returns the size of the width of one square cell.
     * @return square width
     */
    public int getSquareWidth()
    {
        return boardWidthPixels / numRows;
    }
    
    /**
     * Returns the size of the height of one square cell.
     * @return square height
     */
    public int getSquareHeight()
    {
        return boardHeightPixels / numCols;
    }
}
