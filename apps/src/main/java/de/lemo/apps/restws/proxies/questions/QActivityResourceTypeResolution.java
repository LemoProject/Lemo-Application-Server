package de.lemo.apps.restws.proxies.questions;

import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.COURSE_IDS;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.END_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.RESOLUTION;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.START_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.TYPES;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.lemo.apps.restws.entities.ResultListRRITypes;

public interface QActivityResourceTypeResolution {

    @POST
    @Path("activityresourcetyperesolution")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultListRRITypes compute(
            @FormParam(COURSE_IDS) List<Long> courses,
            @FormParam(START_TIME) long startTime,
            @FormParam(END_TIME) long endTime,
            @FormParam(RESOLUTION) long resolution,
            @FormParam(TYPES) List<String> resourceTypes);

}