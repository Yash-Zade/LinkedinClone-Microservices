package com.yash.linkedin.connection_service.repository;

import com.yash.linkedin.connection_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends Neo4jRepository<Person,Long> {

    @Query("MATCH (personA:Person) -[:CONNECTED_TO]- (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("MATCH (personA:Person) -[:CONNECTED_TO*2]- (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN DISTINCT personB")
    List<Person> getSecondDegreeConnections(Long userId);

}
