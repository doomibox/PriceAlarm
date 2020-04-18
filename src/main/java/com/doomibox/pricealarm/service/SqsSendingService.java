package com.doomibox.pricealarm.service;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsSendingService {

    private AmazonSQS sqs;
    private String sqsUrl;

    public SqsSendingService(
            @Value("${sqs.url}") String sqsUrl,
            @Value("${sqs.region}") String sqsRegion) {
        this.sqsUrl = sqsUrl;
        this.sqs = AmazonSQSClientBuilder.standard()
                .withRegion(sqsRegion)
                .build();
    }

    public void sendMessage(String msg) {
        sqs.sendMessage(new SendMessageRequest(sqsUrl, msg));
    }
}
