package com.example.elasticsearch.repository;

import com.example.elasticsearch.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data 在后台会使用反射来分析方法名，并将其转换为查询。在 Elasticsearch 中，Spring Data 会将查询转化为 Lucene 查询（Elasticsearch 是基于 Lucene 构建的），然后通过 Elasticsearch 的 API 执行查询。
 *
 * findByTitle 会转化为类似 {"query": {"match": {"title": "someTitle"}}} 的查询。
 * findByTitleAndContent 会转化为类似 {"query": {"bool": {"must": [{"match": {"title": "someTitle"}}, {"match": {"content": "someContent"}}]}}} 的查询。
 *
 */
@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

    List<Article> findByTitle(String title);  // 根据标题查询

    List<Article> findByContent(String title);  // 根据内容查询
}
