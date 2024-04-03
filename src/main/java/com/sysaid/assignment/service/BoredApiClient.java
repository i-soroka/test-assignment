package com.sysaid.assignment.service;

import com.sysaid.assignment.domain.task.dto.TaskResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BoredApiClient implements IBoredApiClient {

    @Value("${external.boredapi.baseURL}")
    private String baseUrl;
    private final WebClient client;
    private static final String REQUEST_ACTIVITY_BY_TYPE_PATTERN = "%s/activity?type=%s";

    public BoredApiClient() {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public TaskResponse getRandomTaskByType(String type) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<TaskResponse> responseEntity = template.getForEntity(
            String.format(REQUEST_ACTIVITY_BY_TYPE_PATTERN, baseUrl, type),
            TaskResponse.class
        );
        return responseEntity.getBody();
    }
}
