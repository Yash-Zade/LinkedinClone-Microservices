package com.yash.linkedin.connection_service.service;

import com.yash.linkedin.connection_service.dto.PersonDTO;
import com.yash.linkedin.connection_service.entity.Person;
import com.yash.linkedin.connection_service.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final ModelMapper modelMapper;

    public List<PersonDTO> getFirstDegreeConnection(Long userId) {
        List<Person> connection = connectionRepository.getFirstDegreeConnections(userId);
         return connection.stream().map(connection1 -> modelMapper.map(connection1, PersonDTO.class)).toList();

    }
}
