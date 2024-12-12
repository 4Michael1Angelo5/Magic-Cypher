import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
// import java.lang.StringBuilder;

public class MagicCypher {

    // ************************************************************************************************

    // order of magic square
    protected int order;
    // ciphered message
    protected String message;

    // array list (rows) of maps containing key values of an integer (column) and a
    // map
    // containing key values of an Integer (index of char in message) and a string
    // (the char in the message)
    protected ArrayList<ArrayList<Map<Integer, String>>> square = new ArrayList<>();

    // a map of all the string charaters in the message and thier index posiition

    protected ArrayList<Map<Integer, String>> charMapList = new ArrayList<>();

    // ledger of what's been added to our orignal message we are trying to encrypt.
    protected Map<Integer, String> ledgerMap = new HashMap<>();

    // ************************************************************************************************* 

    // =============== setters ========================

    private void setMessage(String message) {
        this.message = message;
    }

    private void setSquare(ArrayList<ArrayList<Map<Integer, String>>> magicSquare) {
        this.square = magicSquare;
    }

    private void setOrder(int order) {
        this.order = order;
    }

    private void setCharMapList(ArrayList<Map<Integer,String>> charMapList){
        this.charMapList = charMapList;
    }

    // ============== empty constructor =======================

    //@TODO explain the design descion to make the constructor is empty


    // =============== meat and potatoes ======================

