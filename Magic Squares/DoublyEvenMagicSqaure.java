public class DoublyEvenMagicSqaure extends MagicSquare{

    public static void main(String[] args) {
        
        DoublyEvenMagicSqaure evenSquare = new DoublyEvenMagicSqaure();
        //  constuctor 
        //  must set order 
        //  parameter to generateMagicSquare needs to match order
        // @TODO remove the need to do that
        
        evenSquare.setOrder(8);               //inherited from MagicSquare
        evenSquare.getMagicConstant();              //inherited from MagicSquare
        evenSquare.generateEvenSquare(8);    //specifc to EvenSquare
        evenSquare.printSquare();                   //inherited from MagicSquare
        evenSquare.isMagic();
        if(evenSquare.isMagic()){
            System.out.println("Conradulations!!! you made it here");
        }


    }


    public void generateEvenSquare(int order){

        int N = order;

        // left side counter going from 0 -> n^2 - 1
        int countLeft = 1; 
        
        // right side counter going from N^2-1 -> 0 
        int countRight = N*N;

        int[][] square = new int[N][N];

        // [X] [O] [O] [X]  ==> [i && j < N/4] [O]    [O] [i && j >= N-N/4 ]
        // [O] [X] [X] [O]
        // [O] [X] [X] [O]
        // [X] [O] [O] [X]
        
        for(int i = 0; i < N ; i++){

            for(int j = 0; j < N ; j++){

                
                if(i<N/4 && j< N/4){
                    // upper left squares

                    square[i][j] = countRight;

                }else
                if(i<N/4 && j>= N-N/4){
                    // upper right square
                    square[i][j] = countRight; 
                }else
                if(i >= N/4 && i <N-N/4 && j>= N/4 && j<N-N/4 ){
                    // middle 4 squares
                    square[i][j] = countRight; 

                }else
                if(i>=N-N/4 && j < N/4){
                    // lower left squares

                    square[i][j] = countRight;
                }else
                if(i>= N-N/4 && j>=N-N/4){
                    // lower right squares
                    square[i][j] = countRight;
                }else{

                    square[i][j] = countLeft;
                }

                countLeft++;
                countRight--;
                

            }
        }

        this.setSquare(square);      //inherited from MagicSquare class


    } 

    
}
