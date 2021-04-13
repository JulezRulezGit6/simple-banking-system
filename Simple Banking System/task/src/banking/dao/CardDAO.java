package banking.dao;

import banking.model.Card;

public interface CardDAO extends DAO<Card> {
    String TABLE_NAME = "card";

    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "  ("
            + " id INTEGER,"
            + " number TEXT,"
            + " pin TEXT,"
            + " balance INTEGER DEFAULT 0"
            + ")";

    String GET_BALANCE = "SELECT BALANCE FROM " + TABLE_NAME + " WHERE number = ?";
    String DELETE_ACCOUNT = "DELETE FROM " + TABLE_NAME + " WHERE number = ?";
    String UPDATE_BALANCE = "UPDATE " + TABLE_NAME + " SET balance = balance + ? WHERE number = ?";
    String INSERT = "INSERT INTO " + TABLE_NAME + " (NUMBER, PIN, BALANCE)" +
            "VALUES(?, ?, ?)";

    String FIND_BY_NUMBER = "SELECT * FROM " + TABLE_NAME + " WHERE number=?";

}