package application.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/albums")
public class AlbumsController {
    private final String BASE_URL = "https://jsonplaceholder.typicode.com/albums";
    private final RestTemplate restTemplate;

    public AlbumsController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("")
    public ResponseEntity<String> getAllAlbums() {
        return restTemplate.getForEntity(BASE_URL, String.class);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<String> getAlbumById(@PathVariable Long postId) {
        String url = BASE_URL + "/" + postId;
        return restTemplate.getForEntity(url, String.class);
    }

    @PostMapping
    public ResponseEntity<String> createAlbum(@RequestBody(required = false) String post) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(post, headers);
        return restTemplate.postForEntity(BASE_URL, requestEntity, String.class);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> updateAlbum(@PathVariable Long postId, @RequestBody(required = false) String post) {
        String url = BASE_URL + "/" + postId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(post, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long postId) {
        String url = BASE_URL + "/" + postId;
        restTemplate.delete(url);
        return ResponseEntity.noContent().build();
    }
}