package com.suntr.lucenesample;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class Searcher {

  public static void search(String indexDir, String q) throws IOException, ParseException {
    //得到索引文件的路径
    Directory directory = FSDirectory.open(Paths.get(indexDir));
    // 通过dir得到的路径下的所有的文件
    IndexReader indexReader = DirectoryReader.open(directory);
    //建立索引查询器
    IndexSearcher searcher = new IndexSearcher(indexReader);
    //实例化分析器
    Analyzer analyzer = new StandardAnalyzer();
    //建立查询解析器
    /**
     * 第一个参数是要查询的字段； 第二个参数是分析器Analyzer
     */
    QueryParser parser = new QueryParser("contents", analyzer);
    //根据传进来的q查找
    Query query = parser.parse(q);
    //计算索引开始时间
    long start = System.currentTimeMillis();
    /**
     * 第一个参数是通过传过来的参数来查找得到的query； 第二个参数是要出查询的行数
     */
    TopDocs hits = searcher.search(query, 10);
    //查找结束时间
    long end = System.currentTimeMillis();
    // 遍历hits.scoreDocs，得到scoreDoc
    /**
     * ScoreDoc:得分文档,即得到文档 scoreDocs:代表的是topDocs这个文档数组
     *
     * @throws Exception
     */
    for (ScoreDoc scoreDoc: hits.scoreDocs){
      Document document = searcher.doc(scoreDoc.doc);
      System.out.println(document.get("fullPath"));
    }
    indexReader.close();
  }

  public static void main(String[] args) {
    String indexDir = "D:\\temp\\kingsoft log\\index_data";
    //搜索的内容
    String q = "checkThreadLocalMapForLeaks";
    q = "Leaks";
    q = "ResourceAction";
    q = "memory";
    try {
      search(indexDir, q);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
