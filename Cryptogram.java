package module1;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


// This class holds the uncoded message, and 2 maps that find an object of the Letter class by it's 
// realLetter (codeKey), and it's pseudonym (decodeKey). It also holds a list of letters that are not
// hint letters so that they may be used as hints in future if necessary. And finally, it holds the
// library of possible messages.
public class Cryptogram {
    public String message;
    public Map<Character, Letter> codeKey;
    public Map<Character, Letter> decodeKey;
    public ArrayList<Character> hintList;
    
    private String[] library =
    // region messages in library
    {
        "And I said unto him: I know that he loveth his children; nevertheless, I do not know the meaning of all things.",
        "But behold, I, Nephi, will show unto you that the tender mercies of the Lord are over all those whom he hath chosen, because of their faith, to make them mighty even unto the power of deliverance.",
        "For, behold, I have refined thee, I have chosen thee in the furnace of affliction.",
        "Sing, O heavens; and be joyful, O earth; for the feet of those who are in the east shall be established; and break forth into singing, O mountains; for they shall be smitten no more; for the Lord hath comforted his people, and will have mercy upon his afflicted.",
        "And it came to pass that I, Nephi, said unto my father: I will go and do the things which the Lord hath commanded, for I know that the Lord giveth no commandments unto the children of men, save he shall prepare a way for them that they may accomplish the thing which he commandeth them.",
        "But the Lord knoweth all things from the beginning; wherefore, he prepareth a way to accomplish all his works among the children of men; for behold, he hath all power unto the fulfilling of all his words. And thus it is. Amen.",
        "Behold, hath the Lord commanded any that they should not partake of his goodness? Behold I say unto you, Nay; but all men are privileged the one like unto the other, and none are forbidden.",
        "And now, Jacob, I speak unto you: Thou art my first-born in the days of my tribulation in the wilderness. And behold, in thy childhood thou hast suffered afflictions and much sorrow, because of the rudeness of thy brethren.",
        "Nevertheless, Jacob, my first-born in the wilderness, thou knowest the greatness of God; and he shall consecrate thine afflictions for thy gain.",
        "Adam fell that men might be; and men are, that they might have joy.",
        "For my soul delighteth in plainness; for after this manner doth the Lord God work among the children of men. For the Lord God giveth light unto the understanding; for he speaketh unto men according to their language, unto their understanding.",
        "And all thy children shall be taught of the Lord; and great shall be the peace of thy children.",
        "Whosoever shall put their trust in God shall be supported in their trials, and their troubles, and their afflictions, and shall be lifted up at the last day.",
        "The soul shall be restored to the body, and the body to the soul; yea, and every limb and joint shall be restored to its body; yea, even a hair of the head shall not be lost; but all things shall be restored to their proper and perfect frame.",
        "And he will take upon him death, that he may loose the bands of death which bind his people; and he will take upon him their infirmities, that his bowels may be filled with mercy, according to the flesh, that he may know according to the flesh how to succor his people according to their infirmities."
    };
    // endregion

    // Cryptogram is a constructor that takes the number of hints wanted and fills out the attributes
    public Cryptogram(int difficulty){
        Random random = new Random();
        int index = random.nextInt((library.length));
        message = library[index];

        makeCodeKey(difficulty);
        makeDecodeKey();
    }
    
    // makeAlphabet makes and returns an ArrayList of Characters a-z. It uses a for loop to add each letter to the ArrayList by going 
    // through the character sequence by 1.
    private static ArrayList<Character> makeAlphabet(){
        ArrayList<Character> alphabet = new ArrayList<Character>();

        for (char letter = 'a'; letter <= 'z'; letter++) {
            alphabet.add(letter);
        }
        return alphabet;
    }
    
