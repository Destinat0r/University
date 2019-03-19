package com.foxminded.university.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionFactory {

    private static final Logger log = LogManager.getLogger(ConnectionFactory.class.getName());
    private static DataSource dataSource;
    
    public static Connection getConnection() {
        Connection connection = null;
        
        log.debug("Creating a new connection");

        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/university");
            connection = dataSource.getConnection();
            
        } catch (SQLException | NamingException e) {
            log.error("Connection has not been created." , e);
            throw new DaoException("Connection has not been created." , e);
        }
        
        log.debug("Connection was successfully created.");
        
        return connection;
    }
}
