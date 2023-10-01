package module1;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;



public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cryptogram cryptogram;

        System.out.println("Welcome to the cryptogram program!");
        System.out.println("");
        System.out.println("1 Easy");
        System.out.println("2 Medium");
        System.out.println("3 Difficult");
        System.out.println("");
        System.out.println("Enter the number associated with the difficulty you would like to play.");
        
        String choice = scanner.nextLine();
        int difficulty = 0;

        System.out.println("");
        System.out.println("");
        System.out.println("");

        // Loop while user's entry is not a valid response
        while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3"))
        {
            System.out.println("Please enter either 1, 2, or 3.");
            choice = scanner.nextLine();
        }

        // The user's entry sets the number of hints below
        switch (choice) {
            case "1":
                difficulty = 4;
                break;
            case "2":
                difficulty = 2;
                break;
            default:
                difficulty = 0;
        }

        // Create a cryptogram object using its constructor and passing the number of hints to it.
        cryptogram = new Cryptogram(difficulty);

        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("While solving the cryptogram you have some additional controls to use:");
        System.out.println("Type 'restart' to clear your guess and try again");
        System.out.println("Type 'hint' for an additional hint");
        System.out.println("Type 'quit' to exit the program");
        System.out.println("");
        scanner.nextLine();

        // Loop through the process of entering guesses until all guesses are correct (or the user breaks)
        // by entering "quit"
        boolean complete = false;
        while (!complete){

            // Use the Code Key to display the message and guesses
            System.out.println(cryptogram.DisplayCryptogram());

            System.out.println("What letter would you like to submit a guess for?");
            String entry = scanner.nextLine();

            // If the user enters more than a single letter, extraControls will check to see if it is
            //a special command
            if(entry.length() > 1){
                if(extraControls(entry,cryptogram,scanner)){
                    break;
                }
            }
            else{
                
                // While the entry is not a letter or one of the pseudonyms, the user must enter a new one
                while(entry.length() != 1 || !Character.isLetter(entry.charAt(0)) || !cryptogram.decodeKey.containsKey(entry.charAt(0))){
                    System.out.println("Please enter one of the single letters displayed above.");
                    entry = scanner.nextLine();
                }
                // The users then enter a guess and the code will loop until it is a single character and a 
                // letter
                System.out.println("What is your guess?");
                String guessLetter = scanner.nextLine();
                while(guessLetter.length() != 1 || !Character.isLetter(guessLetter.charAt(0))){
                    System.out.println("Please enter a single letter");
                    guessLetter = scanner.nextLine();
                }
                // Use the letter and the guess to update the cryptogram
                updateCryptogram(entry, guessLetter, cryptogram.decodeKey);
            }
            // check to see if all guesses are entered and correct
            complete = checkWin(cryptogram.codeKey);
            
        }
        // If the Cryptogram is completed a message appears and the program ends.
        if (complete){
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println(cryptogram.message);
            System.out.println("");
            System.out.println("");
            System.out.println("You did it!!!");
            System.out.println("");
            scanner.close();
        }
    }
    // This method handles the hint, restart, and quit commands, as well as invalid entries over a
    // character long
    public static boolean extraControls(String entry, Cryptogram cryptogram, Scanner scanner){
        entry = entry.toLowerCase();
        boolean quit = false;
        switch (entry){
            case "hint":
                Random random = new Random(0);
                char hint = cryptogram.hintList.remove(random.nextInt(cryptogram.hintList.size()));
                cryptogram.codeKey.get(hint).setHint(true);
                System.out.println(String.format("The hint has revealed letter '%c'", hint));
                scanner.nextLine();
                break;
            case "restart":
                for (Letter l : cryptogram.codeKey.values()) {
                    l.setGuess('_');
                }
                break;
            case "quit":
                quit = true;
                break;
            default:
                System.out.println("You may enter:");
                System.out.println("A letter to guess");
                System.out.println("hint");
                System.out.println("restart");
                System.out.println("quit");
                scanner.nextLine();
                break;
        }
        return quit;
    }
    // This method uses the decodeKey to set the user's guess
    public static void updateCryptogram(String entry, String guess, Map<Character, Letter> decodeKey){
        char guessPseudo = entry.charAt(0);
        char guessLetter= guess.charAt(0);
        Letter l = decodeKey.get(Character.toLowerCase(guessPseudo));
        l.setGuess(Character.toLowerCase(guessLetter));
    }
    // This method goes through the codeKey and checks to see if each letter's correct attribute is true,
    // meaning the user has solved the cryptogram
    public static boolean checkWin(Map<Character, Letter> codeKey){
        for (Letter l : codeKey.values()) {
            if (l.getCorrect() == false){
                return false;
            }
        }
        return true;
    }
}


