import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Encryption:

// SinglyEvenMagicCypher class produces an singly even magic square filled with
// char's from a message. It reads each row of the square and concatenates the chars
// to produce a ciphered text. The chars are mapped by their corresponding position
// in the string message. IE the number 1 corresponds to the first letter in the message
// and that letter would go where the number 1 would go in magic square. 

// Decryption:
// SinglyEvenMagicCypher also uses the same convention to decrypt messages. 

// algorithm we followed https://www.1728.org/magicsq3.htm

public class SinglyEvenMagicCypher extends MagicCypher {

    // order of the square
    protected int order;

    // map (key = indexOfCharInMessage, value = charInMesage) each map can be thought of as a cell
    protected ArrayList<Map<Integer, String>> charMapList = new ArrayList<>();

    // The magic Square containing the maps ("cells")
    protected ArrayList<ArrayList<Map<Integer, String>>> magicSquare = new ArrayList<>();

    // map of chars of the message broken into 4 subsections
    ArrayList<Map<Integer, String>> charForUpperLeftSquare = new ArrayList<>();
    ArrayList<Map<Integer, String>> charForUpperRightSquare = new ArrayList<>();
    ArrayList<Map<Integer, String>> charForLowerLeftSquare = new ArrayList<>();
    ArrayList<Map<Integer, String>> charForlowerRightSquare = new ArrayList<>();

    // 4 temporary odd order squares we will combine to make one single even square
    protected ArrayList<ArrayList<Map<Integer, String>>> upperleftSquare = new ArrayList<>();
    protected ArrayList<ArrayList<Map<Integer, String>>> upperRightSquare = new ArrayList<>();
    protected ArrayList<ArrayList<Map<Integer, String>>> lowerLeftSquare = new ArrayList<>();
    protected ArrayList<ArrayList<Map<Integer, String>>> lowerRightSquare = new ArrayList<>();

    // encryption constructor 
    SinglyEvenMagicCypher(int order,
            ArrayList<Map<Integer, String>> charMapList,
            ArrayList<ArrayList<Map<Integer, String>>> magicSquare) {

        // order of the square
        this.order = order;
        // char map containing the char from message and their index in the message
        this.charMapList = charMapList;
        // an empty magic square that we will fill
        this.magicSquare = magicSquare;
    }
    
    // encryption main method
    protected String generateCypher() {

        // step 1) break up the charMap List into 4 char map Lists
        splitListInto4();

        // step 2) create 4 new OddMagicSquare's out of the list.
        create4_OddMagicSquares();

        // step 3) combine all the odd magic squares
        // combineSquares();
        combineSquares2();

        // step 4) perform left side row column swap operations
        leftSideRowColumnSwapOperations();
        
        // step 5 perform right side row column swap operations
        rightSideRowColumnSwapOperations();

        // @TODO: can step 4 and 5 be combined to be more streamlined/ less redudant

        // step 6) now we need to read the rows line by line 
        // and out put the final cyphered text
        String cypheredText = readSquare(magicSquare);  //inherited from parent

        return cypheredText;
    }

    // ================================= Steps ====================================

    // step 1)
    protected void splitListInto4() {
        // split the charMapList into 4 differnt char map lists

        int size = charMapList.size() / 4; // or maybe order/4 ??

        // size is the square of a singly even number divded by 4:

        // 6^2/4 = 9, which is 3^2
        // 10^2/4 = 25, which is 5^2
        // 14^2/4 = 49, which is 7^2
        // 18^2/4 = 81, which is 9^2
        // 22^2/4 = 121, which is 11^2

        // split the length of the message into the square of an odd number
        // and create 4 new charMapList with stored subsection
        for (int i = 0; i < 4; i++) {
            for (int j = i * size; j < (i + 1) * size; j++) {
                // example:

                // when size = 9
                // for i = 0, j = (0:8);
                // for i = 1, j = (9:17)
                // for i = 2, j = (18,26)
                // for i = 3, j = (27:35

                // first itteration of i 
                // fill up with chars 0:8

                if (i == 0) {

                    charForUpperLeftSquare.add(charMapList.get(j));

                }

                // second itteration of i 
                // fill up with chars (9:17)
                if (i == 1) {

                    charForlowerRightSquare.add(charMapList.get(j));

                }

                // third itteration of i 
                // fill up upper right with chars (18,26)
                if (i == 2) {

                    charForUpperRightSquare.add(charMapList.get(j));

                }

                // fourth itteration of i 
                // fill up with chars j = (27:35)
                if (i == 3) {

                    charForLowerLeftSquare.add(charMapList.get(j));

                }
            }
        }

    }

