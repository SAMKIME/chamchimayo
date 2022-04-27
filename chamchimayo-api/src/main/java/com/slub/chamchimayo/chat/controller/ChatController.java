package com.slub.chamchimayo.chat.controller;

import com.slub.chamchimayo.chat.dto.request.MessageRequest;
import com.slub.chamchimayo.chat.dto.response.MessageResponse;
import com.slub.chamchimayo.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;

    @PostMapping(value = "/api/send", consumes = "application/json", produces = "application/json")
    public void sendMessage(@RequestBody MessageRequest messageRequest) {
        chatService.sendMessage(messageRequest);
    }

    @GetMapping("/data")
    public ResponseEntity<List<MessageResponse>> getMessages(@RequestParam Long lastMessageId,
                                                             @RequestParam int size,
                                                             @RequestParam Long roomId) {

        List<MessageResponse> messageResponses = chatService.fetchMessagePagesBy(lastMessageId, size, roomId);
        return new ResponseEntity<>(messageResponses, HttpStatus.OK);
    }
}
