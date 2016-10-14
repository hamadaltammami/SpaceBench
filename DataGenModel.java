/**
 * DataGenModel.java
 *
 * @author :  Tim Faulkner
 * @version : 12/28/2011
 */

import java.util.Scanner;
import java.io.BufferedInputStream;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import java.util.StringTokenizer;

public class DataGenModel {

   //***
   // instance variables
   //***

   private static final boolean TRACE = false;

   // scene options
   public int theSceneLength;
   public String theFilenamePrefix;
   
   // point values
   public boolean theGeneratePointsFlag;
   public int theNumberOfPoints;
   public boolean theUniquePointsFlag;
   
   // square values
   public boolean theGenerateSquaresFlag;
   public int theNumberOfSquares;
   public int theMaximumSquareSideLength;

   // triangle values
   public boolean theGenerateTrianglesFlag;
   public int theNumberOfTriangles;
   public int theTriangleBBoxHeight;
   public int theTriangleBBoxWidth;

   // polygon values
   public boolean theGeneratePolygonsFlag;
   public int theNumberOfPolygons;
   public int thePolygonMaxVertexCount;
   public int thePolygonBBoxLength;
   
   // linestring values
   public boolean theGenerateLineStringsFlag;
   public int theNumberOfLineStrings;
   public int theLineStringMaxSegmentCount;
   public int theLineStringMaxSegmentLength;

   /**
    * DataGenModel()
    * 
    * This method create the main application window
    */
   DataGenModel()
   {
   }

   /**
    * validate()
    *
    * This method validates the model
    */
   public boolean validate(JFrame aFrame)
   {
      boolean isValid = true;
      String msg = "";
      
      if (TRACE)
         System.out.println("DataGenModel: validate");
         
      //***
      // validate point options
      //***
      
      if (isValid && theGeneratePointsFlag)
      {
         if (theNumberOfPoints <= 0)
         {
            msg = "Points file must contain at least 1 element";
            isValid = false;
         }
         if (theUniquePointsFlag && (theNumberOfPoints >= (theSceneLength * theSceneLength)))
         {
            msg = "Scene is too small for number of unique points.";
            isValid = false;
         }
      }
      
      //***
      // validate square options
      //***

      if (isValid && theGenerateSquaresFlag)
      {
         if (theNumberOfSquares <= 0)
         {
            msg = "Squares file must contain at least 1 element";
            isValid = false;
         }
         if ((theMaximumSquareSideLength <= 0) || (theMaximumSquareSideLength >= theSceneLength))
         {
            msg = "Maximum square length has to be < scene length, and > 0";
            isValid = false;
         }
      }

      //***
      // validate triangle options
      //***
      
      if (isValid && theGenerateTrianglesFlag)
      {
         if (theNumberOfTriangles <= 0)
         {
            msg = "Triangles file must contain at least 1 element";
            isValid = false;
         }
         if ((theTriangleBBoxHeight <= 0) || (theTriangleBBoxHeight >= theSceneLength))
         {
            msg = "Maximum triangle height has to be < scene length, and > 0.";
            isValid = false;
         }
         if ((theTriangleBBoxWidth <= 0) || (theTriangleBBoxWidth >= theSceneLength))
         {
            msg = "Maximum triangle width has to be < scene length, and > 0.";
            isValid = false;
         }
      }

      //***
      // validate polygon options
      //***

      if (isValid && theGeneratePolygonsFlag)
      {
         if (theNumberOfPolygons <= 0)
         {
            msg = "Polygon file must contain at least 1 element";
            isValid = false;
         }
         if (thePolygonMaxVertexCount < 3)
         {
            msg = "Polygons must have at least 3 verticies.";
            isValid = false;
         }
         if ((thePolygonBBoxLength <= 0) || (thePolygonBBoxLength >= theSceneLength))
         {
            msg = "Maximum polygon width has to be < scene length, and > 0.";
            isValid = false;
         }
      }

      //***
      // validate line string options
      //***
      
      if (isValid && theGenerateLineStringsFlag)
      {
         if (theNumberOfLineStrings <= 0)
         {
            msg = "Line string file must contain at least 1 element";
            isValid = false;
         }
         if (theLineStringMaxSegmentCount < 3)
         {
            msg = "LineStrings must have at least 3 segments.";
            isValid = false;
         }
         if ((theLineStringMaxSegmentLength <= 0) || (theLineStringMaxSegmentLength >= theSceneLength))
         {
            msg = "Maximum line string segment length has to be < scene length, and > 0.";
            isValid = false;
         }
      }

      //***
      // show error message
      //***

      if (isValid == false)
      {
         JOptionPane.showMessageDialog(
            aFrame,
            msg,
            "Notice",
            JOptionPane.INFORMATION_MESSAGE,
            null);
      }

      return isValid;
   }

