package com.swj.ics.spring_redis.service.impl;

import com.swj.ics.spring_redis.service.IUserService;
import com.swj.ics.web_dao.domain.autodealer.UnitTestUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by swj on 2016/12/4.
 */
    public class UserServiceImpl implements IUserService {

    private static final AtomicInteger counter=new AtomicInteger();
    private static List<UnitTestUser> users=new ArrayList<UnitTestUser>(
            Arrays.asList(
             new UnitTestUser(counter.incrementAndGet(),"张三"),
                    new UnitTestUser(counter.incrementAndGet(),"李四"),
                    new UnitTestUser(counter.incrementAndGet(),"王五"),
                    new UnitTestUser(counter.incrementAndGet(),"赵六")
            ));


    public List<UnitTestUser> getAll() {
        return users;
    }

    public UnitTestUser findById(int id) {
        for(UnitTestUser user : users)
        {
            if(user.getId()==id)
            {
                return user;
            }
        }
        return null;
    }

    public UnitTestUser findByName(String name) {
        for(UnitTestUser user : users)
        {
            if(user.getUsername().equals(name))
            {
                return user;
            }
        }
        return null;
    }

    public void create(UnitTestUser user) {
        user.setId(counter.incrementAndGet());
        users.add(user);
    }

    public void update(UnitTestUser user) {
        int index=users.indexOf(findById(user.getId()));
        users.set(index,user);
    }

    public void delete(int id) {
        UnitTestUser user=findById(id);
        users.remove(user);
    }

    public boolean exists(UnitTestUser user) {
        return findByName(user.getUsername())!=null;
    }
}
