package banking.dao;

import banking.model.Card;

public interface DAO<T> {
    void createAccount(T account);

    Card findByNumber(String number);
}
