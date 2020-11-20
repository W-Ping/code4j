package com.code4j.util;

import com.code4j.config.Code4jConstants;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author liu_wp
 * @date 2020/11/9
 * @see
 */
public class PropertiesUtil {


    /**
     * @param fileName
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getPropertyObject(String fileName, Class<T> tClass) {
        try {
            Properties properties = new Properties();
            properties.load(getPropertyStream(fileName));
            T t = tClass.newInstance();
            Field[] declaredFields = tClass.getDeclaredFields();
            for (final Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                String name = declaredField.getName();
                Object value = properties.get(name);
                if (value != null) {
                    declaredField.set(t, value);
                }
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param fileName
     * @param object
     * @return
     */
    public static boolean setPropertyValue(String fileName, Object object) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(getPropertyPath(fileName));) {
            Properties properties = new Properties();
            properties.load(getPropertyStream(fileName));
            Class<?> aClass = object.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (final Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                Object value = declaredField.get(object);
                if (value != null) {
                    properties.setProperty(declaredField.getName(), value.toString());
                }
            }
            properties.store(fileOutputStream, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * @param fileName
     * @return
     */
    public static InputStream getPropertyStream(String fileName) {
//        try {
//            InputStream resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
//            return resourceAsStream;
//        } catch (Exception e) {
//
//        }
        String propertyPath = getPropertyPath(fileName);
        try {
            return new FileInputStream(propertyPath);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return null;
    }

    public static String getPropertyPath(String fileName) {
        String path = Code4jConstants.PROJECT_ROOT_PATH + "\\" + fileName;
        File file = new File(path);
        if (!file.exists()) {
            file = new File(path = Code4jConstants.SYS_TEMP_PATH + "\\" + fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
        return path;
    }
}
