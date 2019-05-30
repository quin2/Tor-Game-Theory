///////////////////////////////////////////////////////////////////////////////////////////////
//Original Preliminary Code. Results do not reflect those reported, merely a proof of consept//
//////////////////////////////////////////////////////////////////////////////////////////////


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
int AttackerOwned = 500;
int LowerBool = 0;
int UpperBool = 1;
long HackerWins = 0;
long RemainAnonymous = 0;

//This is the lite version of the Tor Game Theory Simulation

int selected[3];

int TorNetworkNodes[6500];

int Random(int lower, int upper){
  int num = (rand() % (upper - lower + 1) + lower);
  return num;
}

void TorRun(){
  srand(time(0));
  int j = 0;
  for (int i = 0; i<TorNetworkSize; i++){
    TorNetworkNodes[i] = Random(LowerBool, UpperBool);
    if(TorNetworkNodes[i] == 1){
      j++;
      if(j>= AttackerOwned){
	for(int k = i+1; k<TorNetworkSize; k++){
	  TorNetworkNodes[k] = 0;
	  break;
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
  else{
    //printf("The attackers were unable to own your entry adn exit nodes, YOU REMAIN ANONYMOUS\n");
    RemainAnonymous+=1;
  }
  return;
}

int main(){
  for(int p = 0; p<100; p++){
    for(int q = 0; q<10000; q++){
      TorRun();
    }
  }
  printf("How many time the hacker owned both your entry and exit nodes:\n %lu\n", HackerWins);
  printf("The number of times that you successfully remained ANONYMOUS:\n %lu\n", RemainAnonymous);
  return 0;
}
