public class Node {

    private int ExitYorN;
    private int Bandwidth;
    private int AttackerOwnedYorN;

    public int Exitpool = 0;
    public int Attackerpool = 0;

    public Node(){

        if(Exitpool<=500) {
            ExitYorN = (int)((Math.random() * 2) + 0);
            if(ExitYorN == 1) {
                Exitpool++;
            }
        }
        else{
            ExitYorN = 0;
        }
        if(Attackerpool<=AttackerOwnedYorN){
            AttackerOwnedYorN = (int)((Math.random() * 2) + 0);
            if(AttackerOwnedYorN == 1){
                Attackerpool++;
                Exitpool++;
            }
        }
        else{
            AttackerOwnedYorN = 0;
        }

        Bandwidth = (int)((Math.random() * 81) + 20);

        this.ExitYorN = getExitYorN();
        this.Bandwidth = getBandwidth();
        this.AttackerOwnedYorN = getAttackerOwnedYorN();

    }

    public int getExitYorN(){ return ExitYorN; }
    public int getBandwidth(){ return Bandwidth ;}
    public int getAttackerOwnedYorN(){ return AttackerOwnedYorN; }

}
