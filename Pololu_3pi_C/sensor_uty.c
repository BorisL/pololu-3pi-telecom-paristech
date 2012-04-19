#include "sensor_uty.h"




// Auto-calibration: turn right and left while calibrating the 
// sensors.
void autoCalibration() 
{
  int counter; 
  for(counter=0;counter<80;counter++) 
    { 
      if(counter < 20 || counter >= 60) 
	set_motors(40,-40); 
      else 
	set_motors(-40,40); 
  
      // This function records a set of sensor readings and keeps 
      // track of the minimum and maximum values encountered.  The 
      // IR_EMITTERS_ON argument means that the IR LEDs will be 
      // turned on during the reading, which is usually what you 
      // want. 
      calibrate_line_sensors(IR_EMITTERS_ON); 
  
      // Since our counter runs to 80, the total delay will be 
      // 80*20 = 1600 ms. 
      delay_ms(20); 
    } 
  set_motors(0,0); 
} 

void processSensors(int* sensors)
{
if(dir == 's')
	{
	  if(sensors[0] < 500 &&
	     sensors[1] < 500 && 
	     sensors[2] > 700 && 
	     sensors[3] < 500 && 
	     sensors[4] < 500)
	    {set_motors(25,25);} //go straight
	  else 
	    {
	      if(sensors[0] < 500 &&
		 sensors[1] > 700 && 
		 sensors[2] < 500 && 
		 sensors[3] < 500 && 
		 sensors[4] < 500)
		{}
	    }
	  if(sensors[1] > 700 && sensors[2] > 700 && sensors[3] > 700)
		{set_motors(0,0); dir = ' ';}
	}
}
