package com.doomibox.pricealarm.controller;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SchedulerController {

    @Autowired
    private Scheduler scheduler;

    @RequestMapping(value = "/schedules", method = RequestMethod.GET)
    public ResponseEntity<Object> getSchedules() throws SchedulerException {
        log.info("GET schedules request");
        log.info("Job Groups: {}", scheduler.getJobGroupNames().toString());
        return new ResponseEntity<>("Schedules", HttpStatus.ACCEPTED);
    }
}
