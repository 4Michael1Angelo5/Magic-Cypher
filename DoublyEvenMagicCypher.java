import java.util.ArrayList; 
import java.util.Map;

// DoublyEvenMagicCypher uses an algorithm to produce doubly even order magic squares.
// A doubly even number is number divisible by 4 but not by 2.
// A square is represented by an arraylist of arraylist's.
// in Each cell of the 2d ArrayList is a map. 
// The map contains a key integer representing the index of the char in the message
// and the value of that char. 
// To produce the cypher, OddMagicCypher reads each row of the square 
// and concatenates the each string contained with-in the cell map 

public class DoublyEvenMagicCypher extends MagicCypher {

    // order of the square
    protected int order;

    // map (key = indexOfCharInMessage, value = charInMesage) each map can be
    // thought of as a cell
    protected ArrayList<Map<Integer, String>> charMapList = new ArrayList<>();

    // The magic Square containing the maps ("cells") of message
    protected ArrayList<ArrayList<Map<Integer, String>>> magicSquare = new ArrayList<>();

    // Encryption Constructor
    DoublyEvenMagicCypher(int order,
            ArrayList<ArrayList<Map<Integer, String>>> square,
            ArrayList<Map<Integer, String>> charMapList) {
        this.order = order;
        this.magicSquare = square;
        this.charMapList = charMapList; 
    }
    