    // step 2)
    // build four odd order magic squares from our char maps
    protected void create4_OddMagicSquares() {
        //NOTE: this method directly modifies state!

        // build upper left square

        upperleftSquare = createEmptyMatrix(order / 2);

        OddMagicCypher upperLeftOddMagicSquare = new OddMagicCypher(order / 2, charForUpperLeftSquare, upperleftSquare);

        upperleftSquare = upperLeftOddMagicSquare.buildSquare();

        // ==============================================================================================
        // build upper right square

        upperRightSquare = createEmptyMatrix(order / 2);

        OddMagicCypher upperRightOddMagicSquare = new OddMagicCypher(order / 2, charForUpperRightSquare,upperRightSquare);

        upperRightSquare = upperRightOddMagicSquare.buildSquare();

        // ===============================================================================================
        // build lower left square

        lowerLeftSquare = createEmptyMatrix(order / 2);

        OddMagicCypher lowerLeftMagicSquare = new OddMagicCypher(order / 2, charForLowerLeftSquare, lowerLeftSquare);

        lowerLeftSquare = lowerLeftMagicSquare.buildSquare();

        // ================================================================================================
        // build lower right square

        lowerRightSquare = createEmptyMatrix(order / 2);

        OddMagicCypher lowerRightMagicSquare = new OddMagicCypher(order / 2, charForlowerRightSquare, lowerRightSquare);

        lowerRightSquare = lowerRightMagicSquare.buildSquare();

        // NOTE:
        // Each call to this method creates a new instance of the parent class.
        // Be cautious of potential side effects here. 
        // Creating a new instance of the child class OddMagicCypher without first initializing 
        // the parent class can leave some of the parent's fields as null 
        // or lead to a scenario where the state of the application is not in sync
        // ie the children classes will have differnt states then their parent
    
    }

    // step 3)
    // combine all 4 odd magic squares
    private void combineSquares2() {

        ArrayList<ArrayList<Map<Integer, String>>> tempSquare = new ArrayList<>();

        for (int i = 0; i < order; i++) {

            // split the 6x6 square into 4 quadrants composed of 4 3X3 squares

            // if ( order/2 > i >= 0 )
            // fill from upper Square;

            // if ( order/2 > j >=0)
            // fill from left squares

            // if (order > i >= order/2 )
            // fill from lower square

            // if (order > order j>=order/2)
            // fill from right squares

            // create a new row for us to add to our magic square
            ArrayList<Map<Integer, String>> row = new ArrayList<>();

            for (int j = 0; j < order; j++) {

                // when i = 0 1 2 3 4 5
                // rowIndex = 0 1 2 0 1 2

                int rowIndex = i % (order / 2);
                // once were in the 2 quadrant (upper right square)
                // it will be between 3 and 5 we need to fill rows 0 through 2
                // of our odd magic square

                int columnIndex = j % (order / 2);

                if (i >= 0 && i < order / 2) {

                    // then we're in one of the upper two quadrants

                    if (j >= 0 && j < order / 2) {

                        // then were in the upper left quadrant

                        row.add(upperleftSquare.get(rowIndex).get(columnIndex));

                    } else {
                        // were in the upper right quadrant

                        row.add(upperRightSquare.get(rowIndex).get(columnIndex));

                    }
                } else if (i >= order / 2 && i < order) {
                    // then were in one of the lower two quadrants
                    if (j >= 0 && j < order / 2) {

                        // then were in the lower left quadrant

                        row.add(lowerLeftSquare.get(rowIndex).get(columnIndex));
                    } else {
                        // were in the lower right quadrant

                        row.add(lowerRightSquare.get(rowIndex).get(columnIndex));
                    }
                }

            }

            // add row to tempSquare
            tempSquare.add(row);

        }

        this.magicSquare = tempSquare;

    }

    // which one is better ?
    // combine the 4 differnt odd magic squares into one square following the
    // pattern

