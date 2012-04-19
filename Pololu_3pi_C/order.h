#ifndef __ORDER__H
#define __ORDER__H

extern char receive_buffer[32]; 
extern unsigned char receive_buffer_position; 
void process(char* id, char* from, char* to, char* type, char* body, char* state, char* errno);
void readOrder();
void check_for_new_bytes_received();

#endif
