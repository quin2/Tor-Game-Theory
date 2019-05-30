import java.awt.*;
public class GameTheoryTor{


    private  Node[] Network = new Node[6500];

    //public Node[] getNetwork(){ return Network;}

    public static void main(String[] args){
        //Node Network[] = new Node[6500];
        new GameTheoryTor().run();
    }


   public void run(){

        //Node Network[] = new Node[6500];


        Node TorCircuit[] = new Node[3];
        for(int j = 0; j<3; j++){
            TorCircuit[j] = Network[(int)(Math.random()*6501)];
        }
        TorCircuitCounter Simulation[] = new TorCircuitCounter[1000000];
        int AttackerWins = 0;
        int DefenderWins = 0;
        for (int p = 0; p<1000000; p++){
            if(Simulation[p].getAttackerVisibleYorN() == 1){
                AttackerWins++;
            }
            else DefenderWins++;
        }


        System.out.println("Attacker Won out of 1,000,000:");
        System.out.print(AttackerWins);
        System.out.println("Defender won out of 1,000,000");
        System.out.print(DefenderWins);
    }

    public Node getNetwork(int i) {
        return Network[i];
    }

//    public Node getNetwork[]{return Network;}

}
