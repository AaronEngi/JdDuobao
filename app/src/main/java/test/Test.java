package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Test {
	/*
	 * 简单日志记录功能
	 */
	public static void saveString(String s, String file) {
		try {
			PrintStream ps = new PrintStream(new File(file));
			ps.println(s);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	
}
