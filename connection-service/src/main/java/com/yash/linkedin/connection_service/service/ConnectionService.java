package com.yash.linkedin.connection_service.service;

import com.yash.linkedin.connection_service.auth.UserContextHolder;
import com.yash.linkedin.connection_service.dto.PersonDTO;
import com.yash.linkedin.connection_service.entity.Person;
import com.yash.linkedin.connection_service.events.AcceptConnectionRequestEvent;
import com.yash.linkedin.connection_service.events.SendConnectionRequestEvent;
import com.yash.linkedin.connection_service.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptRequestKafkaTemplate;

    public List<PersonDTO> getFirstDegreeConnection() {
        Long userId = UserContextHolder.getCurrentUserId();
        List<Person> connection = connectionRepository.getFirstDegreeConnections(userId);
         return connection.stream().map(connection1 -> modelMapper.map(connection1, PersonDTO.class)).toList();

    }

    public void requestConnection(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        if(senderId.equals(receiverId)){
            throw new RuntimeException("Both Sender and receiver cannot be same");
        }

        boolean connectionRequestAlreadyExist = connectionRepository.connectionRequestExists(senderId,receiverId);
        if(connectionRequestAlreadyExist){
            throw new RuntimeException("Connection Request already exist, cannot send again");
        }

        boolean alreadyConnected = connectionRepository.alreadyConnected(senderId,receiverId);
        if(alreadyConnected){
            throw new RuntimeException("Connection already exist, cannot connect again");
        }
        connectionRepository.requestConnection(senderId,receiverId);

        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .build();

        sendRequestKafkaTemplate.send("send-connection-request-topic",sendConnectionRequestEvent);
    }

    public void acceptConnectionRequest(Long senderId){
         Long receiverId = UserContextHolder.getCurrentUserId();


        boolean connectionRequestAlreadyExist = connectionRepository.connectionRequestExists(senderId,receiverId);
        if(!connectionRequestAlreadyExist){
            throw new RuntimeException("Connection Request dose not exist, cannot connect");
        }

        boolean alreadyConnected = connectionRepository.alreadyConnected(senderId,receiverId);
        if(alreadyConnected){
            throw new RuntimeException("Connection already exist, cannot connect again");
        }

        connectionRepository.acceptConnectionRequest(senderId,receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        acceptRequestKafkaTemplate.send("accept-connection-request-topic", acceptConnectionRequestEvent);
    }

    public void rejectConnectionRequest(Long senderId){
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists = connectionRepository.connectionRequestExists(senderId, receiverId);
        if (!connectionRequestExists) {
            throw new RuntimeException("No connection request exists, cannot delete");
        }
        connectionRepository.rejectConnectionRequest(senderId,receiverId);
    }

}
