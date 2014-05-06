/** Vehicle.java
 * 
 * Author:      James Browning, m140726@usna.edu
 * Date:        1 May 2014
 * Description: This class represents a vehicle object.
 */

package com.jamboix.autoassist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Vehicle implements Serializable {
   
   private ArrayList<String> fuelEntries;
   private String[] maintEntries = new String[4];
   private Calendar creationDate;
   private String name = "Empty";
   private String description = "Empty";
   static final long serialVersionUID = 1L;
   private Double lastMileage = 0.0;
   private int[] nextOdo = new int[4];
   
   /** 
    * This constructor is called when a user defines a new vehicle.
    *
    * @param  title        The vehicle name
    * @param  description  Short description of vehicle
    */
   public Vehicle(String title){
      this.name = title;
      fuelEntries = new ArrayList<>();
      this.creationDate = Calendar.getInstance();
      nextOdo[0] = 7500;
      nextOdo[1] = 25000;
      nextOdo[2] = 50000;
      nextOdo[3] = 100000;
   }
   
   /** 
    * This returns the fuel entries.
    * 
    * @return  fuelEntries  ArrayList of Fuel Entries
    */
   public ArrayList<String> getFuelEntries() {
      return fuelEntries;
   }
   
   /** 
    * This adds a fuel entry.
    * 
    * @param x    Gallons added
    * @param y    Odometer value
    */
   public void addFuelEntry(String x, String y) {
      fuelEntries.add(Calendar.getInstance().getTime()+ "\nGallons: " + x + "\nOdometer: " + y);
      lastMileage = Double.valueOf(y);
   }
   
   public void removeFuelEntries(int x) {
      fuelEntries.remove(x);
   }
   
   /** 
    * This adds a maintenance entry
    * 
    * @param x    Int corresponding to the type of maintenance
    * @param y    Odometer value
    */
   public void addMaintEntry(int x, String y) {
      maintEntries[x] = y;
   }
   
   /** 
    * This returns the maintenance entry
    * 
    * @param   x            Int corresponding to the type of maintenance
    * @return  Double       Odometer value for next maintenance
    */
   public Double getMaintEntry(int x) {      
      if(maintEntries[x] != null){
         return Double.valueOf(maintEntries[x]);
      }
      else {
       return 0.0;
      }
   }
   
   /** 
    * This returns the mileage.
    * 
    * @return  Double   Latest mileage entry.
    */
   public Double getMileage() {
      return this.lastMileage;
   }
   
   /** 
    * This returns the vehicle name.
    * 
    * @return  String   The vehicle name.
    */
   public String getName() {
      return this.name;
   }
   
   /** 
    * This sets the vehicle description.
    * 
    * @param s    The vehicle description.
    */
   public void setDescription(String s) {
      this.description = s;
   }
   
   /** 
    * This returns the vehicle description.
    * 
    * @return  String   The vehicle description.
    */
   public String getDescription() {
      return this.description;
   }
   
   /** 
    * This returns the date the vehicle was entered into the app.
    * 
    * @return  Calendar    Creation date.
    */
   public Calendar getDate(){
      return creationDate;
   }
   
   public int[] getNexts() {
      return nextOdo;
   }
   
   public void setNexts(int i, int n) {
      nextOdo[i] = n;
   }
   
   /** 
    * This returns the vehicle as a string, but really just the title.
    * 
    * @return  String   The vehicle's title.
    */
   public String toString(){
      return name;
   }
}