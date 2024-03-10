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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('ROLE_USERS') OR hasAuthority('ROLE_ADMIN')")
public class UsersController {
    private final String BASE_URL = "https://jsonplaceholder.typicode.com/users";
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<Long, ResponseEntity<String>> userCache = new ConcurrentHashMap<>();

    @GetMapping("")
    public ResponseEntity<String> getAllUsers() {
        if (userCache.containsKey(0L)) {
            return userCache.get(0L);
        } else {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(BASE_URL, String.class);
            userCache.put(0L, responseEntity);
            return responseEntity;
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> getUserById(@PathVariable Long userId) {
        if (userCache.containsKey(userId)) {
            return userCache.get(userId);
        } else {
            String url = BASE_URL + "/" + userId;
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            userCache.put(userId, responseEntity);
            return responseEntity;
        }
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody(required = false) String user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(BASE_URL, requestEntity, String.class);
        userCache.clear();
        return responseEntity;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody(required = false) String user) {
        String url = BASE_URL + "/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
        userCache.put(userId, responseEntity);
        return responseEntity;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        String url = BASE_URL + "/" + userId;
        restTemplate.delete(url);
        userCache.remove(userId);
        return ResponseEntity.noContent().build();
    }
}
