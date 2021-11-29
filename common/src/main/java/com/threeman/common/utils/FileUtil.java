package com.threeman.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.threeman.common.excel.listener.Listener;
import com.threeman.common.exception.CreateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/11/19 10:20
 */
@Slf4j
public class FileUtil {

    /**
     * 存储路径
     */
    private static final String PATH = System.getProperty("java.io.tmpdir");

    /**
     * 文件名称
     */
    private static final String ORIGINAL_FILENAME =null;

    /**
     * 数据导出
     * @param list
     */
    public static void listToFile(List<Object> list,Class clazz,String path,String originalFilename){
        FileOutputStream fileOutputStream =null;
        try {
            fileOutputStream= new FileOutputStream(path+"/"+originalFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ExcelWriterBuilder write = EasyExcel.write(fileOutputStream, clazz);
        ExcelWriterSheetBuilder sheet = write.sheet();
        sheet.doWrite(list);
    }

    public static void listToFile(List<Object> list,Class clazz,String originalFilename){
        FileOutputStream fileOutputStream =null;
        try {
            fileOutputStream= new FileOutputStream(PATH+"/"+originalFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ExcelWriterBuilder write = EasyExcel.write(fileOutputStream, clazz);
        ExcelWriterSheetBuilder sheet = write.sheet();
        sheet.doWrite(list);
    }

    /**
     *
     * @param multipartFile 读取的文件
     * @param clazz 目标对象
     * @return
     */
    public static List<Object> fileSyncToObject(MultipartFile multipartFile, Class clazz){
        File file = FileUtil.multipartFileToFile(multipartFile);
        ExcelReaderBuilder read = EasyExcel.read(file, clazz, new Listener<>());
        ExcelReaderSheetBuilder sheet = read.sheet();
        return sheet.doReadSync();
    }

    public static List<Object> fileSync(MultipartFile multipartFile){
        File file = FileUtil.multipartFileToFile(multipartFile);
        ExcelReaderBuilder read = EasyExcel.read(file, new Listener<>());
        ExcelReaderSheetBuilder sheet = read.sheet();
        return sheet.doReadSync();
    }

    /**
     *
     * @param multipartFile 读取的文件
     * @param clazz 目标对象
     */
    public static void fileToObject(MultipartFile multipartFile, Class clazz){
        File file = FileUtil.multipartFileToFile(multipartFile);
        ExcelReaderBuilder read = EasyExcel.read(file, clazz, new Listener<>());
        ExcelReaderSheetBuilder sheet = read.sheet();
        sheet.doRead();
    }

    public static File multipartFileToFile(MultipartFile multipartFile){
        File file =null;
        try {
            if (multipartFile==null||multipartFile.isEmpty()){
               throw  new CreateException(11000,"传入文件不能为空");
            }
            InputStream inputStream = multipartFile.getInputStream();
            log.info("inputStream:{}",inputStream);
            String contentType = multipartFile.getContentType();
            log.info("contentType:{}",contentType);
            String originalFilename = multipartFile.getOriginalFilename();
            log.info("originalFilename:{}",originalFilename);
            String name = multipartFile.getName();
            log.info("name:{}",name);
            assert originalFilename != null;
            file = new File(PATH+"/"+originalFilename);
            inputStreamToFile(inputStream,file);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     * @param file
     */
    public static void deleteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }
}
