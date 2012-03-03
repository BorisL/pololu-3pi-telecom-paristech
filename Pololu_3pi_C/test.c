// The 3pi include file must be at the beginning of any program that
// uses the Pololu AVR library and 3pi.
#include <pololu/3pi.h>
 
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
int main()
{
  print("Maze explorer ~ v1");
  delay_ms(1000);
  lcd_scroll(LCD_LEFT,18,400);
	
  clear();
  print("Ready...");

  while(1)
    {
	  
      delay_ms(1000);

    }

  return 0;
}
