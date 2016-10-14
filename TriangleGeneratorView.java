/**
 * TriangleGeneratorView.java
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

public class TriangleGeneratorView implements PropertyChangeListener, ItemListener
{
   //***
   // class variables
   //***

   private static final boolean TRACE = false;
   
   private static final String TAB_TITLE = "Triangles";
   private static final String TAB_TOOLTIP = "Triangle generator options";
   private static final String GENERATE_CHECKBOX_TITLE = "Generate data file?";
   private static final boolean DEFAULT_GENERATE_FLAG = true;
   private static final int DEFAULT_TRIANGLE_COUNT = 75;
   private static final int DEFAULT_BBOX_HEIGHT = 50;
   private static final int DEFAULT_BBOX_WIDTH = 50;

   //***
   // instance variables
   //***

   // parent panel for all elements
   private JPanel theTabbedPanePanel;

   // elements for 'generate' flag
   private JCheckBox theGenerateCheckbox;
   private JPanel theGeneratePanel;

   // elements for 'number of triangles'
   private NumberFormat theNumberOfTrianglesFormat;
   private JPanel theNumberOfTrianglesPanel;
   private JLabel theNumberOfTrianglesLabel;
   private JFormattedTextField theNumberOfTrianglesField;

   // elements for 'bbox height'
   private NumberFormat theBBoxHeightFormat;
   private JPanel theBBoxHeightPanel;
   private JLabel theBBoxHeightLabel;
   private JFormattedTextField theBBoxHeightField;

   // elements for 'bbox width'
   private NumberFormat theBBoxWidthFormat;
   private JPanel theBBoxWidthPanel;
   private JLabel theBBoxWidthLabel;
   private JFormattedTextField theBBoxWidthField;

   // property values
   private boolean theGenerateFlag;
   private int theNumberOfTriangles;
   private int theBBoxHeight;
   private int theBBoxWidth;
   
   /**
    * TriangleGeneratorView
    *
    * This class implements the view for the TriangleGenerator class and
    * handles related user interface events
    */
   TriangleGeneratorView()
   {
      theGenerateFlag = DEFAULT_GENERATE_FLAG;
      theNumberOfTriangles = DEFAULT_TRIANGLE_COUNT;
      theBBoxHeight = DEFAULT_BBOX_HEIGHT;
      theBBoxWidth = DEFAULT_BBOX_WIDTH;
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
    * setNumberOfTriangles
    * 
    * This method sets the current number of triangles
    */
   public void setNumberOfTriangles(int aCount)
   {
      theNumberOfTriangles = aCount;
      if (theNumberOfTrianglesField != null)
      {
         theNumberOfTrianglesField.setValue(theNumberOfTriangles);
         theNumberOfTrianglesField.updateUI();
      }
   }

   /**
    * setBBoxHeight
    * 
    * This method sets the current bbox height
    */
   public void setBBoxHeight(int aLength)
   {
      theBBoxHeight = aLength;
      if (theBBoxHeightField != null)
      {
         theBBoxHeightField.setValue(theBBoxHeight);
         theBBoxHeightField.updateUI();
      }
   }
   
   /**
    * setBBoxWidth
    *
    * This method sets the current bbox width
    */
   public void setBBoxWidth(int aLength)
   {
      theBBoxWidth = aLength;
      if (theBBoxWidthField != null)
      {
         theBBoxWidthField.setValue(theBBoxWidth);
         theBBoxWidthField.updateUI();
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
   * getNumberOfTriangles
   *
   * This method returns the current number of squares
   */
   public int getNumberOfTriangles()
   {
      return theNumberOfTriangles;
   }

   /**
    * getBBoxHeight
    * 
    * This method returns the current bbox height
    */
   public int getBBoxHeight()
   {
      return theBBoxHeight;
   }
   
   /**
    * getBBoxHeight
    *
    * This method returns the current bbox width
    */
   public int getBBoxWidth()
   {
      return theBBoxWidth;
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
      // number of triangles
      //***

      // build format arguments
      theNumberOfTrianglesFormat = NumberFormat.getIntegerInstance();

      // create number of point elements [label, field]
      theNumberOfTrianglesLabel = new JLabel("Number of triangles");
      theNumberOfTrianglesLabel.setHorizontalAlignment(JLabel.LEFT);
      theNumberOfTrianglesField = new JFormattedTextField(theNumberOfTrianglesFormat);
      theNumberOfTrianglesField.setValue(new Double(theNumberOfTriangles));
      theNumberOfTrianglesField.setColumns(10);
      theNumberOfTrianglesField.addPropertyChangeListener("value", this);

      // add to containing panel
      theNumberOfTrianglesPanel = new JPanel();
      theNumberOfTrianglesPanel.add(theNumberOfTrianglesLabel);
      theNumberOfTrianglesPanel.add(theNumberOfTrianglesField);

      //***
      // bbox height
      //***

      // build format arguments
      theBBoxHeightFormat = NumberFormat.getIntegerInstance();

      // create number of point elements [label, field]
      theBBoxHeightLabel = new JLabel("Maximum height of triangle bounding box:");
      theBBoxHeightLabel.setHorizontalAlignment(JLabel.LEFT);
      theBBoxHeightField = new JFormattedTextField(theBBoxHeightFormat);
      theBBoxHeightField.setValue(new Double(theBBoxHeight));
      theBBoxHeightField.setColumns(10);
      theBBoxHeightField.addPropertyChangeListener("value", this);

      // add to containing panel
      theBBoxHeightPanel = new JPanel();
      theBBoxHeightPanel.add(theBBoxHeightLabel);
      theBBoxHeightPanel.add(theBBoxHeightField);

      //***
      // bbox width
      //***

      // build format arguments
      theBBoxWidthFormat = NumberFormat.getIntegerInstance();

      // create number of point elements [label, field]
      theBBoxWidthLabel = new JLabel("Maximum width of triangle bounding box:");
      theBBoxWidthLabel.setHorizontalAlignment(JLabel.LEFT);
      theBBoxWidthField = new JFormattedTextField(theBBoxWidthFormat);
      theBBoxWidthField.setValue(new Double(theBBoxWidth));
      theBBoxWidthField.setColumns(10);
      theBBoxWidthField.addPropertyChangeListener("value", this);

      // add to containing panel
      theBBoxWidthPanel = new JPanel();
      theBBoxWidthPanel.add(theBBoxWidthLabel);
      theBBoxWidthPanel.add(theBBoxWidthField);

      //***
      // update tabbed pane
      //***

      // build tab
      theTabbedPanePanel = new JPanel();
      theTabbedPanePanel.setLayout(new BoxLayout(theTabbedPanePanel, BoxLayout.PAGE_AXIS));
      theTabbedPanePanel.add(theGeneratePanel);
      theTabbedPanePanel.add(theNumberOfTrianglesPanel);
      theTabbedPanePanel.add(theBBoxHeightPanel);
      theTabbedPanePanel.add(theBBoxWidthPanel);

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
      if (source == theNumberOfTrianglesField)
      {
         theNumberOfTriangles = ((Number)theNumberOfTrianglesField.getValue()).intValue();
         if (TRACE)
            System.out.println("Triangles: number of squares = " + theNumberOfTriangles);
      }
      else if (source == theBBoxHeightField)
      {
         theBBoxHeight = ((Number)theBBoxHeightField.getValue()).intValue();
         if (TRACE)
            System.out.println("Triangles: bbox height = " + theBBoxHeight);
      }
      else if (source == theBBoxWidthField)
      {
         theBBoxWidth = ((Number)theBBoxWidthField.getValue()).intValue();
         if (TRACE)
            System.out.println("Triangles: bbox width = " + theBBoxWidth);
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
           theNumberOfTrianglesField.setEnabled(true);
           theBBoxHeightField.setEnabled(true);
           theBBoxWidthField.setEnabled(true);
         }
         else
         {
           theNumberOfTrianglesField.setEnabled(false);
           theBBoxHeightField.setEnabled(false);
           theBBoxWidthField.setEnabled(false);
         }
         if (TRACE)
            System.out.println("Triangles: generate = " + theGenerateFlag);
      }
   }
}