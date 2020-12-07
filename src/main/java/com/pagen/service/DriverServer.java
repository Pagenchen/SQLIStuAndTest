package com.pagen.service;

import com.pagen.examination.InjectTest.Driver.BoolBlindDriver;
import com.pagen.examination.InjectTest.Driver.Boundaries;
import com.pagen.examination.InjectTest.Driver.ErrorBaseDriver;
import com.pagen.examination.InjectTest.Driver.UnionSelectDriver;

import java.util.List;

public interface DriverServer {
    /**
     * 获取边界Payloads
     *
     * @return
     */
    List<Boundaries> getAllBoundaries();

    /**
     * 获取所有连接查询注入Payloads
     *
     * @return
     */
    List<UnionSelectDriver> getAllUnionSelectDrivers();

    /**
     * 获取所有Bool盲注Payloads
     *
     * @return
     */
    List<BoolBlindDriver> getAllBoolBlindDrivers();

    /**
     * 获取所有报错注入Payloads
     *
     * @return
     */
    List<ErrorBaseDriver> getAllErrorBaseDrivers();
}