    private void combineSquares() {

        // A C
        // D B

        // where A is the numbers 1 throught n/4
        // where B is the number n/4+1 through n/2
        // where C is the numbers n/2+1 through 3n/4
        // and D is the numbers 3n/4+1 to n

        // Do not directly mutate state!
        ArrayList<ArrayList<Map<Integer, String>>> tempSquare = new ArrayList<>();

        for (int i = 0; i < order; i++) {

            int rowIndex = i % (order / 2);

            ArrayList<Map<Integer, String>> row = new ArrayList<>();

            if (i < order / 2) {
                // then were in the upper half of the square

                // fill all the values of the upperleftsquare
                for (int j = 0; j < order / 2; j++) {
                    row.add(upperleftSquare.get(i).get(j));
                }

                // fill all the values of upper right square
                for (int j = 0; j < order / 2; j++) {
                    row.add(upperRightSquare.get(i).get(j));
                }

            } else {
                // otherwise were in the lower half of the square

                // fill all the values with the lower left square
                for (int j = 0; j < order / 2; j++) {
                    row.add(lowerLeftSquare.get(rowIndex).get(j));
                }

                // fill all the values with the lower right square
                for (int j = 0; j < order / 2; j++) {
                    row.add(lowerRightSquare.get(rowIndex).get(j));
                }
            }

            tempSquare.add(row);

        }

        this.magicSquare = tempSquare;
    } 

    // Step 4)
    private void leftSideRowColumnSwapOperations() {

        // left side row swaping

        // example of row swaping :

                                // All the X's need to be swapped with their corresponding 
                                // lower half. ie item in row 0 column 0 needs to be swaped with
                                // row 0 column 0 of the lower half.  

        //  for n = 6                       for n = 10                  for n = 14
        
        // upper half of quare             upper half                  upper half
     
        //  X 0 0 0 0 0                 X X 0 0 0 0 0 0 0 0         X X X 0 0 0 0 0 0 0 0 0 0 0 
        //  0 X 0 0 0 0                 X X 0 0 0 0 0 0 0 0         X X X 0 0 0 0 0 0 0 0 0 0 0
        //  X 0 0 0 0 0                 0 X X 0 0 0 0 0 0 0         X X X 0 0 0 0 0 0 0 0 0 0 0
        //                              X X 0 0 0 0 0 0 0 0         0 X X X 0 0 0 0 0 0 0 0 0 0  <- shift over one column when i = n/4
        //                              X X 0 0 0 0 0 0 0 0         X X X 0 0 0 0 0 0 0 0 0 0 0
        //                                                          X X X 0 0 0 0 0 0 0 0 0 0 0
        //                                                          X X X 0 0 0 0 0 0 0 0 0 0 0
        // 
        // lower half of square             lower half                     lower half 
        // 
        //  X 0 0 0 0 0                 X X 0 0 0 0 0 0 0 0         X X X 0 0 0 0 0 0 0 0 0 0 0
        //  0 X 0 0 0 0                 X X 0 0 0 0 0 0 0 0         X X X 0 0 0 0 0 0 0 0 0 0 0
        //  X 0 0 0 0 0                 0 0 0 0 0 0 0 0 0 0         X X X 0 0 0 0 0 0 0 0 0 0 0 
        //                              X X 0 0 0 0 0 0 0 0         0 X X X 0 0 0 0 0 0 0 0 0 0  <- shift over one column when i = n/4
        //                              X X 0 0 0 0 0 0 0 0         X X X 0 0 0 0 0 0 0 0 0 0 0
        //                                                          X X X 0 0 0 0 0 0 0 0 0 0 0
        //                                                          X X X 0 0 0 0 0 0 0 0 0 0 0
        
        //pattern :
        //  f(6) = 1                          f(10) = 2                   f(14) = 3                        f(18) = 4 ; ...etc

        // the number of j columns we need to swap as a function of the order of the matrix
        // is f(N) = (N+2)/4  where N is a singly even number. 

        // and there is a special case, that once we've reached the n/4'th row we need to shift over one clolumn. 

        int N = order; 

        Map<Integer,String> upperCell = new HashMap<>();
        Map<Integer,String> lowerCell = new HashMap<>();

        for(int j = 0 ; j < (N+2)/4-1; j++){
            // columns

            for(int  i = 0 ; i < N/2 ;i++){
                // rows               

                if(i== (int) Math.floor(N/4) ){   //<----- i don't think this is neccessary
                // special case shift over one column

                // shift over one column when row is in the midle of
                // the upper left square

                // swap M[i][j+1] with M[i+N/2][j+1]

                upperCell = magicSquare.get(i).get(j+1);
                lowerCell = magicSquare.get(i+N/2).get(j+1);
                
                swap(upperCell, lowerCell);

                }else{
                // swap M[i][j] with M[i+N/2][j]

                upperCell = magicSquare.get(i).get(j);
                lowerCell = magicSquare.get(i+N/2).get(j);

                swap(upperCell, lowerCell);

                }

            }

        }

    }

