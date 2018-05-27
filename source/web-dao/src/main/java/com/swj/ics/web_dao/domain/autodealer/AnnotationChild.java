package com.swj.ics.web_dao.domain.autodealer;

import com.swj.ics.web_dao.domain.autodealer.interfaces.Description;
import com.swj.ics.web_dao.domain.autodealer.interfaces.Description2;
import com.swj.ics.web_dao.domain.autodealer.interfaces.People;

/**
 * Created by swj on 2017/5/21.
 */
@Description2("i am class annotation")
public class AnnotationChild implements People {

    @Description2("i am method annotation")
    @Override
    public String name() {
        return null;
    }

    @Override
    public int age() {
        return 0;
    }

    @Override
    public void work() {

    }
}
