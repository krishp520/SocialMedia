package com.Group10.SocialMediaPlatform.Service;

import com.Group10.SocialMediaPlatform.model.Friend;
import com.Group10.SocialMediaPlatform.model.FriendId;
import com.Group10.SocialMediaPlatform.model.Request;
import com.Group10.SocialMediaPlatform.Repository.FriendRepository;
import com.Group10.SocialMediaPlatform.Repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private FriendRepository friendRepository;

    public Request sendRequest(Request request) {
        request.setStatus("pending");
        return requestRepository.save(request);
    }

    public List<Request> getRequestsByUserId(Integer userId) {
        return requestRepository.findByReceiverUserId(userId);
    }

    public void handleRequest(Integer userId, Integer requestId, String action) {
        Optional<Request> requestOpt = requestRepository.findById(requestId);
        if (requestOpt.isPresent()) {
            Request request = requestOpt.get();
            if (!request.getReceiver().getUserId().equals(userId)) {
                throw new IllegalArgumentException("Request does not belong to this user.");
            }
            request.setStatus(action);

            if ("accepted".equals(action)) {
                Friend receiver = new Friend(new FriendId(request.getSender().getUserId(), request.getReceiver().getUserId()), "accepted", LocalDateTime.now(), request.getSender(), request.getReceiver());
                Friend sender = new Friend(new FriendId(request.getReceiver().getUserId(), request.getSender().getUserId()),
                        "accepted", LocalDateTime.now(), request.getReceiver(), request.getSender());
                friendRepository.save(receiver);
                friendRepository.save(sender);
            }

            requestRepository.delete(request);
        } else {
            throw new IllegalArgumentException("Request not found.");
        }
    }
}
