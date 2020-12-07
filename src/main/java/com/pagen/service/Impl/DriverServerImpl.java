package com.pagen.service.Impl;

import com.pagen.dao.DriverDao;
import com.pagen.examination.InjectTest.Driver.BoolBlindDriver;
import com.pagen.examination.InjectTest.Driver.Boundaries;
import com.pagen.examination.InjectTest.Driver.ErrorBaseDriver;
import com.pagen.examination.InjectTest.Driver.UnionSelectDriver;
import com.pagen.service.DriverServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("driverServer")
public class DriverServerImpl implements DriverServer {
    @Resource
    DriverDao driverDao;

    @Override
    public List<Boundaries> getAllBoundaries() {
        return driverDao.getAllBoundaries();
    }

    @Override
    public List<UnionSelectDriver> getAllUnionSelectDrivers() {
        return driverDao.getAllUnionSelectDrivers();
    }

    @Override
    public List<BoolBlindDriver> getAllBoolBlindDrivers() {
        return driverDao.getAllBoolBlindDrivers();
    }

    @Override
    public List<ErrorBaseDriver> getAllErrorBaseDrivers() {
        return driverDao.getAllErrorBaseDrivers();
    }
}
