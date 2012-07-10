package de.lemo.apps.restws.proxies.questions;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.lemo.apps.restws.entities.ResultListLongObject;

public interface QCourseUsers {

    @GET
    @Path("activecourseusers")
    @Produces("application/json")
    public ResultListLongObject compute(
            @QueryParam("course_ids") List<Long> courseIds,
            @QueryParam("startTime") long startTime,
            @QueryParam("endTime") long endTime);

}
