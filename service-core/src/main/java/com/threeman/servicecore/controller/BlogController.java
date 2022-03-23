package com.threeman.servicecore.controller;

import com.threeman.common.entity.Dictionary;
import com.threeman.common.service.DictionaryService;
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
    public boolean insertBlog(@RequestBody Map<String,Object> param){
         return blogInfoService.insertBlogInfo(param);
    }

    @GetMapping("/getBlogInfos/{text}")
    public List<Map<String,Object>> findBlogInfos(@PathVariable String text){
        log.info("text:{}",text);
        return blogInfoService.findBlogInfos(text);
    }

    @GetMapping("/getBlogInfo/{blogId}")
    public Map<String,Object> findBlogInfo(@PathVariable long blogId ){
        log.info("blogId:{}",blogId);
        return blogInfoService.findBlogInfo(blogId);
    }

    @GetMapping("/getBlogInfosByPage/{text}")
    public List<Map<String,Object>> findBlogInfosByPage(@PathVariable String text,
                                                        @RequestParam(defaultValue = "0") int from,@RequestParam(defaultValue = "3") int size){
        return blogInfoService.findBlogInfosByPage(text,from,size);
    }
}
