package com.example.graphqlclient.client;

import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GraphqlClientExample {

    private final HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder()
            .url("http://localhost:8080/hello")
            .build();

    public GraphqlCallVo get(String id){

        /*
        TODO:
         추후 graphql 파일로 관리하셔도 되요
         HttpGraphQlClient API 문서 참고하세요
         https://docs.spring.io/spring-graphql/docs/current/api/org/springframework/graphql/client/HttpGraphQlClient.html
         */
        String query = String.format("""
                query {
                    getBoardById(id: "%s"){
                        id
                        title
                    }
                }
                """, id);

        return graphQlClient
                .document(query)
                .execute()
                .flatMap(response->{
                    //필드 열면서 데이터 확인해보세요 :)
                    System.out.println("response -> "+ response);

                    return Mono.just(
                            response.field("getBoardById").toEntity(GraphqlCallVo.class)
                    );
                })
                .block();
    }


}
