package com.swj.ics.web_api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by swj on 2020/2/19.
 */
public class IndexController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    public static void main(String[] args) {
        String filepath = "S:\\downloads\\youtube\\filename.txt";
        try {
            List<String> fileNameList = readAllFileContent(filepath);
            Map<String, String> fileNameMap = new HashMap<>(50);
            for (String line : fileNameList) {
                if (line.contains("#")) {
                    String fileNameKey = line.substring(0, line.indexOf("#"));
                    if (fileNameMap.containsKey(fileNameKey)) {
                        LOGGER.info("file file {} already exists", fileNameKey);
                        continue;
                    }
                    fileNameMap.put(fileNameKey, line.substring(line.indexOf("#") + 1));
                }
            }
            File dir = new File(filepath).getParentFile();
            File[] videoFiles = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".mp4");
                }
            });
            if (videoFiles == null || videoFiles.length < 1) {
                LOGGER.info("mp4 files not found");
                return;
            }
            for (File videoF : videoFiles) {
                if (fileNameMap.containsKey(videoF.getName())) {
                    videoF.renameTo(new File(dir, fileNameMap.get(videoF.getName()) + ".mp4"));
                }
                Iterator it = fileNameMap.keySet().iterator();
                String currFileName = videoF.getName();
                String currFileKey = null;
                while (it.hasNext()) {
                    currFileKey = String.valueOf(it.next());
                    if (currFileName.contains(currFileKey)) {
                        videoF.renameTo(new File(dir, fileNameMap.get(currFileKey) + ".mp4"));
                        LOGGER.info("done for file rename from {} to{}", currFileName, fileNameMap.get(currFileKey) + ".mp4"))
                        ;
                        break;
                    }
                }
                LOGGER.info("can not found current file in map,{}" + currFileName);
            }
        } catch (IOException e) {
            LOGGER.error("{}", e);
        }
    }

    private static List<String> readAllFileContent(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line = null;
        List<String> fileNameList = new ArrayList<>(50);
        while ((line = bufferedReader.readLine()) != null) {
            fileNameList.add(line);
        }
        bufferedReader.close();
        return fileNameList;
    }
}
