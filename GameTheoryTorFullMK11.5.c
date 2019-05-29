///////////////////////////////
/*Game Theory Tor Simulation*/
/////////////////////////////

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <time.h>
#include <omp.h>

#define TorNetworkSize 6500
#define CurcuitSize 3
#define AttackerOwnedNodes 500
#define ExitNodes 1000
#define ExitNodesClean 500
#define CleanNodes 5500
#define Symmetry 0
#define SimSize 1000000


typedef struct Node{
  int ExitYorN;
  int Bandwidth;
  int AttackerOwnedYorN;
}Network[TorNetworkSize];

//struct Node Network[6500];
//struct Node TorCurcuit[3];

typedef struct TorCurcuitCounter{
  struct Node TorCurcuit[CurcuitSize];
  int AttackerVisibleYorN;
  int RemainAnonymousYorN;
}Simulation[SimSize];
  
int LowerBool = 0;
int UpperBool = 1;
int BandMax = 100;

int Random(int lower, int upper){
  int random;
  random = (rand() % (upper - lower + 1) + lower);
  return random;
}


void CreateNetwork(){

  int BandMinRule = Random(20, BandMax);
  int ExitPool = 0;
  int AttackerPool = 0;
  for(int i = 0; i<TorNetworkSize; i++){
    if(ExitPool<=ExitNodesClean){
      Network[i].ExitYorN = Random(LowerBool, UpperBool);
    }
    else{
      Network[i].ExitYorN = 0;
    }
    if(AttackerPool<=AttackerOwnedNodes){
      Network[i].AttackerOwnedYorN = Random(LowerBool, UpperBool);
    }
    else{
      Network[i].AttackerOwnedYorN = 0;
    }
    if(Network[i].ExitYorN == 1){
      ExitPool++;
    }
    if(Network[i].AttackerOwnedYorN == 1){
      AttackerPool++;
      ExitPool++;
    }
    Network[i].Bandwidth = Random(BandMinRule, BandMax);
  }
  return;
}

void SelectTorCurcuit(){

  for(int j = 0; j<3; j++){
    TorCurcuit[j] = Network[Random(0, TorNetworkSize)];
  }
  return;
}

struct TorCurcuitCounter TorSelectionSim(struct TorCurcuitCounter Sim[], unsigned int NumofCurcuits){
  int ExitSuccess = 0;
  for(int q = 0; q<Symmetry; q++){
    while(ExitSuccess == 0){
      SelectTorCurcuit();
      if(TorCurcuit[2].ExitYorN == 1){
	ExitSuccess = 1;
      }
      else continue;
    }
  }
  if(TorCurcuit[0] == TorCurcuit[3]){
    TorCurcuitCounter[NumofCurcuits].AttackerVisibleYorN = 1;
  }
  TorCurcuitCounter[NumofCurcuits].RemainAnonymous = ~TorCurcuitCounter[NumofCurcuits].AttackerVisibleYorN;
  return TorCurcuitCounter[NumofCurcuits];
}


int main(){
  struct Node Network[6500];
  //int ExitSuccess = 0;
  //struct Node TorCurcuit[3];
  srand(time(NULL));
  CreateNetwork();
  struct TorCurcuitCounter Simulation[SimSize];
  for(unsigned int x = 0; x<SimSize; x++){
    Simulation[x] = TorSelectionSim(Simulation, x);
  }
  int AttackerWins = 0;
  int DefenderWins = 0;
  for(int p = 0; p<SimSize; p++){
    if(Simulation[p].AttackerVisibleYorN == 1){
      AttackerWins++;
    }
    else DefenderWins++;
  }

  printf("Results:\n");
  printf("The Attacker owned the entry and exit nodes %d times out of %d\n", AttackerWins, SimSize);
  printf("The defender remained Anonymous %d times out of %d\n", DefenderWins, SimSize);
  
  
  /*while(!ExitSuccess){
    SelectTorCurcuit();
    if(TorCurcuit[2].ExitYorN == 1){
      ExitSuccess = 1;
    }
  }*/
  
}
  
