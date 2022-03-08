package com.threeman.servicecore.controller;

import com.threeman.common.entity.Dictionary;
import com.threeman.common.service.DictionaryService;
import com.threeman.servicecore.entity.BlogInfo;
import com.threeman.servicecore.service.BlogInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/2/28 15:29
 */
@RestController
@Slf4j
public class BlogController {


    @Autowired
    BlogInfoService blogInfoService;

    @Autowired
    DictionaryService dictionaryService;

    @GetMapping("/findLabels/{typeName}")
    public List<Dictionary> findLabels(@PathVariable String typeName){
        return dictionaryService.findLabels(typeName);
    }

    @PutMapping("/insertBlog")
    public boolean insertBlog(@RequestBody BlogInfo blogInfo){
         return blogInfoService.insertBlogInfo(blogInfo);
    }

    @GetMapping("/getBlogInfos")
    public List<Map<String,Object>> findBlogInfos(@RequestParam String text){
        log.info("text:{}",text);
        return blogInfoService.findBlogInfos(text);
    }
}
