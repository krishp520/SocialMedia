package com.Group10.SocialMediaPlatform.Controller;

import com.Group10.SocialMediaPlatform.model.Request;
import com.Group10.SocialMediaPlatform.Service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @PostMapping
    public Request sendRequest(@RequestBody Request request) {
        return requestService.sendRequest(request);
    }

    @GetMapping("/{userId}")
    public List<Request> getRequestsByUserId(@PathVariable Integer userId) {
        return requestService.getRequestsByUserId(userId);
    }

    @PatchMapping("/{userId}/{requestId}")
    public void handleRequest(@PathVariable Integer userId, @PathVariable Integer requestId, @RequestParam String action) {
        requestService.handleRequest(userId, requestId, action);
    }
}
