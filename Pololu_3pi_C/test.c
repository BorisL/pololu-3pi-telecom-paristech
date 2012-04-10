// The 3pi include file must be at the beginning of any program that
// uses the Pololu AVR library and 3pi.
#include <pololu/3pi.h>
 
char receive_buffer[32];
unsigned char receive_buffer_position = 0;
char send_buffer[32];

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


void check_for_new_bytes_received()
{
	
	while(serial_get_received_bytes() != receive_buffer_position)
	{
		
		// Process the new byte that has just been received.
		//process_received_byte(receive_buffer[receive_buffer_position]);
	  //wait_for_sending_to_finish();
	  send_buffer[0] = 'b';
	  serial_send(send_buffer, 1);
	  clear();
	  print("ok");
	  delay_ms(1000);
	  clear();
	  print("Ready...");
		// Increment receive_buffer_position, but wrap around when it gets to
		// the end of the buffer. 
		if (receive_buffer_position == sizeof(receive_buffer)-1)
		{
			receive_buffer_position = 0;
		}
		else
		{
			receive_buffer_position++;
		}
	}
}

unsigned char UART_0 = '0';
int main()
{
  print("Maze explorer ~ v1");
  delay_ms(1000);
  lcd_scroll(LCD_LEFT,18,400);
	
  // Set the baud rate to 9600 bits per second.  Each byte takes ten bit
  // times, so you can get at most 960 bytes per second at this speed.
  serial_set_baud_rate(9600);

  // Start receiving bytes in the ring buffer.
  serial_receive_ring(receive_buffer, sizeof(receive_buffer));


  clear();
  print("Ready...");

  while(1)
    {
      serial_check();

      check_for_new_bytes_received();

      //delay_ms(1000);

    }

  return 0;
}
