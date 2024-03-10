package vk_test_task;

import application.controllers.UsersController;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootTest(classes = UsersController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ROLE_USERS")
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USERS")
    public void testGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USERS")
    public void testCreateUser() throws Exception {
        String newUserJson = "{\"name\":\"John\",\"email\":\"john@example.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                .content(newUserJson)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USERS")
    public void testUpdateUser() throws Exception {
        String updatedUserJson = "{\"name\":\"John\",\"email\":\"john.updated@example.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                .content(updatedUserJson)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USERS")
    public void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    // Переопределение mockMvc с отключенным CORS
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(UsersController.class).build();
    }
}
