#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <stdbool.h>

/*#define TorNetworkSize 6500;
#define AttackerOwned 500;
//#define RAND_MAX 1;
#define LowerBool 0;
#define UpperBool 1;*/

int TorNetworkSize = 6500;
int AttackerOwned = 1000;
int ExitClean = 500;
int NotExitClean = 5000;
int LowerBool = 0;
/*0 means not exit, not hacker; 2 means exit, no hacker; 1 means exit, hacker*/
int UpperBool = 2;
long HackerWins = 0;
long RemainAnonymous = 0;
long FailedtoConnect = 0;

//This is the lite version of the Tor Game Theory Simulation

int selected[3];

int TorNetworkNodes[6500];

int Random(int lower, int upper){
int num = (rand() % (upper - lower + 1) + lower);
return num;
}

void TorRun(){
srand(time(NULL));
int j = 0;  //ExitClean
int r = 0;  //AttackerOwned
for (int i = 0; i<TorNetworkSize; i++){
TorNetworkNodes[i] = Random(LowerBool, UpperBool);
if(TorNetworkNodes[i] == 2){
j++;
if(j>= ExitClean){
for(int k = i+1; k<TorNetworkSize; k++){
TorNetworkNodes[k] = Random(0, 1);
if(TorNetworkNodes[k] == 1){
r++;
if(r>= AttackerOwned){
for(int s = k+1; s<TorNetworkSize; s++){
TorNetworkNodes[s] = 0;
}

break;
}
}
break;

if(TorNetworkNodes[i] == 1){
r++;
if(r>= AttackerOwned){
for(int t = i+1; t<TorNetworkSize; t++){
TorNetworkNodes[t] = Random(0, 2);
if((TorNetworkNodes[t] == 1 && t&1==1) || TorNetworkNodes[t] == 2){
TorNetworkNodes[t] = 2;
j++;
if(j>= ExitClean){
for(int v = t+1; v<TorNetworkSize; v++){
TorNetworkNodes[v] = 0;
break;
}
}
}
}
}
}
}
}
}
}
for(int m = 0; m<3; m++){
int n = Random(LowerBool, TorNetworkSize);
selected[m] = TorNetworkNodes[n];
TorNetworkNodes[n] = 9;
}
//printf("The status of the three nodes used in your Tor configuration are\n %d\n %d\n %d\n", selected[0], selected[1], selected[2]);
if(selected[0] == 1 && selected [2] == 1){
//printf("An attacker owns both your entry and exit nodes, THIS IS BAD\n");
HackerWins+=1;
}
 else if(selected[2] == 2 && selected[0] != 1){
//printf("The attackers were unable to own your entry adn exit nodes, YOU REMAIN ANONYMOUS\n");
RemainAnonymous+=1;
}
 else{
FailedtoConnect+=1;
}
return;
}

int main(){
for(int p = 0; p<100; p++){
for(int q = 0; q<10000; q++){
TorRun();
}
}
printf("How many times the hacker owned both your entry and exit nodes (NO LONGER ANONYMOUS):\n %lu\n", HackerWins);
printf("The number of times that you successfully remained ANONYMOUS:\n %lu\n", RemainAnonymous);
printf("The number of times that a curcuit wasn't correctly created: \n %lu\n:", FailedtoConnect);
return 0;
}
