package com.solvd.fooddelivery.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisSessionFactory {

    private static SqlSessionFactory sqlSessionFactory;

    private MyBatisSessionFactory() {
    }

    public static SqlSessionFactory getSqlSessionFactory() {

        if (sqlSessionFactory == null) {
            synchronized (MyBatisSessionFactory.class) {
                if (sqlSessionFactory == null) {
                    try {
                        InputStream inputStream =
                                Resources.getResourceAsStream("mybatis/mybatis-config.xml");

                        sqlSessionFactory =
                                new SqlSessionFactoryBuilder().build(inputStream);

                    } catch (IOException e) {
                        throw new RuntimeException("Failed to create SqlSessionFactory", e);
                    }
                }
            }
        }

        return sqlSessionFactory;
    }
}
