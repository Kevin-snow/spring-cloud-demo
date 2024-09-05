package com.springboot.excel.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.springboot.excel.demo.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin
 * @date 2024/8/29 17:12
 */
@RestController
@RequestMapping("/api")
public class ExcelController {


    @GetMapping("/export/excel")
    public void exportExcel(HttpServletResponse response){

        List<User> list = new ArrayList<>();

        User user = new User();
        user.setName("张三");
        user.setAge("18");

        list.add(user);

        User user1 = new User();
        user1.setName("李四");
        user1.setAge("19");

        list.add(user1);


        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("用户信息", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream())
                    .head(User.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet(fileName)
                    .doWrite(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }





}
