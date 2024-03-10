package vk_test_task;

import application.controllers.AlbumsController;

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

@SpringBootTest(classes = AlbumsController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AlbumsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlbumsController albumsController;

    @Test
    @WithMockUser(authorities = "ROLE_ALBUMS")
    public void testGetAllAlbums() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/albums"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ALBUMS")
    public void testGetAlbumById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ALBUMS")
    public void testCreateAlbum() throws Exception {
        String newAlbumJson = "{\"userId\": 1, \"id\": 101, \"title\": \"new album\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/albums")
                .content(newAlbumJson)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ALBUMS")
    public void testUpdateAlbum() throws Exception {
        String updatedAlbumJson = "{\"userId\": 1, \"id\": 1, \"title\": \"updated album\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/albums/1")
                .content(updatedAlbumJson)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ALBUMS")
    public void testDeleteAlbum() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    // Переопределение mockMvc с отключенным CORS
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(albumsController).build();
    }
}
