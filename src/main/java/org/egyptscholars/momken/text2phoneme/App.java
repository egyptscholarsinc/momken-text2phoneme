package org.egyptscholars.momken.text2phoneme;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Text2Phonem
 * 
 * @author Amgad Madkour <amgad.madkour@egyptscholars.org>
 * 
 */
public class App 
{
	
	private static ArrayList<String> createPhonemes(HashMap<String,String> lookupTable,StringBuffer text){
		
		ArrayList<String> phonemeEntries=new ArrayList<String>();
		
		String lastChar = "";
		for(int i=0 ; i < text.length(); i++){
			String x = lookupTable.get(Character.toString(text.charAt(i)));
			if (x != null){
				String nextCh = lookupTable.get(Character.toString(text.charAt(i+1)));
				//Handle Special Case of aa
				if(nextCh != null && (nextCh.startsWith("Z") ||
									  nextCh.startsWith("d") ||
									  nextCh.startsWith(".d") ||
									  nextCh.startsWith("m") ||
									  nextCh.startsWith("t.")||
									  nextCh.startsWith("z.")||
									  nextCh.startsWith("s."))
						&& x.startsWith("aa")){
					phonemeEntries.add("a 62 2 88 16 88 23 90 30 90 48 90 55 90 62 93 69 93 76 95 83 95 90 97 97 97");
				}else{
					phonemeEntries.add(x);
				}
				lastChar = x;
			}
		}
		return phonemeEntries;
	}
	
    public static void main( String[] args ){
    	
    	String temp;
    	BufferedReader lookupRdr;
    	BufferedReader txtRdr;
    	PrintWriter phonemeWrt;
    	HashMap<String,String> lookupTable;
    	String[] parts;
    	StringBuffer text;
    	ArrayList<String> phonemeEntries;
    	
        try {
        	
        	//TODO Replace with Apache command Line handling later (cleaner)
        	lookupRdr = new BufferedReader(new FileReader(args[0]));
        	txtRdr = new BufferedReader(new FileReader(args[1]));
        	phonemeWrt = new PrintWriter(new FileWriter(args[2]));
        	
        	lookupTable = new HashMap<String,String>();
        	text=new StringBuffer();
        	
			//Load lookup table
			while((temp = lookupRdr.readLine())!= null){
				parts = temp.split("\t");
				lookupTable.put(parts[0], parts[1]);
			}
			
			//Load text
			while((temp = txtRdr.readLine())!= null){
				temp = temp.replace(".", " . ");
				parts = temp.split(" ");
				for (int i=0 ; i<parts.length; i++){
					text.append(parts[i]);
				}
			}
			
			phonemeEntries = createPhonemes(lookupTable, text);
			
			//Output the results to file
			for(String entry:phonemeEntries){
				phonemeWrt.println(entry);
			}
			
			System.out.println("Phoneme File Created");
			
			phonemeWrt.close();
			lookupRdr.close();
			txtRdr.close();			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
