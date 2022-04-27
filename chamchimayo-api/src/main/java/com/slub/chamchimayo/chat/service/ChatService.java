package com.slub.chamchimayo.chat.service;

import com.slub.chamchimayo.chat.constants.KafkaConstants;
import com.slub.chamchimayo.chat.dto.request.MessageRequest;
import com.slub.chamchimayo.chat.dto.response.MessageResponse;
import com.slub.chamchimayo.chat.entity.Message;
import com.slub.chamchimayo.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final KafkaTemplate<String, MessageResponse> kafkaTemplate;

    private final ChatRepository chatRepository;

    public void sendMessage(MessageRequest messageRequest) {

        messageRequest.parsing();
        Message message = chatRepository.save(messageRequest.toEntity());
        try {
            // post 요청으로 전달받은 메시지를 해당 카프카 토픽에 생산
            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, MessageResponse.from(message)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MessageResponse> fetchMessagePagesBy(Long lastMessageId, int size, Long roomId) {
        Page<Message> messages = fetchPages(lastMessageId, size, roomId);
        return messages.getContent()
                .stream()
                .map(message -> MessageResponse.from(message))
                .collect(Collectors.toList());
    }

    private Page<Message> fetchPages(Long lastMessageId, int size, Long roomId) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return chatRepository.findByIdLessThanAndRoomIdOrderByIdDesc(lastMessageId, roomId, pageRequest);
    }

}
