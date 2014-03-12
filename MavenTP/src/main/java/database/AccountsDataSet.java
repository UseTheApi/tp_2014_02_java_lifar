package database;

import javax.persistence.*;

/**
 * Created by Alena on 10.03.14.
 */
@Entity
@Table(name="accounts")
public class AccountsDataSet{

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name="login")
    private String username;

    @Column(name="password")
    private String password;

    public AccountsDataSet(long userId,
                           String username,
                           String password)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public AccountsDataSet(String username,
                           String password)
    {
        this.userId = -1;
        this.username = username;
        this.password = password;
    }

    public AccountsDataSet()
    {
        this.userId = -1;
        this.username = "";
        this.password = "";
    }

    public long getUserId()
    {
        return userId;
    }

    public String getLogin()
    {
        return username;
    }

    public  String getPassword()
    {
        return password;
    }

    public void setUserId(long id)
    {
        this.userId = id;
    }

    public void setUsername(String login)
    {
        this.username = login;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
