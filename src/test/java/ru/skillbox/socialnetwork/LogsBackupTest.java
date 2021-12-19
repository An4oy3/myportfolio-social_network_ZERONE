package ru.skillbox.socialnetwork;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.skillbox.socialnetwork.service.GoogleDriveService;
import ru.skillbox.socialnetwork.utils.BackupGoogleDriveTask;
import ru.skillbox.socialnetwork.utils.LogsGoogleDriveTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertTrue;

public class LogsBackupTest {

    private static final Logger log = LoggerFactory.getLogger(DbBackupsTest.class);
    private static LogsGoogleDriveTask dataCreateTask;
    private static final DateTimeFormatter fileNameDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private static String testFileName = LocalDateTime.now().format(fileNameDateFormat) + ".test";
    private static String testFolderId = "1-_KEk819bSMEB81Kjr1EzPslKqBCfDto";

    @BeforeAll
    static void init() {

        dataCreateTask = new LogsGoogleDriveTask(new GoogleDriveService());

        dataCreateTask.setLocalPath("test/");

        File localFolder = new File(dataCreateTask.getLocalPath());
        log.info("Folder for tests: " + localFolder.getAbsolutePath());

        if(!localFolder.exists()){
            try {
                FileUtils.forceMkdir(localFolder);
            } catch (IOException ex) {
                log.info("Cannot create folder: " + localFolder.getAbsolutePath());
                ex.printStackTrace();
                return;
            }
        }

        try(PrintWriter writer = new PrintWriter(localFolder + "/" + testFileName)){
            writer.println("Test File for javapro");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Проверка метода загрузки содержимого папки на гугл драйв")
    public void googleDriveLoadLogsTest(){

        dataCreateTask.setLocalPath("test/");

        dataCreateTask.loadFilesFromFolder(new File("test"), testFolderId);

    }

    @Test
    @DisplayName("Проверка метода загрузки логов на гугл драйв")
    public void googleDriveLoadTest(){

        dataCreateTask.setLocalPath("test/");

        dataCreateTask.copyLogsToGoogleDrive();
    }


    @AfterAll
    static void afterAllTests(){
        File folder = new File("test/");
        if (folder.exists()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                file.delete();
            }
            folder.delete();
        }
    }

}
