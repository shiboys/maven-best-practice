package com.swj.ics.redis.spring_redis.service.impl;

import com.swj.ics.redis.spring_redis.service.IEmpService;
import com.swj.ics.web_dao.domain.autodealer.Emp;
import com.swj.ics.web_dao.inte.autodealer.EmpMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by swj on 2016/12/1.
 */
    @Service
    public class EmpServiceImpl implements IEmpService {

    //@Resource
    private EmpMapper empDao;

    //如果没有transaction异常标注，则会提交数据库
   @Transactional
    public void addEmp(Emp emp) {
            empDao.insertSelective(emp);
        String msg=null;
        if(msg.equals(""))//这里会抛出nullpointerException
        {
            int i=0;
        }
    }
}
