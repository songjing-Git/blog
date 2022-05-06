package com.threeman.servicecore.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.threeman.common.excel.entityExcel.Entity;
import com.threeman.common.exception.CreateException;
import com.threeman.common.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/3/17 10:52
 */
@RestController
@Slf4j
@Api("excel导出功能")
public class ExcelController {


    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @ApiOperation("短书订单导出，仅小胖使用")
    @PostMapping("/excelReader")
    public String excelReader(MultipartFile multipartFile, String cookie) {
        List<Object> list = FileUtil.fileSync(multipartFile);
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSON(o).toString(), new TypeReference<Map<String, Object>>() {
            });
            String orderId = map.get("0").toString();
            log.info("orderId:{}", orderId);
            String s = HttpClientTest.get(orderId, cookie);
            String substring = s.substring(12, s.length() - 1);
            log.info("substring:{}", substring);

            Map<String, Object> date = JSON.parseObject(substring, new TypeReference<Map<String, Object>>() {
            });
            String extraData1 = date.get("extra_data").toString();
            if (StringUtils.isEmpty(extraData1)) {
                throw new CreateException("extra_datas数据为空");
            }
            Map<String, Object> extraData = JSON.parseObject(extraData1, new TypeReference<Map<String, Object>>() {
            });
            Map<String, Object> buyer = JSON.parseObject(date.get("buyer").toString(), new TypeReference<Map<String, Object>>() {
            });
            Object nickname = buyer.get("nickname");
            Object mobile1 = extraData.get("mobile1");
            Object realName0 = extraData.get("real_name0");
            Object address = extraData.get("area2") + " " + extraData.get("address3");
            if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(mobile1)
                    || StringUtils.isEmpty(realName0) || StringUtils.isEmpty(address)) {
                log.error(orderId + "数据异常");
                continue;
            }
            Entity entity = new Entity();
            entity.setNickname(nickname.toString());
            entity.setMobile1(mobile1.toString());
            entity.setRealName0(realName0.toString());
            entity.setAddress3(address.toString());
            result.add(entity);
        }
        FileUtil.listToFile(result, Entity.class, "订单详情.xlsx");
        return "ok";
    }


    @ApiOperation("短书订单导出，仅小胖使用")
    @PostMapping("/excelReaderByOrderId")
    public void excelReader(MultipartFile multipartFile, String cookie, HttpServletResponse response) throws IOException {
        List<Object> list = FileUtil.fileSync(multipartFile);
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSON(o).toString(), new TypeReference<Map<String, Object>>() {
            });
            String orderId = map.get("0").toString();
            String s = HttpClientTest.get(orderId, cookie);
            if (StringUtils.isEmpty(s)) {
                throw new CreateException("获取不到数据");
            }
            String substring = s.substring(12, s.length() - 1);
            log.info("substring:{}", substring);
            Map<String, Object> date = JSON.parseObject(substring, new TypeReference<Map<String, Object>>() {
            });
            String extraData1 = date.get("extra_data").toString();
            if (StringUtils.isEmpty(extraData1)) {
                throw new CreateException("extra_data数据为空");
            }
            Map<String, Object> extraData = JSON.parseObject(extraData1, new TypeReference<Map<String, Object>>() {
            });
            Map<String, Object> buyer = JSON.parseObject(date.get("buyer").toString(), new TypeReference<Map<String, Object>>() {
            });
            Object nickname = buyer.get("nickname");
            Object mobile1 = extraData.get("mobile1");
            Object realName0 = extraData.get("real_name0");
            Object address = extraData.get("area2") + " " + extraData.get("address3");
            if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(mobile1)
                    || StringUtils.isEmpty(realName0) || StringUtils.isEmpty(address)) {
                throw new CreateException("数据异常");
            }
            Entity entity = new Entity();
            entity.setNickname(nickname.toString());
            entity.setMobile1(mobile1.toString());
            entity.setRealName0(realName0.toString());
            entity.setAddress3(address.toString());
            result.add(entity);
        }
        FileUtil.listToFileResponse(result, Entity.class, "订单详情.xlsx", response);
    }


    @ApiOperation("test")
    @PostMapping("/test")
    public String excelReader(String ok) {
        redisTemplate.opsForValue().set("test", ok);
        Object text = redisTemplate.opsForValue().get("test");
        assert text != null;
        return text.toString();
    }

}
