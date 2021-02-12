package ro.sapientia2015.story.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.internal.matchers.Contains;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;

import ro.sapientia2015.story.controller.StoryController;
import ro.sapientia2015.story.model.Story;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("storyData.xml")
public class ITHomeControllerTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void showHomePage() throws Exception {
        mockMvc.perform(get("/scrumsapientia"))
                .andExpect(status().isOk())
                .andExpect(view().name(HomeController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/home/list.jsp"))
                .andExpect(model().attribute(HomeController.MODEL_ATTRIBUTE_LIST_BUG, hasSize(2)))
                .andExpect(model().attribute(HomeController.MODEL_ATTRIBUTE_LIST_STORY, hasSize(2)))             
                .andExpect(model().attribute(HomeController.MODEL_ATTRIBUTE_LIST_STORY, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("title", is("Bar"))
                        )
                )));
    } 

}