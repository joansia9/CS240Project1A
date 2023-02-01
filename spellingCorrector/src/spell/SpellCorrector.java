package spell;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {
    private final Trie dictionary;
    private final TreeSet<String> candidates; //empty list to store possible words
    private final TreeSet<String> validCandidates; //empty list to store CORRECT words

    public SpellCorrector() {
        dictionary = new Trie();
        candidates = new TreeSet<String>();
        validCandidates = new TreeSet<String>();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        processFile(file);
    }

    public void processFile(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("(#[^\\n]*\\n)|(\\s+)+");

        while(scanner.hasNext()) {
            String str = scanner.next();
            dictionary.add(str);
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        if (inputWord.equals("")) return null;

        inputWord = inputWord.toLowerCase();
        if (dictionary.find(inputWord) != null) return inputWord;

        candidates.clear();
        validCandidates.clear();

        deletionCheck(inputWord);
        transpositionCheck(inputWord);
        alterationCheck(inputWord);
        insertionCheck(inputWord);

        String mostCommonWord;
        mostCommonWord = validateCandidates();

        if (validCandidates.isEmpty()) {

            TreeSet<String> clone_candidates = (TreeSet)candidates.clone();

            for (String str : clone_candidates) {
                deletionCheck(str);
                transpositionCheck(str);
                alterationCheck(str);
                insertionCheck(str);
            }
            mostCommonWord = validateCandidates();
            if (validCandidates.isEmpty()) return null;
        }

        //we want 1 word
        if (validCandidates.size() == 1) return validCandidates.first();

        return mostCommonWord;
    }

    public void deletionCheck(String word) {
        //remember that 2 letters is like "to" or "so" and if less, it is not a word to delete from
        if (word.length() < 2) return;
        //create a word to manipulate!
        StringBuilder newWord = new StringBuilder();
        //iterate through the inputted word
        //For each character, append the input word to the newWord object and delete the current character at the index i
        for (int i = 0; i < word.length(); i++) {
            //add the word to the newWord
            newWord.append(word);
            newWord.deleteCharAt(i);
            //add to new candidates!
            candidates.add(newWord.toString());
            //Clear the newWord object by setting its length to 0
            newWord.setLength(0);
        }
    }

    public void transpositionCheck(String word) {
        //remember if it is a character, theres nothing we can do!
        if (word.length() < 2) return;

        StringBuilder newWord = new StringBuilder();
        StringBuilder subString = new StringBuilder();

        for (int i = 0; i < word.length() - 1; i++) {
            newWord.append(word);
            //extract the pair of characters and append it to the
            subString.append(newWord.substring(i, i+2));
            //Reverse the order of characters in the subString object.
            subString.reverse();
            //replace the pair of characters in the newWord object with the reversed pair of characters in the subString object.
            newWord.replace(i,i + 2, subString.toString());
            //add modified word to candidates
            candidates.add(newWord.toString());
            newWord.setLength(0); //clear
            subString.setLength(0); //clear
        }
    }

    public void alterationCheck(String word) {
        StringBuilder newWord = new StringBuilder();
        char letter;

        //iterate through each letter and compare each letter with all 26 letters of the alphabet
        for (int i = 0; i < word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                //replace each letter at a time with a letter from the alphabet
                letter = 'a';
                letter += j;
                //add the inputed word to newWord (like cloning)
                newWord.append(word);
                //switch the new letter with the index of the newWord (same as inputted word)
                newWord.setCharAt(i, letter);
                //add the modified to possiblities!
                candidates.add(newWord.toString());
                newWord.setLength(0); //clear
            }
        }

    }

    public void insertionCheck(String word) {
        StringBuilder newWord = new StringBuilder();
        char letter;

        for (int i = 0; i < word.length() + 1; i++) {
            for (int j = 0; j < 26; j++) {
                letter = 'a';
                letter += j;
                newWord.append(word);
                //inserts a letter to index i
                newWord.insert(i, letter);
                //add to possibilities
                candidates.add(newWord.toString());
                newWord.setLength(0); //clear
            }
        }

    }

    public String validateCandidates() {
        int max = 0;
        INode tempNode;
        String mostCommonWord = null; //what we will output

        //iterate through each of the candidates
        for (String currCandidates : candidates) {
            //find if it is in the dictionary
            tempNode = dictionary.find(currCandidates);
            //if it is not null, it is found in the dictionary, and it is a valid candidate
            if (tempNode != null) {
                validCandidates.add(currCandidates);
                //if the value of the node is greater than max,
                // set max to the value of the node and
                // set mostCommonWord to the current word
                if (tempNode.getValue() > max) {
                    max = tempNode.getValue();
                    mostCommonWord = currCandidates;
                }
            }
        }
        return mostCommonWord;
    }

}