# 验证

```sql 
CREATE TABLE users (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255),
email VARCHAR(255)
);
INSERT INTO users (name, email) VALUES ('Alice', 'alice@example.com');
INSERT INTO users (name, email) VALUES ('Bob', 'bob@example.com');
```
测试案例见 [postManTest.json](https://github.com/Yning2333/SpringBoot3-LearningLabs/blob/main/spring-boot3-mybatis-plus/postManTest.json)