   /**
    * save()
    *
    * This method saves the model content to disk
    */
   public void save(JFrame anAppFrame)
   {
      if (TRACE)
         System.out.println("DataGenModel: save options file ");

      // get file for model update
      String outputFn = getOutputFilename(anAppFrame);
      if (outputFn.length() == 0)
         return;

      // do model update
      if (writeData(outputFn) == false)
      {
         JOptionPane.showMessageDialog(
            anAppFrame,
            "Error saving to options file.",
            "Notice",
            JOptionPane.INFORMATION_MESSAGE,
            null);
      }
   }

   /**
    * load()
    *
    * This method loads the model content from disk
    */
   public void load(JFrame anAppFrame)
   {
      if (TRACE)
         System.out.println("DataGenModel: load options file");

      // get file for model update
      String inputFn = getInputFilename(anAppFrame);
      if (inputFn.length() == 0)
         return;

      // do model update
      if (readData(inputFn) == false)
      {
         JOptionPane.showMessageDialog(
            anAppFrame,
            "Error opening selected options file.",
            "Notice",
            JOptionPane.INFORMATION_MESSAGE,
            null);
      }
   }

   /**
    * getInputFilename()
    *
    * This method returns the input filename
    */
   private String getInputFilename(JFrame anAppFrame)
   {
      String fn = "";

      // setup file chooser
      JFileChooser csvChooser = new JFileChooser();
      CSVFileFilter filter = new CSVFileFilter();
      csvChooser.setFileFilter(filter);

      // prompt user for input file
      int returnVal = csvChooser.showOpenDialog(anAppFrame);

      // process result
      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         fn = csvChooser.getSelectedFile().getPath();
         if (TRACE)
            System.out.println("DataGenModel: opening " + fn);
      }
      return fn;
   }

   /**
    * getOutputFilename()
    *
    * This method returns the output filename
    */
   private String getOutputFilename(JFrame anAppFrame)
   {
      String fn = "";

      // setup file chooser
      JFileChooser csvChooser = new JFileChooser();
      CSVFileFilter filter = new CSVFileFilter();
      csvChooser.setFileFilter(filter);

      // prompt user for output file
      int returnVal = csvChooser.showSaveDialog(anAppFrame);

      // process result
      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         fn = csvChooser.getSelectedFile().getPath();
         if (TRACE)
            System.out.println("DataGenModel: saving " + fn);
      }
      return fn;
   }

   /**
    * writeData()
    *
    * This method write the data to the given CSV file
    */
   private boolean writeData(String aCSVFilename)
   {
      try
      {
         // open the chosen file
         File file = new File(aCSVFilename);
         BufferedWriter bufWtr = new BufferedWriter(new FileWriter(file));

         // write each line to text file
         writeKeyValuePairs(bufWtr);

         //close the file
         bufWtr.close();
         return true;
      }
      catch (Exception e)
      {
         return false;
      }
   }

   /**
    * readData()
    *
    * This method read the data from the given CSV file
    */
   private boolean readData(String aCSVFilename)
   {
      try
      {
         // open the chosen file
         File file = new File(aCSVFilename);
         BufferedReader bufRdr = new BufferedReader(new FileReader(file));

         // read each line of text file
         String line = null;
         while ((line = bufRdr.readLine()) != null)
         {
            StringTokenizer st = new StringTokenizer(line, ",");
            while (st.hasMoreTokens())
            {
               String key = st.nextToken();
               String value = st.nextToken();
               processKeyValuePair(key, value);
            }
         }
         //close the file
         bufRdr.close();
         return true;
      }
      catch (Exception e)
      {
         return false;
      }
   }

   /**
    * processKeyValuePair()
    *
    * This method converts the key/value pair into appropriate option
    */
   private void processKeyValuePair(String aKey, String aValue)
   {
      if (TRACE)
         System.out.println("DataGenModel: " + aKey + " " + aValue);

      // scene options
      if (aKey.equalsIgnoreCase("SceneLength"))
         theSceneLength = Integer.valueOf(aValue);
      if (aKey.equalsIgnoreCase("FilenamePrefix"))
         theFilenamePrefix = aValue;

      // point options
      if (aKey.equalsIgnoreCase("GeneratePointsFlag"))
         theGeneratePointsFlag = Boolean.valueOf(aValue);
      if (aKey.equalsIgnoreCase("NumberOfPoints"))
         theNumberOfPoints = Integer.valueOf(aValue);
      if (aKey.equalsIgnoreCase("UniquePointsFlag"))
         theUniquePointsFlag = Boolean.valueOf(aValue);

      // square options
      if (aKey.equalsIgnoreCase("GenerateSquaresFlag"))
         theGenerateSquaresFlag = Boolean.valueOf(aValue);
      if (aKey.equalsIgnoreCase("NumberOfSquares"))
         theNumberOfSquares = Integer.valueOf(aValue);
      if (aKey.equalsIgnoreCase("MaximumSquareSideLength"))
         theMaximumSquareSideLength = Integer.valueOf(aValue);

      // triangle options
      if (aKey.equalsIgnoreCase("GenerateTrianglesFlag"))
         theGenerateTrianglesFlag = Boolean.valueOf(aValue);
      if (aKey.equalsIgnoreCase("NumberOfTriangles"))
         theNumberOfTriangles = Integer.valueOf(aValue);
      if (aKey.equalsIgnoreCase("TriangleBBoxHeight"))
         theTriangleBBoxHeight = Integer.valueOf(aValue);
      if (aKey.equalsIgnoreCase("TriangleBBoxWidth"))
         theTriangleBBoxWidth = Integer.valueOf(aValue);

      // polygon options
      if (aKey.equalsIgnoreCase("GeneratePolygonsFlag"))
         theGeneratePolygonsFlag = Boolean.valueOf(aValue);
      if (aKey.equalsIgnoreCase("NumberOfPolygons"))
         theNumberOfPolygons = Integer.valueOf(aValue);
      if (aKey.equalsIgnoreCase("PolygonMaxVertexCount"))
         thePolygonMaxVertexCount = Integer.valueOf(aValue);
      if (aKey.equalsIgnoreCase("PolygonBBoxLength"))
         thePolygonBBoxLength = Integer.valueOf(aValue);
  
      // linestring options
      if (aKey.equalsIgnoreCase("GenerateLineStringsFlag"))
         theGenerateLineStringsFlag = Boolean.valueOf(aValue);
      if (aKey.equalsIgnoreCase("NumberOfLineStrings"))
         theNumberOfLineStrings = Integer.valueOf(aValue);
      if (aKey.equalsIgnoreCase("LineStringMaxSegmentCount"))
         theLineStringMaxSegmentCount = Integer.valueOf(aValue);
      if (aKey.equalsIgnoreCase("LineStringMaxSegmentLength"))
         theLineStringMaxSegmentLength = Integer.valueOf(aValue); 
   }

   /**
    * writeKeyValuePair()
    *
    * This method converts the key/value pair into appropriate option
    */
   private void writeKeyValuePairs(BufferedWriter bufWtr) throws IOException
   {
      // scene options
      bufWtr.write("SceneLength," + Integer.toString(theSceneLength) + "\n");
      bufWtr.write("FilenamePrefix," + theFilenamePrefix + "\n");

      // point options
      bufWtr.write("GeneratePointsFlag," + Boolean.toString(theGeneratePointsFlag) + "\n");
      bufWtr.write("NumberOfPoints," + Integer.toString(theNumberOfPoints) + "\n");
      bufWtr.write("UniquePointsFlag," + Boolean.toString(theUniquePointsFlag) + "\n");

      // square options
      bufWtr.write("GenerateSquaresFlag," + Boolean.toString(theGenerateSquaresFlag) + "\n");
      bufWtr.write("NumberOfSquares," + Integer.toString(theNumberOfSquares) + "\n");
      bufWtr.write("MaximumSquareSideLength," + Integer.toString(theMaximumSquareSideLength) + "\n");

      // triangle options
      bufWtr.write("GenerateTrianglesFlag," + Boolean.toString(theGenerateTrianglesFlag) + "\n");
      bufWtr.write("NumberOfTriangles," + Integer.toString(theNumberOfTriangles) + "\n");
      bufWtr.write("TriangleBBoxHeight," + Integer.toString(theTriangleBBoxHeight) + "\n");
      bufWtr.write("TriangleBBoxWidth," + Integer.toString(theTriangleBBoxWidth) + "\n");

      // polygon options
      bufWtr.write("GeneratePolygonsFlag," + Boolean.toString(theGeneratePolygonsFlag) + "\n");
      bufWtr.write("NumberOfPolygons," + Integer.toString(theNumberOfPolygons) + "\n");
      bufWtr.write("PolygonMaxVertexCount," + Integer.toString(thePolygonMaxVertexCount) + "\n");
      bufWtr.write("PolygonBBoxLength," + Integer.toString(thePolygonBBoxLength) + "\n");

      // linestring options
      bufWtr.write("GenerateLineStringsFlag," + Boolean.toString(theGenerateLineStringsFlag) + "\n");
      bufWtr.write("NumberOfLineStrings," + Integer.toString(theNumberOfLineStrings) + "\n");
      bufWtr.write("LineStringMaxSegmentCount," + Integer.toString(theLineStringMaxSegmentCount) + "\n");
      bufWtr.write("LineStringMaxSegmentLength," + Integer.toString(theLineStringMaxSegmentLength) + "\n");
   }
}
