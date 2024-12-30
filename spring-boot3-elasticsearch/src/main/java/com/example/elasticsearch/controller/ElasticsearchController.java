package com.example.elasticsearch.controller;

import com.example.elasticsearch.entity.Article;
import com.example.elasticsearch.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/es/articles")
public class ElasticsearchController {

    @Autowired
    private ArticleRepository articleRepository;

    /**
     * URL_ADDRESS
     * http://127.0.0.1:8080/es/articles
     *
     * 新增文章
     * @param article 文章实体
     * @return 保存的文章
     */
    @PostMapping
    public Article saveArticle(@RequestBody Article article) {
        return articleRepository.save(article);
    }

    /**
     * URL_ADDRESS
     * http://127.0.0.1:8080/es/articles/{id}
     *
     * 根据 ID 查询文章
     * @param id 文章 ID
     * @return 查询到的文章
     */
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable String id) {
        Optional<Article> article = articleRepository.findById(id);
        return article.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * URL_ADDRESS
     * http://127.0.0.1:8080/es/articles/{id}
     *
     * 根据 ID 删除文章
     * @param id 文章 ID
     * @return 删除结果消息
     */
    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable String id) {
        articleRepository.deleteById(id);
        return "Deleted article with id: " + id;
    }

    /**
     * URL_ADDRESS
     * http://127.0.0.1:8080/es/articles
     *
     * 查询所有文章
     * @return 所有文章列表
     */
    @GetMapping
    public Iterable<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    /**
     * URL_ADDRESS
     * http://127.0.0.1:8080/es/articles/searchByTitle
     *
     * 根据标题查询文章
     * @param title
     * @return
     */
    @GetMapping("/searchByTitle")
    public List<Article> searchByTitle(@RequestParam String title) {
        return articleRepository.findByTitle(title);
    }

    /**
     * URL_ADDRESS
     * http://127.0.0.1:8080/es/articles/searchByContent
     *
     * 根据内容查询文章
     * @param content
     * @return
     */
    @GetMapping("/searchByContent")
    public List<Article> searchByContent(@RequestParam String content) {
        /**
         * 数据：
         *  title: 我爱编程
         *  content: 内容：编程让生活更美好
         *
         * 传入：
         *  编程美好
         * 返回：
         * [
         *     {
         *         "id": "3",
         *         "title": "我爱编程",
         *         "content": "内容：编程让生活更美好"
         *     }
         * ]
         *
         */


        return articleRepository.findByContent(content);
    }
}
