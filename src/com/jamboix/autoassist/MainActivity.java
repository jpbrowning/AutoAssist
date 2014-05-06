/* I certify that this assignment was completed individually, with no outside assistance.  
 * I further acknowledge that plagiarism and/or unauthorized collaboration directly conflicts 
 * with the values expected of future Naval Officers, and is a violation of my personal integrity.  
 * I understand that any suspected honor violations will be investigated and 
 * processed in accordance with USNA Instruction 1610.3H.
 */

/** MainActivity.java
 * 
 * Author:      James Browning, m140726@usna.edu
 * Date:        1 May 2014
 * Description: This class loads the main view, and has listeners for the buttons. It
 *              allows users to open their camera from the app if they choose.
 */

package com.jamboix.autoassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);      
   }
   
   /** 
    * This method starts VehicleList.java when the correct button is pressed.
    */
   public void vehicleButton(View v) {
      Intent intent = new Intent(getBaseContext(), VehicleList.class);
      startActivity(intent);
   }
}