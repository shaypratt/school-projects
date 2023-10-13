// CS2210 Assignment 2
// Author: Shaylan Pratt
// The purpose of this code is to implement a simple spell checker 

import java.util.ArrayList;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Spell {
	static Hashtable<String, Boolean> ht = new Hashtable<>();
    String check;

    // constructor takes in two arguments, the dictionary and the file to check
    Spell(String dictionary, String check){
        // Load dictionary words from file into Hashtable
    	BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(dictionary));
			String line = reader.readLine();   // read the first line in the file
            String key = line;
            ht.put(line, true);         // load the word into the hashtable, whith the key as the word
			while (line != null) {
				line = reader.readLine();     // load the rest of the words into the hastable 
                if (line != null){
                    key = line;
                    ht.put(key, true);
                }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        // Load words in fileToCheck.txt
        try {
			reader = new BufferedReader(new FileReader(check));
			String line = reader.readLine();           // read the first line in the file
            this.check = (line.toLowerCase() +"\n");   // convert the word to lowercase 
            Spell.checkSpelling(line.toLowerCase());   // call the check spelling function on the word
			while (line != null) {
				line = reader.readLine();              // continue to read the words from the file
                if (line != null){
                    this.check += (line.toLowerCase() + "\n");
                    Spell.checkSpelling(line.toLowerCase());  // call the check spelling function to chech the spelling of the word
                }
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        // init an object of type Spell
    	new Spell(args[0], args[1]);
    }


    // this function check if the dictionay is loaded or not
    public static Hashtable<String, Boolean> getDictionary(){
        return ht;
    }

    
    // This function takes a String word as an argument to check if the word exists in the dictionary. 
    // If the word exists, it will print it with a message "Incorrect Spelling:" to the console.
    // Else it will call the suggestCorrections function to provide the correct word from the words given in the dictionary file.
    public static boolean checkSpelling(String word){
       String key = word; 
       if (ht.get(key) != null){      // check to see of the input word is in the hashtable
            System.out.println("Correct Spelling: " + word + "\n");
            return true;
       }
       else{      
            return suggestCorrections(word);  // the word is not in the hasttable so call the suggest corrections function
            
       }
    }


    // This function takes a String input word as argument.
    // It starts by printing the message word: Incorrect Spelling to the console.
    // The function also uses four different methods (correctSpellingWithSubstitution,
    // correctSpellingWithOmission, correctSpellingWithInsertion, correctSpellingWithReversal)
    // to generate possible corrected spellings for the input word.
    // The function then returns the suggestions list which contains the possible corrected spellings.
    public static Boolean suggestCorrections(String word) {
        System.out.println(word+": "+"Incorrect spelling");
        String sub = correctSpellingSubstitution(word);  // call the functions to find a word to suggest 
        String omi = correctSpellingWithOmission(word);
        String rev = correctSpellingWithReversal(word);
        String ins = correctSpellingWithInsertion(word).toString().replace("[","").replace("]","").replace(",","");
        // this is cheking to make sure the same word is not printed twice 
        if(sub != "\0"){
            System.out.print(sub + ' ');
        }
        if(omi != "\0" && omi != sub){
            System.out.print(omi + ' ');
        }
        if(rev != "\0" && rev != sub && rev != omi){
            System.out.print(rev + ' ');
        }
        if(ins != ""){
            if(ins.contains(sub + " ")){
                if(ins.indexOf(sub) == 0){
                    ins = ins.substring(0, ins.indexOf(sub)) + ins.substring(ins.indexOf(sub) + 1 + sub.length(), ins.length());
                }
                else if(ins.indexOf(sub) == ins.length()){
                    ins = ins.substring(0, ins.indexOf(sub) - 1) + ins.substring(ins.indexOf(sub) + sub.length(), ins.length());
                }
                else{
                    ins = ins.substring(0, ins.indexOf(sub) - 1) + ins.substring(ins.indexOf(sub) + 1 + sub.length(), ins.length());
                }
            }
            if(ins.contains(omi + " ") ){
                if(ins.indexOf(omi) == 0){
                    ins = ins.substring(0, ins.indexOf(omi)) + ins.substring(ins.indexOf(omi) + 1 + omi.length(), ins.length());
                }
                else if(ins.indexOf(omi) == ins.length()){
                    ins = ins.substring(0, ins.indexOf(omi) - 1) + ins.substring(ins.indexOf(omi) + omi.length(), ins.length());
                }
                else{
                    ins = ins.substring(0, ins.indexOf(omi) - 1) + ins.substring(ins.indexOf(omi) + 1 + omi.length(), ins.length());
                }
            }
            if(ins.contains(rev + " ")){
                if(ins.indexOf(rev) == 0){
                    ins = ins.substring(0, ins.indexOf(rev)) + ins.substring(ins.indexOf(rev) + 1 + rev.length(), ins.length());
                }
                else if(ins.indexOf(rev) == ins.length()){
                    ins = ins.substring(0, ins.indexOf(rev) - 1) + ins.substring(ins.indexOf(rev) + rev.length(), ins.length());
                }
                if(ins.indexOf(rev) != 0){
                    ins = ins.substring(0, ins.indexOf(rev) - 1) + ins.substring(ins.indexOf(rev) + 1 + rev.length(), ins.length());
                }
            }
            System.out.print(ins + ' ');
        }
        System.out.print('\n');
        if(ht.get(sub) != null || ht.get(omi) != null || ht.get(rev) != null || ht.get(ins) != null){
            return true;
        }
        else{
            return false;
        }
    }

    // This function takes in a string word and tries to correct the spelling by substituting letters and 
    // check if the resulting new word is in the dictionary.
    static String correctSpellingSubstitution(String word) {
        String key = word; 
        StringBuilder build_word;
        for(int i=0; i< word.length(); i++){      // loop through the characters in the word
            build_word = new StringBuilder(word);
            for(int j = 97; j<123; j++){          // loop through the alphabet 
                build_word.setCharAt(i, (char)j); // replace a letter with a new letter
                key = (build_word.toString());
                if(ht.get(key) != null){         // check if the new word is in the dictionary
                    return build_word.toString();
                }
            }
        }
        return "\0";
    }

    // This function tries to omit (in turn, one by one) a single character in the misspelled word 
    // and check if the resulting new word is in the dictionary.
    static String correctSpellingWithOmission(String word) {
        StringBuilder build_word;
        for(int i=0; i<word.length(); i++){       // loop through the letters in the word
            build_word = new StringBuilder(word);
            build_word.deleteCharAt(i);          // delete the letter
            String key = (build_word.toString());
            if (ht.get(key)!=null){              // check if the new word is in the dictionary 
                return build_word.toString();
            }
        }
        return "\0";
    }

    // This function tries to insert a letter in the misspelled word 
    // and check if the resulting new word is in the dictionary.
    static ArrayList<String> correctSpellingWithInsertion(String word) {
        int len = word.length();
        ArrayList<String> list = new ArrayList<String>();
        String new_word;
        for(int i=1; i<len+1; i++){   // loop to iterate through the letters in the word
            for(int j = 97; j<123; j++){  // loop to iterate through the letters in the alphabet 
                char ch = (char)j;        // convert from ascii to char 
                new_word = word.substring(0, i) + ch + word.substring(i, len); //insert the char
                String key = new_word;
                if(ht.get(key)!= null){  // check if the new word is in the dictionary 
                    list.add(new_word);  // add the new word to the ;ist

                }
            }
        }
        return list;
    }
    
    // This function tries swapping every pair of adjacent characters 
    // and check if the resulting new word is in the dictionary.
    static String correctSpellingWithReversal(String word) {
        for (int i = 0; i < word.length() - 1; i ++) {    // loop to iterate through the letters in the word
            char[] ch = word.toCharArray();  // create an array of the characters in the word
            char temp = ch[i];  // create a temporary variable that hold the char of the index we are at 
            ch[i] = ch[i + 1];  
            ch[i + 1] = temp;  // swap the two characters 
            String st = new String(ch);  // convert the array back to a string 
            String key = st;
            if (ht.get(key) != null ){  // check if the new word is in the dictionary 
                return st;
            }
        }
        return "\0";
    }
} 