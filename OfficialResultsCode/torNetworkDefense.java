public class torNetworkDefense {

    /** Current State of Tor Network [Stage 1]; ideal attacker strategy - Regarding ASes: We operate on the Assumption (cited in the Paper) that 30% of autonomous systems are controlled by 6 AS
     * providers. Assuming a uniform distribution among these; that translates into each controlling ~5% of nodes. Also, these providers control
     * more bandwidth; henceforth we assume Tor gives them a degree of preferential treatment.
     *
     * 1. Separate pools for entry / exit nodes (~3% gain)
     * 2. First and Last Bridge outside of Tor cannot be from the same AS // ~2.7% additional gain along with (3), assumed a joint probabilistic way
     * 3. Asymmetric Routing of ASes eliminated / reduced // ~2.7% additional gain along with (2), assumed a joint probabilistic way
     * 4. Bandwidth requirement eliminated // ~4.5% gain in security
     * We Assume asymmetric routing within Tor - can we tackle this?
     */
    public static void main(String[] args) {
        int numberOfNodes = 6500;
        double exitNodes = 13;                   //this is a percentage value, i.e. exitNodes=5 means 5% of the nodes are exit eligible
        double maliciousExitNodes = 50;          //this is a percentage value, i.e. maliciousExitNodes=50 means 50% of exitNodes are malicious
        double maliciousNonExitNodes = 3;       //this is a percentage value, i.e. maliciousNonExitNodes=50 means 50% of nonExitNodes are malicious
        double repeats = 1000000;                //how many times we want to repeat the experiment
        double bandwidthThreshold = 1;           //Bandwidth threshold (from 1-100), here eliminated due to improvements
        double autonomousSystemsControl = 5;     //percent of 'external nodes' autonomous systems control (6 ASes control ~30%, assume uniform distr.; ~5% each)
        int numberOfASes = 6;                    // number of autonomous systems
        nodeSelector(numberOfNodes, exitNodes, maliciousExitNodes, maliciousNonExitNodes, repeats, bandwidthThreshold, autonomousSystemsControl, numberOfASes); //all nodes are within the Tor System
    }

    /**returns an array of Nodes with benevolent, malicious and exit nodes
     * 0 = benevolent node, 1 = exit node & benevolent node, 2 = malicious node & exit node, 3 = non-exit & malicious nodes
     * also assigns
     * These are nodes within the Tor System only
     */
    public static int[] createNodes(int numberOfNodes, double exitNodes, double maliciousExitNodes, double maliciousNonExitNodes){
        int[] nodes = new int[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) nodes[i]=0;      // sets all nodes to be "benevolent" & non exit

        for (int i = 0; i < numberOfNodes ; i++) {             // randomly selects exit eligible and benevolent nodes
            int probability = StdRandom.uniform(100);
            if(probability<=exitNodes && nodes[i]!=1 && nodes[i]!=2) nodes[i]=1;
        }

        for (int i = 0; i < numberOfNodes; i++) {            // out of the exit eligible nodes selects a certain percentage to be malicious
            int probability = StdRandom.uniform(100);
            if(probability<=maliciousExitNodes && nodes[i]==1) nodes[i]=2;
        }

        for (int i = 0; i < numberOfNodes; i++) {            // out of the exit eligible nodes selects a certain percentage to be malicious
            int probability = StdRandom.uniform(100);
            if(probability<=maliciousNonExitNodes && nodes[i]==0) nodes[i]=3;
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
    public static void nodeSelector(int numberOfNodes, double exitNodes, double maliciousExitNodes, double maliciousNonExitNodes, double repeats, double bandwidthThreshold, double autonomousSystemsControl, int numberOfASes){
        int[] nodes = createNodes(numberOfNodes, exitNodes, maliciousExitNodes, maliciousNonExitNodes);
        int[] bandwidthOfNode = assignBandwidthToNodes(nodes, numberOfNodes);
        double caught = 0;

        for (int i = 0; i < repeats; i++) {
            int entryChoice = StdRandom.uniform(6500);                 //from individual to destination
            int exitChoice = StdRandom.uniform(6500);

            int backwardsEntryChoice = StdRandom.uniform(6500);                 //from destination to individual, backwards
            int backwardsExitChoice = StdRandom.uniform(6500);

            if (entryChoice == exitChoice || backwardsExitChoice == backwardsEntryChoice
                    || nodes[entryChoice]==2 || nodes[entryChoice]==1 ||
                    nodes[exitChoice]==0 || nodes[exitChoice]==3 || nodes[backwardsEntryChoice]==2 || nodes[backwardsEntryChoice]==1 ||
                    nodes[backwardsExitChoice]==0 || nodes[backwardsExitChoice]==3) {
                i--;
               // StdOut.println("Repeating"+i);
                continue;

            }

            if(bandwidthOfNode[exitChoice]<bandwidthThreshold || bandwidthOfNode[backwardsExitChoice]<bandwidthThreshold) {     //bandwidth condition, only applied to exit node
                i--;
                continue;
            }

           // if(nodes[exitChoice]<1 || nodes[backwardsExitChoice]<1) i--;                        //exit node condition (must be exit eligible)

            boolean autonomousCatch = autonomousSystemCatch(autonomousSystemsControl, numberOfASes);
            if((nodes[entryChoice]==3 && nodes[exitChoice]==2) || (nodes[backwardsEntryChoice] == 3 && nodes[backwardsExitChoice] == 2) ||
                    autonomousCatch) caught++;        // if both entry & exit are malicious
           // StdOut.println("Iteration" );
        }

        double probabilityOfGettingCaught = 100*caught/repeats;
        StdOut.print("The attacker succeeds " + probabilityOfGettingCaught + " % of the time within the Tor System.");
    }

    /**
     * Autonomous Systems; we return a boolean if ASes catch us (true); and false if they do not
     */
    public static boolean autonomousSystemCatch(double autonomousSystemsControl, int numberOfASes){
        for (int i = 0; i < numberOfASes; i++) {                             //repeat for each AS
            for (int j = 0; j < 2; j++) {                                   //send in both directions
                int beginning = StdRandom.uniform(100);
                int end = StdRandom.uniform(100);
                int weGetAway = StdRandom.uniform(100);
                if(weGetAway>80) return false;                        // represents steps 2, 3 in "improvement" probabilistically, notice that one should think of the complement probabilities here
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

