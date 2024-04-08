package dongfeng;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.xiaojianzheng.framework.util.ExcelConvertUtil;
import cn.xiaojianzheng.framework.util.ExcelUtil;
import javax.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Application {

    public static void main(String[] args) throws IOException, JAXBException {

        ExcelConvertUtil excelConvertUtil = new ExcelConvertUtil();
        if (!Arrays.asList(args).contains("test")) {
            List<File> excelFiles = Files.list(Paths.get("C:\\Users\\jiahe\\OneDrive\\工作\\东风要素表转海规要素表工具\\新建文件夹"))
                    .map(Path::toFile)
                    .filter(file -> ExcelUtil.isExcel(file.getName()) && !file.getName().contains("-转换后"))
                    .collect(Collectors.toList());

            if (excelFiles.isEmpty()) {
                log.error("当前文件夹下没有Excel文件");
            }


            for (File excelFile : excelFiles) {
                String name = excelFile.getName();
                int i = name.lastIndexOf(".");
                String fileName = name.substring(0, i);
                File target = new File(excelFile.getParent(), fileName + "-转换后.xlsx");

                InputStream inputStream;
                if (fileName.contains("借款") || fileName.contains("贷款")) {
                    inputStream = new ClassPathResource("jiekuan.xml").getStream();
                } else if (fileName.contains("融资")) {
                    inputStream = new ClassPathResource("rongzi.xml").getStream();
                } else {
                    throw new RuntimeException("不支持要素表【" + fileName + "】对应的模板");
                }

                try {
                    log.info("开始转换：{}", excelFile.getName());
                    excelConvertUtil.convert(excelFile, target, inputStream);
                    log.info("转换成功：{}", target.getName());
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }

            }
        }

    }

}
