// The 3pi include file must be at the beginning of any program that 
// uses the Pololu AVR library and 3pi. 
#include <pololu/3pi.h> 
#include "order.h" 
#include "sensor_uty.h"
 
char receive_buffer[32]; 
unsigned char receive_buffer_position = 0; 
char send_buffer[32]; 
char dir; // s = straight; l = left; r = right; 
 
// Data for generating the characters used in load_custom_characters 
// and display_readings.  By reading levels[] starting at various 
// offsets, we can generate all of the 7 extra characters needed for a 
// bargraph.  This is also stored in program space. 
const char levels[] PROGMEM = { 
	0b00000, 
	0b00000, 
	0b00000, 
	0b00000, 
	0b00000, 
	0b00000, 
	0b00000, 
	0b11111, 
	0b11111, 
	0b11111, 
	0b11111, 
	0b11111, 
	0b11111, 
	0b11111 
}; 
 
// This function loads custom characters into the LCD.  Up to 8 
// characters can be loaded; we use them for 7 levels of a bar graph. 
void load_custom_characters() 
{ 
	lcd_load_custom_character(levels+0,0); // no offset, e.g. one bar 
	lcd_load_custom_character(levels+1,1); // two bars 
	lcd_load_custom_character(levels+2,2); // etc... 
	lcd_load_custom_character(levels+3,3); 
	lcd_load_custom_character(levels+4,4); 
	lcd_load_custom_character(levels+5,5); 
	lcd_load_custom_character(levels+6,6); 
	clear(); // the LCD must be cleared for the characters to take effect 
} 

// This function displays the sensor readings using a bar graph. 
void display_readings(const unsigned int *calibrated_values) 
{ 
	unsigned char i; 
 
	for(i=0;i<5;i++) { 
		// Initialize the array of characters that we will use for the 
		// graph.  Using the space, an extra copy of the one-bar 
		// character, and character 255 (a full black box), we get 10 
		// characters in the array. 
		const char display_characters[10] = {' ',0,0,1,2,3,4,5,6,255}; 
 
		// The variable c will have values from 0 to 9, since 
		// calibrated values are in the range of 0 to 1000, and 
		// 1000/101 is 9 with integer math. 
		char c = display_characters[calibrated_values[i]/101]; 
 
		// Display the bar graph character. 
		print_character(c); 
	} 
} 


// Initializes the 3pi, displays a welcome message, calibrates, and 
// plays the initial music. 
void initialize() 
{ 
   
  // This must be called at the beginning of 3pi code, to set up the 
  // sensors.  We use a value of 2000 for the timeout, which 
  // corresponds to 2000*0.4 us = 0.8 ms on our 20 MHz processor. 
  pololu_3pi_init(2000); 
  load_custom_characters(); // load the custom characters 
	 
  autoCalibration(); 
 
} 
 
int main() 
{ 
   
  print("Maze explorer"); 
  delay_ms(500); 
   
  unsigned int sensors[5]; // an array to hold sensor values 
  dir = ' '; 
 
  // Set the baud rate to 9600 bits per second.  Each byte takes ten bit 
  // times, so you can get at most 960 bytes per second at this speed. 
  serial_set_baud_rate(9600); 
 
  // Start receiving bytes in the ring buffer. 
  serial_receive_ring(receive_buffer, sizeof(receive_buffer)); 
 
 
  clear(); 
  print("Press B"); 
   
  while(1) 
    { 
      serial_check(); 
 
      readOrder(); 
 
      // read sensors 
      unsigned int position = read_line(sensors,IR_EMITTERS_ON); 
      display_readings(sensors); 
      
      delay_ms(100); 
      clear(); 
       
      processSensors(sensors);
      
      if (button_is_pressed(MIDDLE_BUTTON)) 
	{ 
		 
	  wait_for_button_release(MIDDLE_BUTTON); 
	  initialize(); 
	  clear(); 
	  print("Ready..."); 
	  dir = 's';
	} 
    } 
 
  return 0; 
} 
