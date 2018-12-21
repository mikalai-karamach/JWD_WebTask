package by.etc.karamach.dao.sql.impl;

import by.etc.karamach.bean.Test;
import by.etc.karamach.dao.DAOException;
import by.etc.karamach.dao.TestDAO;
import by.etc.karamach.dao.pool.ConnectionPool;
import by.etc.karamach.dao.pool.ConnectionPoolException;
import by.etc.karamach.dao.sql.query.FindAllTests;
import by.etc.karamach.dao.sql.query.FindTestsByOwnerId;
import by.etc.karamach.dao.sql.query.SaveNewTest;
import by.etc.karamach.utils.sql.ResourceDestroyer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLTestDAO implements TestDAO {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public List<Test> getAllTests() throws DAOException {

        List<Test> tests;

        Connection connection = null;

        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(FindAllTests.statement);

            resultSet = preparedStatement.executeQuery();

            tests = new ArrayList<>();
            while (resultSet.next()) {
                Test test = new Test();

                fillTestDataFromResultSet(resultSet, test);

                tests.add(test);
            }

        } catch (ConnectionPoolException e) {
            //TODO: LOG !
            throw new DAOException("Couldn't take connection from connection pool", e);

        } catch (SQLException e) {

            throw new DAOException("Couldn't execute query to data source", e);

        } finally {

            ResourceDestroyer.closeAll(connection, preparedStatement, resultSet);
        }


        return tests;
    }

    @Override
    public List<Test> getMyTests(int userId) throws DAOException {
        List<Test> tests;

        Connection connection = null;

        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(FindTestsByOwnerId.statement);

            preparedStatement.setInt(FindTestsByOwnerId.OWNER_ID_INPUT_INDEX, userId);

            resultSet = preparedStatement.executeQuery();

            tests = new ArrayList<>();
            while (resultSet.next()) {
                Test test = new Test();

                fillTestDataFromResultSet(resultSet, test);

                tests.add(test);
            }

        } catch (ConnectionPoolException e) {
            //TODO: LOG !
            throw new DAOException("Couldn't take connection from connection pool", e);

        } catch (SQLException e) {

            throw new DAOException("Couldn't execute query to data source", e);

        } finally {

            ResourceDestroyer.closeAll(connection, preparedStatement, resultSet);
        }

        return tests;
    }

    @Override
    public void saveNewTest(Test test) throws DAOException {
        Connection connection = null;

        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SaveNewTest.statement);

            //TODO: Extract to CONST
            preparedStatement.setInt(SaveNewTest.OWNER_ID_INPUT_INDEX, test.getOwnerId());
            preparedStatement.setString(SaveNewTest.NAME_INPUT_INDEX, test.getName());

            preparedStatement.execute();


        } catch (ConnectionPoolException e) {
            //TODO: LOG !
            throw new DAOException("Couldn't take connection from connection pool", e);

        } catch (SQLException e) {

            throw new DAOException("Couldn't execute query to data source", e);

        } finally {

            ResourceDestroyer.closeAll(connection, preparedStatement);
        }
    }


    private void fillTestDataFromResultSet(ResultSet resultSet, Test test) throws SQLException {
        int id = resultSet.getInt(FindAllTests.ID_RESULT_INDEX);
        String name = resultSet.getString(FindAllTests.NAME_RESULT_INDEX);
        int owner_id = resultSet.getInt(FindAllTests.OWNER_ID_RESULT_INDEX);

        test.setId(id);
        test.setName(name);
        test.setOwnerId(owner_id);

    }
}
