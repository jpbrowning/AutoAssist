/** MaintActivity.java
 * 
 * Author:      James Browning, m140726@usna.edu
 * Date:        1 May 2014
 * Description: This class loads the view showing users when upcoming maintenece should be performed,
 *              based on the last odometer value they entered in a fuel entry.
 */

package com.jamboix.autoassist;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class MaintActivity extends Activity {
   
   TextView oilNum, brakeNum, transNum, beltNum, odoNum;
   String FILENAME = "vehicle_list";
   ArrayList<Vehicle> vehicles;
   FileInputStream fis;
   ObjectInputStream ois;
   double currentOdo, thisNum;
   int position;
   int[] nextOdo;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_maint);
      
      getList();
      Intent intent = getIntent();
      position = intent.getIntExtra("position", 1);
      
      oilNum = (TextView) findViewById(R.id.oil_num);
      brakeNum = (TextView) findViewById(R.id.brake_num);
      transNum = (TextView) findViewById(R.id.trans_num);
      beltNum = (TextView) findViewById(R.id.belt_num);
      odoNum = (TextView) findViewById(R.id.odo_num);
      
      getList();
      
      nextOdo = vehicles.get(position).getNexts();
      
      getNums();
   }
   
   /** 
    * This method gets all of the values for upcoming maintenece from the saved arraylist of vehicles.
    * The "+ x" at the end of each .setText is the recommended interval between maintenence.
    */
   private void getNums() {      
      if(vehicles.get(position).getMileage() != 0.0){
         currentOdo = vehicles.get(position).getMileage();
         odoNum.setText(String.valueOf(currentOdo));
      }
      else {
         odoNum.setText("Log a fuel entry to set your vehicle odometer.");
      }
      if(vehicles.get(position).getMaintEntry(0) != 0.0){
         System.out.println(vehicles.get(position).getMaintEntry(0));
         thisNum = vehicles.get(position).getMaintEntry(0) + nextOdo[0];
         if(thisNum < currentOdo) {
            oilNum.setTextColor(Color.RED);
         }
         oilNum.setText(String.valueOf(thisNum));
      }
      else {
         oilNum.setText("Log this maintenance for a value.");
      }
      if(vehicles.get(position).getMaintEntry(1) != 0.0){
         thisNum = vehicles.get(position).getMaintEntry(1) + nextOdo[1];
         System.out.println(vehicles.get(position).getMaintEntry(1));
         if(thisNum < currentOdo) {
            brakeNum.setTextColor(Color.RED);
         }
         brakeNum.setText(String.valueOf(thisNum));
      }
      else {
         brakeNum.setText("Log this maintenance for a value.");
      }
      if(vehicles.get(position).getMaintEntry(2) != 0.0){
         System.out.println(vehicles.get(position).getMaintEntry(2));
         thisNum = vehicles.get(position).getMaintEntry(2) + nextOdo[2];
         if(thisNum < currentOdo) {
            transNum.setTextColor(Color.RED);
         }
         transNum.setText(String.valueOf(thisNum));
      }
      else {
         transNum.setText("Log this maintenance for a value.");
      }
      if(vehicles.get(position).getMaintEntry(3) != 0.0){
         System.out.println(vehicles.get(position).getMaintEntry(3));
         thisNum = vehicles.get(position).getMaintEntry(3) + nextOdo[3];
         if(thisNum < currentOdo) {
            beltNum.setTextColor(Color.RED);
         }
         beltNum.setText(String.valueOf(thisNum));
      }
      else {
         beltNum.setText("Log this maintenance for a value.");
      }
   }

   /** 
    * This method gets the list of vehicles from the saved file.
    */
   @SuppressWarnings("unchecked")
   private void getList() {
      File file = getFileStreamPath(FILENAME);

      if (!file.exists()) {
         try {
            file.createNewFile();
            vehicles = new ArrayList<Vehicle>();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      else {
         try {
            fis = openFileInput(FILENAME);
            ois = new ObjectInputStream(fis);
            vehicles = (ArrayList<Vehicle>) ois.readObject();
            return;
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
      vehicles = new ArrayList<Vehicle>();
   }
}
