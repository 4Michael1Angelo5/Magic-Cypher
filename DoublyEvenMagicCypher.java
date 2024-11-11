import java.util.ArrayList;
import java.util.Map;

public class DoublyEvenMagicCypher extends MagicCypher {

    // order of the square
    protected int order;

    // map (key = indexOfCharInMessage, value = charInMesage) each map can be
    // thought of as a cell
    protected ArrayList<Map<Integer, String>> charMapList = new ArrayList<>();

    // The magic Square containing the maps ("cells") of message
    protected ArrayList<ArrayList<Map<Integer, String>>> magicSquare = new ArrayList<>();

    DoublyEvenMagicCypher(int order,
            ArrayList<ArrayList<Map<Integer, String>>> square,
            ArrayList<Map<Integer, String>> charMapList) {
        this.order = order;
        this.magicSquare = square;
        this.charMapList = charMapList; 
    }

    
    protected String generateCypher() {
        // responsible for handling encryption logic 
        
        // step 1 build the square from the char map list
        buildSquare();
         
        // step 2)  read each row line by line and cocatenate the result
        String cipherdText = readSquare(magicSquare); // inherited from parent 

        // steo 2)  return the ciphered text
        return cipherdText;
    }


    // pattern: 

    //  N = 4                   N = 8                            N = 12

    //  X 0 0 X             X X 0 0 0 0 X X               X X X 0 0 0 0 0 0 X X X 
    //  0 X X 0             X X 0 0 0 0 X X               X X X 0 0 0 0 0 0 X X X 
    //  X 0 0 X             0 0 X X X X 0 0               X X X 0 0 0 0 0 0 X X X 
    //                      0 0 X X X X 0 0               0 0 0 X X X X X X 0 0 0 
    //                      0 0 X X X X 0 0               0 0 0 X X X X X X 0 0 0 
    //                      X X 0 0 0 0 X X               0 0 0 X X X X X X 0 0 0
    //                      X X 0 0 0 0 X X               0 0 0 X X X X X X 0 0 0 
    //                                                    0 0 0 X X X X X X 0 0 0 
    //                                                    0 0 0 X X X X X X 0 0 0  
    //                                                    X X X 0 0 0 0 0 0 X X X
    //                                                    X X X 0 0 0 0 0 0 X X X
    //                                                    X X X 0 0 0 0 0 0 X X X  

    // approach:

    // create a left counter going from 1 to N^2;
    // create a right counter going from N^2 to 1; 

    // start at the first row and first column.
    // go left to right through each column of every row of the matrix
    // with every cell that you explore add one to left counter and substract one from right counter
    // if it is an X fill it with right counter, otherwise fill it with left counter 

    // size of outer corner squares is N/4   
    // we can create boolean staements to check if row i and column j fall with-in these parameters 
    // and if they do we know that we need to fill it with rightCounter. 
    // otherwise we need to fill with left counter. 


    
    // this method uses builds magic squares doubly even magic squares
    protected void buildSquare() {
 

        int N = this.order;

        int countLeft = 0;
        int countRight = N * N - 1;

        for (int i = 0; i < N; i++) {

            for (int j = 0; j < N; j++) {

                // get the key (char index) from  the charMapList using leftCounter's value 
                int indexLeft = charMapList.get(countLeft).keySet().iterator().next(); 
                
                // get the char value to the corresponding key of indexLeft
                String charLeft = charMapList.get(countRight).entrySet().iterator().next().getValue();

                
                // get the key (char index) from  the charMapList using rightCounter's value 
                int indexRight = charMapList.get(countRight).keySet().iterator().next(); 
                
                // get the char value to the corresponding key of indexRight
                String charRight = charMapList.get(countRight).entrySet().iterator().next().getValue();




                if (i < N / 4 && j < N / 4) {

                    // upper left squares
                    
                    // get the key (char index) from  the charMapList using rightCounter's value 
                    // int charIndex = charMapList.get(countRight).keySet().iterator().next();
                    

                    // String charInMessage = charMapList.get(countRight).entrySet().iterator().next().getValue();

                    // magicSquare.get(i).get(j).put(charIndex, charInMessage);
                    magicSquare.get(i).get(j).put(indexRight, charRight);

                } else if (i < N / 4 && j >= N - N / 4) {
                    // upper right square
                    // square[i][j] = countRight;


                    // int charIndex = charMapList.get(countRight).keySet().iterator().next();

                    // String charInMessage = charMapList.get(countRight).entrySet().iterator().next().getValue();

                    // magicSquare.get(i).get(j).put(charIndex, charInMessage);

                    magicSquare.get(i).get(j).put(indexRight, charRight);

                } else if (i >= N / 4 && i < N - N / 4 && j >= N / 4 && j < N - N / 4) {
                    // middle 4 squares
                    // square[i][j] = countRight;

                    // int charIndex = charMapList.get(countRight).keySet().iterator().next();

                    // String charInMessage = charMapList.get(countRight).entrySet().iterator().next().getValue();

                    // magicSquare.get(i).get(j).put(charIndex, charInMessage);

                    magicSquare.get(i).get(j).put(indexRight, charRight);

                } else if (i >= N - N / 4 && j < N / 4) {
                    // lower left squares
                    // square[i][j] = countRight;

                    // int charIndex = charMapList.get(countRight).keySet().iterator().next();

                    // String charInMessage = charMapList.get(countRight).entrySet().iterator().next().getValue();

                    // magicSquare.get(i).get(j).put(charIndex, charInMessage);

                    magicSquare.get(i).get(j).put(indexRight, charRight);

                } else if (i >= N - N / 4 && j >= N - N / 4) {
                    // lower right squares
                    // square[i][j] = countRight;

                    // int charIndex = charMapList.get(countRight).keySet().iterator().next();

                    // String charInMessage = charMapList.get(countRight).entrySet().iterator().next().getValue();

                    // magicSquare.get(i).get(j).put(charIndex, charInMessage);

                    magicSquare.get(i).get(j).put(indexRight, charRight);

                } else {
                    // fill it with left counter 
                    // square[i][j] = countLeft;
                    
                    // get the key (char index) from  the charMapList using leftCounter's value 
                    // int charIndex = charMapList.get(countLeft).keySet().iterator().next();
                    
                    // // get the char value to the corresponding key
                    // String charInMessage = charMapList.get(countLeft).entrySet().iterator().next().getValue();

                    // magicSquare.get(i).get(j).put(charIndex, charInMessage);

                    magicSquare.get(i).get(j).put(indexLeft, charLeft);

                }
                // increment our counters 
                countLeft++;
                countRight--;

            }
        }
    }

}
