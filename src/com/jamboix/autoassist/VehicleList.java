/** VehicleList.java
 * 
 * Author:      James Browning, m140726@usna.edu
 * Date:        1 May 2014
 * Description: This class shows you vehicles you have input, lets you select one,
 *              or add a new one.
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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class VehicleList extends Activity implements OnClickListener{
   
   String FILENAME = "vehicle_list";
   ArrayList<Vehicle> vehicles;
   FileInputStream fis;
   ObjectInputStream ois;
   ArrayAdapter<Vehicle> listAdapter;
   EditText vehicleName, vehicleDesc;
   AlertDialog addDlg, clickDlg, deleteDlg;
   static int position;
   ListView listView;
   int VEHICLE_ACTIVITY = 2;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_vehicle_list);
      
      getList();
      
      listAdapter = new ArrayAdapter<Vehicle>(this, 
            R.layout.list_layout, R.id.textView, vehicles);
      listView = (ListView) findViewById(R.id.vehicle_list);
      listView.setAdapter(listAdapter);
      
      listView.setOnItemClickListener(new OnItemClickListener(){

         @Override
         public void onItemClick(AdapterView<?> parent, View view,
               int position, long id) {
            VehicleList.position = position;
            buildClickDialog();
            clickDlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            clickDlg.show();
         }         
      });
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == VEHICLE_ACTIVITY) {
           if (resultCode == RESULT_OK) {
              getList();
           }
       }
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
   
   /** 
    * This method refreshes the list view when a new vehicle is added or
    * one is deleted.
    */
   public void refresh(){
      listView.invalidate();
      listAdapter.notifyDataSetChanged();
   }
   
   /** 
    * This method builds a dialog for adding vehicles.
    */
   private void buildAddDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      LayoutInflater inflater = LayoutInflater.from(this);
      View layout = inflater.inflate(R.layout.input, null);
      vehicleName = (EditText) layout.findViewById(R.id.vehicleName);
      builder.setView(layout);
      builder.setTitle("Add New Vehicle:")
        .setPositiveButton("OK", this)
        .setNegativeButton("Cancel", this);
      addDlg = builder.create();      

   }
   
   /** 
    * This method builds a dialog for clicking a vehicle.
    */
   public void buildClickDialog(){
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle(vehicles.get(position).getName() + ":")
        .setPositiveButton("Go", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
              // Open new vehicle activity
              Intent intent = new Intent(getBaseContext(), VehicleActivity.class);
              intent.putExtra("position", position);
              startActivityForResult(intent, VEHICLE_ACTIVITY); 
           }
       })
        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
               buildDeleteDialog();
               deleteDlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
               deleteDlg.show();
           }
       });
      clickDlg = builder.create();
   }
   
   /** 
    * This method builds a confirmation dialog for deleting vehicles.
    */
   public void buildDeleteDialog(){
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("Really delete " + vehicles.get(position).getName() + "?")
        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
              vehicles.remove(position);
              listView.invalidate();
              listAdapter.notifyDataSetChanged();
              saveList();
              refresh();
           }
       })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
              return;
           }
       });
      deleteDlg = builder.create();
   }
   
   /** 
    * This method opens the dialog for adding a vehicle when the add button is pressed.
    */
   public void addButton(View v) {
      buildAddDialog();
      addDlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
      addDlg.show();
   }

   @Override
   public void onClick(DialogInterface dialog, int which) {
      switch (which) {
      case Dialog.BUTTON_NEGATIVE:
         break;
      case Dialog.BUTTON_POSITIVE:
         if(vehicleName.getText().toString().length() > 0) {
            vehicles.add(new Vehicle(vehicleName.getText().toString()));
            saveList();
            refresh();
         }
         break;
      }
   }
   
   // Save the list if the users abandons the ship.
   public void onStop() {
      super.onStop();
      saveList();
   }
   
   public void onPause() {
      super.onPause();
      saveList();
   }
}
