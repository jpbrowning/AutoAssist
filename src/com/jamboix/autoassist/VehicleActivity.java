/** VehicleActivity.java
 * 
 * Author:      James Browning, m140726@usna.edu
 * Date:        1 May 2014
 * Description: This class is what you see when you select a vehicle.
 *              It lets you add fuel, view MPG over time, or look at upcoming maintenance.
 */

package com.jamboix.autoassist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VehicleActivity extends Activity implements OnClickListener {
   
   TextView vehicleName;
   String FILENAME = "vehicle_list";
   ArrayList<Vehicle> vehicles;
   FileInputStream fis;
   ObjectInputStream ois;
   int position;
   EditText odometer, gallons, oilNum, brakeNum, transNum, beltNum;
   AlertDialog addDlg, maintDlg, customDlg;
   Spinner maint_spin;
   int ENTRY_INTENT = 0;
   

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_vehicle);
      
      getList();
      Intent intent = getIntent();
      position = intent.getIntExtra("position", 1);
      
      vehicleName = (TextView) findViewById(R.id.vehicleName);

      vehicleName.setText(vehicles.get(position).getName());
   }
   

   
   public void returnResult() {
      Intent intent = new Intent();
      setResult(RESULT_OK, intent);
      finish();
   }
   
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
       if ((keyCode == KeyEvent.KEYCODE_BACK)) { //Back key pressed
          returnResult();
           return true;
       }
       return super.onKeyDown(keyCode, event);
   }

   /** 
    * This method allows the user to input a fuel entry.
    */
   public void fuelButton(View v) {
      buildAddDialog();
      addDlg.show();
   }
   
   /** 
    * This method shows the user a graph of MPG over time.
    */
   public void mpgButton(View v) {
      Intent intent = new Intent(getBaseContext(), ChartActivity.class);
      intent.putExtra("position", position);
      startActivity(intent);       
      saveList();
   }
   
   /** 
    * This method allows the user to input new maintenance.
    */
   public void addMaint(View v) {
      buildMaintDialog();
      maintDlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
      maintDlg.show();
   }
   
   public void setMaint(View v) {
      buildCustomDialog();
      customDlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
      customDlg.show();
   }
   
   /** 
    * This shows the user upcoming maintenance.
    */
   public void viewMaint(View v){
      Intent intent = new Intent(getBaseContext(), MaintActivity.class);
      intent.putExtra("position", position);
      startActivity(intent);
   }
   
   public void viewEntries(View v){
      Intent intent = new Intent(getBaseContext(), EntriesActivity.class);
      intent.putExtra("position", position);
      startActivityForResult(intent, ENTRY_INTENT);
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == ENTRY_INTENT) {
           if (resultCode == RESULT_OK) {
              getList();
           }
       }
   }
   
   private void buildCustomDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      LayoutInflater inflater = LayoutInflater.from(this);
      View layout = inflater.inflate(R.layout.maint_edit, null);
      oilNum = (EditText) layout.findViewById(R.id.oilNum);
      brakeNum = (EditText) layout.findViewById(R.id.brakeNum);
      transNum = (EditText) layout.findViewById(R.id.transNum);
      beltNum = (EditText) layout.findViewById(R.id.beltNum);
      builder.setView(layout);
      oilNum.setText(String.valueOf(vehicles.get(position).getNexts()[0]));
      brakeNum.setText(String.valueOf(vehicles.get(position).getNexts()[1]));
      transNum.setText(String.valueOf(vehicles.get(position).getNexts()[2]));
      beltNum.setText(String.valueOf(vehicles.get(position).getNexts()[3]));      
      builder.setTitle("Editing Maintenance Intervals:")
        .setPositiveButton("Finished", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
              if(oilNum.getText().length() > 0 && brakeNum.getText().length() > 0
                    && transNum.getText().length() > 0 && beltNum.getText().length() > 0) {
                 vehicles.get(position).setNexts(0, Integer.valueOf(oilNum.getText().toString()));
                 vehicles.get(position).setNexts(1, Integer.valueOf(brakeNum.getText().toString()));
                 vehicles.get(position).setNexts(2, Integer.valueOf(transNum.getText().toString()));
                 vehicles.get(position).setNexts(3, Integer.valueOf(beltNum.getText().toString()));
              }
              else {
                 maintErrorToast();
              }
              saveList();
           }

         private void maintErrorToast() {
            Toast.makeText(getBaseContext(), "Need a value for all four maintenance items.",
                  Toast.LENGTH_LONG).show();
            
         }
       })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
              return;
           }
       });
      customDlg = builder.create();
   }
   
   /** 
    * This builds a dialog for adding fuel.
    */
   private void buildAddDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      LayoutInflater inflater = LayoutInflater.from(this);
      View layout = inflater.inflate(R.layout.fuel_input, null);
      gallons = (EditText) layout.findViewById(R.id.gallons);
      odometer = (EditText) layout.findViewById(R.id.odometer);
      builder.setView(layout);
      builder.setTitle("Adding Gallons:")
        .setPositiveButton("Go", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
              // Open new vehicle activity
              vehicles.get(position).addFuelEntry(gallons.getText().toString(), 
                    odometer.getText().toString());
              Toast.makeText(getBaseContext(), gallons.getText().toString() + " gallons recorded.", 
                    Toast.LENGTH_SHORT).show();
              saveList();
           }
       })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
              return;
           }
       });
      addDlg = builder.create();
   }
   
   /** 
    * This method builds a dialog for adding maintenance.
    */
   private void buildMaintDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      LayoutInflater inflater = LayoutInflater.from(this);
      View layout = inflater.inflate(R.layout.maint_input, null); 
      maint_spin = (Spinner) layout.findViewById(R.id.maint_spin);
      odometer = (EditText) layout.findViewById(R.id.odometer);
      builder.setView(layout);
      builder.setTitle("Recording Maintenance")
        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
              // Open new vehicle activity
              vehicles.get(position).addMaintEntry(maint_spin.getSelectedItemPosition(),
                    odometer.getText().toString());
              Toast.makeText(getBaseContext(), "Maintenence Recorded.", Toast.LENGTH_LONG).show();
              saveList();
           }
       })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
              return;
           }
       });
      maintDlg = builder.create();
   }

   /** 
    * This method gets the list of vehicles.
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
   
   /** 
    * This method saves the list of vehicles.
    */
   public void saveList() {
      try {
           FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
           ObjectOutputStream oos = new ObjectOutputStream(fos);
           oos.writeObject(vehicles); 
           oos.close();

       } catch (Exception e) {
           e.printStackTrace();
       }
   }

   @Override
   public void onClick(DialogInterface dialog, int which) {
      // TODO Auto-generated method stub
      
   }
}
