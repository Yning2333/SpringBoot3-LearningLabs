package com.example.freemarker.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ResignationController {

    @Autowired
    private freemarker.template.Configuration freemarkerConfig;

    /**
     * URL_ADDRESS:
     * http://localhost:8080/exportResignation?name=%E5%BC%A0%E4%B8%89&syear=2020&smonth=1&sday=1&eyear=2022&emonth=12&eday=31&job=%E8%BD%AF%E4%BB%B6%E5%B7%A5%E7%A8%8B%E5%B8%88&reason=%E4%B8%AA%E4%BA%BA%E5%8E%9F%E5%9B%A0&companyName=%E6%9F%90%E6%9F%90%E5%85%AC%E5%8F%B8&nyear=2025&nmonth=1&nday=9
     * @param name
     * @param syear
     * @param smonth
     * @param sday
     * @param eyear
     * @param emonth
     * @param eday
     * @param job
     * @param reason
     * @param companyName
     * @param nyear
     * @param nmonth
     * @param nday
     * @return
     */
    @GetMapping("/exportResignation")
    public void exportResignation(@RequestParam String name, @RequestParam int syear, @RequestParam int smonth,
                                  @RequestParam int sday, @RequestParam int eyear, @RequestParam int emonth,
                                  @RequestParam int eday, @RequestParam String job, @RequestParam String reason,
                                  @RequestParam String companyName, @RequestParam int nyear,
                                  @RequestParam int nmonth, @RequestParam int nday, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("syear", syear);
        data.put("smonth", smonth);
        data.put("sday", sday);
        data.put("eyear", eyear);
        data.put("emonth", emonth);
        data.put("eday", eday);
        data.put("job", job);
        data.put("reason", reason);
        data.put("companyName", companyName);
        data.put("nyear", nyear);
        data.put("nmonth", nmonth);
        data.put("nday", nday);

        response.setContentType("application/msword");
        response.setHeader("Content-Disposition", "attachment; filename=resignation.doc");

        try (OutputStream outputStream = response.getOutputStream()) {
            freemarkerConfig.getTemplate("离职证明.ftl").process(data, new java.io.OutputStreamWriter(outputStream, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "生成文件失败");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}