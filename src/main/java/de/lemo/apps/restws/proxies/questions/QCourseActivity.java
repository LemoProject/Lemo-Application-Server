package de.lemo.apps.restws.proxies.questions;

import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.COURSE_IDS;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.END_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.RESOLUTION;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.ROLE_IDS;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.START_TIME;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.lemo.apps.restws.entities.ResultListLongObject;

public interface QCourseActivity {

    @POST
    @Path("courseactivity")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultListLongObject compute(
            @FormParam(COURSE_IDS) List<Long> courses,
            @FormParam(ROLE_IDS) List<Long> roles,
            @FormParam(START_TIME) Long starttime,
            @FormParam(END_TIME) Long endtime,
            @FormParam(RESOLUTION) Integer resolution);
}