    protected String encryptMessage(String message) {

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
    protected String encryptMessage(File file) {

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
    private String readFile(File file){
        
        // initilize a string builder more efficient than string concatenation 
        StringBuilder message = new StringBuilder();

        try{
            
            // used buffered reader with a file reader 
            // b/c it's more effecient than a Scanner
            BufferedReader reader = new BufferedReader( new FileReader(file));
    
            String nextLine;

            try{

                while( (nextLine = reader.readLine())!= null){

                    message.append(nextLine);

                }
            // handle exceptions thrown by FileReader
            }catch(IOException e){
                e.printStackTrace();
            }
            
        // handle exceptions thrown by BufferedReader
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        return message.toString();
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

        // String tempMessage = "";
        StringBuilder tempMessage = new StringBuilder();
        String comparisonString; 
        for (int i = 0; i < message.length(); i++) {

            comparisonString = "" + message.charAt(i);

            if (comparisonString.equals(" ")) {

                // fill in spaces between words with a char
                tempMessage.append("_");

            } else { 
                // otherwise its a char we need to append
                tempMessage.append(comparisonString);
            }
        }

        // need to deal with when message length is less than N^2
        // ie continue adding random char's until it is length is n^2

        while (tempMessage.length() < order * order) {
 
            tempMessage.append("_");
        }
        return tempMessage.toString();
    }

    // step 4)

    private void generateCharMap(String message) {
        // updates charMap field
        // arraylist of maps containing:
        // key: index of char in message
        // value: char in message

        //create a temporary ArrayList to store our message
        ArrayList<Map<Integer,String>> newCharArrayList = new ArrayList<>();

        int index = 0;

        while (index < message.length()) {

            Map<Integer, String> charMap = new HashMap<>();

            charMap.put(index, "" + message.charAt(index));

            newCharArrayList.add(charMap);

            index++;
        }
        
        //this line of code is important!
        setCharMapList(newCharArrayList);

        // do not directly mutate the state of the application!
        // example:  
        // say a user creates a cipher object. 
        // they feed it a message to encrypt.
        // MagicCipher creates the decrypted message, boom done
        // but then the user wants to create another encrypted message with
        // the same MagicCyper Object they just created. 
        // if we directly manipulate this.charMapList then the ArrayList grows
        // and the new AraryList will contain characters from both messages. 

        // this line of code ensures the application is flexible and now the user can 
        // encrypt multiple messages with the same MagicCypher object.
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
        // differnt OddMagicCypher Objects becuase they both share the same parent.

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
        
        System.out.println("Determining cipher algorithm for encryption...\n");

        if (order % 2 == 0) {

            if (order % 4 == 0) {
                // doubly even case

                System.out.println("Using doubly even magic cypher\n");

                // create new doubly even magic cypher to handle encryption logic 
                DoublyEvenMagicCypher doublyEvenCypher = new DoublyEvenMagicCypher(order, square, charMapList);

                // call generateCypher method to run the encryption
                String cypheredText = doublyEvenCypher.generateCypher();
                
                System.out.println("Generating Magic Cipher\n");

                try{


                    simulateLoaderBar(42, 60);

                }
                catch(InterruptedException e){

                    System.out.println(e);

                }

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

                System.out.println("Using singly even magic cypher...\n");
                
                // create new singly even magic cypher to handle encryption logic 
                SinglyEvenMagicCypher singlyEvenMagicCypher = new SinglyEvenMagicCypher(order, charMapList, square);

                // call generateCypher method to run the encryption
                String cipheredText = singlyEvenMagicCypher.generateCypher();

                try{

                    System.out.println("Generating Magic Cipher\n");

                    simulateLoaderBar(42, 60);

                }
                catch(InterruptedException e){

                    System.out.println(e);

                }

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

            System.out.println("using odd encryption butter\n");

            // create new odd Magic Cypher object to handle encryption logic
            OddMagicCypher oddCypher = new OddMagicCypher(order, charMapList, square);

            // call generateCypher Method to run the encryption
            String cypheredText = oddCypher.generateCypher();

            try{

                System.out.println("Generating Magic Cipher\n");

                simulateLoaderBar(42, 60);

            }
            catch(InterruptedException e){

                System.out.println(e);

            }

            printSquare(oddCypher.square);

            // this check needs to go here becuase I don't want 
            // it in odd magic Cypher class becuase every time signly even Magic cypher creates a new 
            // square the test will fail. 
            if(isMagic(oddCypher.square)){

                setSquare(oddCypher.square);

                // update the message field and overwrite original input with cipherd text
                setMessage(cypheredText); 
            }

         
        }

        return message;

    }


// helper methods ===================================================================================

    // calculatMagicConstant;
    protected int calculateMagicConstant(int order) {
        // the magic constant is the number every row, column, and diagonal sum to. 

        int n = order;

        return n * (n * n + 1) / 2;

    }

    // toString Method but with void return type
    protected void printSquare(ArrayList<ArrayList<Map<Integer, String>>> square) {
        
        // NOTE: square.size() NOT "order"
        // "order" will be undefined when singlyEvenMagicCypher creates 
        // 4 oddMagicCyphers because it does not initlize this class before their creation
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

        //UI/UX feature loading bar
        try{
            simulateLoaderBar(42, 60);
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        Map<Integer, String> cellInColumn = new HashMap<>();
        Map<Integer, String> cellInRow = new HashMap<>();

        int sumRow;
        int sumColumn;
        int magicConstant = calculateMagicConstant(magicSquare.size());


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
        
        //success message 
        System.out.println("All the rows equal the magic constant\n");
        System.out.println("All the columns equal the magic constant\n");
        System.out.println("Both Diagonals equal the magic constant\n");      

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
        
        //row
        ArrayList<Map<Integer, String>> row = new ArrayList<>();
        
        // string builder more effecien then string concatenation
        StringBuilder cipheredText = new StringBuilder();
  

        for (int i = 0; i < square.size(); i++) {

            // row of matrix
            row = square.get(i);

            for (int j = 0; j < square.size(); j++) {

                // get the first key value of the cell in the column of row i
                cipheredText.append(row.get(j).entrySet().iterator().next().getValue());
            }
        }

        // return the ciphered text
        return cipheredText.toString();
    }

    private String determineDecryptionAlgorithm(int order, ArrayList<ArrayList<Map<Integer,String>>> square){
        // determins which decryption algorithm to use and uses it
        // return the decrypted message; 
        System.out.println("\nDetermining cipher algorithm for decryption...");
        
 
        if(order%2==0){

            if(order%4==0){
                //use doubly even
                DoublyEvenMagicCypher doublyEvenMagicCypher = new DoublyEvenMagicCypher(square);
                String deCryptedmessage = doublyEvenMagicCypher.decyptMessage(square);

                

                return deCryptedmessage;
            }else{
                // use singly even

                SinglyEvenMagicCypher singlyEvenMagicCypher = new SinglyEvenMagicCypher(order, square);
                String deCryptedmessage = singlyEvenMagicCypher.decryptMessage(square);

                

                return  deCryptedmessage;
            }

        }else{
            // use odd            
            OddMagicCypher oddMagicCypher = new OddMagicCypher(order,square);
            String deCryptedmessage = oddMagicCypher.decryptMessage(square);
            //should this setState and should we have a new field for decryptedMessage?
            return deCryptedmessage;
        }

    }

    //================================================================================================
    //decryption 

    // step 1) check if the message we are trying to decrypt is a valid cipher
    
    // helper method determines whether or not an integer is a square number
    private static boolean isSquare(int num){

        //cast the result of sqrt(num) to an integer
        // ie sqrt(2) = 1.41421356237 becomes 1
        // 1*1 != 2 there for 2 is not a square number 
        int sqrt = (int) Math.sqrt(num);
 
        return sqrt*sqrt == num;
    }
    
    // checks to see if the message length is a square number
    private static boolean isValidCipher(String message) throws IllegalArgumentException{
        // this method is used by decryptMessage to determin if a message is 
        // a valid cipher

        //check if the message length is a square number eg: 9,16,25...
        if(isSquare(message.length())){ 
            return true;
        }else{
            throw new IllegalArgumentException("the message you are trying to decrypt is not a valid cipher");
        }
    }

    // step 2 check if the key provided is equal to the magic constant

    private  boolean isValidKey(int order ,int key){
 

        int magicConstant = (int) calculateMagicConstant(order);
        
        // if the key does not match the magic constant throw IllegalAccessError
        if(key!= magicConstant){
            throw new IllegalAccessError("Permison not granted.\n You have provided an invalid key to access the messsage.");
        }else{
            return true;
        }

    }

    // step 3) 
    private static ArrayList< ArrayList< Map<Integer,String>>> stringToSquare(String message){
        //constructs a 2d arraylist of maps cotaining the letter and index of the char in the message

        ArrayList< ArrayList< Map<Integer,String>>> square = new ArrayList<>();

        int order = (int) Math.sqrt(message.length());
        int charIndex = 0;
        
        // fill the matrix from top row to bottom row, and from left column to right column
        for(int i = 0 ; i < order ; i++){
            ArrayList<Map<Integer,String>> row = new ArrayList<>();
            for(int j = 0 ; j < order ; j ++){
                //each cell is single map  
                Map<Integer,String> cell = new HashMap<>();
                // the char's index is the key and the char is its value
                cell.put(charIndex, "" +message.charAt(charIndex)); //"" + to convert from char to string
                // add the map to the row
                row.add(cell);
                // increase char index
                charIndex++;
            }
            // add the row to the square
            square.add(row);
        }

        return square;

    }


    protected String decryptMessage(String message,int key){

        // we don't need to worry about sanitizing the string bc Magic Cypher's out put is always the 
        // the product of a sanitized message. 

        // step 1) check if the message is a valid cipher (eg. a square number)
        if(isValidCipher(message)){
            
            int messageLength = message.length();

            int order = (int) Math.sqrt(messageLength); 
            
            // step 2) check that the key is equal to the magic constant
            if(isValidKey(order, key)){


                // step 3) build a square to fit our encrypted message;                
                ArrayList< ArrayList< Map<Integer,String>>> cipheredSquare = stringToSquare(message);

             
                // step 4) feed the square to determineDecryptionAlgorithm
                String decryptedMessage = determineDecryptionAlgorithm(order, cipheredSquare);

                // step 5) return the message and simulate the loaer bar for improved UX

                try{
                    simulateLoaderBar(42, 60);
                    
                }
                catch(InterruptedException  e){
                    System.out.println(e);
                }

                System.out.println("Your decrypted message is: \n");

                System.out.println(decryptedMessage+"\n");
                
                return decryptedMessage;
            
            }
        }

        //default behavior 
        // if an invalid message is provided, return the message
        // but it will never get here becuase if it fails the previous
        // checks then it will throw an exception   

        System.out.println("An error occured trying to decrypt the following message: \n");

        System.out.println(message+"\n");

        return message;

    }

    /**
     * Simulates a loader bar in the console.
     *
     * @param totalSteps   the total number of steps for the loader to complete
     * @param delayMillis  the delay in milliseconds between each step
     * @throws InterruptedException if the thread sleep is interrupted
     */
    
    public static void simulateLoaderBar(int totalSteps, int delayMillis) throws InterruptedException {
            
    // loaderbar feature written with help from chatgpt
    // Example: simulateLoaderBar(50, 100) => 50 steps with a delay of 100ms per step 
    

        int barWidth = 100; // Length of the loader bar

        System.out.println("Loading...");

        for (int step = 0; step <= totalSteps; step++) {
            int progress = (step * barWidth) / totalSteps;
            StringBuilder loader = new StringBuilder();

            loader.append("[");
            for (int i = 0; i < barWidth; i++) {
                if (i < progress) {
                    loader.append("=");
                } else if (i == progress) {
                    loader.append(">");
                } else {
                    loader.append(" ");
                }
            }
            loader.append("]");

            int percentage = (step * 100) / totalSteps;

            // Overwrite the current line with the loader bar and percentage
            System.out.print("\r" + loader + " " + percentage + "%");

            Thread.sleep(delayMillis);
        }

        System.out.println("\nDone!\n");
    }    

}