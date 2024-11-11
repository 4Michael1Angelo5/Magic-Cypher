import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MagicCypher {

    // ************************************************************************************************

    // order of magic square
    protected int order;
    // ciphered message
    protected String message;

    // keep track of where we've added random char for decryption
    private ArrayList<Integer> indicesOfWhiteSpace = new ArrayList<>();

    // array list (rows) of maps containing key values of an integer (column) and a
    // map
    // containing key values of an Integer (index of char in message) and a string
    // (the char in the message)
    public ArrayList<ArrayList<Map<Integer, String>>> square = new ArrayList<>();

    // a map of all the string charaters in the message and thier index posiition

    public ArrayList<Map<Integer, String>> charMapList = new ArrayList<>();

    // ledger of what's been added to our orignal message we are trying to encrypt.
    protected Map<Integer, String> ledgerMap = new HashMap<>();

    // *************************************************************************************************

    // main method testing

    public static void main(String[] args) {
        // MagicCypher myCypher = new MagicCypher();
        // myCypher.encryptMessage("Hello My");
        // System.out.print(myCypher.message);

        // File file = new File("message.txt");
        // MagicCypher cipher2 = new MagicCypher();
        // cipher2.encryptMessage(file); 

        File file  = new File("oddCipherText.txt");
        MagicCypher myCipher = new MagicCypher();
        myCipher.encryptMessage(file);
    }

    // =============== setters ========================

    private void setMessage(String message) {
        this.message = message;
    }

    protected void setSquare(ArrayList<ArrayList<Map<Integer, String>>> magicSquare) {
        this.square = magicSquare;
    }

    protected void setOrder(int order) {
        this.order = order;
    }

    // ============== empty constructor =======================


    // =============== meat and potatoes ======================

    private String encryptMessage(String message) {

        // 1) remove any leading or trailing white space
        message = message.trim();

        // 2) determine the order of a square matrix that can fit the message
        this.order = calculateOrder(message.length());

        // 3) sanitize the message for encryption
        String scrubedMessage = sanitizeMessage(message);
        setMessage(scrubedMessage);

        // 4) create a character map containng the values of each char in the message
        // and their index
        generateCharMap(scrubedMessage);

        // 5) create a matrix with empty maps in each cell
        createEmptyMatrix(order);

        // 6) determine approriate child class to handle encryption:
        determineCypherAlgorithm();
        
        // 7) print the ciphered text to the comand line
        System.out.println("your ciphered text is:\n" + "\n" + this.message + "\n");

        return this.message;
    }

    // same as above but for files
    private String encryptMessage(File file) {

        // 0) read the message
        String readMessage = readFile(file);

        // 1 remove any leading or trailing white space
        readMessage = readMessage.trim();

        // 2) calculate the order of a square matrix that can fit our message
        this.order = calculateOrder(readMessage.length());

        // 3) sanitize the message to get ready for encryption
        String scrubedMessage = sanitizeMessage(readMessage);
        setMessage(scrubedMessage);

        // 4) create a character map containng the values of each char in the message
        // and their index
        generateCharMap(scrubedMessage);

        // 5) create an matrix with empty maps in each cell
        createEmptyMatrix(order);

        // 6) determine approriate child class to handle encryption:
        determineCypherAlgorithm();

        // 7) print the ciphered text to the comand line
        System.out.println("your ciphered text is:\n" + "\n" + this.message + "\n");

        return this.message;
    }
    // ========== steps  ============

    // step 0)

    // Helper method for MagicCypher to read file input
    private String readFile(File file) {

        String message = "";
        String lineBreak;

        try {
            // Create a Scanner object to read the file
            Scanner scanner = new Scanner(file);

            // Read the file line by line
            while (scanner.hasNextLine()) {
                if (scanner.hasNextLine()) {
                    // adjust for line breaks
                    lineBreak = " "; 
 
                } else {
                    lineBreak = "";
                }

                String line = scanner.nextLine();

                // add a white space between
                // lines other wise:
                // Hello
                // World
                // Becomes "HelloWorld" instead of "Hello World"

                // concatenate
                message += line + lineBreak;
                // message += line;
            }

            // close the scanner
            scanner.close();

            // deal with not being able to find the file
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        return message;
    }

    // step 2)

    private int calculateOrder(int lengthOfMessage) {

        // Now we need to figure out what square will fit our message
        // example message = "12345678"

        // the string message contains 8 char's
        // So the smallest square that will fit 8 char's is
        // a 3x3 square which has room for 9 char's

        int order = (int) Math.ceil(Math.sqrt(lengthOfMessage));

        if (order < 3) {
            // we can not make magic squares of order less then 3
            // except the trivial magic square of order 1;
            return 3;
        } else {
            return order;
        }

    }

    // step 3)

    private String sanitizeMessage(String message) {
        // clean up the message to get ready for encryption

        // @TODO: optimize so that it doesn't loop if there are no whitespaces
        // or maybe put that check before we even get here
        // maybe use regEx?

        String tempMessage = "";
        String comparisonString;
        int indexOfWhiteSpaceInMessage; // @TODO finish this
        for (int i = 0; i < message.length(); i++) {

            comparisonString = "" + message.charAt(i);

            if (comparisonString.equals(" ")) {

                // fill in spaces between words with random char
                tempMessage += "X";

                // @TODO this should actually fill it with a random char;
                // Jake you wanna tackle this one?

                // @TODO add the index of where in the string we've added a random char

            } else {
                tempMessage += comparisonString;

            }
        }

        // need to deal with when message length is less than N^2
        // ie continue adding random char's until it is length is n^2

        while (tempMessage.length() < order * order) {

            tempMessage += "Z";
            // @TODO this should actually fill it with a random char;
            // @TODO need to keep track of what we've added so that it
            // isn't included in decryted message.

        }
        return tempMessage;
    }

    // step 4)

    private void generateCharMap(String message) {
        // updates charMap field
        // arraylist of maps containing:
        // key: index of char in message
        // value: char in message

        int index = 0;

        while (index < message.length()) {

            Map<Integer, String> charMap = new HashMap<>();

            charMap.put(index, "" + message.charAt(index));

            charMapList.add(charMap);

            index++;
        }
    }

    // step 5

    protected ArrayList<ArrayList<Map<Integer, String>>> createEmptyMatrix(int order) {
        // create an empty matrix to be passed off to children
        // arrayList (row) of arrayList (columns) of maps (cells)

        // very important! create a temp square do not directly mutate square field
        ArrayList<ArrayList<Map<Integer, String>>> tempSquare = new ArrayList<>();

        // Note:

        // do not directly mutate the "square" object. this causes unexpexted behavior
        // when child classes
        // call this method.

        // example SinglyEvenMagicCypher calls this method 4 times to create 4 odd magic
        // squares to build its singly even magic square
        // with each call square's value will grow becuase it reference persists between
        // child and parent even when you create new
        // differnt new OddMagicCypher Object becuase they both share the same parent.

        for (int i = 0; i < order; i++) {

            ArrayList<Map<Integer, String>> row = new ArrayList<>();

            for (int j = 0; j < order; j++) {

                // create an empty map in each cell
                // to use for comparison purposes in children classes
                // to determine occupancy

                Map<Integer, String> cell = new HashMap<>();

                row.add(cell);

            }

            tempSquare.add(row);
        }

        // set square field to newly created empty matrix
        setSquare(tempSquare);

        return square;
    }

    // step 6)
    private String determineCypherAlgorithm() {
        // determin which child class to pass the encription logic off to 
        // based on the order of the square.   

        if (order % 2 == 0) {

            if (order % 4 == 0) {
                // doubly even case

                System.out.println("\nusing doubly even magic cypher\n");

                // create new doubly even magic cypher to handle encryption logic 
                DoublyEvenMagicCypher doublyEvenCypher = new DoublyEvenMagicCypher(order, square, charMapList);

                // call generateCypher method to run the encryption
                String cypheredText = doublyEvenCypher.generateCypher();

                printSquare(doublyEvenCypher.magicSquare);
                
                // check validity of encryption algorithm
                if(isMagic(doublyEvenCypher.magicSquare)){

                    // update square state with magic square
                    setSquare(doublyEvenCypher.magicSquare);

                    // update message state with ciphered text
                    setMessage(cypheredText);

                }


            } else {
                // singly even case

                System.out.println("\nusing singly even magic cypher...\n");
                
                // create new singly even magic cypher to handle encryption logic 
                SinglyEvenMagicCypher singlyEvenMagicCypher = new SinglyEvenMagicCypher(order, charMapList, square);

                // call generateCypher method to run the encryption
                String cipheredText = singlyEvenMagicCypher.generateCypher();

                printSquare(singlyEvenMagicCypher.magicSquare);
                
                // check validity of the encryption aglorithm
                if(isMagic(singlyEvenMagicCypher.magicSquare)){
                    
                    // if its valid update square field
                    setSquare(singlyEvenMagicCypher.magicSquare);

                    // update message state with ciphered text
                    setMessage(cipheredText);

                }

            }

        } else if (order % 2 == 1) {
            // odd case

            System.out.println("\nusing odd encryption butter\n");

            // create new odd Magic Cypher object to handle encryption logic
            OddMagicCypher oddCypher = new OddMagicCypher(order, charMapList, square);

            // call generateCypher Method to run the encryption
            String cypheredText = oddCypher.generateCypher();

            
            // this check needs to go here becuase I don't want 
            // it in odd magic Cypher class becuase every time signly even Magic cypher creates a new 
            // square the test will fail. 

            printSquare(oddCypher.square);

            if(isMagic(oddCypher.square)){

                setSquare(oddCypher.square);

                // update the message field and overwrite original input with cipherd text
                setMessage(cypheredText); 
            }

         
        }

        return message;

    }


    // helper methods 

    // calculatMagicConstant;
    protected double calculateMagicConstant(double order) {
        // the magic constant is the number every row, column, and diagonal sum to. 

        double n = order;

        return n * (n * n + 1) / 2;

    }

    // toString Method but with void return type
    protected void printSquare(ArrayList<ArrayList<Map<Integer, String>>> square) {


        // System.out.println("check inside print square, order: " + order + " size: " + square.size()); // why is order zero?

        for (int i = 0; i < square.size(); i++) {
            // print out each row of the square
            System.out.println(square.get(i));
        }
    }

    // test's children classes encryption algorithms to make sure they have magic
    // properties
    protected boolean isMagic(ArrayList<ArrayList<Map<Integer, String>>> magicSquare) {


        // this method loops through each row and column of the array list
        // and iterates over every key in the list and adds them up to see if they equal
        // the magic constant
        // then it checks the two diagonals to see if they equal the magic constant
        // if all tests pass then it returns true.
        // if one of the sums don't equal the magic constant then it thows an illegal
        // state exception;

        System.out.println("\nChecking the validity of the algorithm... \n ");

        // @TODO implement a cool UX here where it goes step by step an shows you what its doing
        // IDEAS: it would be cool if we set a timestamp at the begining of the function and implemented 
        //  a loading bar using "*" or something so that a star would get printed out every half second 
        // then it would go step by step and be like checking all rows and columns .... 
        // now checking all diagonals .... 
        // then print the final message which says something like algorithm perofmred corecetly.

    
        // System.out.println("checking order from insie isMagic in parent: "+this.order); 
        //why is this zero when child classes are calling it but not when the parent calls it? 

        Map<Integer, String> cellInColumn = new HashMap<>();
        Map<Integer, String> cellInRow = new HashMap<>();

        int sumRow;
        int sumColumn;
        double magicConstant = calculateMagicConstant(magicSquare.size());
        for (int i = 0; i < magicSquare.size(); i++) {

            sumRow = 0;
            sumColumn = 0;

            for (int j = 0; j < magicSquare.size(); j++) {

                // rows
                cellInRow = magicSquare.get(i).get(j); // returns a map

                // columns
                cellInColumn = magicSquare.get(j).get(i); // returns a map

                // add up all the keys in each row

                // get the key in each cell @TODO can this be faster if I know there is always
                // only one map in each column
                for (Integer key : cellInRow.keySet()) {
                    // the keys represnt the index of the char in the string
                    // because they are zero indexed we need to add 1 to each key
                    sumRow += key + 1;
                }

                // add up all the keys in each column
                for (Integer key : cellInColumn.keySet()) {
                    // the keys represnt the index of the char in the string
                    // because they are zero indexed we need to add 1 to each key
                    sumColumn += key + 1;

                }

            }

            if (sumRow != magicConstant) {
                // if the sum in a row is not equal to the magic constant then the algorithm was
                // not performed
                // correctly

                throw new IllegalStateException("Row: " + (i + 1) + " does not equal the magic constant ");

            }
            if (sumColumn != magicConstant) {
                // if the sum in a column is not equal to the magic constant then the algorithm
                // was not performed
                // correctly

                throw new IllegalStateException("column: " + (i + 1) + " does not equal the magic constant ");

            }

            // System.out.println("row " + (i+1) + " sum: " + sumRow); 
            // System.out.println("column " + (i+1) + " sum: " + sumRow); 

        }
        if (!isDiagonalsMagic(magicSquare, magicConstant)) {

            throw new IllegalStateException("one of the diagonals does not equal the magic constant ");
        }

        // if all these tests passed then the encryption algorithm was performed
        // correctly
        System.out.println("Magic Cipher Algorithm performed correctly\n");
        return true;
    }
    
    // helper method for isMagic
    private boolean isDiagonalsMagic(ArrayList<ArrayList<Map<Integer, String>>> magicSquare, double magicConstant) {

        // tests to see if the 2 diagonals of the square sum to the magic constant
        // if they do it returns true, otherwise it return false;

        // row
        int i = 0;

        // column
        int j = 0;

        Map<Integer, String> cellLeft = new HashMap<>();
        Map<Integer, String> cellRight = new HashMap<>();

        int sumLeftDiagonal = 0;
        int sumRightDiagonal = 0;

        int N = magicSquare.size();

        while (i < magicSquare.size()) {

            // move from top left to bottom right diagonally
            cellLeft = magicSquare.get(i).get(j);

            // move from top right to bottom left diagonally
            cellRight = magicSquare.get(i).get(N - j - 1);

            // sum up the key's in each cell
            for (Integer key : cellLeft.keySet()) {

                // the keys represent the index of the char in the messge
                // bc they are zero indexed we need to adjust by + 1 to
                // to check their magic properties

                sumLeftDiagonal += key + 1;
            }

            for (Integer key : cellRight.keySet()) {

                // the keys represent the index of the char in the messge
                // bc they are zero indexed we need to adjust by + 1 to
                // to check their magic properties

                sumRightDiagonal += key + 1;
            }

            i++;
            j++;
        }

        // if one of the diagonals does not equal the magic constant then return false
        if (sumLeftDiagonal != magicConstant || sumRightDiagonal != magicConstant) {

            return false;
        }
        // otherwise
        return true;
    }

    // this method is used by all children
    protected String readSquare(ArrayList<ArrayList<Map<Integer, String>>> square) {
        // read each row line by line and concatenate the string values of each map
        // to form a ciphered text

        ArrayList<Map<Integer, String>> row = new ArrayList<>();
        String cipheredText = "";
        String nextChar;

        for (int i = 0; i < square.size(); i++) {

            // row of matrix
            row = square.get(i);

            for (int j = 0; j < square.size(); j++) {

                // get the first key value of the cell in the column of row i
                nextChar = row.get(j).entrySet().iterator().next().getValue();

                // concatenate the message
                cipheredText += nextChar;
            }
        }

        //update message field ?? 

        // return the ciphered text
        return cipheredText;
    }

}