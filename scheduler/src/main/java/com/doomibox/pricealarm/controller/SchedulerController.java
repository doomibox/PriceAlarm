package com.doomibox.pricealarm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchedulerController {

    @RequestMapping(value = "/schedules", method = RequestMethod.GET)
    public ResponseEntity<Object> getSchedules() {
        return new ResponseEntity<>("Schedules", HttpStatus.ACCEPTED);
    }
}
