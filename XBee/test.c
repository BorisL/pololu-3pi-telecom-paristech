// The 3pi include file must be at the beginning of any program that
// uses the Pololu AVR library and 3pi.
#include <pololu/3pi.h>
 
char receive_buffer[32];
unsigned char receive_buffer_position = 0;
char send_buffer[32];
char dir;
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

void process(char* id, char* from, char* to, char* type, char* body, char* state, char* errno)
{
  if(strcmp(type,"TEXT") == 0)
    {
      clear();
      print(body);   
      delay_ms(500);
    }
  if(strcmp(type,"MUSIC") == 0)
    {
      play(body);
      while(is_playing()){}
    }

  if(strcmp(type,"GOSTRAIGHT") == 0)
    {
      print("GO STRAIGHT");
      dir='s';
    }

  if(strcmp(type,"TURNLEFT") == 0)
    {
      print("TURN LEFT");
      dir = 'l';
    }

  if(strcmp(type,"TURNRIGHT") == 0)
    {
      print("TURN RIGHT");
      dir = 'r';
    }

  char rep [500];
  strcat(rep,id);strcat(rep,";");
  strcat(rep,from);strcat(rep,";");
  strcat(rep,to);strcat(rep,";");
  strcat(rep,"ACK");strcat(rep,";");
  strcat(rep,body);strcat(rep,";");
  strcat(rep,state);strcat(rep,";");
  strcat(rep,errno);strcat(rep,".");
  serial_send_blocking(rep,strlen(rep));
  memset(rep,0,500);
      
}
char id[50];
char from[50];
char to[50];
char type[50];
char body [500];
char state [50];
char errno [50];
char* ptr = id;
int order_index = 0;
int index =0;

void check_for_new_bytes_received()
{

  while(serial_get_received_bytes() != receive_buffer_position)
    {

      // Process the new byte that has just been received.
      //process_received_byte(receive_buffer[receive_buffer_position]);
     
       
      ptr[index]=receive_buffer[receive_buffer_position];
      index ++;

      if(receive_buffer[receive_buffer_position]==';')
	{// next argument
	  ptr[index-1]='\0';
	  order_index++;
	  index = 0;

	  switch(order_index)
	    {
	    case 0 : ptr = id; break;
	    case 1 : ptr = from; break;
	    case 2 : ptr = to; break;
	    case 3 : ptr = type; break;
	    case 4 : ptr = body; break;
	    case 5 : ptr = state; break;
	    case 6 : ptr = errno; break;
	    default : order_index = 0; break;
	    }

	}
      if(receive_buffer[receive_buffer_position]=='.')
	{// order totaly received
	  ptr[index-1]='\0';
	  
	  /*int tempo = 500;
	  clear();
	  int(id);delay_ms(tempo);clear();
	    print(from);delay_ms(tempo);clear();
	    print(to);delay_ms(tempo);clear();
	    print(type);delay_ms(tempo);clear();
	    print(body);delay_ms(tempo);clear();
	    print(state);delay_ms(tempo);clear();
	    print(errno);delay_ms(tempo);clear();*/
	  order_index=0;
	  ptr = id;
	  index = 0;
	  //process order
	  process(id, from, to, type, body, state, errno);

	  memset(id,0,50);
	  memset(from,0,50);
	  memset(to,0,50);
	  memset(type,0,50);
	  memset(body,0,500);
	  memset(state,0,50);
	  memset(errno,0,50);

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
}

char evaluateDirection(int* sensors)
{

  if(sensors[0] > 300  && sensors[4] > 300)
    {
    dir = ' ';
    return 's';
    }
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

processSensors(int* sensors)
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
	      if(sensors[2] > 800)
		dir = 's';
	    }
	   
	}
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
  while(1)
    {
      serial_check();
      if(ready)
	{
	  readOrder();
      // read sensors 
      unsigned int position = read_line(sensors,IR_EMITTERS_ON); 
      /*display_readings(sensors);
      delay_ms(100);
      clear(); */
      processSensors(sensors);
      
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
