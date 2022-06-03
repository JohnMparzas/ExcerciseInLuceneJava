package indexer;

import java.io.FileFilter;
import java.io.IOException;
import java.text.ParseException;

import javax.management.Query;

import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document; 
import org.apache.lucene.queryparser.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc; 
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
public class LuceneTester 
{ 
	private static final String indexDir = "C:\\Users\\Admin\\Desktop\\anaktish_plhroforias\\index";
	private static final String dataDir = "C:\\Users\\Admin\\Desktop\\anaktish_plhroforias\\arxeia"; 
	pIndexer indexer; 
	private Searcher searcher;
	private QueryParser queryParser;
	public static void main(String[] args) throws org.apache.lucene.queryparser.classic.ParseException 
	{ 
		LuceneTester tester; 
		try 
		{ 
			tester = new LuceneTester(); 
			tester.createIndex(); 
			tester.search("Mohan"); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
		catch (ParseException e) 
		{ 
			e.printStackTrace(); 
		}
	} 
	private void createIndex() throws IOException
	{ 
		indexer = new Indexer( indexDir); 
		
		int numIndexed; 
		long startTime = System.currentTimeMillis(); 
		numIndexed = indexer.createIndex(dataDir, new TextFilter()); 
		long endTime = System.currentTimeMillis(); 
		indexer.close(); 
		System.out.println(numIndexed+" File indexed, time taken: " +(endTime-startTime)+" ms"); 
	} 
	private void search(String searchQuery) throws ParseException,IOException, org.apache.lucene.queryparser.classic.ParseException
	{ 
		searcher = new Searcher( indexDir); 
		long startTime = System.currentTimeMillis(); 
		QueryParser  queryParser = new QueryParser(luceneConstants.CONTENTS,new StandardAnalyzer());
		//org.apache.lucene.search.Query query = queryParser.parse(searchQuery);
		
		TopDocs hits = searcher.search(searchQuery); 
		long endTime = System.currentTimeMillis(); 
		System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime)); 
		for(ScoreDoc scoreDoc : hits.scoreDocs) 
		{ 
			Document doc = searcher.getDocument(scoreDoc); 
			System.out.println("File: " + doc.get(luceneConstants.FILE_PATH)); 
		} 
		searcher.close(); 
		
	} 
}
