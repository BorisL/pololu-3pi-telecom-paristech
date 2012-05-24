#include "order.h"

void process(char* id, char* from, char* to, char* type, char* body, char* state, char* errno)
{ 
  if(strcmp(type,"TEXT") == 0) 
    { 
      print(body); 
       
       
    } 
  if(strcmp(type,"MUSIC") == 0) 
    { 
      play(body); 
      // while(is_playing()){} 
    } 
 
  if(strcmp(type,"GOSTRAIGHT") == 0) 
    { 
      print("GO STRAIGHT"); 
      dir = 's';
    } 
 
  if(strcmp(type,"TURNLEFT") == 0) 
    { 
      print("TURN LEFT");  
      dir = 'L';
    } 
 
if(strcmp(type,"TURNRIGHT") == 0) 
    { 
      print("TURN RIGHT");  
      dir = 'R';
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


void readOrder() 
{ check_for_new_bytes_received(); } 

void check_for_new_bytes_received() 
{ 

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
  
  while(serial_get_received_bytes() != receive_buffer_position) 
    { 
      /*clear();
      print_long(receive_buffer_position);
      delay_ms(1000);
      clear();*/
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
	    case 3 : ptr = type;  break; 
	    case 4 : ptr = body;  break; 
	    case 5 : ptr = state;  break; 
	    case 6 : ptr = errno;  break;	 
	    default : order_index = 0; break; 
	    } 
 
	} 
      if(receive_buffer[receive_buffer_position]=='.') 
	{// order totaly received 
	  ptr[index-1]='\0'; 
	  /*clear(); 
	  int tempo = 1000; 
	  print(id);delay_ms(tempo);clear(); 
	  print(from);delay_ms(tempo);clear(); 
	  print(to);delay_ms(tempo);clear(); 
	  print(type);delay_ms(tempo);clear(); 
	  print(body);delay_ms(tempo);clear(); 
	  print(state);delay_ms(tempo);clear(); 
	  print(errno);delay_ms(tempo);clear(); */
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
	  print_long(receive_buffer_position);
	  delay_ms(1000);
	  clear();
	} 
    } print("EXITTTT");
   delay_ms(5000);
} 
