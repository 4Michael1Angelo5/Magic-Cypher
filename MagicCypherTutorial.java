import java.io.File;

//Demonstration of how to interact with cipher objects

public class MagicCypherTutorial {

    public static void main(String[] args) {

        //step 1) 
        //create a cipher Object

        MagicCypher cipher = new MagicCypher();

        // now you can use encryptMessage method with the cipher object

        //===================================================
        // Singly Even Algorithm example:
        // 10x10

        cipher.encryptMessage(  "The moonlight gleams on waves so wide,\n" + 
                                "A quiet breeze, ocean's guide.\n" + 
                                "Stars above in peaceful light."); // poem written by chatGPT

        
        // Note that MagicCypher produces a flexible cipher object. 
        // the user can simply feed it a new message to encrypt without 
        // having to create new cipher objects.

        //====================================================
        // Odd Cipher Algorithm example: 
        // 29x29
        
        // here's how to use it with a file:
        // create a new file object
        File file1 = new File("ghettoScholar.txt"); // poem written by chatGPT
        
        //encrypt the message
        String encryptedMessage = cipher.encryptMessage(file1);

        // =========================================================
        // Doubly Even Cipher Algorithm example: 
        // 80x80 

        File file = new File("message.txt"); //wikipedia article about magic squares

         encryptedMessage = cipher.encryptMessage(file);

        cipher.decryptMessage(encryptedMessage, cipher.calculateMagicConstant(cipher.order));


        // (15x15) odd cipher demo using string input 

        encryptedMessage = cipher.encryptMessage(
            "All you who sleep tonight " + 
            "Far from the ones you love, " + 
            "No hand to left or right " + 
             "And emptiness above " + 
             
            "Know that you aren't alone " + 
            "The whole world shares your tears, " + 
            "Some for two nights or one, " + 
            "And some for all their years. "
            );// poem by Vikram Seth

        //========================================================
        //decryption demo:
        
        // get the key which is equal to the magic constant
        int key = cipher.calculateMagicConstant(cipher.order);
        
        // decrypt the message
        cipher.decryptMessage(encryptedMessage, key);

        // note that you will have to find the order and key yourself 
        // if you do not have access to the cipher object that created the cipher
        // you can do this by taking n = sqrt(encryptedMessage.length) and then
        // use the magic constant formula: n(n^2+1)/2. cheers!


    }
    
}
