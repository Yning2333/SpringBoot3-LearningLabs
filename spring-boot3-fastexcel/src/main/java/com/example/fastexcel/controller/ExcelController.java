package com.example.fastexcel.controller;

import cn.idev.excel.FastExcel;
import com.example.fastexcel.entity.User;
import com.example.fastexcel.listener.BaseExcelListener;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExcelController {


    /**
     * 导出Excel
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("test", "UTF-8");
        response.setHeader("Content-disposition",
                "attachment;filename*=utf-8''" + fileName + ".xlsx");
        Long start = System.currentTimeMillis();
        // 写入数据
        FastExcel.write(response.getOutputStream(), User.class)
                .sheet("模板")
                .doWrite(buildData());
        Long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));

    }

    /**
     * 导入Excel
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择一个文件上传！");
        }
        try {
            Long start = System.currentTimeMillis();
            BaseExcelListener<User> baseExcelListener = new BaseExcelListener<>();
            FastExcel.read(file.getInputStream(), User.class, baseExcelListener).sheet().doRead();
            List<User> dataList = baseExcelListener.getDataList();
            System.out.println(dataList.size());
            Long end = System.currentTimeMillis();
            return ResponseEntity.ok("文件上传并处理成功！耗时：" + (end - start) + "ms");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件处理失败！");
        }
    }

    private List<User> buildData() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            User user = new User();
            user.setId(i);
            user.setAge(88);
            user.setName("张三" + i);
            list.add(user);
        }
        return list;
    }
}
