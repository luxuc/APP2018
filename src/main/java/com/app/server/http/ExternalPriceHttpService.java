package com.app.server.http;

import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.services.SessionService;
import com.app.server.services.ExternalPriceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("mockServer")
public class ExternalPriceHttpService {
    private ExternalPriceService service;
    private ObjectWriter ow;


    public ExternalPriceHttpService() {
        service = service.getInstance();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    @OPTIONS
    @PermitAll
    public Response optionsById() {

        return Response.ok().build();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getPrice(Object request) {

        return new APPResponse(service.getPrice(request));
    }
}
