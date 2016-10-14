/**
 * PointGenerator.java
 *
 * @author :  Weijun Huang, Tim Faulkner
 * @version : 12/19/2011
 */

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class PointGenerator
{
   /**
    * PointGenerator
    *
    * This class generates points randomly within a (N by N) space, optionally
    * checking for duplicate points.  The points are writen to a ASCII text
    * file.
    */
   PointGenerator()
   {
   }
   
   /**
    * generate
    *
    * This method places random points, on integer bounds, within the grid,
    * writting their locations to the output file. It optionally checks for
    * duplicate points.
    *
    * Duplicates are check by allocating a boolean grid, and marking points
    * as they are created. Large sized datasets may require large amounts of
    * memory
    */
   public void generate(DataGenModel aModel) throws IOException
   {
      int x,y,i,j;
      long cnt;
      String outFilename;
      FileWriter f = null;
      PrintWriter out = null;

      // do we wish points generated?
      if (aModel.theGeneratePointsFlag == false)
         return;

      // setup file output
      outFilename = aModel.theFilenamePrefix + "points.txt";
      f = new FileWriter(outFilename);
      out = new PrintWriter(f);

      // generate points
      System.out.println("  creating points datafile [" + outFilename + "]");
      if (aModel.theUniquePointsFlag == false)
      {
         System.out.println("    dataset may contain duplicate points");
         cnt = 0;
         while (cnt < aModel.theNumberOfPoints)
         {
               x = (int)(Math.random()*aModel.theSceneLength)+1;
               y = (int)(Math.random()*aModel.theSceneLength)+1;
               out.println("POINT (" + x + " " + y + ")");
               cnt++;
               if (cnt%100000 == 0)
                  System.out.println("    generated " + cnt + " of " + aModel.theNumberOfPoints);
         }
         System.out.println("    " + aModel.theNumberOfPoints + " points were generated.");
      }
      else
      {
           System.out.println("    data will contain only unique points");
           boolean[][] Grid = new boolean[aModel.theSceneLength + 1][aModel.theSceneLength + 1];
           int duplicates = 0;
           cnt = 0;
           while (cnt < aModel.theNumberOfPoints)
           {
                 x = (int)(Math.random()*aModel.theSceneLength)+1;
                 y = (int)(Math.random()*aModel.theSceneLength)+1;
                 if (Grid[x][y])
                    duplicates++;
                 else
                 {
                    Grid[x][y] = true;
                    out.println("POINT (" + x + " " + y + ")");
                    cnt++;
                   if (cnt%100000 == 0)
                      System.out.println("    generated " + cnt + " of " + aModel.theNumberOfPoints);
                 }
           }
           System.out.println("    " + aModel.theNumberOfPoints + " points were generated.");
           System.out.println("    " + duplicates + " duplicates were eliminated.");
      }
      out.close();
   }
}