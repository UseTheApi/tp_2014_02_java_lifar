package database;

import exception.DataServiceException;
import org.hibernate.*;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.criterion.Restrictions;

/**
 * Created by Alena on 10.03.14.
 */
public class AccountsDAO implements interfaceDAO {

    private SessionFactory sessionFactory;

    public AccountsDAO(SessionFactory session)
    {
        this.sessionFactory = session;
    }

    @Override
    public AccountsDataSet getAccount(String name)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(AccountsDataSet.class);
        return (AccountsDataSet) criteria.add(Restrictions.eq("login", name)).uniqueResult();
    }

    @Override
    public boolean saveAccount(AccountsDataSet dataSet)
    {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try
        {
            session.save(dataSet);
            transaction.commit();
        }
        catch (HibernateException e)
        {
            session.close();
            return false;
        }

        session.close();
        return true;
    }
}