    // makeCodeKey takes a message and makes a map that will code the message. The map consists of a
    // letter object (value) and that letter's realLetter(Key). This is where all letter objects are 
    // created.
    private void makeCodeKey(int difficulty){
        codeKey = new HashMap<>();
        char pseudonym = 'a';
        Random random = new Random();
        int randomIndex = 0;
        ArrayList<Character> alphabet = makeAlphabet();
        hintList = new ArrayList<>();

        // Go through each letter in the message and if it is not a symbol or already apart of the codeKey
        // make a letter object for it.
        for (Character letter : message.toCharArray()) {
            letter = Character.toLowerCase(letter);
            if ((Character.isLetter(letter)) && (!codeKey.containsKey(letter))){
                do {
                    randomIndex = random.nextInt(alphabet.size());
                    pseudonym = alphabet.get(randomIndex);
                } while( pseudonym == letter);
                alphabet.remove(randomIndex);

                Letter l = new Letter();
                l.setRealLetter(letter);
                l.setPseudonym(pseudonym);
                codeKey.put(letter, l);

                // Each letter added as a key to the codeKey is also added to the hint list so it may
                // be chosen as a hint.
                hintList.add(letter);
            }
        }
        // Letters are now randomly selected to make the specified number of hints/
        for(int i = 0; i < difficulty; i++){
            char hint = hintList.remove(random.nextInt(hintList.size()));
            codeKey.get(hint).setHint(true);
        }
    }

    // Decode key makes a map using codeKey where each value is accessed by the pseudonym.
    private void makeDecodeKey(){
        decodeKey = new HashMap<>();
        for(Letter l : codeKey.values()){
            decodeKey.put(l.getPseudonym(), l);
        }
    }
    
    // Display Cryptogram. Returns a string that displays the guesses and coded message.
    public String DisplayCryptogram(){
        String cryptogram = "";
        String guesses = "";
        char pseudonym = 'e';
        char guess = 'b';
        ArrayList<Integer> spaceIndexes = new ArrayList<Integer>();
        int count = 0;
        boolean isHint = false;
        
        // Go through each character in the message, add symbols and code letters to the returned String.
        // Note if a letter is capitalized so that it may be re-capitalized before being displayed. Also
        // create a second string that displays the current guesses of the user.
        for (char letter : message.toCharArray()) {
            Boolean capitalized = false; 
            
            if(Character.isLetter(letter)){
                if (Character.isUpperCase(letter)){
                    capitalized = true;
                    letter = Character.toLowerCase(letter);
                }
                Letter l = codeKey.get(letter);
                pseudonym = l.getPseudonym();
                guess = l.getGuess();
                isHint = l.getHint();

            }
            else{
                pseudonym = letter;
                guess = letter;
            }

            if (capitalized){
                pseudonym = Character.toUpperCase(pseudonym);
                guess = Character.toUpperCase(guess);
            }
            
            if (count > 100 && pseudonym == ' '){
                spaceIndexes.add(cryptogram.length()+1);
                count = 0;
            }

            if (isHint){
                pseudonym = ' ';
                isHint = false;
            }

            guesses += guess;
            cryptogram += pseudonym;
            count++;
        }
        cryptogram = formatCryptogram(guesses, cryptogram, spaceIndexes);
        
        return cryptogram;
    }
    // Called by displayCryptogram, format cryptogram breaks the coded message and guesses up into 
    // lines so that they may be seen side by side.
    private String formatCryptogram(String guesses, String cryptogram, ArrayList<Integer> spaceIndexes){

        StringBuilder finalCryptogram = new StringBuilder();
        int lastIndex = 0;

        if(spaceIndexes.size() > 0){
            spaceIndexes.add(cryptogram.length());

            for (int i : spaceIndexes){

                finalCryptogram.append(guesses.substring(lastIndex, i));
                finalCryptogram.append("\n");
                finalCryptogram.append(cryptogram.substring(lastIndex, i));
                finalCryptogram.append("\n");
                finalCryptogram.append("\n");
    
                lastIndex = i;
            }
        }
        else{
            finalCryptogram.append(guesses);
            finalCryptogram.append("\n");
            finalCryptogram.append(cryptogram);
        }
        
        return finalCryptogram.toString();
    }
}
