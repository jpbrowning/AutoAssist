package com.jamboix.autoassist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EntriesActivity extends ListActivity {
   
   String FILENAME = "vehicle_list";
   ArrayList<Vehicle> vehicles;
   FileInputStream fis;
   ObjectInputStream ois;
   int position, entry;
   AlertDialog deleteDlg;
   ArrayAdapter<String> adapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      Intent intent = getIntent();
      position = intent.getIntExtra("position", 1);
      
      getList();
      
      adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, vehicles.get(position).getFuelEntries());
      
      setListAdapter(adapter);

   }
   
   public void buildDeleteDialog(){
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("Really delete fuel entry?")
        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
              vehicles.get(position).removeFuelEntries(entry);
              adapter.notifyDataSetChanged();
              saveList();
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
   
   @Override
   public void onListItemClick(ListView parent, View view, int pos, long id) {
      entry = pos;
      buildDeleteDialog();
      deleteDlg.show();
   } 
   
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
   
   public void returnResult() {
      Intent intent = new Intent();
      setResult(RESULT_OK, intent);
      saveList();
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
   
   public void onStop() {
      super.onStop();
      saveList();
   }
   
   public void onPause() {
      super.onPause();
      saveList();
   }
}
