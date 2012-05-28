// The 3pi include file must be at the beginning of any program that
// uses the Pololu AVR library and 3pi.
#include <pololu/3pi.h>
 
char receive_buffer[500];
unsigned char receive_buffer_position = 0;
char send_buffer[32];
char ptr[500];
int is_order =0;
char message[500];
char order[50];

int order_index = 0;
int index =0;
int index_ptr = 0;
char dir;
char nextDir;
char state;
/*
  state = f => follow line
  state = i => intersection
 */
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
      // track of the minimum and maximum values encountered. The
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

void process(char* order,char* body)
{
  if(order != 0)
    {
      clear();
      print(order);
      delay_ms(1000);
    }
  
  if(strcmp(order,"TEXT") == 0)
    {
      clear();
      print(body);   
      delay_ms(500);
    }
  if(strcmp(order,"MUSIC") == 0)
    {
      play(body);
      while(is_playing()){}
    }

  if(strcmp(order,"GOSTRAIGHT") == 0)
    {
      print("GO STRAIGHT");
      dir='s';
      serial_send_blocking(message,strlen(message));
    }

  if(strcmp(order,"TURNLEFT") == 0)
    {
      print("TURN LEFT");
      dir = 'l';
    }

  if(strcmp(order,"TURNRIGHT") == 0)
    {
      print("TURN RIGHT");
      dir = 'r';
    }

  memset(order,0,50);

}

char from[50];
char to[50];
char order[50];
int start;

void check_for_new_bytes_received()
{

  while(serial_get_received_bytes() != receive_buffer_position)
    {
      char car;
      // Process the new byte that has just been received.
      car = receive_buffer[receive_buffer_position];
      ptr[index_ptr]=car;
      message[index]=car;
      
      index ++;
      index_ptr++;
      
      if(car == '\"')
	{
	  if(start)
	    {// new parameter
	      memset(ptr,0,50);
	      start = 0;
	      index_ptr = 0;
	    }
	  else
	    {// parameter finish
	      ptr[index_ptr-1]='\0';
	      start = 1;
	      if(is_order == 1)
		{
		  strcpy(order,ptr);
		  is_order = 0;
		}
	      if(strcmp(ptr,"order")==0 && is_order == 0)
		is_order = 1;

	    }
	}
	
      
      
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

void readOrder()
{
  
  check_for_new_bytes_received();
  process(order,0);
}

char* evaluateIntersection(int* sensors)
{
  char found[3];
  found[0]=0; //found left
  found[2]=0; //found right
  found[1]=0; //found straight
  
  if(sensors[0] > 150)
    found[0] = 1;
    
  if(sensors[4] > 150)
    found[2] = 1;

  if(sensors[1] > 150 || sensors[2] > 150 || sensors[3] > 150)
    found[1] = 1;

  return found;
}

void followDirection(int* sensors, int position)
{
  if(sensors[1] > 400 && sensors[2] < 200)
    set_motors(20,30);
  if(sensors[2] < 200 && sensors[3] > 400)
    set_motors(30,20);
  if(sensors[2] > 400)
    set_motors(30,30);
}

void deadEnd(int* sensors, int position)
{
  set_motors(30,-30);
  if(sensors[2] > 500 || sensors[1] > 500 || sensors[3] > 500)
    state = 'f';
}

void turnLeft(int* sensors, int position)
{
  set_motors(30,-30);
  if(sensors[2] > 500 || sensors[1] > 500 || sensors[3] > 500)
    state = 'f';
  
}

void turnRight(int* sensors, int position)
{
  set_motors(-30,30);
  if(sensors[2] > 500 || sensors[1] > 500 || sensors[3] > 500)
    state = 'f';
}

int isDeadEnd(int* sensors, int position)
{
  return sensors[1] < 100 && sensors[2] < 100 && sensors[3] < 100 && sensors[0] < 100 && sensors[4] < 100;
}

int isIntersection(int* sensors, int position)
{
  if(state == 'd')
    return 0;
  if(sensors[0] > 200 || sensors[4] > 200)
    return 1;
  if(sensors[1] > 500 && sensors[2] > 500 && sensors[3] > 500)
    return 1;
  return 0;
}

void processSensors(int* sensors, int position)
{
  char* found;
  
  if(isIntersection(sensors,position)&& state == 'f')
    {
      set_motors(25,25);
      delay_ms(100);
      set_motors(0,0);
    }
  else if(isIntersection(sensors,position))
    {
      found = evaluateIntersection(sensors); 
      if(found[0] && !found[1] && !found[2])
	{
	  // only one possible direction : left 
	  state = 'l';
	  return;
	}
      if(!found[0] && !found[1] && found[2])
	{
	  // only one possible direction : right
	  state = 'r';
	  return;
	}
      // multiple direction possible
      // wait order from user
      state = 's';
    }
  else if(isDeadEnd(sensors,position))
    {
      state = 'd';
    }
  if(state == 'd')
    deadEnd(sensors,position);
  if(state == 'f')
    followDirection(sensors,position); // there is a path straight
  if(state == 's')
    set_motors(0,0);
  if(state == 'r')
    turnRight(sensors,position);
  if(state == 'l')
    turnLeft(sensors,position);
}


int main()
{
  
  print("Maze explorer");
  delay_ms(500);
  

  // Set the baud rate to 9600 bits per second. Each byte takes ten bit
  // times, so you can get at most 960 bytes per second at this speed.
  serial_set_baud_rate(9600);

  // Start receiving bytes in the ring buffer.
  serial_receive_ring(receive_buffer, sizeof(receive_buffer));

  unsigned int sensors[5];
  clear();
  print("Press B");
  int ready = 0;
  dir = ' ';
  nextDir = ' ';
  state = 'f';
  start = 1;
  while(1)
    {
      display_readings(sensors);
      delay_ms(100);
      clear();
      serial_check();
      if(ready)
	{
	  display_readings(sensors);
	  delay_ms(100);
	  clear();
	  readOrder();
	  // read sensors 
	  unsigned int position = read_line(sensors,IR_EMITTERS_ON); 
	  
	  //processSensors(sensors,position);
	  
	  if(button_is_pressed(MIDDLE_BUTTON))
	    {
	      wait_for_button_release(MIDDLE_BUTTON);
	      dir = 'g';
	    }
	  if(button_is_pressed(BUTTON_A))
	    {
	      wait_for_button_release(BUTTON_A);
	      dir = 'r';
	    }
	  if(button_is_pressed(BUTTON_C))
	    {
	      wait_for_button_release(BUTTON_C);
	      dir = 'l';
	    }
	  
	}
      if (button_is_pressed(MIDDLE_BUTTON))
	{
	  
	  wait_for_button_release(MIDDLE_BUTTON);
	  
	  pololu_3pi_init(2000); 
	  load_custom_characters(); // load the custom characters 
	  autoCalibration();
	  clear();
	  print("Ready...");
	  ready = 1;
	  //dir = 's';
	}
    }

  return 0;
}
