package com.example.file.grep;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextSearch {
	public static String searchString(String fileName, String phrase)
			throws IOException {
		Scanner fileScanner = new Scanner(new File(fileName));
		StringBuffer sb = new StringBuffer();
		// 	"([\\s-]*)(public|private|global|)(?!\\=)([\\s-]*)(\\w*)([\\s-]*)(newCommentNew)(?!\\()(\\w*)"
		sb.append("([\\s-]*)(public|private|global)([\\s-]*)(\\w*)([\\s-]*)(").append(phrase).append(")(\\w*)");
		Pattern pattern = Pattern.compile(sb.toString());// ,Pattern.CASE_INSENSITIVE);
		Matcher matcher = null;
		String group = null;
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			matcher = pattern.matcher(line);
			if (matcher.find()) {
				group = matcher.group(4);
			}
		}
		fileScanner.close();
		return group;
	}
}