    // encryption main method
    protected String generateCypher() {
        // responsible for handling encryption logic 
        
        // step 1 build the square from the char map list
        buildSquare();
        
        // step 2 transpose for enhanced obfusication 
        transpose();
         
        // step 3)  read each row line by line and concatenate the result
        String cipherdText = readSquare(magicSquare); // inherited from parent 

        // steo 4)  return the ciphered text
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

    // create an ascending counter going from 1 to N^2;
    // create a descending counter going from N^2 to 1; 

    // start at the first row and first column.
    // go left to right through each column of every row of the matrix
    // with every cell that you explore add one to left counter and substract one from right counter
    // if it is an X fill it with right counter, otherwise fill it with left counter 

    // size of outer corner squares is N/4   
    // we can create boolean statements to check if row i and column j fall with-in these parameters 
    // and if they do we know that we need to fill it with rightCounter. 
    // otherwise we need to fill with left counter. 

    // ********************* Encryption ******************************
    
    //step 1)
    protected void buildSquare() {
        // this method builds a doubly even magic square
        // and places the char map in each cell
        
        // short hand 
        int N = this.order;

        int ascendingCount = 0;   
        int descendingCount = N * N - 1;

        for (int i = 0; i < N; i++) {

            for (int j = 0; j < N; j++) {

                // get the key (char index) from  the charMapList using ascendingCount's value 
                int indexLeft = charMapList.get(ascendingCount).keySet().iterator().next(); 
                
                // get the char value to the corresponding key of indexLeft
                String charLeft = charMapList.get(ascendingCount).entrySet().iterator().next().getValue();
                
                // get the key (char index) from  the charMapList using rightCounter's value 
                int indexRight = charMapList.get(descendingCount).keySet().iterator().next(); 
                
                // get the char value to the corresponding key of indexRight
                String charRight = charMapList.get(descendingCount).entrySet().iterator().next().getValue();

                if (i < N / 4 && j < N / 4) {

                    // upper left squares
                    
                    // get the key (char index) from  the charMapList using rightCounter's value 
                    // int charIndex = charMapList.get(countRight).keySet().iterator().next();
                  
                    magicSquare.get(i).get(j).put(indexRight, charRight);

                } else if (i < N / 4 && j >= N - N / 4) {
                    // upper right square
                    // square[i][j] = countRight;

                    magicSquare.get(i).get(j).put(indexRight, charRight);

                } else if (i >= N / 4 && i < N - N / 4 && j >= N / 4 && j < N - N / 4) {
                    // middle 4 squares
                    // square[i][j] = countRight;

                    magicSquare.get(i).get(j).put(indexRight, charRight);

                } else if (i >= N - N / 4 && j < N / 4) {
                    // lower left squares
                    // square[i][j] = countRight;

                    magicSquare.get(i).get(j).put(indexRight, charRight);

                } else if (i >= N - N / 4 && j >= N - N / 4) {
                    // lower right squares
                    // square[i][j] = countRight;

                    magicSquare.get(i).get(j).put(indexRight, charRight);

                } else {
                    // fill it with left counter 
                    // square[i][j] = countLeft;
                    
                    magicSquare.get(i).get(j).put(indexLeft, charLeft);

                }
                // increment our counters 
                ascendingCount++;
                descendingCount--;

            }
        }
    }
    
    //step2
    //transpose matrix 
    protected void transpose(){
        // interchange row and columns
        // method adds increased obfusication to ciphered text
        // while maintaining magic properties      
        ArrayList< ArrayList <Map<Integer,String>>> tempSquare = new ArrayList<>();
         
        for(int j = 0 ; j < order; j++){
            ArrayList<Map<Integer,String>> column = new ArrayList<>();
            for(int i = 0 ; i < order ; i++){
                
                Map<Integer,String> cell = magicSquare.get(i).get(j);
                
                column.add(cell);
            }
            tempSquare.add(column);
        } 

        this.magicSquare = tempSquare;
    }

    // ********************* Decryption ******************************

       // Decryption Constructor
    DoublyEvenMagicCypher(ArrayList<ArrayList<Map<Integer, String>>> square){
        this.magicSquare = square;
        this.order = square.size(); 
    }


    //decryption main method
    protected String decyptMessage(ArrayList<ArrayList<Map<Integer, String>>> square){

        //step 1) transpose the matrix (interchange row and columns)
        transpose(); 
        
        //step 2) traverse the square using the same encrypion logic
        String decryptedMessage = traverseSquare();
        
        return decryptedMessage;
    }
    
    protected String traverseSquare(){
        // traverse square traverses the square in the same pattern
        // used for encryption but gets the character stored in each cell
        // and places the character in it's corresponding index into an array
        // then itterates over the array concatenating each character to form
        // a decrypted message. 
        
        // it depends on the magicSquare field which get ininitlized in the decyption constructor. 
  

        int N = this.order;
        int descendingCount = N*N-1;
        int ascendingCount = 0; 
        
        // create an array to store the characters
        String[] decryptedMesssageArray = new String[N*N];

        // initilize a decryptedMesssage string for us to concatenate 
        StringBuilder decryptedMesssage = new StringBuilder();
        // String decryptedMesssage="";

        //flag to determin which rule was used
        boolean useDescendingCount = true;  

        for (int i = 0; i < N; i++) {

            for (int j = 0; j < N; j++) {

                // get the cell         
                Map<Integer,String> cell = magicSquare.get(i).get(j);

                // get the char in the cell
                String charInCell = cell.entrySet().iterator().next().getValue();
                // System.out.println(charInCell);
                
                if (i < N / 4 && j < N / 4) {
                    // upper left squares   
                    useDescendingCount = true;      
                     

                } else if (i < N / 4 && j >= N - N / 4) {
                    // upper right square
                    useDescendingCount = true; 
 

                } else if (i >= N / 4 && i < N - N / 4 && j >= N / 4 && j < N - N / 4) {
                    // middle 4 squares
                    // square[i][j] = countRight;
                    useDescendingCount = true;         


                } else if (i >= N - N / 4 && j < N / 4) {                    
                    // lower left squares
                    useDescendingCount = true; 
 

                } else if (i >= N - N / 4 && j >= N - N / 4) {
                    // lower right squares 
                    useDescendingCount = true; 
 

                } else {
                    // fill it with acending counter 
                    // square[i][j] = countLeft;
                    useDescendingCount = false; 
             
                }
                
                // determine if we used acending or descending counter
                // and fill decrypted message array with that char
                // in the corresponding position from the counter.
                if(useDescendingCount==true){
                    
                    decryptedMesssageArray[descendingCount] = charInCell;
                }else{
                    
                    decryptedMesssageArray[ascendingCount] = charInCell;
                }
                
                descendingCount--;
                ascendingCount++;

            }

        } 
        
        // itterate over the decryptedMessage array and concatenate the final result
        // to form a decrypted message
        for(int i = 0 ; i< N*N ; i ++ ){
            // decryptedMesssage+=decryptedMesssageArray[i];
            decryptedMesssage.append(decryptedMesssageArray[i]);
        }

        return decryptedMesssage.toString();
    }
}
