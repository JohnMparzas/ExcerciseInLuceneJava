package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataExtract
{
	private static Indexer indexer;
	private static IndexWriter writer;
	private static final String indexDir = "C:\\Users\\Admin\\Desktop\\anaktish_plhroforias\\index";
	private static final String dataDir = "C:\\Users\\Admin\\Desktop\\anaktish_plhroforias\\arxeia";
	
    public static void main(String[] args) throws IOException
    {       
    	 indexer=new Indexer(indexDir);
    	
        readJsonFile();
    }
    

    private static void readJsonFile() {

        BufferedReader br = null;
        JSONParser parser = new JSONParser();

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("C:\\Users\\Admin\\Desktop\\anaktish_plhroforias\\dataset\\business.json"));
            int i=1;
            while ((sCurrentLine = br.readLine()) != null) {
               // System.out.println("Record:\t" + sCurrentLine);
            	

                Object obj;
                try {
                    obj = parser.parse(sCurrentLine);
                    JSONObject jsonObject = (JSONObject) obj;
                    String city = (String) jsonObject.get("city");
                    //System.out.println(city);
                    if(city.equals("Las Vegas")) {

                    	String business_id = (String) jsonObject.get("business_id");
                    	//System.out.println(business_id);

                    	String name = (String) jsonObject.get("name");
                    	//System.out.println(name);

                    	String address = (String) jsonObject.get("address");
                    	//System.out.println(address);
                    
                    	Double stars = (Double) jsonObject.get("stars");
                    	//System.out.println(stars);
                    
                    	Long review_count =(Long) jsonObject.get("review_count");
                    	//System.out.println(review_count);
                    
                    	String categories = (String) jsonObject.get("categories");
                    	//System.out.println(categories);
                    	
                    	File tipsfile=new File("tipsfile.txt");
                    	File reviewsfile=new File("reviewsfile.txt");
                    	
                    	writetipfile(business_id,tipsfile);
                    	writereviewfile(business_id,reviewsfile);
                    	
                    	indexer.getdoc(tipsfile,reviewsfile,categories,address,business_id,name,review_count,stars);
                    	System.out.println("Record:\t" + i);
                    	i++;
                    }
                    
                    

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println("it's done!");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private static void writetipfile(String business_id,File tipsfile) {
    	BufferedReader br = null;
        JSONParser parser = new JSONParser();

        try {

            String sCurrentLine;
            
            PrintWriter pw=new PrintWriter(tipsfile);

            br = new BufferedReader(new FileReader("C:\\Users\\Admin\\Desktop\\anaktish_plhroforias\\dataset\\tip.json"));

            while ((sCurrentLine = br.readLine()) != null) {
               // System.out.println("Record:\t" + sCurrentLine);

                Object obj;
                try {
                    obj = parser.parse(sCurrentLine);
                    JSONObject jsonObject = (JSONObject) obj;
                    String business_ido = (String) jsonObject.get("business_id");
                    if(business_ido.equals(business_id)) {
                    	String text = (String) jsonObject.get("text");
                    	pw.println(text+"	");
                    }
                    
                    
                    
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    	
    	
    	
    }
    private static void writereviewfile(String business_id,File reviewsfile) {
    	BufferedReader br = null;
        JSONParser parser = new JSONParser();

        try {

            String sCurrentLine;
            
            PrintWriter pw=new PrintWriter(reviewsfile);

            br = new BufferedReader(new FileReader("C:\\Users\\Admin\\Desktop\\anaktish_plhroforias\\dataset\\review.json"));

            while ((sCurrentLine = br.readLine()) != null) {
               // System.out.println("Record:\t" + sCurrentLine);

                Object obj;
                try {
                    obj = parser.parse(sCurrentLine);
                    JSONObject jsonObject = (JSONObject) obj;
                    String business_ido = (String) jsonObject.get("business_id");
                    if(business_ido.equals(business_id)) {
                    	String text = (String) jsonObject.get("text");
                    	pw.println(text+"	");
                    }
                    
                    
                    
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    	
    	
    	
    }
}