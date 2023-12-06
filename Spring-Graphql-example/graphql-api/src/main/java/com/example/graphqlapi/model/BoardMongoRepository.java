package com.example.graphqlapi.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardMongoRepository extends MongoRepository<Board, String> {
}
