package com.wind.phonewords;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class WordReaderTest {

	private static WordReader _dictionary = null;
	
	@BeforeClass
	public static void beforeClass() {
		
		System.out.println("Testing WordReader Started.");
		
		_dictionary = new WordReader("/dict4test.txt");
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("Testing WordReader Ended.");
	}
	
	@Test
	public void testGetWordList() {
		assertTrue(_dictionary.getWordList().size() > 0);
	}

	@Test
	public void testGetDictionary() {
		assertTrue(_dictionary.getDictionary().size() > 0);
	}

	@Test
	public void testLoadDict() {
		_dictionary.loadDict("/dict4test-3words.txt");
		assertEquals(3, _dictionary.getWordList().size());
		_dictionary.loadDict("/dict4test.txt");
	}

	@Test
	public void testHasMatchedWord() {
		assertTrue(_dictionary.hasMatchedWord("356937"));
	}

	@Test
	public void testFindWords() {
		assertTrue(_dictionary.findWords("356937").size() > 0);
	}

}
