package com.example.javaapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.example.javaapp.model.WebhookResponse;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void executeFlow() {
        // Step 1: Generate webhook
        String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Thirumurugan P");
        requestBody.put("regNo", "22BEC1473");
        requestBody.put("email", "thirumuruganpooventhan1074@gmail.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<WebhookResponse> response = restTemplate.exchange(
                generateUrl, HttpMethod.POST, entity, WebhookResponse.class);

        WebhookResponse webhookResponse = response.getBody();
        assert webhookResponse != null;

        String webhookUrl = webhookResponse.getWebhook();
        String accessToken = webhookResponse.getAccessToken();

        // Step 2: Prepare SQL Query (from your Question 1 solution)
        String finalQuery =
                "SELECT p.AMOUNT AS SALARY, " +
                "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
                "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
                "d.DEPARTMENT_NAME " +
                "FROM PAYMENTS p " +
                "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "WHERE DAY(p.PAYMENT_TIME) <> 1 " +
                "ORDER BY p.AMOUNT DESC LIMIT 1;";

        // Step 3: Submit final query to webhook
        Map<String, String> answerBody = new HashMap<>();
        answerBody.put("finalQuery", finalQuery);

        HttpHeaders submitHeaders = new HttpHeaders();
        submitHeaders.setContentType(MediaType.APPLICATION_JSON);
        submitHeaders.set("Authorization", accessToken);

        HttpEntity<Map<String, String>> submitEntity = new HttpEntity<>(answerBody, submitHeaders);

        ResponseEntity<String> submitResponse = restTemplate.exchange(
                webhookUrl, HttpMethod.POST, submitEntity, String.class);

        System.out.println("Submission response: " + submitResponse.getBody());
    }
}
