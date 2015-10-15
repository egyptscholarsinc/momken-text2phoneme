package org.egyptscholars.momken.text2phoneme;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import edu.stanford.nlp.international.arabic.Buckwalter;


/**
 * Text2Phonem
 * 
 * @author Amgad Madkour <amgad.madkour@egyptscholars.org>
 * 
 */
public class App 
{
	
	private static ArrayList<String> createPhonemes(HashMap<String,String> lookupTable,String text){
		
		ArrayList<String> phonemeEntries=new ArrayList<String>();
		
		System.out.println(text);
		//String lastChar = "";
		for(int i=0 ; i < text.length()-1; i++){
			String currentChar = lookupTable.get(Character.toString(text.charAt(i)));
			if (currentChar != null){
				//Add the current character
				phonemeEntries.add(currentChar);
				String nextCh = lookupTable.get(Character.toString(text.charAt(i+1)));
				//Handle Special Case of aa
				if(nextCh != null && (currentChar.startsWith("d.") ||
						currentChar.startsWith("t.")||
						currentChar.startsWith("z.")||
						currentChar.startsWith("s."))){
					
					if(nextCh.charAt(0) == 'a'){
						phonemeEntries.add("a. 70 28 109 71 104");
						i++;
					}else if(nextCh.charAt(0) == 'i'){
						phonemeEntries.add("i. 70 28 109 71 104");
						i++;
					}else if(nextCh.charAt(0) == 'u'){
						phonemeEntries.add("u. 61 35 101 83 102");
						i++;
					}
				}
				//lastChar = currentChar;
			}
		}
		System.out.println("---"+text);
		if(lookupTable.get(Character.toString(text.charAt(text.length()-1)))!=null)
			phonemeEntries.add(lookupTable.get(Character.toString(text.charAt(text.length()-1))));
		
		//Cleanup redundant entries due to Tashkeel (i.e. i. followed by i etc.)
		int sz = phonemeEntries.size();
		for(int i = 0 ; i < sz-1; i++){
			if(phonemeEntries.get(i).charAt(0) == phonemeEntries.get(i+1).charAt(0)){
				phonemeEntries.remove(i+1);
				sz -= 1;
			}
			if(phonemeEntries.get(i).startsWith("u.") && phonemeEntries.get(i+1).startsWith("a")){
				phonemeEntries.remove(i+1);
				sz -= 1;
			}
		}
		System.out.println(phonemeEntries);
		return phonemeEntries;
	}
	
	public static String cleanBuckwalter(String buck){
		
		System.out.println("Before: "+buck);
		StringBuffer buff=new StringBuffer();
		String lst = "A'btvjHxd*rzs$SDTZEgfqklmnhwyaiu}p><O";
		for(int i = 0 ; i < buck.length() ; i++){
			if(lst.contains(String.valueOf(buck.charAt(i)))){
				buff.append(String.valueOf(buck.charAt(i)));
			}
		}
		return buff.toString();
	}
	
	
    public static void main( String[] args ){
    	
    	String temp;
    	BufferedReader lookupRdr;
    	BufferedReader txtRdr;
    	PrintWriter phonemeWrt;
    	HashMap<String,String> lookupTable;
    	String[] parts;
    	ArrayList<String> phonemeEntries= new ArrayList<String>();
    	
        try {
        	
        	//TODO Replace with Apache command Line handling later (cleaner)
        	lookupRdr = new BufferedReader(new FileReader(args[0]));
        	txtRdr = new BufferedReader(new FileReader(args[1]));
        	phonemeWrt = new PrintWriter(new FileWriter(args[2]));
        	
        	lookupTable = new HashMap<String,String>();
        	
			//Load lookup table
			while((temp = lookupRdr.readLine())!= null){
				parts = temp.split("\t");
				lookupTable.put(parts[0], parts[1]);
			}
			
			//Load text
			Buckwalter b = new Buckwalter();
			String words;

			while((temp = txtRdr.readLine())!= null){
				temp = temp.replace(".", "");
				words = b.unicodeToBuckwalter(temp);
		    	parts = words.split(" ");
				for (int i=0 ; i<parts.length; i++){
					String cleaned = cleanBuckwalter(parts[i]);
					if(cleaned.length()>0){
						ArrayList<String> ph = createPhonemes(lookupTable, cleaned);
						phonemeEntries.addAll(ph);
						phonemeEntries.add("_ 5");
					}
				}
			}
			
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
