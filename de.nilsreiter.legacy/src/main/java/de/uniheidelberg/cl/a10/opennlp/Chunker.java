package de.uniheidelberg.cl.a10.opennlp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.lang.english.PosTagger;
import opennlp.tools.lang.english.Tokenizer;
import opennlp.tools.lang.english.TreebankChunker;
import opennlp.tools.postag.POSDictionary;
import opennlp.tools.postag.POSTagger;

//converts the output of the OpenNLP Chunker into SalsaTigerXml subcorpora files
//the used main corpus for SalsaTigerXml is the same that was already created

public class Chunker {
	public static void main(String[] args) throws IOException
	// INPUT: one or more ritual txt-files from 01-clean
	// OUTPUT: (into /'user.dir'/output) .xml- SalsaTiger files
	
	  {	String md = "/resources/platforms/opennlp-tools-1.4.3/models/english";
        String posModelPath = md+"/postag/tag.bin.gz";
        String posDictionaryPath = md+"/postag/tagdict";
        String modelChunkerPath = md+"/chunker/EnglishChunk.bin.gz";
        
        Tokenizer tokenizer = new Tokenizer(md+"/tokenize/EnglishTok.bin.gz");
        POSDictionary dictionary = new POSDictionary(posDictionaryPath);
        POSTagger myTagger = new PosTagger(posModelPath, dictionary);
        TreebankChunker myChunker = new TreebankChunker(modelChunkerPath);
         
        
        try {
        	for (int aFile=0; aFile<args.length;aFile++){
        	       	
	        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[aFile]),"UTF-8"));

	        String inputFile = args[aFile];
	        // get the name of the input file for sentence labels
	        Pattern p = Pattern.compile(".*?([^/]+)\\.txt");
	        Matcher m = p.matcher(inputFile);
	        m.matches();
	        String fileId = m.group(1);
	        
	        String path = System.getProperty("user.dir") + "/output/";
        	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path+fileId+".xml"),"UTF-8"));
        	

            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            out.write("<subcorpus name=\""+fileId+"\">" +"\n");

            int sentenceId = 0; //incremented with every new sentence
        	String sentence;
	        while ((sentence = in.readLine()) != null) {
        	
	        	out.write("<s id=\""+fileId+"_"+sentenceId+"\">");
	        	out.write("\n");
	        	
	            String[] tokens = tokenizer.tokenize(sentence);
	        	String[] tags = myTagger.tag(tokens);
	        	ArrayList<String> ntTaglist = new ArrayList<String>(); //stores non-terminal post-tags for inclusion into main corpus file

	        	//generate a list of id labels for parse tree nodes
            	ArrayList<String> idList = new ArrayList<String>();
            	int len = tokens.length;
            	while (len >= 0) {
            		idList.add(String.valueOf(len));
            		len -= 1;
            	}
            	
            	String rootId = idList.get(0);
            	idList.remove(0);
            	out.write("<graph root=\""+fileId+"_"+sentenceId+"_"+rootId+"\">"+"\n");
            	out.write("<terminals>");
            	out.newLine();
            	ArrayList<String> output = new ArrayList<String>(); //array of nonterminals to be written to the output file
            	ArrayList<String> chinks = new ArrayList<String>(); //stores 'chinks'('O') to be written later directly on the root 'S'
            	output.add("<nonterminals>");
            	String[] chunks = myChunker.chunk(tokens, tags);
    	        	for (int ci=0,cn=chunks.length;ci<cn;ci++) {
    	        		String id = idList.get(0);
    	          	  	idList.remove(0);
    	          	  	out.write("<t id=\""+fileId+"_"+sentenceId+"_"+id+"\" word=\"" + tokens[ci]+ "\" pos=\"" + tags[ci] + "\" />");
    	          	  	out.write("\n");
    	        		if (ci > 0 && !chunks[ci].startsWith("I-") && !chunks[ci-1].equals("O")) {
    	        			output.add("</nt>"); //it closes a chunk
    	                }           
    	                if (chunks[ci].startsWith("B-")) { //it 'opens' a chunk(B-CP)
    	                  output.add("<nt id="+'"'+fileId+"_"+sentenceId+"_"+id+"_nt\" cat="+'"'+chunks[ci].substring(2)+'"'+">");
    	                  ntTaglist.add("<edge label=\"_\" " + "idref=" + '"'+ fileId+"_"+sentenceId+"_"+id+"_nt\""+ "/>");
    	                }
    	                if (chunks[ci].equals("O")){ //CHINKS('O') are written directly on the root 'S'
    	                	chinks.add("<edge label=\"_\" " + "idref=" + '"'+ fileId+"_"+sentenceId+"_"+id+'"'+ "/>");
    	                }
    	                else { //for I-CP 
    	                output.add("<edge label=\"_\" " + "idref=" + '"'+ fileId+"_"+sentenceId+"_"+id+'"'+ "/>");
    	                }
    	              }
    	              if (!chunks[chunks.length-1].equals("O")) { //it closes sentences with a period at the end.
    	            	  output.add("</nt>");
    	              }
    	            
	           out.write("</terminals>");
	           out.newLine();
	            for (String line : output) {
	        		out.write(line);
	        		out.newLine();
	        	}
	            out.write("<nt id="+'"'+fileId+"_"+sentenceId+"_"+rootId+"\" cat=\"S\" >" );
	            out.newLine();
	            for(String chink : chinks) {
	            	out.write(chink);
	            	out.newLine();
	            }
	            for(String nonTerminal : ntTaglist) {
	            	out.write(nonTerminal);
	            	out.newLine();
	            }
	            out.write("</nt>");
	            out.newLine();
	            out.write("</nonterminals>");
	            out.newLine();
	            out.write("</graph>");
	            out.newLine();
	            out.write("<matches>");
	            out.newLine();
	            out.write("</matches>");
	            out.newLine();
	            out.write("</s>");
	            out.newLine();
	            sentenceId +=1;
	        }
	        out.write("</subcorpus>");
	        out.close();
	        in.close();
        	}
	    } catch (IOException e) {
	    	System.out.println("Something gone wrong with the parameters!");
	    }
        }
}
   
