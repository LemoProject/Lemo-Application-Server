package de.lemo.apps.restws.proxies.questions;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

public interface QUserPathAnalysis {

    @GET
    @Path("userpathanalysis")
    @Produces("application/json")
    public String compute(
            @QueryParam("course_ids") List<Long> courseIds,
            @QueryParam("user_ids") List<Long> userIds,
            @QueryParam("types") List<String> types,
            @QueryParam("logout_flag") boolean considerLogouts,
            @QueryParam("start_time") Long startTime,
            @QueryParam("end_time") Long endTime);

}