    // step 5)
    private void rightSideRowColumnSwapOperations(){


        // right side row swaping

        // example of row swaping :

                                // All the X's need to be swapped with their corresponding 
                                // lower half. ie item in row 0 column 0 needs to be swaped with
                                // row 0 column 0 of the lower half. that is the i'th row gets swaped with i+n/2 row in the same column j 

        //  for n = 6                       for n = 10                  for n = 14
        
        // upper half of quare             upper half                  upper half
     
        //  0 0 0 0 0 0                 0 0 0 0 0 0 0 0 0 X         0 0 0 0 0 0 0 0 0 0 0 0 X X 
        //  0 0 0 0 0 0                 0 0 0 0 0 0 0 0 0 X         0 0 0 0 0 0 0 0 0 0 0 0 X X
        //  0 0 0 0 0 0                 0 0 0 0 0 0 0 0 0 X         0 0 0 0 0 0 0 0 0 0 0 0 X X
        //                              0 0 0 0 0 0 0 0 0 X         0 0 0 0 0 0 0 0 0 0 0 0 X X
        //                              0 0 0 0 0 0 0 0 0 X         0 0 0 0 0 0 0 0 0 0 0 0 X X
        //                                                          0 0 0 0 0 0 0 0 0 0 0 0 X X
        //                                                          0 0 0 0 0 0 0 0 0 0 0 0 X X
        // 
        // lower half of square             lower half                     lower half 
        // 
        //  0 0 0 0 0 0                 0 0 0 0 0 0 0 0 0 X         0 0 0 0 0 0 0 0 0 0 0 0 X X
        //  0 0 0 0 0 0                 0 0 0 0 0 0 0 0 0 X         0 0 0 0 0 0 0 0 0 0 0 0 X X
        //  0 0 0 0 0 0                 0 0 0 0 0 0 0 0 0 X         0 0 0 0 0 0 0 0 0 0 0 0 X X 
        //                              0 0 0 0 0 0 0 0 0 X         0 0 0 0 0 0 0 0 0 0 0 0 X X
        //                              0 0 0 0 0 0 0 0 0 X         0 0 0 0 0 0 0 0 0 0 0 0 X X
        //                                                          0 0 0 0 0 0 0 0 0 0 0 0 X X
        //                                                          0 0 0 0 0 0 0 0 0 0 0 0 X X

        //  f(6) = 0                        f(10) = 1                      f(14) = 2 

        // pattern: 

        // f(N) = (N+2)/4 -2, where N is a singly even number

        //  f(6) = (6+2)/4 - 2  =  8/4 - 2 = 0

        // f(10) = (10+2)/4 - 2 = 12/4 - 2 = 1

        // f(14) = (14+2)/4 - 2 = 16/4 - 2 = 2

        int N = this.order; 

        Map<Integer,String> upperCell = new HashMap<>();
        Map<Integer,String> lowerCell = new HashMap<>() ;
        
        for(int j = 0 ; j < (N+2)/4 - 2 ; j++){
            // columns

            for(int i = 0 ; i < N/2 ; i++){
                // rows

                // swap upper right rows in the (N-j-1) column 
                // with the corresponding lower right (i+N/2) row in the same column
                
                // upper cell to swap
                upperCell = magicSquare.get(i).get(N-j-1);
                
                // lower cel' to swap
                lowerCell = magicSquare.get(i+N/2).get(N-j-1);
                
                // swap cells
                swap(upperCell, lowerCell);
               
            }
        }  
        
    }

    // =============================================================================

    // helper method to streamline map key value pair swaping 
    private void swap(Map<Integer,String> A, Map<Integer,String> B ){

        // swap to map's key value pairs
        // note that A.size() == B.size() == 1 

        //  save A's key-value pair   
        Integer keyFromA = A.keySet().iterator().next();
        String valueFromA =  A.entrySet().iterator().next().getValue();

        // Save B's key-value pair
        Integer keyFromB = B.keySet().iterator().next();
        String valueFromB =  B.entrySet().iterator().next().getValue();
        
        // clear all the values in A and B
        A.clear(); 
        B.clear();
        
        // put the stored values from A into B
        A.put(keyFromB, valueFromB);

        // put the stored values from B into A
        B.put(keyFromA, valueFromA);

    }

    //**************************************************************************************************
    // ******************************************Decryption*********************************************

    // constructor for decryption
    SinglyEvenMagicCypher(int order, ArrayList<ArrayList<Map<Integer,String>>> square){
        // takes an integer,"order": order of the square eg: 6x6, 10x10
        // and an 2d array list of maps representing a char dictionary of the message
        
        this.order = order;
        this.magicSquare = square; 
    
    }
        //decryption main method
        protected String decryptMessage(ArrayList<ArrayList<Map<Integer,String>>> square){
    
            //step 1) perform row column swaping on left side
            leftSideRowColumnSwapOperations(); 

            //step 2) perform row column swaping on right side
            rightSideRowColumnSwapOperations();

            //step 3)  split the square into 4     
            splitInto4Squares();

            //step 4) use OddMagicCyphers decryptMessage method to read each square
            String decryptedMessage = readSquares();

            //step 5) return the result    
            return decryptedMessage;
        }
    

