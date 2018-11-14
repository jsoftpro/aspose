package pro.jsoft.t9spelling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class T9Test {
	private static final String[] DEFAULT_BUTTONS = { " ", null, "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };
	private static final String[] CYRILLIC_BUTTONS = { " ", null, "абвг", "дежз", "ийкл", "мноп", "рсту", "фхцч", "шщъы", "ьэюя" };

	@Test
	public void testWithDefaultKeypad() {
		T9 clicker = new T9(DEFAULT_BUTTONS);
		assertFalse(clicker.type("4").isPresent());
		assertEquals("44 444", clicker.type("hi").get());
		assertEquals("4433555 555666096667775553", clicker.type("hello world").get());
	}
	
	@Test
	public void testWithCyrillicPad() {
		T9 clicker = new T9(CYRILLIC_BUTTONS);
		assertFalse(clicker.type("4").isPresent());
		assertFalse(clicker.type("hi").isPresent());
		assertEquals("555564222336660546", clicker.type("привет мир").get());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWithBadArgument() {
		new T9(new String[] {});
	}
}
