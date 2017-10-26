package com.example.model.DBManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.sql.Connection;


/**
 * Created by Marina on 19.10.2017 ?..
 */
@Component
public  class  AbstractDao {
    @Autowired
    DBManager dbManager;


    public AbstractDao() {
        dbManager=new DBManager();
    }


    public Connection getConnection(){
        return dbManager==null? null:dbManager.getCon();
    }
}
