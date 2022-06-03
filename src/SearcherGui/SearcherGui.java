package SearcherGui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.text.Highlighter;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.xml.builders.MatchAllDocsQueryBuilder;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryRescorer;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.QueryBuilder;

import indexer.Indexer;
import indexer.Searcher;
import indexer.luceneConstants;

import javax.swing.JTabbedPane;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.SimpleFormatter;

import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import java.awt.TextArea;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;


public class SearcherGui {

	private JFrame frame;
	private JTextField txtKoogle;
	private String txtquery=null;
	private int querykind=0;
	private TopDocs hits;
	private TextArea textArea;
	private static final String indexDir = "C:\\Users\\Admin\\Desktop\\anaktish_plhroforias\\index";
	//private static final String dataDir = "C:\\Users\\Admin\\Desktop\\anaktish_plhroforias\\arxeia"; 
	private Indexer indexer; 
	private Searcher searcher;
	private QueryParser queryParser;
	private boolean fixedorder=true;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearcherGui window = new SearcherGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SearcherGui() {
		initialize();
	}
	private void search(String searchQuery,int querykindl) throws ParseException,IOException, org.apache.lucene.queryparser.classic.ParseException
	{ 
		searcher = new Searcher( indexDir); 
		long startTime = System.currentTimeMillis(); 
		//QueryParser  queryParser = new QueryParser(luceneConstants.CONTENTS,new StandardAnalyzer());
		//org.apache.lucene.search.Query query = queryParser.parse(searchQuery);
		
		 hits = searcher.search(searchQuery,querykindl); 
		long endTime = System.currentTimeMillis(); 
		System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime)); 
		showResults();
		
	} 
	private void PhraseSearch(String phrase) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
		searcher = new Searcher( indexDir);
		String [] tokens=phrase.split(" ");
		PhraseQuery phrasequery=new PhraseQuery(querykind, phrase, tokens);
		//PhraseQuery phrasequery2=new PhraseQuery(querykind, phrase, null);
		
		
		
		
		 hits = searcher.search(phrasequery);
		 
		 showResults();
		
	}
	private void BooleanSearch(String query) throws CorruptIndexException, IOException, org.apache.lucene.queryparser.classic.ParseException {
		searcher = new Searcher( indexDir);
		
		
		Object builder;
		/**Query[] queries;
		String [] tokens=query.split(" ");
		 queries[0]=new TermQuery(new Term(luceneConstants.NAME,query));
		 queries[1]=new TermQuery(new Term(luceneConstants.CATEGORY,query));
		 queries[2]=new PhraseQuery(querykind,query,tokens);
		 queries[3]=new PhraseQuery(querykind,query,tokens);**/
		 
		 StandardAnalyzer analyzer = new StandardAnalyzer();
		 MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"NAME","CATEGORY","TIPS","REVIEWS"},analyzer);
		 Query q = parser.parse(query);
		 hits=searcher.getsearcher().search(q, luceneConstants.MAX_SEARCH);
		 showResults();
		 
		/**bquery.add(query1,BooleanClause.Occur.MUST);
		query = builder.createBooleanQuery(field, query, occur);
				
		BooleanQuery booleanQuery = new BooleanQuery(); 
		TermQuery catQuery1 = new TermQuery(new Term("category_name", "Electronics"));
		TermQuery catQuery2 = new TermQuery(new Term("category_name", "Home"));
		booleanQuery.add(new BooleanClause(catQuery1, BooleanClause.Occur.SHOULD));
		 booleanQuery.add(frame, new BooleanClause(catQuery2, BooleanClause.Occur.SHOULD), querykind);**/
		
		
	}
	private ScoreDoc [] sortTopDocsByStars(TopDocs hits ) throws CorruptIndexException, IOException {

		
		
		ScoreDoc unsorted[]=hits.scoreDocs;
		double stars;
		int review_count;
		int i,j;  
		ScoreDoc temp;
		Document docj1;
		Document docj2;
		double star1,star2;
		int rev1,rev2;
		
	    for (i = 0; i <unsorted.length-1; i++) { 
	    	
	    	for (j = 0; j < unsorted.length-i-1; j++) {
	    		 docj1 = searcher.getDocument(unsorted[j]);
	    		 docj2 = searcher.getDocument(unsorted[j+1]);
	    		 star1=Double.parseDouble(docj1.get(luceneConstants.STARS));
	    		 star2=Double.parseDouble(docj2.get(luceneConstants.STARS));
	    		 rev1=Integer.parseInt(docj1.get(luceneConstants.STARS));
	    		 rev2=Integer.parseInt(docj2.get(luceneConstants.STARS));
	    		if (star2>star1 || (star2==star1 && rev2>rev1) ) {
	    			temp=unsorted[j];
	    			unsorted[j]=unsorted[j+1];
	    			unsorted[j+1]=temp;
	    		}
	    		
			}
		}
		
		return unsorted;	
		
		
	}
	
	private void showResults() throws CorruptIndexException, IOException {
		ScoreDoc sorted[]=hits.scoreDocs;
		
		if(fixedorder==false) {
			 sorted=sortTopDocsByStars(hits);
			
		}
		for(ScoreDoc scoreDoc : sorted) 
		{ 
			Document doc = searcher.getDocument(scoreDoc); 
			textArea.setText("File: " + doc.get(luceneConstants.ID) +" "+doc.get(luceneConstants.NAME)+""+ doc.get(luceneConstants.ADDRESS) +" "+doc.get(luceneConstants.CATEGORY) +" " ); 
			textArea.setText(" " + doc.get(luceneConstants.STARS) +" "+ doc.get(luceneConstants.REVIEW_COUNT) +" " ); 
			textArea.setText(" " + doc.get(luceneConstants.REVIEWS) +" "+ doc.get(luceneConstants.TIPS) +" " );
			/**Document docHit = searcher.getDocument(scoreDoc)
			 String text = docHit.get("content");
			StandardAnalyzer analyzer=new StandardAnalyzer();
             TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getReader(), new String[]{"NAME","CATEGORY","TIPS","REVIEWS"}, analyzer);
             TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 4);**/
			
			textArea.setText("------------------------------------------------");
		} 
		searcher.close(); 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.ORANGE);
		frame.setBounds(200, 100, 518, 543);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtKoogle = new JTextField();
		txtKoogle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtKoogle.setBounds(119, 43, 214, 30);
		frame.getContentPane().add(txtKoogle);
		txtKoogle.setColumns(10);
		
		
		JButton btnSearch = new JButton("search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try 
				{
					txtquery=txtKoogle.getText();
					if(querykind==0 || querykind==1 ) {//phrase-tip-review
						PhraseSearch(txtquery);
						
						
					
						
					}else if(querykind==2) {//boolean
						search(txtquery,querykind);
						
					}else if (querykind==3) {//category
						search(txtquery,querykind);
						
					}else if(querykind==4) {//name
						search(txtquery,querykind);
					}
				} catch (ParseException | IOException | org.apache.lucene.queryparser.classic.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnSearch.setBounds(365, 47, 89, 23);
		frame.getContentPane().add(btnSearch);
		
		JLabel lblStroongle = new JLabel("doogle");
		lblStroongle.setForeground(Color.RED);
		lblStroongle.setHorizontalAlignment(SwingConstants.CENTER);
		lblStroongle.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblStroongle.setBounds(168, 13, 120, 30);
		frame.getContentPane().add(lblStroongle);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		tabbedPane.setToolTipText("");
		tabbedPane.setBounds(20, 119, 478, 367);
		frame.getContentPane().add(tabbedPane);
		
		textArea = new TextArea();
		tabbedPane.addTab("Results:", null, textArea, null);
		
		
		JMenu mnSearchBy = new JMenu("search by");
		
		mnSearchBy.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent arg0) {
			}
			public void menuDeselected(MenuEvent arg0) {
			}
			public void menuSelected(MenuEvent arg0) {
			}
		});
		mnSearchBy.setBounds(23, 86, 123, 24);
		frame.getContentPane().add(mnSearchBy);
		
		JButton btnStars = new JButton("phrase-key-tip");
		btnStars.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				querykind=0;
			}
		});
		mnSearchBy.add(btnStars);
		
		JButton btnCategory = new JButton("category");
		btnCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				querykind=3;
			}
		});
		
		JButton btnPhrasekeyreview = new JButton("phrase-key-review");
		btnPhrasekeyreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				querykind=1;
			}
		});
		mnSearchBy.add(btnPhrasekeyreview);
		
		JButton btnBooleanquery = new JButton("boolean-query");
		btnBooleanquery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				querykind=2;
			}
		});
		mnSearchBy.add(btnBooleanquery);
		mnSearchBy.add(btnCategory);
		
		JButton btnName = new JButton("name");
		btnName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				querykind=4;
			}
		});
		mnSearchBy.add(btnName);
		mnSearchBy.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mnSearchBy.isShowing();
			}
		});
		
		JRadioButton rdbtnStarsOrder = new JRadioButton("stars order");
		rdbtnStarsOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fixedorder=false;
			}
		});
		rdbtnStarsOrder.setBounds(327, 85, 97, 25);
		frame.getContentPane().add(rdbtnStarsOrder);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("fixed order");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fixedorder=true;
			}
		});
		rdbtnNewRadioButton.setBounds(226, 85, 97, 25);
		frame.getContentPane().add(rdbtnNewRadioButton);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
