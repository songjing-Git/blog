import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/5/7 10:31
 */
public class Test {

    public static String readDoc(String path) throws IOException {
        String resullt = "";
        //首先判断文件中的是doc/docx
        try {
                InputStream is = new FileInputStream(new File(path));
                XWPFDocument document=new XWPFDocument(is);
                XWPFWordExtractor wordExtractor=new XWPFWordExtractor(document);
                String text = wordExtractor.getText();
                resullt=text;
                is.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return resullt;
    }


    //main方法测试
    public static void main(String[] args) {

        String filename="C:\\Users\\86186\\Desktop\\z_工作文件夹\\政企三期\\CollLetterTemplate.docx";
        String textContent = null;
        try {
            textContent = readDoc(filename);
            int index = textContent.indexOf("移动公司客户经理（签字）：");
            int index1 = textContent.indexOf(" ");

            System.out.println(index);

            System.out.println("截取："+textContent.substring(index+3,index1).trim().replace("_",""));

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(textContent);

    }
}
