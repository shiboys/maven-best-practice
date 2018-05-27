package com.swj.ics.web_dao.domain.autodealer;

import java.io.Serializable;

/**
 * Created by swj on 2016/12/3.
 */
public class UnitTestUser implements Serializable {
 private int id;
    private String username;

    public UnitTestUser(int id,  String username) {
        this.id = id;
        this.username = username;

    }

    public UnitTestUser(String username) {
        this.username = username;
    }

    public UnitTestUser()
    {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitTestUser that = (UnitTestUser) o;

        if (id != that.id) return false;
        if(username!=null)
        {
            if(!username.equals(that.username))
                return false;
        }
        else
        {
            if(that.username!=null)
                return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username!=null ? username.hashCode():0);
        return result;
    }

    @Override
    public String toString() {
        return "UnitTestUser{" +
                "id=" + id +
                ", name='" + username + '\'' +
                '}';
    }
}
