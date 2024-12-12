import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class MagicCypherDemo {

	// map to keep track of individual timers
	private Map<String, Double> startTimes = new HashMap<>();
	private Map<String, Double> endTimes = new HashMap<>();


	// starts the compile timer
	private void startTimer(String timer) {
		startTimes.put(timer, (double) System.nanoTime());
	}

	// ends the compile timer
	private void endTimer(String timer) {
		endTimes.put(timer, (double) System.nanoTime());
	}

	// returns the compile time in seconds
	private double getTime(String timer) {
		return (endTimes.get(timer) - startTimes.get(timer)) / 1_000_000_000;
	}

	// handles string message cyphering
	private static void cypherMessage(String message) {
		MagicCypherDemo demo = new MagicCypherDemo();
		MagicCypher magicCypher = new MagicCypher();
		
		// encryption 
		demo.startTimer("Encrypt");
		System.out.println("Encryption starting");
		magicCypher.encryptMessage(message);
		demo.endTimer("Encrypt");
		System.out.println(String.format("%.4f seconds to encrypt.", demo.getTime("Encrypt")));

		// order
		System.out.println("The order of your message is: " + magicCypher.order + "\n");
		int key = (int) magicCypher.calculateMagicConstant(magicCypher.order);

		// decryption
		demo.startTimer("Decrypt");
		String decryptedMessage = magicCypher.decryptMessage(magicCypher.message, key);
		System.out.print(decryptedMessage);
		demo.endTimer("Decrypt");
		System.out.println(String.format("%.4f seconds to decrypt.\n", demo.getTime("Decrypt")));
	}
	
	// handles text file cyphering
	private static void cypherFile(File file) {
		MagicCypherDemo demo = new MagicCypherDemo();
		MagicCypher magicCypher = new MagicCypher();
		
		// encryption
		demo.startTimer("Encrypt");
		System.out.println("Encryption starting");
	    magicCypher.encryptMessage(file);  
	    demo.endTimer("Encrypt");
	    System.out.println(String.format("%.4f seconds to encrypt.\n", demo.getTime("Encrypt")));

	    // order
	    System.out.println("The order of your message is: " + magicCypher.order + "\n");	    
	    int key = (int) magicCypher.calculateMagicConstant(magicCypher.order);
	    
	    // decryption
	    demo.startTimer("Decrypt");
	    String decryptedMessage1 = magicCypher.decryptMessage(magicCypher.message,key);
	    System.out.print(decryptedMessage1);
	    demo.endTimer("Decrypt");
	    System.out.println(String.format("%.4f seconds to decrypt.\n", demo.getTime("Decrypt")));
	}


	// main method for testing
	public static void main(String[] args) {
		MagicCypherDemo demo = new MagicCypherDemo();
		
		// singly even test with file
		demo.startTimer("Test2");
		File singlyEvenTest = new File("ghettoScholar.txt");
		cypherFile(singlyEvenTest);
		demo.endTimer("Test2");
		System.out.println(String.format("%.4f seconds to cypher message.\n", demo.getTime("Test2")));
		
		
		// doubly even test with file		
		demo.startTimer("Test3");
		File doublyEvenTest = new File("message.txt");
		cypherFile(doublyEvenTest);
		demo.endTimer("Test3");
		System.out.println(String.format("%.4f seconds to cypher message.", demo.getTime("Test3")));
		
		
        //********************STRESS TESTING**************************
		// console character limit must be greatly increased or removed
		// otherwise the console will not have a complete output!
		// The Expedition.txt contains 855451 characters
		// all testing books obtained legally through https://www.gutenberg.org/
		
		
		// Test 1) Text file of Shakespear's "Romeo and Juliet".
		demo.startTimer("Test4");
		File romeoTest = new File("RomeoAndJuliet.txt");
		cypherFile(romeoTest);
		demo.endTimer("Test4");
		System.out.println(String.format("%.4f seconds to cypher message.", demo.getTime("Test4")));
		
		// Test 2) Text file of T. Smollett's "The Expedition"
		demo.startTimer("Test5");
		File expeditionTest = new File("The Expedition.txt");
		cypherFile(expeditionTest);
		demo.endTimer("Test5");
		System.out.println(String.format("%.4f seconds to cypher message.", demo.getTime("Test5")));
		
	}
}