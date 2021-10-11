package cn.xiaojianzheng;

import cn.xiaojianzheng.framework.util.ExcelConvertUtil;
import cn.xiaojianzheng.framework.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    @Value("${user.dir}")
    private String appHome;

    @Autowired
    private ExcelConvertUtil excelConvertUtil;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!Arrays.asList(args).contains("test")) {
            List<File> excelFiles = Files.list(Paths.get("C:\\Users\\JIAHE\\Desktop\\123"))
                    .map(Path::toFile)
                    .filter(file -> ExcelUtil.isExcel(file.getName()) && !file.getName().contains("-转换后"))
                    .collect(Collectors.toList());

            if (excelFiles.isEmpty()) {
                logger.error("当前文件夹下没有Excel文件");
            }

            excelFiles.forEach(file -> {
                String name = file.getName();
                int i = name.lastIndexOf(".");
                String fileName = name.substring(0, i);
                File target = new File(file.getParent(), fileName + "-转换后.xlsx");
                if (fileName.contains("借款")) {
                    try (InputStream jiekuan = new ClassPathResource("dongfeng/jiekuan.xml").getInputStream()) {
                        excelConvertUtil.convert(file, target, jiekuan);
                        logger.error("【{}】 ===> 【{}】", file.getName(), target.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (fileName.contains("融资")) {
                    try (InputStream rongzi = new ClassPathResource("dongfeng/rongzi.xml").getInputStream();) {
                        excelConvertUtil.convert(file, target, rongzi);
                        logger.error("【{}】 ===> 【{}】", file.getName(), target.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
}
