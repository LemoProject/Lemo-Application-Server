package de.lemo.apps.restws.proxies.questions;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public interface QUserPathAnalysis {

    @POST
    @Path("userpathanalysis")
    @Produces("application/json")
    public String compute(
            @FormParam("course_ids") List<Long> courseIds,
            @FormParam("user_ids") List<Long> userIds,
            @FormParam("types") List<String> types,
            @FormParam("logout_flag") boolean considerLogouts,
            @FormParam("start_time") Long startTime,
            @FormParam("end_time") Long endTime);

}
