/**
 * PolygonGenerator.java
 *
 * @author :  Weijun Huang, Tim Faulkner
 * @version : 12/19/2011
 */


/*  The primary data structure used:

    int[] pointsX, pointsY: coordinates of vertices of a polygon

    char[][] Grid:  Grid[x][y] denotes point (x, y) in the grid
                    Grid[x][y] = '.' means (x, y) is NOT a vertice of the polygon being generated
                    Grid[x][y] = '*' means (x, y) is an existing vertice of the polygon being generated
*/

import javax.swing.*;
import java.lang.Math;
import java.io.*;

public class PolygonGenerator {

   /**
    * PolygonGenerator
    *
    * This class generates polygons randomly within a (N by N) space.  The
    * polygons are writen to a ASCII text file.
    */
   PolygonGenerator()
   {
   }

   /**
    * generate
    *
    * This method places random polygons, on integer bounds, within the grid,
    * writting their locations to the output file. Polygons are solid and may
    * have upto a specified number of verticies (minimum of 3)
    */
   public void generate(DataGenModel aModel) throws IOException
   {

      String parameter;

        int PolygonCNT;
        int NumVer, VerCNT;
        int i, j;
        int x, y, leftX, lowerY;
      String outFilename;
      FileWriter f = null;
      PrintWriter out = null;

      // do we wish polygons generated?
      if (aModel.theGeneratePolygonsFlag == false)
         return;
         
      // setup file output
      outFilename = aModel.theFilenamePrefix + "polygons.txt";
      f = new FileWriter(outFilename);
      out = new PrintWriter(f);

      // generate polygons
      System.out.println("  creating polygon datafile [" + outFilename + "]");


      char[][] Grid = new char[aModel.theSceneLength + 1][aModel.theSceneLength + 1];

        int[] pointsX = new int[aModel.thePolygonMaxVertexCount + 1];
        int[] pointsY = new int[aModel.thePolygonMaxVertexCount + 1];

      int cnt, trialNum = (int)Math.pow((double)(aModel.thePolygonBBoxLength + 1), 2.0) / 2;

        PolygonCNT = 0;
        while (PolygonCNT < aModel.theNumberOfPolygons)
        {
         // wipe away polygon vertices from the grid
         for (i = 0; i <= aModel.theSceneLength; i ++)
            for (j = 0; j <= aModel.theSceneLength; j ++)
               Grid[i][j] = '.';

            // the number of vertices of the polygon >= 3
            NumVer = (int)Math.round( Math.random() * (aModel.thePolygonMaxVertexCount - 3) + 3 );

            // the least x & y of the bounding square
            leftX  = (int)Math.round( Math.random() * (aModel.theSceneLength - aModel.thePolygonBBoxLength));
            lowerY = leftX;

         // the vertex 0 of the polygon
         x = leftX + (int) Math.round(Math.random() * aModel.thePolygonBBoxLength);
         y = lowerY + (int) Math.round(Math.random() * aModel.thePolygonBBoxLength);
         pointsX[0] = x;
         pointsY[0] = y;
         Grid[x][y] = '*';

            while(true)
            {
            x = leftX + (int) Math.round(Math.random() * aModel.thePolygonBBoxLength);
            y = lowerY + (int) Math.round(Math.random() * aModel.thePolygonBBoxLength);

             if ( (x >= leftX && x <= (leftX+aModel.thePolygonBBoxLength)) && (y >= lowerY && y <= (lowerY+aModel.thePolygonBBoxLength))
                 // if (x, y) falls in the bounding square
                 && (x != pointsX[0] || y != pointsY[0]) )
            {
               pointsX[1] = x;
               pointsY[1] = y;
               Grid[x][y] = '*';
               break;
            }

            }


         // finish the polygon: a sequence of >= 3 vertices
         VerCNT = 2;
         while (VerCNT < NumVer)
         {
            // generate a randomly vertex
            cnt = 1;
            while (cnt <= trialNum)
            {
                 // give a candidate vertex
               x = leftX + (int) Math.round(Math.random() * aModel.thePolygonBBoxLength);
               y = lowerY + (int) Math.round(Math.random() * aModel.thePolygonBBoxLength);

                  // check the validness of the vertex
                if ( (x < leftX || x > (leftX+aModel.thePolygonBBoxLength)) || (y < lowerY || y > (lowerY+aModel.thePolygonBBoxLength)) )
                  //if (x, y) is outside the bounding square
                   ;

                else if ( InvalidVertex(x, y, pointsX, pointsY, VerCNT - 1, Grid, '*') )
                        ;

                else
                {
                  // (x, y) is selected
                   pointsX[VerCNT] = x;
                   pointsY[VerCNT] = y;
                  Grid[x][y] = '*';
                   break;
                }

                    cnt ++;

            }  // while (cnt <= trialNum)

            if (cnt > trialNum)
               break;
            VerCNT ++;
            if (VerCNT % 1000 == 0)
               System.out.println(VerCNT/1000);

         }  // while loop: finish the polygon

         // print out & draw the polygon

            if (VerCNT >= 3)
            {
               NumVer = VerCNT;
               pointsX[NumVer] = pointsX[0];
               pointsY[NumVer] = pointsY[0];
               out.print("POLYGON (");
               out.print("(" + pointsX[0] +" " + pointsY[0] + ") ");
               for (i = 1; i <= NumVer-2; i++)
                  out.print("(" + pointsX[i] +" " + pointsY[i] + ") ");
               out.print("(" + pointsX[NumVer-1] +" " + pointsY[NumVer-1] + ")");
               out.println(")");
            }
            PolygonCNT ++;
            if (PolygonCNT % 1000 == 0)
               System.out.println(PolygonCNT/1000);
        }  // while (PolygonCNT < PolygonNum)
        out.close();
        System.out.println("    " + aModel.theNumberOfPolygons + " polygons were generated.");
    }
    
