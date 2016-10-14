/**
 * PolygonGeneratorView.java
 *
 * @author :  Tim Faulkner
 * @version : 12/19/2011
 */

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent; 
import java.text.NumberFormat;
import javax.swing.text.*;

public class PolygonGeneratorView implements PropertyChangeListener, ItemListener
{
   //***
   // class variables
   //***

   private static final boolean TRACE = false;
   
   private static final String TAB_TITLE = "Polygons";
   private static final String TAB_TOOLTIP = "Polygon generator options";
   private static final String GENERATE_CHECKBOX_TITLE = "Generate data file?";
   private static final boolean DEFAULT_GENERATE_FLAG = true;
   private static final int DEFAULT_POLYGON_COUNT = 50;
   private static final int DEFAULT_MAX_VERTEX_COUNT = 10;
   private static final int DEFAULT_BBOX_LENGTH = 100;
   
   //***
   // instance variables
   //***

   // parent panel for all elements
   private JPanel theTabbedPanePanel;

   // elements for 'generate' flag
   private JCheckBox theGenerateCheckbox;
   private JPanel theGeneratePanel;

   // elements for 'number of squares'
   private NumberFormat theNumberOfPolygonsFormat;
   private JPanel theNumberOfPolygonsPanel;
   private JLabel theNumberOfPolygonsLabel;
   private JFormattedTextField theNumberOfPolygonsField;

   // elements for 'max vertex count'
   private NumberFormat theMaximumVertexCountFormat;
   private JPanel theMaximumVertexCountPanel;
   private JLabel theMaximumVertexCountLabel;
   private JFormattedTextField theMaximumVertexCountField;

   // elements for 'bounding box length'
   private NumberFormat theBBoxLengthFormat;
   private JPanel theBBoxLengthPanel;
   private JLabel theBBoxLengthLabel;
   private JFormattedTextField theBBoxLengthField;

   // property values
   private boolean theGenerateFlag;
   private int theNumberOfPolygons;
   private int theMaximumVertexCount;
   private int theBBoxLength;

   /**
    * PolygonGeneratorView
    *
    * This class implements the view for the PolygonGenerator class and
    * handles related user interface events
    */
   PolygonGeneratorView()
   {
      theGenerateFlag = DEFAULT_GENERATE_FLAG;
      theNumberOfPolygons = DEFAULT_POLYGON_COUNT;
      theMaximumVertexCount = DEFAULT_MAX_VERTEX_COUNT;
      theBBoxLength = DEFAULT_BBOX_LENGTH;
   }

   /**
    * setGenerateFlag
    *
    * This method sets the generate datfile property
    */
   public void setGenerateFlag(boolean aFlag)
   {
      if (theGenerateCheckbox != null)
      {
         if (theGenerateFlag != aFlag)
         {
            theGenerateCheckbox.doClick();
            theGenerateCheckbox.updateUI();
         }
      }
   }

   /**
    * setNumberOfPolygons
    * 
    * This method sets the current number of squares
    */
   public void setNumberOfPolygons(int aCount)
   {
      theNumberOfPolygons = aCount;
      if (theNumberOfPolygonsField != null)
      {
         theNumberOfPolygonsField.setValue(theNumberOfPolygons);
         theNumberOfPolygonsField.updateUI();
      }
   }

   /**
    * setMaximumVertexCount
    *
    * This method sets the current maximum side length
    */
   public void setMaximumVertexCount(int aCount)
   {
      theMaximumVertexCount = aCount;
      if (theMaximumVertexCountField != null)
      {
         theMaximumVertexCountField.setValue(theMaximumVertexCount);
         theMaximumVertexCountField.updateUI();
      }
   }

   /**
    * setBBoxLength
    * 
    * This method sets the current maximum bbox length
    */
   public void setBBoxLength(int aLength)
   {
      theBBoxLength = aLength;
      if (theBBoxLengthField != null)
      {
         theBBoxLengthField.setValue(theBBoxLength);
         theBBoxLengthField.updateUI();
      }
   }

  /**
   * getGenerateFlag
   *
   * This method returns the generate datfile property
   */
   public boolean getGenerateFlag()
   {
      return theGenerateFlag;
   }

 /**
   * getNumberOfPolygons
   *
   * This method returns the current number of squares
   */
   public int getNumberOfPolygons()
   {
      return theNumberOfPolygons;
   }

   /**
    * getMaximumVertexCount
    *
    * This method returns the current maximum vertex count
    */
   public int getMaximumVertexCount()
   {
      return theMaximumVertexCount;
   }

   /**
    * getBBoxLength
    * 
    * This method returns the current bbox side length
    */
   public int getBBoxLength()
   {
      return theBBoxLength;
   }

