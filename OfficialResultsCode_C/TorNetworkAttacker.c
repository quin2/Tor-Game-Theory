#include <stdio.h>
#include <stdlib.h> 
#include <time.h> 
#include <math.h>

#define NODES 6500
#define EXIT 13			//percentage of nodes that are exit eligible 
#define MALICIOUS 50 	//percentage of nodes that are malicious
#define REPEATS 1000000 //number of times to repeat experiment
#define BANDWIDTHTH 60  //lower bandwidth threshhold
#define ASC 5 			//percentage of nodes controlled autonomously
#define NASC 6 			//number of autonomous systems

struct node
{
	int status;
	int bandwidth;
};

struct node network[NODES]; //work array 

//generates random # within range
int rand2(int max)
{
   return rand() / (RAND_MAX / (max + 1) + 1);
}

//init node status and assign bandwidth to it
void initNodes()
{ 
	for(int i = 0; i < NODES; i++)
	{
		network[i].status = 0;
		network[i].bandwidth = 0;

		int seed = rand2(100);
		if(seed <= EXIT)
		{
			seed = rand2(100);
			if(seed <= MALICIOUS)
			{
				network[i].status = 2;
				network[i].bandwidth = rand2(70) + 30;
			}
			else
			{
				network[i].status = 1;
				network[i].bandwidth = rand2(70);
			}
		} 
	}
}

int asCatch()
{
	for(int i = 0; i < NASC; i++)
	{
		for(int j = 0; j < 2; j++)
		{
			int beginning = rand2(100);
			int end = rand2(100);

			if(end == beginning)
			{
				j--;
				continue;
			}

			if(end <= ASC && beginning <= ASC) return 1;
		}
	}

	return 0;
}

double selectNodes()
{
	double caught = 0.0;

	for(int i = 0; i < REPEATS; i++)
	{
		int entryChoice = rand2(NODES); //need to find threadsafe uniform rand2 function for C
		int exitChoice = rand2(NODES);

		int backwardsEntryChoice = rand2(NODES);
		int backwardsExitChoice = rand2(NODES);

		if(entryChoice == exitChoice || backwardsEntryChoice == backwardsExitChoice)
		{
			i--;
			continue;
		}

		if(network[exitChoice].bandwidth < BANDWIDTHTH || network[backwardsExitChoice].bandwidth < BANDWIDTHTH)
		{
			i--;
			continue;
		}

		if(network[exitChoice].status < 1 || network[backwardsExitChoice].status < 1) i--;

		int AS = asCatch();

		if((network[entryChoice].status == 2 && network[exitChoice].status == 2) || 
		   (network[backwardsEntryChoice].status == 2 && network[backwardsExitChoice].status == 2) || AS)
		{
			caught += 1.0;
		}
	}

	double caughtProbability = 100.0 * caught / (double)REPEATS;
	return caughtProbability;
}

int main(int argc, char* argv[])
{
	srand(time(NULL)); //seed RNG 

	initNodes();
	double result = selectNodes();

	printf("Attacker success rate is %lf\n", result);
}