    private boolean isCollinear(int x1, int y1, int x2, int y2, int x3, int y3)  {
         // check collinearity of three points (x1, y1), (x2, y2), & (x3, y3)

        // Case 1: two of the points overlap
        if ( (x1 == x2 && y1 == y2) || (x2 == x3 && y2 == y3) || (x3 == x1 && y3 == y1) )
         return true;

        // Case 2: the points are totally isolated
      int d1 = x1 - x3;
      int d2 = x2 - x3;
      if (d1 == 0 && d2 == 0)
         return true;
      else
      if (d1 != 0 && d2 != 0)
         if ( (y1 - y3)/d1 == (y2 - y3)/d2 )
            return true;

      return false;

    }

    private boolean passVertex(int x1, int y1, int x2, int y2, int[] X, int[] Y, int start, int end)  {
      // detects if line segment (x1, y1) -> (x2, y2) passes thru any (X[i], Y[i]) for i = start ~ end

      for (int i = start; i <= end; i ++)
         if (isCollinear(X[i], Y[i], x1, y1, x2, y2))
         {
             /* Case 1: (x1, y1) -> (x2, y2) is vertical */
            if (x1 == x2)
                if ( (y1 <= Y[i] && Y[i] <= y2) || (y2 <= Y[i] && Y[i] <= y1) )
                  return true;
             /* Case 2: (x1, y1) -> (x2, y2) is NOT vertical */
            else
                if ( (x1 <= X[i] && X[i] <= x2) || (x2 <= X[i] && X[i] <= x1) )
                  return true;
         }

        return false;
    }

    private boolean inSegment(int x, int y, int[] X, int[] Y, int m)  {
      // detect if (x, y) lies in line segments (X[i-1], Y[i-1]) -> (X[i], Y[i]), for i = 1, 2, ..., m

        for (int i = 1; i <= m; i ++)
         if (isCollinear(x, y, X[i-1], Y[i-1], X[i], Y[i]))
         {
             /* Case 1: the segment is vertical */
            if (X[i-1] == X[i])
                if ( (Y[i-1] <= y && y <= Y[i]) || (Y[i] <= y && y <= Y[i-1]) )
                  return true;
             /* Case 2: the segment is NOT vertical */
            else
                if ( (X[i-1] <= x && x <= X[i]) || (X[i] <= x && x <= X[i-1]) )
                  return true;
         }

        return false;
    }


