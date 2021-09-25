package com.handturn.bole.monitor.controller;

import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.utils.DateUtil;
import com.handturn.bole.monitor.endpoint.FebsHttpTraceEndpoint;
import com.handturn.bole.monitor.entity.FebsHttpTrace;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.handturn.bole.monitor.endpoint.FebsHttpTraceEndpoint.FebsHttpTraceDescriptor;

/**
 * @author Eric
 */
@Slf4j
@RestController
@RequestMapping("bole/actuator")
public class FebsActuatorController {

    @Autowired
    private FebsHttpTraceEndpoint httpTraceEndpoint;

    @GetMapping("httptrace")
    @RequiresPermissions("httptrace:view")
    @ControllerEndpoint(exceptionMessage = "请求追踪失败")
    public FebsResponse httpTraces(String method, String url) {
        FebsHttpTraceDescriptor traces = httpTraceEndpoint.traces();
        List<HttpTrace> httpTraceList = traces.getTraces();
        List<FebsHttpTrace> febsHttpTraces = new ArrayList<>();
        httpTraceList.forEach(t -> {
            FebsHttpTrace febsHttpTrace = new FebsHttpTrace();
            febsHttpTrace.setRequestTime(DateUtil.formatInstant(t.getTimestamp(), DateUtil.FULL_TIME_SPLIT_PATTERN));
            febsHttpTrace.setMethod(t.getRequest().getMethod());
            febsHttpTrace.setUrl(t.getRequest().getUri());
            febsHttpTrace.setStatus(t.getResponse().getStatus());
            febsHttpTrace.setTimeTaken(t.getTimeTaken());
            if (StringUtils.isNotBlank(method) && StringUtils.isNotBlank(url)) {
                if (StringUtils.equalsIgnoreCase(method, febsHttpTrace.getMethod())
                        && StringUtils.containsIgnoreCase(febsHttpTrace.getUrl().toString(), url))
                    febsHttpTraces.add(febsHttpTrace);
            } else if (StringUtils.isNotBlank(method)) {
                if (StringUtils.equalsIgnoreCase(method, febsHttpTrace.getMethod()))
                    febsHttpTraces.add(febsHttpTrace);
            } else if (StringUtils.isNotBlank(url)) {
                if (StringUtils.containsIgnoreCase(febsHttpTrace.getUrl().toString(), url))
                    febsHttpTraces.add(febsHttpTrace);
            } else {
                febsHttpTraces.add(febsHttpTrace);
            }
        });
        Map<String, Object> data = new HashMap<>();
        data.put("rows", febsHttpTraces);
        data.put("total", febsHttpTraces.size());
        return new FebsResponse().success().data(data);
    }
}