    //step 3) 
    protected void splitInto4Squares(){
        // the method splits a singly even square into 4 odd squares
        // then it creates 4 oddMagicCypher Objects out of the split squares
        // and uses the decryption process for those squares. 
        // NOTE: this method updates state!
        
        // short hand 
        int N = order;
        
        //clear existing state of squares if any to avoid bullshit <= (can't think of a bettter word =/ )
        upperleftSquare.clear();
        upperRightSquare.clear();
        lowerLeftSquare.clear();
        lowerRightSquare.clear();
        
        // initilize space to store squares
        ArrayList<ArrayList<Map<Integer,String>>> upperLeftSquare = new ArrayList<>();
        ArrayList<ArrayList<Map<Integer,String>>> upperRightSquare = new ArrayList<>();
        ArrayList<ArrayList<Map<Integer,String>>> lowerLeftSquare  = new ArrayList<>();
        ArrayList<ArrayList<Map<Integer,String>>> lowerRightSquare = new ArrayList<>();

        for(int i = 0 ; i < N/2; i++){
            //only need to itterate through half of the rows
            
            //create a space to store cells in each row
            ArrayList<Map<Integer,String>> upperLeftRow = new ArrayList<>();
            ArrayList<Map<Integer,String>> upperRightRow = new ArrayList<>();
            ArrayList<Map<Integer,String>> lowerLeftRow = new ArrayList<>();
            ArrayList<Map<Integer,String>> lowerRightRow = new ArrayList<>();

            for(int j = 0 ; j < N ; j ++){
                
                //get the cells and add them to the corresponding row
                Map<Integer,String> upperCell = magicSquare.get(i).get(j);

                //get the correponding rows in the lower half of the square
                Map<Integer,String> lowerCell = magicSquare.get(i+N/2).get(j);
            
                if(j<N/2){
                    //then were in left side of the square
                    upperLeftRow.add(upperCell);    
                    
                    // lower left side of the square
                    lowerLeftRow.add(lowerCell);

                }else{
                    //otherwise we're in the right side
                    upperRightRow.add(upperCell);

                    //lower right side of the square
                    lowerRightRow.add(lowerCell);
                }                    
            }
            
            //add each row to the squares
            upperLeftSquare.add(upperLeftRow);
            upperRightSquare.add(upperRightRow);
            lowerLeftSquare.add(lowerLeftRow);
            lowerRightSquare.add(lowerRightRow);

        }
        //setState
        this.upperleftSquare = upperLeftSquare;
        this.upperRightSquare = upperRightSquare;
        this.lowerLeftSquare = lowerLeftSquare;
        this.lowerRightSquare = lowerRightSquare;

    }

    protected String readSquares(){
        // create 4 oddMagicCypher objects for us to decrypt using 
        // OddMagicCypher's decryption constructor
        
        //==================== upper left square =======================================
    
        OddMagicCypher  upperLeftOddMagicSquare = new OddMagicCypher(order, upperleftSquare);
        String uppleftText = upperLeftOddMagicSquare.decryptMessage(upperleftSquare);

        //==================== upper right square =======================================

        OddMagicCypher  upperRightOddMagicSquare = new OddMagicCypher(order, upperRightSquare);
        String upperRightText = upperRightOddMagicSquare.decryptMessage(upperRightSquare);

        //==================== lower left square =======================================

        OddMagicCypher  lowerLeftOddMagicSquare = new OddMagicCypher(order, lowerLeftSquare);
        String lowerLeftText = lowerLeftOddMagicSquare.decryptMessage(lowerLeftSquare);

        //==================== lower right square =======================================

        OddMagicCypher  lowerRightOddMagicCypher = new OddMagicCypher(order, lowerRightSquare);
        String lowerRightText = lowerRightOddMagicCypher.decryptMessage(lowerRightSquare);

        //recall pattern for building singly even squares is:

        // A  C     A = 1:N^2/4            C = 2*N^2/4+1: 3N^2/4
        // D  B     B = N^2/4+1 : 2*N^2/4  D = 3N^2/4+1 : N^2

        //          A              B              C                 D
        return uppleftText + lowerRightText + upperRightText + lowerLeftText;
     
    }


}
