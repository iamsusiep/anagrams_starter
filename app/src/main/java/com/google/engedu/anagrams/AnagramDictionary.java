/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer, ArrayList<String>> sizeToWords;


    public AnagramDictionary(Reader reader) throws IOException {
        wordLength = DEFAULT_WORD_LENGTH;
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedStr = sortLetters(word);
            if (lettersToWord.containsKey(sortedStr)){
                lettersToWord.get(sortedStr).add(word);
            }
            else{
                ArrayList<String> values = new ArrayList<>();
                values.add(word);
                lettersToWord.put(sortedStr, values);
            }
            int length = word.length();

            if (sizeToWords.containsKey(length)){
                sizeToWords.get(length).add(word);
            }
            else{
                ArrayList<String> values = new ArrayList<>();
                values.add(word);
                sizeToWords.put(length, values);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);

    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for (String word: wordList){
            if (word.length() == targetWord.length()){
                if (sortLetters(word).equals(sortLetters(targetWord))){
                    result.add(word);
                }
            }
        }
        return result;
    }
    private String sortLetters(String word){
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char i = 'a' ; i <= 'z'; i++){
            String sortedStr = sortLetters(word.concat("" + i));
            if (lettersToWord.containsKey(sortedStr)){
                result.addAll(lettersToWord.get(sortedStr));
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> values = sizeToWords.get(wordLength);
        if (wordLength < MAX_WORD_LENGTH){
            wordLength++;
        }
        while(true){
            String randomWord = values.get(random.nextInt(values.size()));
            if (getAnagramsWithOneMoreLetter(randomWord).size() >= MIN_NUM_ANAGRAMS) {
                return randomWord;
            }
        }

    }

}
