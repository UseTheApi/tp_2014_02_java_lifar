package database;

import messageSystem.MessageSystem;
import messageSystem.Abonent;
import messageSystem.Address;
import exception.AccountServiceException;
import exception.EmptyDataException;
import exception.ExceptionMessageClass;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLException;

/**
 * Created by Alena on 10.03.14.
 */
public class AccountServiceImpl implements AccountService, Abonent, Runnable {
    private AccountsDAO accountsDAO;
    private MessageSystem messageSystem;
    private Address address;
    public static final String OK_SESSION = "It's ok!";

    public AccountServiceImpl(AccountsDAO dao, MessageSystem messageSystem)
    {
        this.accountsDAO = dao;
        setMessageSystem(messageSystem);
    }

    @Override
    public AccountSession regist(String sessionId, String login, String password) throws AccountServiceException
    {
        Long userId = accountsDAO.getAccount(login).getUserId();
        try
        {
            if(accountsDAO.getAccount(login) != null) throw new AccountServiceException(ExceptionMessageClass.EXIST_USER);
            accountsDAO.saveAccount(new AccountsDataSet(login, password));
        }
        catch(ConstraintViolationException e)
        {
            throw new AccountServiceException(e.getMessage());
        }
        return new AccountSession(sessionId, userId, login, OK_SESSION, false);
    }

    @Override
    public AccountSession auth(String sessionId, String login, String password) throws AccountServiceException
    {
        AccountsDataSet account = accountsDAO.getAccount(login);
        try
        {
            if(account == null) throw new AccountServiceException(ExceptionMessageClass.INVALID_LOGIN);
            if(!account.getPassword().equals(password)) throw new AccountServiceException(ExceptionMessageClass.INVALID_PASS);
        }
        catch (ConstraintViolationException e)
        {
            throw new AccountServiceException(e.getMessage());
        }
        return new AccountSession(sessionId, account.getUserId(), login, OK_SESSION, false);
    }

    @Override
    public void deleteAccount(String login) throws AccountServiceException, SQLException
    {
        try
        {
            accountsDAO.deleteAccount(login);
        }
        catch (Exception e)
        {
           e.getMessage();
        }
    }

    @Override
    public Address getAddress() {
        return this.address;
    }

    @Override
    public MessageSystem getMessageSystem()
    {
        return this.messageSystem;
    }

    @Override
    public void setMessageSystem(MessageSystem messageSystem)
    {
        this.messageSystem = messageSystem;
        this.address = new Address();
        messageSystem.addService(this);
        messageSystem.getAddressService().setAccountService(this.address);
    }

    @Override
    public void run()
    {
        while (true) {
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}