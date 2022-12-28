package com.swj.ics.redis.spring_redis.serviceTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swj.ics.redis.spring_redis.service.IUserService;
import com.swj.ics.web_api.config.WebConfig;
import com.swj.ics.web_api.controllers.UserController;
import com.swj.ics.web_api.filter.CORSFilter;
import com.swj.ics.web_dao.domain.autodealer.UnitTestUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by swj on 2016/12/5.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class)
public class UserControllerUnitTest  {

    private MockMvc mockMvc;

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        mockMvc= MockMvcBuilders.standaloneSetup(userController)
                .addFilter(new CORSFilter())
                .build();
    }

    @Test
    public void test_get_all_user_expected_success() throws Exception {
        List<UnitTestUser> users= Arrays.asList(
          new UnitTestUser(1,"张三"),
                new UnitTestUser(2,"李四")
        );

        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$",hasSize(2)))
        .andExpect(jsonPath("$[0].id",is(1)))
        .andExpect(jsonPath("$[0].username",is("张三")))
        .andExpect(jsonPath("$[1].id",is(2)))
        .andExpect(jsonPath("$[1].username",is("李四")))
        ;

        verify(userService,times(1)).getAll();
        verifyNoMoreInteractions(userService);
    }


    @Test
    public  void test_get_by_userId_expected_success() throws Exception
    {
        Integer id=1;
        String userName="zhangsan";
        UnitTestUser user=new UnitTestUser(id,userName);

        when(userService.findById(id)).thenReturn(user);

        mockMvc.perform(get("/users/{id}",id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id",is(id)))
                .andExpect(jsonPath("$.username",is(userName)))
        ;
        verify(userService,times(1)).findById(id);
    }

    @Test
    public void test_get_by_userid_when_user_is_null_then_400_not_found() throws Exception
    {
        UnitTestUser user=null;
        int userId=1;
        when(userService.findById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/"+ userId))
                .andExpect(status().isNotFound());
        verify(userService,times(1)).findById(userId);
    }

    @Test
    public void test_create_user_success() throws Exception
    {
        UnitTestUser user=new UnitTestUser("zhangsan");
        when(userService.exists(user)).thenReturn(false);
        doNothing().when(userService).create(user);

        mockMvc.perform(
                post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializeObject(user)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location",containsString("/users/0")));

        verify(userService,times(1)).exists(user);
        verify(userService,times(1)).create(user);
    }

    @Test
    public void test_update_user_success() throws  Exception
    {
        int userId=1;
        UnitTestUser user=new UnitTestUser(userId,"zhangsan");
        when(userService.findById(userId)).thenReturn(user);
        doNothing().when(userService).update(user);

        mockMvc.perform( put("/users/{id}",userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(serializeObject(user)))
                .andExpect(status().isOk());
        verify(userService,times(1)).findById(userId);
        verify(userService,times(1)).update(user);

        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_update_user_expected_404_not_found_when_user_not_exists() throws Exception
    {
        UnitTestUser user=null;
        int userId=1;
        user=new UnitTestUser(userId,"user not found");
        when(userService.findById(userId)).thenReturn(null);

        mockMvc.perform(put("/users/{id}",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializeObject(user)))
                .andExpect(status().isNotFound());

        verify(userService,times(1)).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_delete_user_success() throws Exception
    {
        int userId=1;
        UnitTestUser user=new UnitTestUser(userId,"zhangsan");

        when(userService.findById(userId)).thenReturn(user);
        doNothing().when(userService).delete(userId);

        mockMvc.perform(delete("/users/{id}",userId))
                .andExpect(status().isOk());

        verify(userService,times(1)).findById(userId);
        verify(userService,times(1)).delete(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_delete_user_expected_404_not_found_when_user_not_exists() throws Exception
    {
        int userId=1;
        //UnitTestUser user=new UnitTestUser(userId,"zhangsan");
        when(userService.findById(userId)).thenReturn(null);

        mockMvc.perform(delete("/users/{id}",userId))
                .andExpect(status().isNotFound());
        verify(userService,times(1)).findById(userId);
        verify(userService,times(0)).delete(userId);
        verifyNoMoreInteractions(userService);
    }


    private String serializeObject(final Object o)
    {
        try {
            ObjectMapper mapper=new ObjectMapper();
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




}
