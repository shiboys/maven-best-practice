package com.swj.ics.redis.spring_redis.service;

import com.swj.ics.web_dao.domain.autodealer.UnitTestUser;

import java.util.List;

/**
 * Created by swj on 2016/12/3.
 */
public interface IUserService {
    List<UnitTestUser> getAll();

    UnitTestUser findById(int id);

    UnitTestUser findByName(String name);

    void  create(UnitTestUser user);

    void update(UnitTestUser user);

    void delete(int id);

    boolean exists(UnitTestUser user);
}
