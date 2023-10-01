package module1;

// The letter class keeps track of the real letter, the pseudonym and the guess for each letter in the
// Cryptogram. It also notes if the letter has been correctly guessed or not, and if this letter is being
// used as a hint.
public class Letter {
    private char realLetter;
    private char pseudonym;
    private char guess = '_';
    private boolean correct = false;
    private boolean hint = false;

    public char getRealLetter() {
        return realLetter;
    }

    public void setRealLetter(char realLetter) {
        this.realLetter = realLetter;
    }

    public char getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(char pseudonym) {
        this.pseudonym = pseudonym;
    }

    // set guess does not set a guess if this is a hint letter. Otherwise, the guess is set and correct
    // is either set as true of false.
    public void setGuess(char guess) {
        if(hint){
            return;
        }
        
        this.guess = guess;
        if (guess == realLetter){
            correct = true;
        }
        else{
            correct = false;
        }
    }
    
    public char getGuess() {
        return guess;
    }

    public boolean getCorrect(){
        return correct;
    }
    
    public boolean getHint(){
        return hint;
    }
    // Set hint marks this letter as a hint, reveals the realLetter through guess and marks it as correctly
    // guessed
    public void setHint(boolean hint) {
        this.hint = hint;

        guess = realLetter;
        correct = true;
    }

}
