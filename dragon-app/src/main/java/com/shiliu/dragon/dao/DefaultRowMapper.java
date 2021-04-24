package com.shiliu.dragon.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Arrays;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class DefaultRowMapper<T> {

    public T row2Module(T clazz, ResultSet resultSet) {
        try {
            T t = (T) Class.forName(clazz.getClass().getName()).newInstance();
            Field[] fields = clazz.getClass().getFields();
            Method[] methods = clazz.getClass().getMethods();
            for(Field field : fields){
                field.setAccessible(true);
                char[] chars = field.getName().toCharArray();
                //首字母转换成大写
                chars[0] = (char)(chars[0] - 32);
                String methodName = "set"+new String(chars);
                Method method = Arrays.stream(methods).filter(s->s.getName().equals(methodName)).findAny().orElse(null);
                method.setAccessible(true);
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
