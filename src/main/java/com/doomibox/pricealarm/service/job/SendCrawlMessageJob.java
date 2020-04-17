package com.doomibox.pricealarm.service.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class SendCrawlMessageJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Sending Crawl Message to SQS: {}", jobExecutionContext.getJobDetail().getJobDataMap().getString("url"));
        // TODO: Send Message to SQS
    }
}
