package pro.jsoft.t9spelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class T9 {
	/** Minimum character code to reduce mapping array index */
	private final int offset;
	/** Mapping of character codes to keypress representations */
	private final String[] charMap;

	/**
	 * Creates a new {@code T9} instance with relations between characters and its numeric representations
	 * 
	 * @param buttons
	 * 		  The string array which contains letters binded to keys where index in array is a number of pad key
	 * 
	 * @throws IllegalArgumentException
	 * 		   If the {@code buttons} argument is null or its size out of numeric buttons range
	 */
	
	public T9(String[] buttons) {
		if (buttons == null || buttons.length == 0 || buttons.length > 10) {
			throw new IllegalArgumentException("Size of the string array must be between 1 and 10");
		}

		final String alphabeth = Arrays.asList(buttons)
				.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.joining(""));
		
		final char[] codes = alphabeth.toCharArray();
		Arrays.sort(codes);
		offset = codes[0];
		charMap = new String[codes[codes.length - 1] - offset + 1];
		Arrays.fill(charMap, null);
		
		int num = 0;
		for (char ch : alphabeth.toCharArray()) {
			while (num < buttons.length) {
				if (buttons[num] != null) {
					int multi = buttons[num].indexOf(ch);
					if (multi >= 0) {
						final String[] clickSequence = new String[multi + 1];
						Arrays.fill(clickSequence, String.valueOf(num));
						charMap[ch - offset] = String.join("", clickSequence);
						break;
					}
				}
				++ num;
			}
		}
	}

	/**
	 * Returns the {@code String} which contains a sequence of keypresses
	 * 
	 * @param s
	 * 		  The line for typing
	 * 
	 * @return the {@code Optional} with sequence of keypresses or empty if there were non-alpha characters only
	 */
	
	public Optional<String> type(String s) {
		StringBuilder sb = new StringBuilder();
		for (int ch : s.toCharArray()) {
			int index = ch - offset;
			if (index >= 0 && index < charMap.length) {
				String clickSequence = charMap[index];
				if (clickSequence != null) {
					if (sb.length() > 0 && clickSequence.charAt(0) == sb.charAt(sb.length() - 1)) {
						sb.append(' ');
					}

					sb.append(clickSequence);
				}
			}
		}
		
		return (sb.length() == 0) ? Optional.empty() : Optional.of(sb.toString());
	}

	private static final String[] BUTTONS = { " ", null, "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };

	public static void main(String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException("The first argument must be a full name of input text file");
		}
		
		File inputFile = new File(args[0]);
		try (Scanner fileScanner = new Scanner(inputFile)) {
			T9 clicker = new T9(BUTTONS);
			int i = 1;
			while (fileScanner.hasNextLine()) {
				Optional<String> encoded = clicker.type(fileScanner.nextLine());
				if (encoded.isPresent()) {
					System.out.println(String.format("Case #%d: %s", i++, encoded.get()));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
