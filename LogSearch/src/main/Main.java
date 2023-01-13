package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFileChooser choose  = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "log");
		choose.setFileFilter(filter);
		
		int returnVal = choose.showOpenDialog(null);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String pathabs = choose.getSelectedFile().getAbsolutePath();
			System.out.println("file: " + pathabs);
			try {
				Scanner scan = new Scanner(new File(pathabs));
				while(scan.hasNextLine()) {
					System.out.println(scan.nextLine());
				}
				scan.close();
			} catch (FileNotFoundException e) {
				System.out.println("not found");
			}
//			Scanner scanner = new Scanner();
		}
	}

}
