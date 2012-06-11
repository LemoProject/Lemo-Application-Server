package de.lemo.apps.restws.proxies.questions;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.tapestry5.json.JSONObject;
 

import de.lemo.apps.restws.entities.ResultListLongObject;

public interface QCourseUserPaths {

    @GET
    @Path("courseuserpaths")
    @Produces("application/json")
    public String compute(
            @QueryParam("course_ids") List<Long> courseIds,
            @QueryParam("start_time") Long startTime,
            @QueryParam("end_time") Long endTime);

}
