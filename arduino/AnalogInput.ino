int sensorPinL = A0;    
int sensorValueL = 0;

int sensorPinR = A1;    
int sensorValueR = 0;

int sensorPinU = A1;    
int sensorValueU = 33;
  
void setup() {
  Serial.begin(9600);
}

void loop() {
  // read the value from the sensor:
  delay(30);
  sensorValueL = analogRead(sensorPinL) / 4;
  sensorValueR = analogRead(sensorPinR) / 4;

  Serial.print(sensorValueL);
  Serial.print(",");
  Serial.print(sensorValueR);
  Serial.print(",");
  Serial.print(sensorValueU);
  Serial.print("\n");
}
