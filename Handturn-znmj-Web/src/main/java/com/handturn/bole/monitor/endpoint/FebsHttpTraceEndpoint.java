package com.handturn.bole.monitor.endpoint;

import com.handturn.bole.common.annotation.FebsEndPoint;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;

import java.util.List;

/**
 * @author Eric
 */
@FebsEndPoint
public class FebsHttpTraceEndpoint {

    private final HttpTraceRepository repository;

    public FebsHttpTraceEndpoint(HttpTraceRepository repository) {
        this.repository = repository;
    }

    public FebsHttpTraceDescriptor traces() {
        return new FebsHttpTraceDescriptor(this.repository.findAll());
    }

    public static final class FebsHttpTraceDescriptor {

        private final List<HttpTrace> traces;

        private FebsHttpTraceDescriptor(List<HttpTrace> traces) {
            this.traces = traces;
        }

        public List<HttpTrace> getTraces() {
            return this.traces;
        }
    }
}
