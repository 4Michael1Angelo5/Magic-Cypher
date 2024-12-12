import java.util.ArrayList;
import java.util.Map;

// OddMagicCypher uses an algorithm to produce odd order magic squares
// A square is represented by an arraylist of arraylist's.
// in Each cell of the 2d ArrayList is a map. 
// The map contains a key integer representing the index of the char in the message
// and the value of that char. 
// To produce the cypher, OddMagicCypher reads each row of the square 
// and concatenates the each string contained with-in the cell map 

public class OddMagicCypher extends MagicCypher {

    // order of the square
    protected int order;

    // map (key = indexOfCharInMessage, value = charInMesage) each map can be thought of as a cell
    protected ArrayList<Map<Integer, String>> charMapList = new ArrayList<>();
    
    // The magic Square containing the maps ("cells")
    protected ArrayList<ArrayList<Map<Integer, String>>> square = new ArrayList<>();

    // *************************field for decryption *******************
    protected boolean[][] cellOccupancy; // determine cell occupancy of the matrix

    
    // constructor
    OddMagicCypher( int order,                                          // order of square
                    ArrayList<Map<Integer, String>> charMapList,        // char map list
                    ArrayList<ArrayList<Map<Integer, String>>> square)  // empty matrix
        {

        this.order = order;
        this.charMapList = charMapList;
        this.square = square;

    }
    
    //encryption main method
    protected String generateCypher(){
        // this is the main method responsible for generating the cypher
        // it reads a magic square row by row and concatenates the string values to form
        // an encrypted message

        // step 1)
        buildSquare();
        
        // step 2) 
        String cipheredText = readSquare(square);

        // step 3)  return the ciphered text
        return cipheredText;
    }
      
    //algorithm to produce odd magic squares
    protected ArrayList<ArrayList<Map<Integer, String>>> buildSquare() {
        // this method makes the magic square 
        // filled with char maps
        
        // short hand 
        int N = order; 
        int middleColumn = (int) Math.floor(N / 2);
        
        // row (i) & coloumn (j) size
        int i = 0; // i starts in the first row
        int j = middleColumn; // j starts in the middle column
        int indexOfChar = 0; // index of characters in the message

        // initialize row variable
        ArrayList<Map<Integer, String>> row = new ArrayList<>();

        while (indexOfChar < N * N) {
            
            // reassign row variable with each itteration
            row = square.get(i);
            
            // map 
            // key is index of char in message we're trying to encrypt
            // value is the string char   
            Map<Integer, String> cell = charMapList.get(indexOfChar);
            
            // put cell in column j of row i 
            row.set(j, cell);

            if (i - 1 < 0 && j + 1 < N) {

                // rule 1
                // if incrementing up and to the right results in
                // row being out of bound and column is still in bounds
                // let row = bottom row

                i = N - 1;
                j++;

            } else if (i - 1 >= 0 && j + 1 >= N) {
                // rule 2
                // if incremenitng up and the right results in column being out of bounds
                // but row is still in bounds, let column = the first column ;

                j = 0;
                i--;

            } else if (i - 1 < 0 && j + 1 >= N) {
                // rule 3
                // if incrementing up and to the right results
                // in both i and j being out of bounds, go down one row
                // in the same column

                i++;

                // note that Rule 3 must go before rule 4 because it catches
                // rule 4 from creating error out of bounds 

            } else
            // row => column=> cell=> <indexOfChar,message.charAt(i)>
            if (!square.get(i - 1).get(j + 1).isEmpty() && i + 1 < N) {
                // check what's inside the next row above and next column over
                // if it's not empty, then its occupied

                // // rule 4
                // // cell is occupied go down one row

                i++;
            } else {
                // No cases match increment going up and to the right
                i--;
                j++;
            }
            
            // move to next index of char in message
            indexOfChar++;

        }

        return square;

    }

    // ======================================================================================================
    // ********************************************Decryption************************************************

    // constructor for decryption
    OddMagicCypher(int order, ArrayList<ArrayList<Map<Integer, String>>> square){
        this.order = order;     // order of square 
        this.square = square;   // square filled with encrypted message
        this.cellOccupancy = new boolean[order][order]; // 2d matrix of booleans to determine occupancy of cells
    }
    
    //decryption main method   
    protected String decryptMessage(ArrayList<ArrayList<Map<Integer,String>>> square){
        // step 1) 
        // traverse the square using the same algorithm used for encryption
        // concatenate the cell values
        String decryptedMessage = traverseSquare(square);

        // step 2)
        // return the final decrypted message 
        return decryptedMessage;
    }

    //@TODO: consider refactoring the logic. traverseSquare method is essentially the same as the buildSquare method
    // maybe consolidate the code so that one method is used for both encryption and decryption.
    // one possibility is to have buildSquare or traverseSquare return [row, column] pairs instead of having seperate 
    // methods that essentially do the same thing. 
    
    // this method traverse's a square in the pattern of building a magic square
    // it travels each cell in the square and concatenates cell's string value
    // and returns a decrypted message;
    protected String traverseSquare(ArrayList<ArrayList<Map<Integer,String>>> square){

        // ie a 3x3 magic square is order 3, a 5x5 is order 5
        int N = square.size(); 
        int middleColumn = (int) Math.floor(N / 2);

        // row (i) & coloumn (j) size
        int i = 0; // i starts in the first row
        int j = middleColumn; // j starts in the middle column
        int indexOfChar = 0; // index of characters in the message
        
        //more effecient then String concatenation
        StringBuilder decryptedMessage = new StringBuilder();

        while (indexOfChar < N * N) {
            
            // reassign row variable with each itteration
            ArrayList<Map<Integer,String>> row = square.get(i);
            
            // map 
            // key is index of char in encrypted message we're trying to decrypt
            // value is the char
            
            // get cell in column j of row i             
            Map<Integer, String> cell = row.get(j);
            
            // concatenate the message 
            decryptedMessage.append(cell.entrySet().iterator().next().getValue());
            
            //mark the cell as being occupied
            cellOccupancy[i][j] = true;

            if (i - 1 < 0 && j + 1 < N) {

                // rule 1
                // if incrementing up and to the right results in
                // row being out of bound and column is still in bounds
                // let row = bottom row

                i = N - 1;
                j++;

            } else if (i - 1 >= 0 && j + 1 >= N) {
                // rule 2
                // if incremenitng up and the right results in column being out of bounds
                // but row is still in bounds, let column = the first column ;

                j = 0;
                i--;

            } else if (i - 1 < 0 && j + 1 >= N) {
                // rule 3
                // if incrementing up and to the right results
                // in both i and j being out of bounds, go down one row
                // in the same column

                i++;

                // note that Rule 3 must go before rule 4 because it catches
                // rule 4 from creating error out of bounds 

            } else
            // row => column=> cell=> <indexOfChar,message.charAt(i)>
            if (cellOccupancy[i-1][j+1]==true && i + 1 < N) {
                // check what's inside the next row above and next column over
                // if it's not empty, then its occupied

                // // rule 4
                // // cell is occupied go down one row

                i++;
            } else {
                // No cases match increment going up and to the right
                i--;
                j++;
            }
            
            // move to next index of char in message
            indexOfChar++;

        }
 
        
        return decryptedMessage.toString();
    }
}
