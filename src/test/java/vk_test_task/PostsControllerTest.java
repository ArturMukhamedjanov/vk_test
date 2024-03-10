package vk_test_task;

import application.controllers.PostsController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(classes = PostsController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostsController postsController;

    @Test
    @WithMockUser(authorities = "ROLE_POSTS")
    public void testGetAllPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_POSTS")
    public void testGetPostById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_POSTS")
    public void testCreatePost() throws Exception {
        String newPostJson = "{\"userId\": 1, \"id\": 101, \"title\": \"new post\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                .content(newPostJson)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ROLE_POSTS")
    public void testUpdatePost() throws Exception {
        String updatedPostJson = "{\"userId\": 1, \"id\": 1, \"title\": \"updated post\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/1")
                .content(updatedPostJson)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_POSTS")
    public void testDeletePost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    // Переопределение mockMvc с отключенным CORS
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(postsController).build();
    }
}
