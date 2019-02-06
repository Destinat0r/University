package com.foxminded.university.dao;

import java.sql.*;
import java.util.*;
import com.foxminded.university.domain.*;

public class LectorDao {
    
    private static final String CREATE_QUERY = "INSERT INTO lectors (first_name, last_name) "
            + "VALUES (?, ?);";
    
    private static final String READ_QUERY = "SELECT id, first_name, last_name "
            + "FROM lectors "
            + "WHERE id = ?";
    
    private static final String APPOINT_TO_SUBJECT_BY_ID_QUERY = "INSERT INTO lectors_subjects (lector_id, subject_id) "
            + "VALUES(?, ?);";
    
    private static final String READ_ALL_QUERY = "SELECT id, first_name, last_name FROM lectors;";
    
    private static final String DISCARD_SUBJECT_BY_ID_QUERY = "DELETE FROM lectors_subjects "
            + "WHERE lector_id = ? AND subject_id = ?";
    
    public Lector create(Lector lector) {

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, lector.getFirstName());
            statement.setString(2, lector.getLastName());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                lector.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lector; 
    }
    
    public Lector findById(int id) {

        Lector lector = null;

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                lector = new Lector(id, resultSet.getString("first_name"), resultSet.getString("last_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lector;
    }
    
    public List<Lector> findAll() {

        List<Lector> lectors = new ArrayList<>();
        SubjectDao subjectDao = new SubjectDao();
        
        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(READ_ALL_QUERY);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                
                Lector lector = new Lector(id, firstName, lastName);
                lector.setSubjects(subjectDao.findAllByLectorId(id));
                
                lectors.add(lector);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lectors;
    }
    
    public void appointToSubjectById(Lector lector, int subjectId) {
        
        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(APPOINT_TO_SUBJECT_BY_ID_QUERY)) {
            
                statement.setInt(1, lector.getId());
                statement.setInt(2, subjectId);
                statement.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void discardSubjectById(Lector lector, int subjectId) {
        
        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(DISCARD_SUBJECT_BY_ID_QUERY)) {
            
            statement.setInt(1, lector.getId());
            statement.setInt(2, subjectId);
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
}
