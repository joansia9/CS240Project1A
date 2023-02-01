package spell;

import java.io.IOException;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {
	
	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("Please provide the dictionary file name and the word to correct as arguments.");
			return;
		}

		String dictionaryFileName = args[0];
		String inputWord = args[1];

		//
        //Create an instance of your corrector here
        //
		ISpellCorrector corrector = new SpellCorrector();
		//ISpellCorrector corrector = null;
		corrector.useDictionary(dictionaryFileName);
		String suggestion = corrector.suggestSimilarWord(inputWord);

		if (suggestion == null) {
		    suggestion = "No similar word found";
		}
		
		System.out.println("Suggestion is: " + suggestion);
	}
}
