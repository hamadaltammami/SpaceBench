/**
 * PointGeneratorView.java
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

public class PointGeneratorView implements PropertyChangeListener, ItemListener
{
   //***
   // class variables
   //***

   private static final boolean TRACE = false;
   
   private static final String TAB_TITLE = "Points";
   private static final String TAB_TOOLTIP = "Point generator options";
   private static final String GENERATE_CHECKBOX_TITLE = "Generate data file?";
   private static final boolean DEFAULT_GENERATE_FLAG = true;
   private static final int DEFAULT_POINT_COUNT = 100;
   private static final boolean DEFAULT_UNIQUE_POINTS_FLAG = false;
   private static final String UNIQUE_POINTS_CHECKBOX_TITLE = "Should points be unique?";

   //***
   // instance variables
   //***

   // parent panel for all elements
   private JPanel theTabbedPanePanel;

   // elements for 'generate' flag
   private JCheckBox theGenerateCheckbox;
   private JPanel theGeneratePanel;
   
   // elements for 'number of points'
   private NumberFormat theNumberOfPointsFormat;
   private JPanel theNumberOfPointsPanel;
   private JLabel theNumberOfPointsLabel;
   private JFormattedTextField theNumberOfPointsField;

   // elements for 'unique points'
   private JCheckBox theUniquePointsCheckbox;
   private JPanel theUniquePointsPanel;

   // property values
   private boolean theGenerateFlag;
   private int theNumberOfPoints;
   private boolean theUniquePointsFlag;

   /**
    * PointGeneratorView
    *
    * This class implements the view for the PointGenerator class and
    * handles related user interface events
    */
   PointGeneratorView()
   {
      theGenerateFlag = DEFAULT_GENERATE_FLAG;
      theNumberOfPoints = DEFAULT_POINT_COUNT;
      theUniquePointsFlag = DEFAULT_UNIQUE_POINTS_FLAG;
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
    * setNumberOfPoints
    * 
    * This method sets the current number of points property that
    * controls the number of points generated in the scene
    */
   public void setNumberOfPoints(int aCount)
   {
      theNumberOfPoints = aCount;
      if (theNumberOfPointsField != null)
      {
         theNumberOfPointsField.setValue(theNumberOfPoints);
         theNumberOfPointsField.updateUI();
      }
   }

   /**
    * setUniquePointsFlag
    * 
    * This method sets the unique points property, if true duplicate
    * points are ignored.  Setting this flag requires the allocation
    * of a boolean matrix.
    */
   public void setUniquePointsFlag(boolean aFlag)
   {
      if (theUniquePointsCheckbox != null)
      {
         if (theUniquePointsFlag != aFlag)
         {
            theUniquePointsCheckbox.doClick();
            theUniquePointsCheckbox.updateUI();
         }
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
    * getNumberOfPoints
    * 
    * This method returns the current number of points property
    */
   public int getNumberOfPoints()
   {
      return theNumberOfPoints;
   }

   /**
   * getUniquePointsFlag
   * 
   * This method returns the unique points property
   */
   public boolean getUniquePointsFlag()
   {
      return theUniquePointsFlag;
   }  

   /**
    * build
    * 
    * This method builds the user interface and ties in any
    * event listeners
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
      // number of points
      //***

      // build format arguments
      theNumberOfPointsFormat = NumberFormat.getIntegerInstance();

      // create number of point elements [label, field]
      theNumberOfPointsLabel = new JLabel("Number of points:");
      theNumberOfPointsLabel.setHorizontalAlignment(JLabel.LEFT);
      theNumberOfPointsField = new JFormattedTextField(theNumberOfPointsFormat);
      theNumberOfPointsField.setValue(new Double(theNumberOfPoints));
      theNumberOfPointsField.setColumns(10);
      theNumberOfPointsField.addPropertyChangeListener("value", this);

      // add to containing panel
      theNumberOfPointsPanel = new JPanel();
      theNumberOfPointsPanel.add(theNumberOfPointsLabel);
      theNumberOfPointsPanel.add(theNumberOfPointsField);

      //***
      // unique points flag
      //***

      // create unique points elements [checkbox]
      theUniquePointsCheckbox = new JCheckBox(UNIQUE_POINTS_CHECKBOX_TITLE);
      theUniquePointsCheckbox.setSelected(theUniquePointsFlag);
      theUniquePointsCheckbox.addItemListener(this);

      // add to containing panel
      theUniquePointsPanel = new JPanel();
      theUniquePointsPanel.add(theUniquePointsCheckbox);

      //***
      // update tabbed pane
      //***

      // build tab
      theTabbedPanePanel = new JPanel();
      theTabbedPanePanel.setLayout(new BoxLayout(theTabbedPanePanel, BoxLayout.PAGE_AXIS));
      theTabbedPanePanel.add(theGeneratePanel);
      theTabbedPanePanel.add(theNumberOfPointsPanel);
      theTabbedPanePanel.add(theUniquePointsPanel);

      // add new tab to tabbed pane
      aTabbedPane.addTab(TAB_TITLE, null, theTabbedPanePanel, TAB_TOOLTIP);
   }

   /** 
    * propertyChange
    * 
    * Called when a textbox's "value" changes
    */
   public void propertyChange(PropertyChangeEvent e)
   {
      Object source = e.getSource();
      if (source == theNumberOfPointsField)
      {
         theNumberOfPoints = ((Number)theNumberOfPointsField.getValue()).intValue();
         if (TRACE)
            System.out.println("Points: number of points = " + theNumberOfPoints);
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
           theNumberOfPointsField.setEnabled(true);
           theUniquePointsCheckbox.setEnabled(true);
         }
         else
         {
           theNumberOfPointsField.setEnabled(false);
           theUniquePointsCheckbox.setEnabled(false);
         }
         if (TRACE)
            System.out.println("Points: generate = " + theGenerateFlag);
      }
      if (source == theUniquePointsCheckbox)
      {
         theUniquePointsFlag = !theUniquePointsFlag;
         if (TRACE)
            System.out.println("Points: unique points = " + theUniquePointsFlag);
      }
   }
    
}