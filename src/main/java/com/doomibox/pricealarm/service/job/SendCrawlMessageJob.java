package com.doomibox.pricealarm.service.job;

import com.doomibox.pricealarm.service.SqsSendingService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SendCrawlMessageJob implements Job {

    @Autowired
    private SqsSendingService sqs;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Map<String, String> dataMap = new HashMap<>();
        jobDataMap.forEach((k, v) -> { dataMap.put(k, v.toString()); });
        String jsonData = new JSONObject(dataMap).toString();
        log.info("Sending Crawl Message to SQS: {}", jsonData);
        sqs.sendMessage(jsonData);
    }
}
