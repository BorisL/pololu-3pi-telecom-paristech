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

char evaluateDirection(int* sensors)
{

  if(sensors[0] > 300  && sensors[4] > 300)
    dir = ' ';
    return 's';
  if(
     sensors[2] > 300  
     )
    return 'g'; // go
  if(     sensors[1] > 300  
     )
    return 'l'; // turn left
  if(
     sensors[3] > 300  
     )
    return 'r'; // turn right
  if(
     sensors[0] > 400  
     )
    {
      dir = 'l';
      return 'L';
    }
    
     if(
     sensors[4] > 400  
     )
       {
	 dir = 'r';
	 return 'R';
       }
     
  return 's'; // stop
}

void processSensors(int* sensors)
{
if(dir != ' ')
	{
	  if(dir == 's')
	    {
	  char choice = evaluateDirection(sensors);
	  switch(choice)
	    {
	    case 'g': set_motors(25,25); break;
	    case 'l': set_motors(10,25); break;
	    case 'r': set_motors(25,10); break;
	    case 'R': set_motors(25,-25); break;
	    case 'L': set_motors(-25,25); break;
	    case 's': set_motors(0,0); break;
	    }
	    }
	  else
	    {
	      if(dir == 'l')
		set_motors(-25,25);
	      if(dir == 'r')
		set_motors(25,-25);
	      if(sensors[2] > 300)
		dir = 's';
	    }
	   
	}
}
