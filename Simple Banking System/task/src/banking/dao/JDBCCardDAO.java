package banking.dao;

import banking.model.Card;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class JDBCCardDAO implements CardDAO {

    DataSource dataSource;

    public JDBCCardDAO(SQLiteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init() {

        try (final Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public void addIncome(Card card, int deposit) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BALANCE)) {
            statement.setInt(1, deposit);
            statement.setString(2, card.getCardNumber());
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void createAccount(Card card) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {

            statement.setString(1, card.getCardNumber());
            statement.setString(2, card.getPin());
            statement.setDouble(3, card.getBalance());

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Card findByNumber(String number) {

        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement stmt = connection.prepareStatement(FIND_BY_NUMBER)) {

            stmt.setString(1, number);

            try (final ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    final String pin = res.getString("pin");
                    final double balance = res.getDouble("balance");
                    return new Card(number, pin, balance);
                } else {
                    return null;
                }

            }
        } catch (SQLException e) {
            System.out.println("Error reading from file");
            throw new DAOException("Error While Finding Element");
        }
    }

    public int getBalance(Card card) {
        int balance = 0;
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement stmt = connection.prepareStatement(GET_BALANCE)) {

            stmt.setString(1, card.getCardNumber());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                balance = resultSet.getInt("balance");
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return balance;
    }

    public void deleteAccount(Card card) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement stmt = connection.prepareStatement(DELETE_ACCOUNT)) {
            stmt.setString(1, card.getCardNumber());
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public void transferAmount(Card card, String receivingCardNumber, int amountToTransfer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BALANCE)) {
            statement.setInt(1, -amountToTransfer);
            statement.setString(2, card.getCardNumber());

            statement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BALANCE)) {

            statement.setInt(1, amountToTransfer);
            statement.setString(2, receivingCardNumber);

            statement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public boolean insufficientFunds(Card card, int amountToTransfer) {
        int balance = getBalance(card);
        return balance < amountToTransfer;
    }
}
