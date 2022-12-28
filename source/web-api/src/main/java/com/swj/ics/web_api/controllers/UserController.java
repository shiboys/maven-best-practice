package com.swj.ics.web_api.controllers;

import com.swj.ics.redis.spring_redis.service.IUserService;
import com.swj.ics.web_dao.domain.autodealer.UnitTestUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by swj on 2016/12/4.
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final Logger logger= LoggerFactory.getLogger("mvcUnitTestLogger");

    @Autowired
    private IUserService userService;

    //=============================================Get All User===========================
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UnitTestUser>> getAll()
    {
        logger.info("getting all users");
        List<UnitTestUser> users=userService.getAll();
        if(users==null || users.isEmpty())
        {
            logger.info("no user found");
            return new ResponseEntity<List<UnitTestUser>>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<UnitTestUser>>(users,HttpStatus.OK);
    }

    //=============================================Get By UserId===========================

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public ResponseEntity<UnitTestUser> get(@PathVariable("id") int id)
    {
        logger.info("called get by userId,userId:{}",id);
        UnitTestUser user = userService.findById(id);
        if(user==null)
        {
            logger.info("user with id {} not found!",id);
            return new ResponseEntity<UnitTestUser>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UnitTestUser>(user,HttpStatus.OK);
    }

    //=============================================Create New User===========================

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody UnitTestUser user, UriComponentsBuilder ucBuilder)
    {
        logger.info("create new user :{}",user);

        if(userService.exists(user))
        {
            logger.info("a user with name {} already exists !",user.getUsername());
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        userService.create(user);
        HttpHeaders headers=new HttpHeaders();
        headers.setLocation(ucBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri());

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
//=============================================Update Existing User===========================

    @RequestMapping(value = "{id}",method = RequestMethod.PUT)
    public ResponseEntity<UnitTestUser> update(@PathVariable int id,@RequestBody UnitTestUser user)
    {
        logger.info("updating user {}",user);

        UnitTestUser currentUser=userService.findById(id);
        if(currentUser==null)
        {
            logger.info("user with id :{} not exists",id);
            return  new ResponseEntity<UnitTestUser>(HttpStatus.NOT_FOUND);
        }

        currentUser.setUsername(user.getUsername());
        userService.update(currentUser);

        return new ResponseEntity<UnitTestUser>(currentUser,HttpStatus.OK);
    }

    //=============================================Delete User By Id===========================

    @RequestMapping(value = "{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable int id)
    {
        logger.info("delete user with id:{}",id);
        UnitTestUser user= userService.findById(id);
        if(user==null)
        {
            logger.info("Unable to delete user, user with id {} not exists",id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        userService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

