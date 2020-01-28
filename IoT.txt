/* Define pins */
int LEDs = D1;
int Flap = D2;          // when HIGH = closed
int Sensor = A2;
/* Define variables */
int iReferenceValue;
int iFlap;
bool bPostReceived = false;
String(sERROR);
String (sTimeReceived);
int iSetupMeasure = 0;

void setup() {
Time.zone(+1);    
/* Set In- and Outputs */  
    pinMode(LEDs, OUTPUT);
    pinMode(Sensor, INPUT);
    pinMode(Flap, INPUT);
/* Set variables for the cloud  */
    Particle.variable("PostInBox", bPostReceived);
    Particle.variable("FlapStatus", sERROR);
    Particle.variable("ReceivedAt",sTimeReceived);
    //Particle.variable("Value",iReferenceValue);
    
/* Setup measure to calibrate the post box*/
    digitalWrite(LEDs, HIGH);
    for (int i=0;i<4;i++)                                           //measure 4 times
    {
        delay(5000); 
        int iMeasure = analogRead(Sensor);                         // measures the reference value
        iSetupMeasure = iSetupMeasure + iMeasure;  
    }
    digitalWrite(LEDs, LOW);
    iSetupMeasure = (iSetupMeasure/4)-300;                          // 300 is the gap value between empty and full post box                    
}

void loop() {
    
    iFlap = digitalRead(Flap);                                              //if closed, returns 1
    if (iFlap == 0)
     {
        delay(3000);
        iFlap = digitalRead(Flap);
        if (iFlap == 0)
            {
                sERROR = "Flap is open";   
                bPostReceived = true;
                sTimeReceived = Time.timeStr(Time.now());                   // Save Time as Wed May 21 01:08:47 2014 in the String
            }
        else
            {
                sERROR = "Flap is closed";
                digitalWrite(LEDs, HIGH);                             
                delay(5000); 
                iReferenceValue = analogRead(Sensor);                       // measures the reference value
                if (iReferenceValue < iSetupMeasure)                        // over iSetupMeasure (2900)  is when nothing is in the box
                    {
                        bPostReceived = true;                               // updates the vriable, if post is revceived
                        sTimeReceived = Time.timeStr(Time.local());           // Save Time as Wed May 21 01:08:47 2014 in the String
                    }
                else
                    {
                        bPostReceived = false;
                    }
                digitalWrite(LEDs, LOW);
            }
     }
}




