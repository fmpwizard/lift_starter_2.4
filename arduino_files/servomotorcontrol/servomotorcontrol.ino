/**
  * Based on http://shazsterblog.blogspot.com/2011/11/arduino-google-voice-activated-servo.html
  * We control this servo motor from Lift
  */

#include <Servo.h>
#define MAX 150
#define MIN 0

int greenLedPin = 2;
int redLedPin = 3;
int servoPin = 9;
int command = 0;
boolean open = false;

Servo doorLock;

void setup() {
 Serial.begin(9600);
 doorLock.attach(9);
 pinMode(greenLedPin, OUTPUT);
 pinMode(redLedPin, OUTPUT);
 digitalWrite(greenLedPin, LOW);
 digitalWrite(redLedPin, HIGH);
}

void loop() {
 if(Serial.available() >= 2) {
  if(Serial.read() == 0xff) {
    command = Serial.read();
      if(command == 1) {
        if(!open) {
         doorLock.write(MAX); 
         digitalWrite(redLedPin, LOW);
         digitalWrite(greenLedPin, HIGH);
         open = true;
        }
      } else if(command == 2) {
        if(open) {
         doorLock.write(MIN);
         digitalWrite(greenLedPin, LOW);
         digitalWrite(redLedPin, HIGH);
         open = false;
      } 
     }     
  }  
 }  
 delay(10);
}

