#ifndef __SENSOR__UTY__H
#define __SENSOR_UTY__H
#include <pololu/3pi.h> 

extern char dir;

void autoCalibration();
void processSensors(int* sensors);
#endif
