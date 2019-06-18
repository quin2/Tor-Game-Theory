public class torNetworkAttack {

    /** Current State of Tor Network [Stage 1]; ideal attacker strategy - Regarding ASes: We operate on the Assumption (cited in the Paper) that 30% of autonomous systems are controlled by 6 AS
     * providers. Assuming a uniform distribution among these; that translates into each controlling ~5% of nodes. Also, these providers control
     * more bandwidth; henceforth we assume Tor gives them a degree of preferential treatment.
     *
     * To Be Done with this: plot a graph regarding how attacker strategy becomes more successful a certain variables change
     * Apart from this; Code 'Defended' version
     */
    public static void main(String[] args) {
    	long startTime = System.nanoTime();
    	
        int numberOfNodes = 6500;
        double exitNodes = 13;                   //this is a percentage value, i.e. exitNodes=5 means 5% of the nodes are exit eligible
        double maliciousNodes = 50;              //this is a percentage value, i.e. maliciousNodes=50 means 50% of exitNodes are malicious
        double repeats = 1000000;                //how many times we want to repeat the experiment
        double bandwidthThreshold = 60;          //Bandwidth threshold below which exit nodes are deemed ineligible (Scale: from 1 to 100)
        double autonomousSystemsControl = 5;     //percent of 'external nodes' autonomous systems control (6 ASes control ~30%, assume uniform distr.; ~5% each)
        int numberOfASes = 6;                    // number of autonomous systems
        nodeSelector(numberOfNodes, exitNodes, maliciousNodes, repeats, bandwidthThreshold, autonomousSystemsControl, numberOfASes); //all nodes are within the Tor System
        
        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) + " ns"); 
    }

    /**returns an array of Nodes with benevolent, malicious and exit nodes
     * 0 = benevolent node, 1 = exit node & benevolent node, 2 = malicious node & exit node
     * also assigns
     * These are nodes within the Tor System only
     */
    public static int[] createNodes(int numberOfNodes, double exitNodes, double maliciousNodes){
        int[] nodes = new int[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) nodes[i]=0;      // sets all nodes to be "benevolent" & non exit

        for (int i = 0; i < numberOfNodes ; i++) {             // randomly selects exit eligible and benevolent nodes
            int probability = StdRandom.uniform(100);
            if(probability<=exitNodes && nodes[i]!=1 && nodes[i]!=2) nodes[i]=1;
        }

        for (int i = 0; i < numberOfNodes; i++) {            // out of the exit eligible nodes selects a certain percentage to be malicious
            int probability = StdRandom.uniform(100);
            if(probability<=maliciousNodes && nodes[i]==1) nodes[i]=2;
        }
        return nodes;
    }

    /**
     * Assigns a bandwidth value to all exit eligible nodes within the Tor Network
     */
    public static int[] assignBandwidthToNodes(int[] nodes, int numberOfNodes){
        int[] bandwidthOfNode = new int[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            if(nodes[i]==0) bandwidthOfNode[i]=0;                   // the bandwidth of non-exit eligible nodes is irrelevant for selection

            if(nodes[i]==1) bandwidthOfNode[i] = StdRandom.uniform(70); // non-malicious exit eligible nodes are uniformly distributed

            if(nodes[i]==2) bandwidthOfNode[i] = StdRandom.uniform(70) + 30; // the +30 is to show the preferences of attackers to occupy higher bandwidth nodes
        }
        return bandwidthOfNode;
    }

    /** Selects entry % exit nodes, and checks if both are malicious. Prints out how many times (of #of repeats) are both
     * selected nodes malicious, represents it as a percentage. It applies the bandwidth cutoff to Exit nodes; and also takes into account
     * ASes
     */
    public static void nodeSelector(int numberOfNodes, double exitNodes, double maliciousNodes, double repeats, double bandwidthThreshold, double autonomousSystemsControl, int numberOfASes){
        int[] nodes = createNodes(numberOfNodes, exitNodes, maliciousNodes);
        int[] bandwidthOfNode = assignBandwidthToNodes(nodes, numberOfNodes);
        double caught = 0;

        for (int i = 0; i < repeats; i++) {
            int entryChoice = StdRandom.uniform(6500);                 //from individual to destination
            int exitChoice = StdRandom.uniform(6500);

            int backwardsEntryChoice = StdRandom.uniform(6500);                 //from destination to individual, backwards
            int backwardsExitChoice = StdRandom.uniform(6500);

            if (entryChoice == exitChoice || backwardsExitChoice == backwardsEntryChoice) {
                i--;
                continue;
            }

            if(bandwidthOfNode[exitChoice]<bandwidthThreshold || bandwidthOfNode[backwardsExitChoice]<bandwidthThreshold) {     //bandwidth condition, only applied to exit node
                i--;
                continue;
            }

            if(nodes[exitChoice]<1 || nodes[backwardsExitChoice]<1) i--;                        //exit node condition (must be exit eligible)

            boolean autonomousCatch = autonomousSystemCatch(autonomousSystemsControl, numberOfASes);
            if((nodes[entryChoice]==2 && nodes[exitChoice]==2) || (nodes[backwardsEntryChoice] == 2 && nodes[backwardsExitChoice] == 2) || autonomousCatch) caught++;        // if both entry & exit are malicious
        }

        double probabilityOfGettingCaught = 100*caught/repeats;
        StdOut.println("The attacker succeeds " + probabilityOfGettingCaught + " % of the time within the Tor System.");
    }

    /**
    * Autonomous Systems; we return a boolean if ASes catch us (true); and false if they do not
    */
    public static boolean autonomousSystemCatch(double autonomousSystemsControl, int numberOfASes){
        for (int i = 0; i < numberOfASes; i++) {                             //repeat for each AS
            for (int j = 0; j < 2; j++) {                                   //send in both directions
                int beginning = StdRandom.uniform(100);
                int end = StdRandom.uniform(100);

                if(end==beginning){
                    j--;
                    continue;
                }

                if(end<=autonomousSystemsControl && beginning<=autonomousSystemsControl) return true;
            }
        }
        return false;
    }

}
