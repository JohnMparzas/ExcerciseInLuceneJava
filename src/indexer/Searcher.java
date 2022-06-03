package indexer;
import java.io.File; 
import java.io.IOException;
import java.nio.file.Path;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document; 
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query; 
import org.apache.lucene.search.ScoreDoc; 
import org.apache.lucene.search.TopDocs; 
import org.apache.lucene.store.Directory; 
import org.apache.lucene.store.FSDirectory; 
import org.apache.lucene.util.Version; 
import org.apache.lucene.queryparser.classic.QueryParser;
import java.nio.file.Paths;
import java.text.ParseException;
public class Searcher {
	private IndexSearcher isearcher; 
	private QueryParser queryParser; 
	private Query query; 
	private QueryParser[] parsers;
	private IndexReader ireader;
	
	public Searcher(String indexDirectoryPath)throws IOException{
		parsers[0]=new QueryParser(luceneConstants.TIPS,new StandardAnalyzer());
		parsers[1]=new QueryParser(luceneConstants.REVIEWS,new StandardAnalyzer());
		//parsers[2]=new QueryParser(luceneConstants.this,new StandardAnalyzer());
		parsers[3]=new QueryParser(luceneConstants.CATEGORY,new StandardAnalyzer());
		parsers[4]=new QueryParser(luceneConstants.NAME,new StandardAnalyzer());
		
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath)); 
		 ireader=DirectoryReader.open(indexDirectory);
		 isearcher= new IndexSearcher(ireader); 
		 
		 
	
		   
		
	}
	public Query getQuery() {
		return query;
	}
	public IndexSearcher getsearcher() {
		return isearcher;
	}
	public IndexReader getReader() {
		return  ireader;
	}
	public TopDocs search(String searchQuery,int i) throws  IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException{
		queryParser=parsers[i];
		query =queryParser.parse(searchQuery);
		return isearcher.search(query, luceneConstants.MAX_SEARCH);
	}
	public TopDocs search(PhraseQuery query) throws  IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException{
		
		return isearcher.search(query, luceneConstants.MAX_SEARCH);
	}
	
	public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException
	{ 
		return isearcher.doc(scoreDoc.doc); 
	} 
	public void close() throws IOException{ 
		//isearcher.close();
	} 
}
