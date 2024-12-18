# MagicCypher

MagicCypher is a Java-based encryption tool that utilizes the properties of magic squares to cipher text messages. The program can handle both plain text input and text files, making it versatile for different use cases. The algorithm ensures that the encrypted message retains specific "magic" properties, making it a unique approach to data encryption.

## Key Features

- **Magic Square Encryption**: Uses the structure of magic squares (odd, singly even, and doubly even) to encrypt messages.
- **Customizable Input**: Accepts both string input and file input for encryption.
- **Message Sanitization**: Cleans up input messages by trimming white spaces and padding with characters to fit the magic square.
- **Validation**: Includes checks to ensure the magic properties of the encryption are maintained (sums of rows, columns, and diagonals).
- **High Scalability**: Handles large text files efficiently, demonstrated with datasets as large as 857,476 characters.
- **Optimized Performance**: Utilizes `StringBuilder` for iterative operations, significantly improving runtime compared to traditional `String` concatenation.

## How It Works

1. **Sanitize the Message**
   - Remove any leading or trailing white spaces.
   - Insert characters between spaces of words.
   - Add additional characters to the end of the message so that the final length of the message is a square number (e.g., 9, 16, 25, etc.).

2. **Determine the Order of the Square Matrix**
   - Calculate the order of the square matrix based on the length of the sanitized string.
   - For example, if the string length is 9, the order is 3, and the Siamese algorithm is selected to generate a 3x3 magic square.

3. **Select the Appropriate Child Class for Encryption**
   - Determine which child class to use based on the matrix order:
     - **OddMagicCypher** for odd orders.
     - **SingleEvenMagicCypher** for single even orders (e.g., 6).
     - **DoublyEvenMagicCypher** for doubly even orders (e.g., 4, 8).
   - The user interacts only with the parent class, **MagicCypher**; it manages the selection of the child class internally.

4. **Generate Character Map**
   - Creates a map of characters in the message along with their position indices in the original message.

5. **Generate the Magic Square**
   - Each child class uses its algorithm to generate a magic square of the specified order.
   - The magic square is filled with characters from the sanitized string passed by the parent class.

6. **Encryption Process**
   - Each child class implements a `generateCypher` method:
     - The method produces the magic cypher and returns an array list of maps containing key-value pairs.
     - The key is the index of the character in the original message, and the value is the corresponding character.
   - The child class then uses the `cypheredText` method:
     - It accepts the array list of maps, reads each row line by line, and constructs the encrypted (cyphered) text.

7. **Update and Access the Encrypted Message**
   - The cyphered text is sent back to the parent class, which updates the state of the string message.
   - The user can access the encrypted message through `myCypher.message` after creating a new `MagicCypher` object.

8. **Validation with Tester Method**
   - The **MagicCypher** class includes a tester method to verify the correctness of the generated magic square.
     - It sums the keys in each row and column to check if they equal the magic constant, ensuring the square's validity.

## Performance Benchmarks

### Romeo and Juliet (158,404 Characters)
- **Encryption Time**: ~14 seconds
- **Decryption Time**: < 4 seconds

### The Expedition (857,476 Characters)
- **Encryption Time**: < 30 seconds

These benchmarks highlight MagicCypher's ability to efficiently process substantial datasets, making it a practical choice for large-scale text encryption tasks.

## Example Usage

Here's a brief example of how to use MagicCypher in a Java application:

```java
import java.io.File;

public class Example {
    public static void main(String[] args) {
        File file = new File("message.txt");

        MagicCypher myCypher = new MagicCypher();
        myCypher.encryptMessage(file);
        
        System.out.println("Encrypted Message:");
        System.out.println(myCypher.message);

        // or

        MagicCypher myCypher2 = new MagicCypher();
        myCypher2.encryptMessage("Hello World!");
        
        System.out.println("Encrypted Message:");
        System.out.println(myCypher2.message);
    }
}
```

## Planned Enhancements

Future updates may include:
- Parallelized processing for even faster performance
- Comparative benchmarks against industry-standard encryption algorithms

## System Requirements

- Java 8 or later
- Minimum 2GB of RAM for processing larger datasets

## License

This project is licensed under the [MIT License](LICENSE).