   /**
    * build
    * 
    * This method builds the user interface and ties in any
    * evenet listeners
    */
   public void build(JTabbedPane aTabbedPane)
   {
      //***
      // generate flag
      //***

      // create generate flag [checkbox]
      theGenerateCheckbox = new JCheckBox(GENERATE_CHECKBOX_TITLE);
      theGenerateCheckbox.setSelected(theGenerateFlag);
      theGenerateCheckbox.addItemListener(this);

      // add to containing panel
      theGeneratePanel = new JPanel();
      theGeneratePanel.add(theGenerateCheckbox);

      //***
      // number of squares
      //***

      // build format arguments
      theNumberOfPolygonsFormat = NumberFormat.getIntegerInstance();

      // create number of elements [label, field]
      theNumberOfPolygonsLabel = new JLabel("Number of polygons:");
      theNumberOfPolygonsLabel.setHorizontalAlignment(JLabel.LEFT);
      theNumberOfPolygonsField = new JFormattedTextField(theNumberOfPolygonsFormat);
      theNumberOfPolygonsField.setValue(new Double(theNumberOfPolygons));
      theNumberOfPolygonsField.setColumns(10);
      theNumberOfPolygonsField.addPropertyChangeListener("value", this);

      // add to containing panel
      theNumberOfPolygonsPanel = new JPanel();
      theNumberOfPolygonsPanel.add(theNumberOfPolygonsLabel);
      theNumberOfPolygonsPanel.add(theNumberOfPolygonsField);

      //***
      // maximum vertex count
      //***

      // build format arguments
      theMaximumVertexCountFormat = NumberFormat.getIntegerInstance();

      // create number of point elements [label, field]
      theMaximumVertexCountLabel = new JLabel("Maximum vertex count:");
      theMaximumVertexCountLabel.setHorizontalAlignment(JLabel.LEFT);
      theMaximumVertexCountField = new JFormattedTextField(theMaximumVertexCountFormat);
      theMaximumVertexCountField.setValue(new Double(theMaximumVertexCount));
      theMaximumVertexCountField.setColumns(10);
      theMaximumVertexCountField.addPropertyChangeListener("value", this);

      // add to containing panel
      theMaximumVertexCountPanel = new JPanel();
      theMaximumVertexCountPanel.add(theMaximumVertexCountLabel);
      theMaximumVertexCountPanel.add(theMaximumVertexCountField);

      //***
      // bbox length
      //***

      // build format arguments
      theBBoxLengthFormat = NumberFormat.getIntegerInstance();

      // create number of point elements [label, field]
      theBBoxLengthLabel = new JLabel("Maximum side length:");
      theBBoxLengthLabel.setHorizontalAlignment(JLabel.LEFT);
      theBBoxLengthField = new JFormattedTextField(theBBoxLengthFormat);
      theBBoxLengthField.setValue(new Double(theBBoxLength));
      theBBoxLengthField.setColumns(10);
      theBBoxLengthField.addPropertyChangeListener("value", this);

      // add to containing panel
      theBBoxLengthPanel = new JPanel();
      theBBoxLengthPanel.add(theBBoxLengthLabel);
      theBBoxLengthPanel.add(theBBoxLengthField);

      //***
      // update tabbed pane
      //***

      // build tab
      theTabbedPanePanel = new JPanel();
      theTabbedPanePanel.setLayout(new BoxLayout(theTabbedPanePanel, BoxLayout.PAGE_AXIS));
      theTabbedPanePanel.add(theGeneratePanel);
      theTabbedPanePanel.add(theNumberOfPolygonsPanel);
      theTabbedPanePanel.add(theMaximumVertexCountPanel);
      theTabbedPanePanel.add(theBBoxLengthPanel);

      // add new tab to tabbed pane
      aTabbedPane.addTab(TAB_TITLE, null, theTabbedPanePanel, TAB_TOOLTIP);
   }

   /** 
    * propertyChange
    * 
    * Called when a field's "value" property changes
    */
   public void propertyChange(PropertyChangeEvent e)
   {
      Object source = e.getSource();
      if (source == theNumberOfPolygonsField)
      {
         theNumberOfPolygons = ((Number)theNumberOfPolygonsField.getValue()).intValue();
         if (TRACE)
            System.out.println("Polygons: number of squares = " + theNumberOfPolygons);
      }
      else if (source == theMaximumVertexCountField)
      {
         theMaximumVertexCount = ((Number)theMaximumVertexCountField.getValue()).intValue();
         if (TRACE)
            System.out.println("Polygons: maximum vertex count = " + theMaximumVertexCount);
      }
      else if (source == theBBoxLengthField)
      {
         theBBoxLength = ((Number)theBBoxLengthField.getValue()).intValue();
         if (TRACE)
            System.out.println("Polygons: maximum side length = " + theBBoxLength);
      }
   }
   
   /**
    * itemStateChanged
    *
    * Called when a checkbox's state changes
    */
   public void itemStateChanged(ItemEvent e)
   {
      Object source = e.getItemSelectable();
      if (source == theGenerateCheckbox)
      {
         theGenerateFlag = !theGenerateFlag;
         if (theGenerateFlag)
         {
           theNumberOfPolygonsField.setEnabled(true);
           theMaximumVertexCountField.setEnabled(true);
           theBBoxLengthField.setEnabled(true);
         }
         else
         {
           theNumberOfPolygonsField.setEnabled(false);
           theMaximumVertexCountField.setEnabled(false);
           theBBoxLengthField.setEnabled(false);
         }
         if (TRACE)
            System.out.println("Polygons: generate = " + theGenerateFlag);
      }
   }
}