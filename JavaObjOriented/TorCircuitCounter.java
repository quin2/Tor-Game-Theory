public class TorCircuitCounter {

    private int Symmetry = 2;

    GameTheoryTor tor = new GameTheoryTor();

    private Node TorCircuit[] = new Node[3];
    private int AttackerVisibleYorN;
    private int RemainAnonymousYorN;




    public TorCircuitCounter(){
        int ExitSuccess = 0;
        for(int k = 0; k<3; k++) {
            TorCircuit[k] = tor.getNetwork((int)(Math.random()*6500));
        }
        for(int q = 0; q<Symmetry; q++){
            while(ExitSuccess == 0){

                if(TorCircuit[2].getExitYorN() == 1){
                    ExitSuccess = 1;
                }
                else{
                    continue;
                }

            }
        }


        if(TorCircuit[0].getAttackerOwnedYorN() == TorCircuit[2].getAttackerOwnedYorN()){
            AttackerVisibleYorN = 1;
        }
        RemainAnonymousYorN = Math.abs(AttackerVisibleYorN - 1);
        this.AttackerVisibleYorN = getAttackerVisibleYorN();
        this.RemainAnonymousYorN = getRemainAnonymousYorN();

    }

    public int getAttackerVisibleYorN(){ return AttackerVisibleYorN; }
    public int getRemainAnonymousYorN(){ return RemainAnonymousYorN; }
    //public Node getTorCircuit(){ return TorCircuit[] }


}
