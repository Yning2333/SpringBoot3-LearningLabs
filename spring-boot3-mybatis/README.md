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

测试地址: http://127.0.0.1:8080/users/annotation

测试地址: http://127.0.0.1:8080/users/xml

