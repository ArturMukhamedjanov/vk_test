package application.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('ROLE_USERS') OR hasAuthority('ROLE_ADMIN')")
public class UsersController {
    private final String BASE_URL = "https://jsonplaceholder.typicode.com/users";
    private final RestTemplate restTemplate;

    public UsersController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("")
    public ResponseEntity<String> getAllUsers() {
        return restTemplate.getForEntity(BASE_URL, String.class);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<String> getUserById(@PathVariable Long postId) {

        String url = BASE_URL + "/" + postId;
        return restTemplate.getForEntity(url, String.class);
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody(required = false) String post) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(post, headers);
        return restTemplate.postForEntity(BASE_URL, requestEntity, String.class);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> updateUser(@PathVariable Long postId, @RequestBody(required = false) String post) {
        String url = BASE_URL + "/" + postId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(post, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long postId) {
        String url = BASE_URL + "/" + postId;
        restTemplate.delete(url);
        return ResponseEntity.noContent().build();
    }
}
