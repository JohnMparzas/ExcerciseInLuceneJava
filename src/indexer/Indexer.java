package indexer;
import java.io.File; 
import java.io.FileFilter; 
import java.io.FileReader; 
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer; 
import org.apache.lucene.document.Document; 
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException; 
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableFieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory; 
import org.apache.lucene.store.FSDirectory; 
import org.apache.lucene.document.NumericDocValuesField;;
//import org.apache.lucene.util.Version; 
//import org.apache.lucene.util.*;
//import org.apache.lucene.codecs.*;
//import org.apache.lucene.search.*;




public class Indexer {
	private IndexWriter writer;
	private IndexableFieldType NOT_ANALYZED;
	
	public Indexer(String indexDirectoryPath) throws IOException{

		Analyzer analyzer = new StandardAnalyzer();
		//this directory will contain the indexes 
		Directory indexDirectory=FSDirectory.open(Paths.get(indexDirectoryPath));//------
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer=new IndexWriter(indexDirectory,config);
	}
	public Indexer() {
		// TODO Auto-generated constructor stub
	}
	public int createIndex(String dataDirPath,FileFilter filter) throws  IOException {
		File[] files=new File(dataDirPath).listFiles();
		for(File file:files) {
			if(!file.isDirectory() && !file.isHidden() &&file.exists() &&file.canRead() &&filter.accept(file)) {
					indexFile(file);
			
			}
		}
		return writer.numRamDocs();
	}
	public void indexFile(File file) throws IOException{
		System.out.println("Indexing "+file.getCanonicalPath());
		//Document document=getDocument(file);
		System.out.println("teos getDoc2");
		//writer.addDocument(document);
	}
	/*public Document getDocument(File file)throws IOException{
		
		Document document=new Document();
		System.out.println("t");
		
		//TextField contentField = new TextField(luceneConstants.CONTENTS, new FileReader(file));
	     // TextField fileNameField = new TextField(luceneConstants.FILE_NAME, file.getName(),TextField.Store.YES);
	     // TextField filePathField = new TextField(luceneConstants.FILE_PATH,file.getCanonicalPath(),TextField.Store.YES);
	      //TextField addressf = new TextField(luceneConstants.ADDRESS,address,TextField.Store.YES);
	      //Field idf=new Field(luceneConstants.ID, ids, NOT_ANALYZED);
	      
	      
		//Field contentField = new Field(luceneConstants.CONTENTS, new FileReader(file),NOT_ANALYZED); ///=====edw provlima
		//Field con=new Field(luceneConstants.CONTENTS, new FileReader(file), NOT_ANALYZED);
		System.out.println("te getDoc");
		
		///Field fileNameField = new Field(luceneConstants.FILE_NAME, file.getName(), NOT_ANALYZED); 
		//Field filePathField = new Field(luceneConstants.FILE_PATH, file.getCanonicalPath(), NOT_ANALYZED); 
		
		//Field filePathField = new Field(luceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES,Field.Index.NOT_ANALYZED); 
		System.out.println("teos getDoc");
		
			//document.add(contentField); 
			//document.add(fileNameField); 
			//document.add(filePathField); 
			
			return document; 
	}*/
	public void getdoc(File filetips,File filereviews,String category,String address,String id,String name,long review_count,double stars) {
		Document document=new Document();
		//System.out.println("t");
		try {
		  TextField tips = new TextField(luceneConstants.TIPS, new FileReader(filetips));
	      TextField reviews = new TextField(luceneConstants.REVIEWS,  new FileReader(filereviews));
	      TextField categoryf = new TextField(luceneConstants.CATEGORY,category,TextField.Store.YES);
	      TextField addressf = new TextField(luceneConstants.ADDRESS,address,TextField.Store.YES);
	      Field idf=new Field(luceneConstants.ID, id, NOT_ANALYZED);
	      TextField namef = new TextField(luceneConstants.NAME,name,TextField.Store.YES);
	      Field review_countf=new Field(luceneConstants.REVIEW_COUNT, ""+review_count,NOT_ANALYZED);// int field prepei
	      Field starsf=new Field(luceneConstants.STARS,""+ stars, NOT_ANALYZED);
	      document.add(tips);
	      document.add(reviews);
	      document.add(categoryf);
	      document.add(addressf);
	      document.add(idf);
	      document.add(namef);
	      document.add(review_countf);
	      document.add(starsf);
	      writer.addDocument(document);
	      
	      
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	      
	      
	      
		//Field contentField = new Field(luceneConstants.CONTENTS, new FileReader(file),NOT_ANALYZED); ///=====edw provlima
		//Field con=new Field(luceneConstants.CONTENTS, new FileReader(file), NOT_ANALYZED);
		//System.out.println("te getDoc");
		
		///Field fileNameField = new Field(luceneConstants.FILE_NAME, file.getName(), NOT_ANALYZED); 
		//Field filePathField = new Field(luceneConstants.FILE_PATH, file.getCanonicalPath(), NOT_ANALYZED); 
		
		//Field filePathField = new Field(luceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES,Field.Index.NOT_ANALYZED); 
		//System.out.println("teos getDoc");
		
			//document.add(contentField); 
			//document.add(fileNameField); 
			//document.add(filePathField); 
			
			//return document; 
		
	}
	public void close() throws CorruptIndexException, IOException{
		writer.close();	
		
	}
	

}
