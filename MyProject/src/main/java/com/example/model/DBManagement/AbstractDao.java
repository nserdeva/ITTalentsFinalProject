package com.example.model.DBManagement;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;


/**
 * Created by Marina on 19.10.2017 ?..
 */
public abstract class AbstractDao {
    @Autowired
    DBManager dbManager;
    @Autowired
    Connection connection=dbManager.getConnection();
    

}
