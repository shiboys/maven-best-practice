package com.swj.ics.spring_redis.serviceTest;

import com.swj.ics.spring_redis.service.IEmpService;
import com.swj.ics.web_dao.domain.autodealer.Emp;
import com.swj.ics.web_dao.inte.autodealer.EmpMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by swj on 2016/12/1.
 */
public class SpringTransactionTest extends SpringTestCase {

    @Autowired
    private IEmpService empService;

    //@Test
    public void test_spring_transaction()
    {
        Emp emp=new Emp();
        emp.setEname("somebody");
        emp.setSalary(new BigDecimal(12000));
        emp.setHiredate(new Date(2000,1,1));
        emp.setDeptno(101);

        empService.addEmp(emp);
    }
    //到这里应该回滚事务
}
