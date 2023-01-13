package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONObject;

public class Main {

	public static ArrayList<String> getKeyword (String datakeyword) {

		ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(datakeyword.split(",")));
		return keywords;
	}
	

	public static void main(String[] args) {

		System.out.println("masukkan keyword, pisahkan dengan tanda koma");
		Scanner scanner = new Scanner(System.in);
		String datakeyword = scanner.nextLine(); 
		ArrayList<String> keyword = getKeyword(datakeyword);
		System.out.println(keyword);
		JFileChooser choose  = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "log");
		choose.setFileFilter(filter);
		
		int returnVal = choose.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String pathabs = choose.getSelectedFile().getAbsolutePath();
			JSONObject json = new JSONObject();
			for(int i = 0; i < keyword.size(); i++) {
				int hitungKeywords = 0;
				try {
					Scanner scan = new Scanner(new File(pathabs));
					while(scan.hasNextLine()) {
						if(scan.nextLine().contains(keyword.get(i))) {
							hitungKeywords++;
						}
					}
					scan.close();
				} catch (FileNotFoundException e) {
					System.out.println("not found");
				}
				json.put(keyword.get(i), hitungKeywords);
			}
			System.out.println(json.toString());
		}
	}

}
