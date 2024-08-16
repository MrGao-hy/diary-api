package com.example.demo.utils;

import org.springframework.stereotype.Component;

@Component
public class Constant {
    /**
     * 字节转化MB
     */
    public static final long MB = 1024 * 1024;


    /**
     * 文件大小智能转换
     * 会将文件大小转换为最大满足单位
     *
     * @param size（文件大小，单位为B）
     * @return 文件大小
     */
    public static String formatFileSize(Long size) {
        String sizeName = null;
        if (1024 * 1024 > size && size >= 1024) {
            sizeName = String.format("%.2f", size.doubleValue() / 1024) + "KB";
        } else if (1024 * 1024 * 1024 > size && size >= 1024 * 1024) {
            sizeName = String.format("%.2f", size.doubleValue() / (1024 * 1024)) + "MB";
        } else if (size >= 1024 * 1024 * 1024) {
            sizeName = String.format("%.2f", size.doubleValue() / (1024 * 1024 * 1024)) + "GB";
        } else {
            sizeName = size.toString() + "B";
        }
        return sizeName;
    }
}