    private boolean Intersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)  {
      // detects if line segments S12 (x1, y1) -> (x2, y2) & S34 (x3, y3) -> (x4, y4) intersect
        // ATTENTION:  accuracy matters to calculation of slopes, and to solving equations

        int  x12min = Math.min(x1, x2),    x12max = Math.max(x1, x2);
        int  x34min = Math.min(x3, x4),    x34max = Math.max(x3, x4);
        int  y12min = Math.min(y1, y2),    y12max = Math.max(y1, y2);
        int  y34min = Math.min(y3, y4),    y34max = Math.max(y3, y4);

        double y, m12, b12, m34, b34;


        // S12 and S34 are vertical
        if (x1 == x2 && x3 == x4)
            if (x1 == x3 && y12min <= y34max && y34min <= y12max)
               return true;

        // only S12 is vertical
        if (x1 == x2 && x3 != x4)
        {
         m34 = (double)(y4 - y3) / (double)(x4 - x3);
         b34 = y3 - m34*x3;
         y = m34 * x1 + b34;
            if ((x34min <= x1 && x1 <= x34max) && (y12min <= y && y <= y12max))
               return true;
        }

        // only S34 is vertical
        if (x1 != x2 && x3 == x4)
        {
         m12 = (double)(y2 - y1) / (double)(x2 - x1);
         b12 = y1 - m12*x1;
         y = m12 * x3 + b12;
            if ((x12min <= x3 && x3 <= x12max) && (y34min <= y && y <= y34max))
               return true;
        }

        // neither of S12 & S34 is vertical
        if (x1 != x2 && x3 != x4)
        {
         m12 = (double)(y2 - y1) / (double)(x2 - x1);
         b12 = y1 - m12*x1;
         m34 = (double)(y4 - y3) / (double)(x4 - x3);
         b34 = y3 - m34*x3;

         // they have equal slopes
         if (m12 == m34)
                if (b12 == b34 && x12min <= x34max && x34min <= x12max)
                  return true;

            // they have different slopes
            if (m12 != m34)
                if (x12min <= x34max && x34min <= x12max);
                {
                  int x_highmin = Math.max(x12min, x34min);
               int x_lowmax  = Math.min(x12max, x34max);
               double x = (b34 - b12) / (m12 - m34);   // the x value of the intersection point
               if (x_highmin <= x && x <= x_lowmax)
                  return true;
                }
        }

        return false;

    }

    private boolean InvalidVertex(int x, int y, int[] X, int[] Y, int n, char[][] Grid, char vertex)  {

    /* the coordinates of vertices 0 ~ n of the polygon are already stored by X and Y */

    // (1) check the invalidness of segment (X[n], Y[n]) -> (x, y)

        /* Case 1: (X[n-1], Y[n-1]), (X[n], Y[n]), and (x, y) are collinear */
        if ( isCollinear(X[n-1], Y[n-1], X[n], Y[n], x, y) )    return true;

        /* Case 2: (X[n], Y[n]) -> (x, y) passes thru one of vertices 0 ~ n - 1 of the polygon */
        if (passVertex(X[n], Y[n], x, y, X, Y, 0, n - 1))       return true;

        /* Case 3: (x, y) falls in one of the first n - 1 segments of the polygon */
        if (Grid[x][y] == vertex)               return true;
        if (inSegment(x, y, X, Y, n - 1))       return true;

      /* Case 4: (X[n], Y[n]) -> (x, y) intersects and overlaps (X[i-1], Y[i-1]) -> (X[i], Y[i]) */
        for (int i = n - 1; i > 0; i --)
         if ( Intersect(X[n], Y[n], x, y, X[i-1], Y[i-1], X[i], Y[i]) )
            return true;

    // (2) check the invalidness of segment (x, y) -> (X[0], Y[0])

        /* Case 1: (X[n], Y[n]), (x, y), and (X[0], Y[0]) are collinear */
        if ( isCollinear(X[n], Y[n], x, y, X[0], Y[0]) )    return true;

        /* Case 2: (x, y) -> (X[0], Y[0]) passes thru one of vertices 1 ~ n of the polygon */
        if (passVertex(x, y, X[0], Y[0], X, Y, 1, n))       return true;

      /* Case 3: (x, y) -> (X[0], Y[0]) intersects and overlaps (X[i-1], Y[i-1]) -> (X[i], Y[i]) */
        for (int i = n; i > 0; i --)
         if ( Intersect(x, y, X[0], Y[0], X[i-1], Y[i-1], X[i], Y[i]) )
            return true;


        return false;
    }
}