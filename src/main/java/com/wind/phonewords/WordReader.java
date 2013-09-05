package com.wind.phonewords;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Luke Feng
 */
public class WordReader {
	
	/**
	 * @param dictPath
	 */
	public WordReader(String dictPath) {
		super();
		loadDict(dictPath);
	}

	private DictMap dictionary = new DictMap(); // put number as key, word list as value
	private List<String> wordList = new ArrayList<String>();

	public List<String> getWordList() {
		return wordList;
	}

	public Map<String, List<String>> getDictionary() {
		return dictionary;
	}

	private void syncDictFromList() {

		if(dictionary == null) {
			dictionary = new DictMap();
		}
		// clear existing elements
		dictionary.clear();

		String word = "";
		for (int i = 0; i < wordList.size(); i++) {
			word = wordList.get(i);
			dictionary.put(PhonewordFinder.word2num(word), word);
		}
	}

	/**
	 * @param fileName
	 * @return
	 */
	public void loadDict(String fileName) {

		if(fileName == null || "".equals(fileName)) {
			return;
		}
		if(wordList == null) {
			wordList = new ArrayList<String>();
		}
		// clear existing elements
		wordList.clear();

		String line;
		String word;
		try {
			BufferedReader br = null;
			InputStream is = getClass().getResourceAsStream(fileName);
			if(is == null) {
				// another chance to load from file reader
				br = new BufferedReader(new FileReader(fileName));
			} else {
				br = new BufferedReader(new InputStreamReader(is));
			}
			
			while ((line = br.readLine()) != null) {
				word = line.replaceAll("[\\W]|_", "").toUpperCase();
				wordList.add(word);
			}
			// System.out.println("Dictionary ["+ fileName + "] loaded successfully!");

		} catch (IOException e) {
			System.out.println("Dictionary loading failed! " + e.getMessage());
			e.printStackTrace();
		}

		syncDictFromList();
	}
	
	

	public boolean hasMatchedWord(String wordnum) {
		return dictionary.containsKey(wordnum);
	}
	
	public List<String> findWords(String wordnum) {
		
		/*for(Map.Entry e : dictionary.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }*/
		
		return dictionary.get(wordnum);
	}

	class DictMap extends HashMap<String, List<String>> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 924411138076694207L;

		public void put(String key, String word) {
			List<String> current = get(key);
			if (current == null) {
				current = new ArrayList<String>();
				super.put(key, current);
			}
			current.add(word);
		}

	}
	
	public static void main(String[] args) {

		WordReader wordReader = new WordReader("dict.txt");
		System.out.println(wordReader.getDictionary());

	}
	
}
