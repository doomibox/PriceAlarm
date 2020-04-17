package com.doomibox.pricealarm.controller;

import com.doomibox.pricealarm.dao.AddJobRequest;
import com.doomibox.pricealarm.dao.DeleteJobRequest;
import com.doomibox.pricealarm.dao.JobInfo;
import com.doomibox.pricealarm.service.job.SendCrawlMessageJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class SchedulerController {

    public static final String CRAWL_JOBS_GROUP = "crawl-jobs";
    public static final int INTERVAL_MIN = 1;

    @Autowired
    private Scheduler scheduler;

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public ResponseEntity<Object> listJobs() throws SchedulerException {
        log.info("GET listJobs request");

        List<JobInfo> jobInfoList = new ArrayList<>();
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                JobDataMap jobDataMap = jobDetail.getJobDataMap();
                jobInfoList.add(JobInfo.builder()
                        .jobKey(jobDetail.getKey().getName())
                        .merchantName(jobDataMap.getString("name"))
                        .merchantUrl(jobDataMap.getString("url"))
                        .build());
            }
        }
        return ResponseEntity.ok(jobInfoList);
    }

    @RequestMapping(value = "/addJob", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> addJob(@RequestBody AddJobRequest request) {
        log.info("POST addJob request: {}", request);
        if (request.getMerchantUrl() == null) {
            String errMsg = "Missing merchantUrl";
            log.info("POST addJob err: {}", errMsg);
            return ResponseEntity.badRequest().body("{\"err\":\"" + errMsg + "\"}");
        }

        try {
            JobDetail jobDetail = buildJobDetail(request);
            Trigger trigger = buildJobTrigger(jobDetail);
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"err\":\"Internal Scheduling Error\"");
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteJob", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> deleteJob(@RequestBody DeleteJobRequest request) {
        log.info("POST deleteJob request: {}", request);
        if (request.getJobKey() == null) {
            String errMsg = "Missing jobKey";
            log.info("POST deleteJob err: {}", errMsg);
            return ResponseEntity.badRequest().body("{\"err\":\"" + errMsg + "\"}");
        }

        try {
            scheduler.deleteJob(JobKey.jobKey(request.getJobKey(), CRAWL_JOBS_GROUP));
            // TODO: Also delete related data in MerchantPriceDB
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"err\":\"Internal Scheduling Error\"");
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private JobDetail buildJobDetail(AddJobRequest request) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("name", request.getMerchantName());
        jobDataMap.put("url", request.getMerchantUrl());

        return JobBuilder.newJob(SendCrawlMessageJob.class)
                .withIdentity(UUID.randomUUID().toString(), CRAWL_JOBS_GROUP)
                .withDescription("Crawling Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName())
                .withDescription("Crawling Trigger")
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(INTERVAL_MIN))
                .build();
    }
}
