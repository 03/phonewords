package com.wind.phonewords;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PhonewordFinderTest {

	private static PhonewordFinder _phonewordFinder = null;
	private static WordReader _dictionary = null;
	
	@BeforeClass
	public static void beforeClass() {
		
		System.out.println("Testing PhonewordFinder Started.");
		
		_dictionary = new WordReader("/dict4test.txt");
		_phonewordFinder = new PhonewordFinder(_dictionary);
		
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("Testing PhonewordFinder Ended.");
	}

	@Test
	public void testWord2num() {
		String num = PhonewordFinder.word2num("FLOWER");
		assertEquals("356937", num);
	}

	@Test
	public void testNum2word() {

		assertTrue(_phonewordFinder.num2words("43289").size() > 0);
		assertTrue(_phonewordFinder.num2words("356937").size() > 0);
		assertTrue(_phonewordFinder.num2words("625673278").size() > 0);
	}
	
	@Test
	public void testNum2wordWithPunctuation() {

		List<String> wordList = _phonewordFinder.num2words("2255.63");
		assertNotNull(wordList);
		assertTrue(wordList.size() > 0);
	}
	
	@Test
	public void testNum2wordWithZeroOrOne() {

		assertTrue(_phonewordFinder.num2words("3569370").size() > 0);
		assertTrue(_phonewordFinder.num2words("3569371").size() > 0);
		assertTrue(_phonewordFinder.num2words("03569371").size() > 0);
	}
	
	@Test
	public void testNum2wordWithMoreThanOneResults() {

		List<String> wordList = _phonewordFinder.num2words("625673278");
		assertTrue(wordList.size() > 0);
	}
	
	@Test
	public void testNum2wordWithConsecutiveNoMatch() {

		List<String> wordList = _phonewordFinder.num2words("35693710");
		assertEquals(0, wordList.size());
	}
	
	
	
}
