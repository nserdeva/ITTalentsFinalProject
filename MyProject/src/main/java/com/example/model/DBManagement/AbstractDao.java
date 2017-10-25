package com.example.model.DBManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Connection;


/**
 * Created by Marina on 19.10.2017 ?..
 */
@Component
public abstract class AbstractDao {
    @Autowired
    DBManager dbManager;
    Connection connection;

    public AbstractDao() {
        dbManager=new DBManager();
        this.connection=dbManager.getConnection();
    }
}
