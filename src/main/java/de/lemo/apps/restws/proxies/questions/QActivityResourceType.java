package de.lemo.apps.restws.proxies.questions;

import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.COURSE_IDS;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.END_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.START_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.TYPES;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;

public interface QActivityResourceType {

    @POST
    @Path("activityresourcetype")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultListResourceRequestInfo compute(
            @FormParam(COURSE_IDS) List<Long> courses,
            @FormParam(START_TIME) Long startTime,
            @FormParam(END_TIME) Long endTime,
            @FormParam(TYPES) List<String> resourceTypes);

}