package com.shiliu.dragon.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class PictureUtils {
    private static Logger logger = LoggerFactory.getLogger(PictureUtils.class);

    public static List<String> uploadPicture(List<MultipartFile> files,String uploadPath,String uriPath) {
        List<String> picutures = new ArrayList<>();
        if (files.isEmpty() || uploadPath == null || uriPath == null) {
            return picutures;
        }
        for (MultipartFile picture : files) {
            if (picture == null || !(picture.getOriginalFilename().endsWith(".jpg") || picture.getOriginalFilename().endsWith(".png"))) {
                logger.warn("Upload file is error {}", picture);
                continue;
            }
            logger.info("Begin upload file " + picture.getOriginalFilename());
            //获取文件后缀
            String suffix = picture.getOriginalFilename().substring(picture.getOriginalFilename().lastIndexOf("."), picture.getOriginalFilename().length());
            File localFile = new File(uploadPath, new Date().getTime() + suffix);
            try {
                localFile.createNewFile();
                //将传输内容进行转换
                picture.transferTo(localFile);
                String url = uriPath+localFile.getName();
                picutures.add(url);
            } catch (IOException e) {
                logger.warn("Upload picture to ngnix failed ", e);
            }
        }
        return picutures;
    }
}
