package org.example.lucenesample;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class FuzzySearcher {

  private static Directory directory;
  private static IndexReader reader;
  private static IndexSearcher searcher;

  /**
   *
   * @param indexDir  索引位置
   * @param query    // 所给出的必须是单词，不然差不到
   * @throws IOException
   */
  private static void search(Query query, String indexDir) throws IOException {
    directory = FSDirectory.open(Paths.get(indexDir));
    reader = DirectoryReader.open(directory);
    searcher = new IndexSearcher(reader);

    TopDocs hits = searcher.search(query, 10);
    // hits.totalHits：查询的总命中次数。即在几个文档中查到给定单词
    System.out.println("总共查询到" + hits.totalHits + "个文档");
    for (ScoreDoc scoreDoc : hits.scoreDocs) {
      Document doc = searcher.doc(scoreDoc.doc);
      System.out.println(doc.get("fullPath"));
    }
    reader.close();
  }

  private static Query termQuery(String searchField, String q) {
    // 一个Term表示来自文本的一个单词。
    Term term = new Term(searchField, q);
    // 为Term构造查询。
    Query query = new TermQuery(term);
    return query;
  }

  private static Query fuzzyQuery(String searchField, String q) {
    /**
     * 1.需要根据条件查询
     *
     * 2.最大可编辑数，取值范围0，1，2
     * 允许我的查询条件的值，可以错误几个字符
     *
     */
    Query query = new FuzzyQuery(new Term(searchField, q), 1);
    return query;
  }

  private static Query parserQuery(String searchField, String q) throws ParseException {
    Analyzer analyzer = new StandardAnalyzer();
    QueryParser parser = new QueryParser(searchField, analyzer);
    Query query = parser.parse(q);
    return query;
  }

  public static void termSearch(String indexDir, String q) throws IOException {
    String searchField = "contents";
    search(termQuery(searchField, q), indexDir);
  }

  public static void fuzzySearch(String indexDir, String q) throws IOException {
    String searchField = "contents";
    search(fuzzyQuery(searchField, q), indexDir);
  }

  public static void parserSearch(String indexDir, String q) throws IOException, ParseException {
    String searchField = "contents";
    search(parserQuery(searchField, q), indexDir);
  }

  public static void main(String[] args) {
    String indexDir = "D:\\temp\\kingsoft log\\index_data";
    //搜索的内容
    String q = "ResourceAction";
    q = "memori";
    try {
      termSearch(indexDir, q);
      fuzzySearch(indexDir, q);
      parserSearch(indexDir, "likely memory");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
