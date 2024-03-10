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
@RequestMapping("/api/albums")
@PreAuthorize("hasAuthority('ROLE_ALBUMS') OR hasAuthority('ROLE_ADMIN')")
public class AlbumsController {
    private final String BASE_URL = "https://jsonplaceholder.typicode.com/albums";
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<Long, ResponseEntity<String>> albumCache = new ConcurrentHashMap<>();

    @GetMapping("")
    public ResponseEntity<String> getAllAlbums() {
        if (albumCache.containsKey(0L)) {
            return albumCache.get(0L);
        } else {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(BASE_URL, String.class);
            albumCache.put(0L, responseEntity);
            return responseEntity;
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<String> getAlbumById(@PathVariable Long postId) {
        if (albumCache.containsKey(postId)) {
            return albumCache.get(postId);
        } else {
            String url = BASE_URL + "/" + postId;
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            albumCache.put(postId, responseEntity);
            return responseEntity;
        }
    }

    @PostMapping
    public ResponseEntity<String> createAlbum(@RequestBody(required = false) String post) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(post, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(BASE_URL, requestEntity, String.class);
        albumCache.clear();
        return responseEntity;
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> updateAlbum(@PathVariable Long postId, @RequestBody(required = false) String post) {
        String url = BASE_URL + "/" + postId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(post, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
        albumCache.put(postId, responseEntity);
        return responseEntity;
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long postId) {
        String url = BASE_URL + "/" + postId;
        restTemplate.delete(url);
        albumCache.remove(postId);
        return ResponseEntity.noContent().build();
    }
}
