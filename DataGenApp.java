import java.io.*;

public class DataGenApp {

   //***
   // instance variables
   //***
   
   DataGenerator theDataGenerator;

   //***
   // main entry point
   //***
   
   public static void main (final String[] args) throws IOException {
   
      DataGenerator dataGen = new DataGenerator();
      dataGen.run();
   }

}
