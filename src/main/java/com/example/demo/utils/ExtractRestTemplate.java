package com.example.demo.utils;

import com.example.demo.model.RestResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * TODO
 *
 * @author zhou.xy
 * @date 2019/8/8
 * @since 1.0
 */
public class ExtractRestTemplate extends FilterRestTemplate {
    protected RestTemplate restTemplate;

    public ExtractRestTemplate(RestTemplate restTemplate) {
        super(restTemplate);
        this.restTemplate = restTemplate;
    }

    public <T> RestResponseDTO<T> postForEntityWithNoException(String url, Object request, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        RestResponseDTO<T> restResponseDTO = new RestResponseDTO<T>();
        ResponseEntity<T> tResponseEntity;
        try {
            tResponseEntity = restTemplate.postForEntity(url, request, responseType, uriVariables);
            restResponseDTO.setData(tResponseEntity.getBody());
            restResponseDTO.setMessage(tResponseEntity.getStatusCode().name());
            restResponseDTO.setStatusCode(tResponseEntity.getStatusCodeValue());
        } catch (Exception e) {
            restResponseDTO.setStatusCode(RestResponseDTO.UNKNOWN_ERROR);
            restResponseDTO.setMessage(e.getMessage());
            restResponseDTO.setData(null);
        }
        return restResponseDTO;
    }
}
