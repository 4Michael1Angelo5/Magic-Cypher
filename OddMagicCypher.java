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

    // message we are trying to encrypt
    protected String message;
    
    // order of the square
    protected int order;

    // map (key = indexOfCharInMessage, value = charInMesage) each map can be thought of as a cell
    protected ArrayList<Map<Integer, String>> charMapList = new ArrayList<>();
    
    // The magic Square containing the maps ("cells")
    protected ArrayList<ArrayList<Map<Integer, String>>> square = new ArrayList<>();

    
    // constructor
    OddMagicCypher( int order, 
                    ArrayList<Map<Integer, String>> charMapList,
                    ArrayList<ArrayList<Map<Integer, String>>> square)
        {

        this.order = order;
        this.charMapList = charMapList;
        this.square = square;

    }


    protected String generateCypher(){
        // this is the main method responsible for generating the cypher
        // it reads a magic square row by row and concatenates the string values to form
        // an encrypted message

        
        // helper method to build our magic square
        // it updates the square field with the string char's 
        // mapped out in the same pattern as a magic square
        // ie the first letter of the string goes where the number 1 would go
        // the second letter of the string goes where the number 2 would go etc...
        buildSquare();

        ArrayList<Map<Integer,String>> row = new ArrayList<>();
        String cipheredText = "";
        String nextChar;

        for(int i = 0 ; i < order; i ++){

            //row of matrix
            row = square.get(i);

            for(int j = 0 ; j < order; j++){
                
                // get the first key value of the cell in the column of row i 
                nextChar = row.get(j).entrySet().iterator().next().getValue();

                // concatenate the message
                cipheredText+= nextChar;

            }
        }
            // return the ciphered text
            return cipheredText;
    }
     
    public void printSquare(){
        // print the square

        System.out.println(square.size());
        
        for(int i = 0 ; i < order ; i++){
            // print out each row of the square 
                System.out.println(square.get(i));
        
        }
    }

    protected ArrayList<ArrayList<Map<Integer, String>>> buildSquare() {
        // this method makes the magic square 
        // filled with the chars and index's of the chars in the message
        
        // ie a 3x3 magic square is order 3, a 5x5 is order 5
        int N = order; 
        int middleColumn = (int) Math.floor(N / 2);
        
        // row (i) & coloumn (j) size
        int i = 0; // i starts in the first row
        int j = middleColumn; // j starts in the middle column
        int indexOfChar = 0; // index of characters in the message

        // initilize row variable
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

        // System.out.println("inside odd magic Cypher's buildSquare method");

        System.out.println("\n");
        
        // for testing purposes to see how the square looks
        printSquare();

        System.out.println("\n");
        
        // inherited from parent

        return square;

    }
}
