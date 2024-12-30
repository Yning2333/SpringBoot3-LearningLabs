//package com.example.elasticsearch;
//
//import com.example.elasticsearch.entity.Article;
//import com.example.elasticsearch.repository.ArticleRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataLoader implements CommandLineRunner {
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 创建一些测试数据
//        Article article1 = new Article();
//        article1.setId("1");
//        article1.setTitle("阿伟已经死了");
//        article1.setContent("内容：恐龙抗狼");
//
//        Article article2 = new Article();
//        article2.setId("2");
//        article2.setTitle("春天来了");
//        article2.setContent("内容：花开花落，春风又绿江南水");
//
//        Article article3 = new Article();
//        article3.setId("3");
//        article3.setTitle("我爱编程");
//        article3.setContent("内容：编程让生活更美好");
//
//        // 插入数据
//        articleRepository.save(article1);
//        articleRepository.save(article2);
//        articleRepository.save(article3);
//
//        System.out.println("数据插入成功！");
//    }
//}
