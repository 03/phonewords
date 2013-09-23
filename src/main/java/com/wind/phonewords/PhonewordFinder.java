package com.wind.phonewords;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhonewordFinder {

	static final String DELIMITER = "-";
	static final String DELIMITER_POS = "#";
	static final Pattern allNumsPtn = Pattern.compile("\\d+");

	private WordReader dictionary = null;

	/**
	 * @param dictionary
	 */
	public PhonewordFinder(WordReader dictionary) {
		super();
		this.dictionary = dictionary;
	}

	/**
	 * Convert word to phone number
	 * 
	 * @param word
	 * @return
	 */
	public static String word2num(String word) {

		// convert word to upper case
		String _word = word.trim().toUpperCase();

		// for each letter, convert to number
		StringBuilder sb = new StringBuilder();
		String _num = "";
		for (int i = 0; i < _word.length(); i++) {
			_num = getNumberEncoding(_word.substring(i, i + 1));
			sb.append(_num);
		}

		return sb.toString();
	}

	/**
	 * Convert phone number to possible words
	 * 
	 * @param phoneNum
	 * @return
	 */
	public List<String> num2words(String phoneNum) {

		// List to store all words
		List<String> wordList = new ArrayList<String>();

		// Extract number, ignore whitespace and punctuation
		String _phoneNum = extractPureNumbers(phoneNum);

		// Get Phone Number Combinations
		List<String> combinations = getNumCombinations(_phoneNum);
		for (int i = 0; i < combinations.size(); i++) {
			// Find match in the dictionary
			wordList.addAll(searchWords(combinations.get(i)));
		}

		System.out.println("WordList for <"+ phoneNum + ">: " + wordList);
		return wordList;
	}

	/**
	 * Get Phone Number Combinations
	 * @param phoneNum
	 * @return
	 */
	private List<String> getNumCombinations(String phoneNum) {

		List<String> numCombinations = new ArrayList<String>();

		// Get Position Combinations for Inserting, each combination may form a new set of words
		List<String> insertPointList = getInsertPositions(phoneNum.length() - 1);
		Iterator<String> itorator = insertPointList.iterator();
		while (itorator.hasNext()) {
			String insertPointStr = itorator.next();
			
			//char[] insertPoints = insertPointStr.toCharArray();
			String[] insertPoints = insertPointStr.split(DELIMITER_POS);

			StringBuilder sb = new StringBuilder();
			String substr = "";
			int lastposition = 0;
			for (int i = 0; i < insertPoints.length; i++) {
				int position = Integer.parseInt(insertPoints[i]);

				substr = phoneNum.substring(lastposition, position);
				if (!"".equals(substr)) {
					sb.append(substr + DELIMITER);
				}

				lastposition = position;
			}
			// Add last part
			sb.append(phoneNum.substring(lastposition));

			numCombinations.add(sb.toString());
		}

		return numCombinations;

	}

	/**
	 * Search matched words and check the result should not contain consecutive
	 * digits remain unchanged <p>
	 * 
	 * @param phoneNumWithDelimeter
	 * E.g: For "123", we will have [123, 1-23, 12-3, 1-2-3] 
	 * @return
	 */
	private List<String> searchWords(String phoneNumWithDelimeter) {
		
		List<String> wordList = new ArrayList<String>();

		// for each formatted string, find matched word from dictionary
		String[] formatted = phoneNumWithDelimeter.split(DELIMITER);
		int countOfParts = formatted.length;

		// check whether match can be found
		boolean shouldProceed = true;
		boolean hasMatch = true;
		for (int i = 0; i < countOfParts; i++) {

			// check no two consecutive parts can remain unchanged
			String part = formatted[i];
			if (hasMatch == false) {

				hasMatch = hasMatchedWord(part);
				if (hasMatch == false) {
					shouldProceed = false;
					break;
				}
				continue;
			}

			hasMatch = hasMatchedWord(part);
			if (!hasMatch) {
				// only one element or number length > 1
				if (countOfParts == 1 || (part.length() > 1)) {
					shouldProceed = false;
					break;
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		// go find match
		if (shouldProceed) {
			sb.setLength(0);

			for (int i = 0; i < countOfParts; i++) {
				String part = formatted[i];
				List<String> words = findMatchedWords(part);
				if (words == null) {
					sb.append(part);
				} else {
					// separated by '|' if more than one words being found
					for (int j = 0; j < words.size(); j++) {
						sb.append(words.get(j) + "|");
					}
					sb.deleteCharAt(sb.length() - 1);
				}
				sb.append(DELIMITER);
			}
			sb.deleteCharAt(sb.length() - 1);

			wordList.add(sb.toString());
		}

		return wordList;

	}

	/**
	 * Delegate to dictionary to do the search
	 * 
	 * @param phoneNum
	 * @return
	 */
	private boolean hasMatchedWord(String phoneNum) {

		return dictionary.hasMatchedWord(phoneNum);
	}

	/**
	 * Delegate to dictionary to do the search
	 * 
	 * @param phoneNum
	 * @return
	 */
	private List<String> findMatchedWords(String phoneNum) {

		return dictionary.findWords(phoneNum);
	}

	/**
	 * Get Position Combinations for Inserting
	 * <p>
	 * 
	 * The simplest algorithm for generating subsets of a set of size N is to
	 * consider all binary numbers using N bits. Each position in the number
	 * represents an element from the set. If a bit in the number is 1, the
	 * corresponding set element is in the subset, otherwise the element isn't
	 * in the subset. Since the bits in a number are ordered, this preserves the
	 * ordering of the original set.
	 * 
	 * @param count
	 * @return
	 * 
	 */
	private static List<String> getInsertPositions(int count) {

		int[] elems = new int[count];
		for (int m = 0; m < count; m++) {
			elems[m] = m + 1;
		}

		int length = elems.length;
		int countOfCombination = (int) Math.pow(2, length) - 1;

		List<String> results = new ArrayList<String>();
		// add whole word
		results.add("0");

		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= countOfCombination; i++) {
			sb.setLength(0);
			for (int j = 0; j < length; j++) {
				if ((i & (1 << j)) != 0) {
					sb.append(elems[j]);
					// add delimiter in case it exceed 9, i.e. 10, 11, 12 ...
					sb.append(DELIMITER_POS);
				}
			}
			results.add(sb.substring(0, sb.length() - 1).toString());
		}

		return results;
	}

	/**
	 * Number encoding table
	 * 
	 * @param characters
	 * @return
	 */
	private static String getNumberEncoding(String characters) {

		if ("ABC".contains(characters)) {
			return "2";
		} else if ("DEF".contains(characters)) {
			return "3";
		} else if ("GHI".contains(characters)) {
			return "4";
		} else if ("JKL".contains(characters)) {
			return "5";
		} else if ("MNO".contains(characters)) {
			return "6";
		} else if ("PQRS".contains(characters)) {
			return "7";
		} else if ("TUV".contains(characters)) {
			return "8";
		} else if ("WXYZ".contains(characters)) {
			return "9";
		} else {
			return characters;
		}

	}

	/**
	 * Extract only numbers from word
	 * 
	 * @param word
	 * @return
	 */
	private static String extractPureNumbers(String word) {

		StringBuilder sb = new StringBuilder();

		Matcher m = allNumsPtn.matcher(word);
		while (m.find()) {
			sb.append(m.group());
			// System.out.println(m.group());
		}

		return sb.toString();

	}

}
