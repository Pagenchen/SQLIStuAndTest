package com.pagen.controller;

import com.pagen.examination.InjectTest.Driver.Boundaries;
import com.pagen.service.DriverServer;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class DriverController {
    @Resource
    DriverServer driverServer;

    public List<Boundaries> getAllBoundaries(){
        return driverServer.getAllBoundaries();
    }
}
