// Name: Vincent Ma
// TA: Tyler Mi
// CSE 143
// Assignment #4
// This class keeps track of a game of Evil Hangman. It cheats by not picking a word in the beginning,
// and instead keeping its options open to a vareity of different possible words.

import java.util.*;

public class HangmanManager{

   private int length;
   private int max;
   private String word;
   private Set<String> possible;
   private Map<String, Set<String>> patternMap;
   private SortedSet<Character> guesses;
   private int wrongGuesses;

   // pre: accepts a collection that serves as a word bank, an integer for desired word length,
   //      and an integer number of guesses
   // post: adds all words of the desired length into the possible set of words
   public HangmanManager(Collection<String> dictionary, int length, int max){
      if(length < 1 || max < 0){
         throw new IllegalArgumentException();
      }
      this.length = length;
      this.max = max;
      possible = new TreeSet<String>();
      for(String word : dictionary){
         if(word.length() == length){
            possible.add(word);
         }
      }
      word = "";
      for(int i = 0; i < length; i++){
         word += "- ";
      }
      guesses = new TreeSet<Character>();
   }

   // post: returns the possible set of words
   public Set<String> words(){
      return possible;
   }

   // post: returns the number of guesses left
   public int guessesLeft(){
      return max - wrongGuesses;
   }

   // post: returns the guesses made by the client
   public SortedSet<Character> guesses(){
      return guesses;
   }
   
   // pre: if there aren't any possible words left, throws an IllegalArgumentException
   // post: returns the current pattern
   public String pattern(){
      if(possible.isEmpty()){
         throw new IllegalArgumentException();
      }
      return word;
   }

   // pre: accepts a char as a guess from the client
   //      if the client doesn't have any guesses left or there aren't any possible words,
   //      throws an IllegalStateException
   //      if guess has already been made, throws an IllegalArgumentException
   // post: returns the number of occurences a letter has in the pattern
   public int record(char guess){
      if(guessesLeft() < 1 || possible.isEmpty()){
         throw new IllegalStateException();
      }
      if(!possible.isEmpty() && guesses.contains(guess)){
         throw new IllegalArgumentException();
      }
      guesses.add(guess);
      Map<String, Set<String>> patternMap = makeMap(guess);
      possible = biggestGroup(patternMap);
      int occurences = 0;
      for(int i = 0; i < word.length(); i++){
         if(word.charAt(i) == guess){
            occurences++;
         }
      }
      if(occurences == 0){
         wrongGuesses++;
      }
      return occurences;
   }
   
   // pre: accepts a char as a guess made by the client
   // post: returns a map of all possible patterns and their corresponding words
   private Map<String, Set<String>> makeMap(char guess){
      Map<String, Set<String>> patternMap = new TreeMap<String, Set<String>>();
      int counter = 0;
      for(String s : possible){
         String key = "";
         for(int i = 0; i < length; i++){
            if(s.charAt(i) == guess){
               key += guess + " ";
               counter++;
            }else{
               key += "- ";
            }
         }
         if(!patternMap.containsKey(key)){
            patternMap.put(key, new TreeSet<String>());
            patternMap.get(key).add(s);
         }else{
            patternMap.get(key).add(s);
         }
      }
      return patternMap;
   }
   
   // pre: accepts a map that has patterns with their corresponding possible words
   // post: returns the set with the largest size in the map
   private Set<String> biggestGroup(Map<String, Set<String>> patternMap){
      Set<String> group = new TreeSet<String>();
      for(String pattern: patternMap.keySet()){
         if(group.size() < patternMap.get(pattern).size()){
            group = patternMap.get(pattern);
            word = pattern;
         }
      }
      return group;
   }

}