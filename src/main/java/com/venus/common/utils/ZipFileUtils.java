package com.venus.common.utils;

import com.venus.common.exception.VenusException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class ZipFileUtils {
    private static final Logger logger = LoggerFactory.getLogger(ZipFileUtils.class);

    private static final Set<String> REQUIRED_FILES = new HashSet<>(Arrays.asList(".shp", ".dbf", ".shx", ".prj"));


    /**
     * 解压 ZIP 文件到指定文件夹
     *
     * @param zipFile ZIP 文件
     * @param tempDir 临时文件夹
     */
    public static List<String> extractZipToFolder(MultipartFile zipFile, File tempDir) throws IOException {
        if(zipFile.isEmpty()) {
            throw new VenusException("上传的 ZIP 文件为空！");
        }

        // 确保指定文件夹存在，不存在则创建
        if(!tempDir.exists()) {
            if(!tempDir.mkdirs()) {
                throw new VenusException("无法创建临时文件夹: " + tempDir.getAbsolutePath());
            }
        }

        List<String> fileNames = new ArrayList<>();

        try (InputStream inputStream = zipFile.getInputStream();
             ZipArchiveInputStream zipInput = new ZipArchiveInputStream(inputStream, "UTF-8")) {

            ZipArchiveEntry entry;
            while ((entry = zipInput.getNextEntry()) != null) {
                // 跳过目录
                if(entry.isDirectory()) {
                    throw new VenusException("压缩包中包含文件夹，不符合要求！");
                }

                // 获取文件名
                String fileName = new File(entry.getName()).getName(); // 获取文件名

                // 目标文件路径
                File outputFile = new File(tempDir, fileName);
                fileNames.add(fileName);

                // 写入文件内容到目标路径
                try (OutputStream outputStream = Files.newOutputStream(outputFile.toPath())) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zipInput.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                }
            }
        }
        return fileNames;
    }

    /**
     * 获取压缩包中的文件名
     * @param inputStream 输入流
     */
    public static List<String> getNamesFromStream(InputStream inputStream) throws IOException {
        List<String> fileNames = new ArrayList<>();
        try {
            ZipArchiveInputStream zipInput = new ZipArchiveInputStream(inputStream, "UTF-8");
            ZipArchiveEntry entry;
            while ((entry = zipInput.getNextEntry()) != null) {
                // 获取文件名并校验合法性
                String fileName = new File(entry.getName()).getName(); // 获取文件名
                fileNames.add(fileName);
            }
        } catch (Exception e) {
            throw new VenusException(e.getMessage());
        }
        return fileNames;
    }

    public static byte[] changeZipShpStream(InputStream inputStream, String targetName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream);
             ZipArchiveOutputStream zos = new ZipArchiveOutputStream(baos)) {

            ZipArchiveEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // 确定新的条目名称
                String entryName = entry.getName();
                String extName = entryName.substring(entryName.lastIndexOf('.')).toLowerCase();

                if(REQUIRED_FILES.contains(extName)) {
                    // 创建新的 ZIP 条目
                    ZipArchiveEntry newEntry = new ZipArchiveEntry(targetName + extName);
                    zos.putArchiveEntry(newEntry);
                    // 将文件内容复制到新的 ZIP 条目中
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zis.read(buffer)) != -1) {
                        zos.write(buffer, 0, bytesRead);
                    }
                    zos.closeArchiveEntry();
                }
            }
        } catch (IOException e) {
            throw new VenusException(e.getMessage());
        }

        // 返回字节数组
        return baos.toByteArray();
    }
}
