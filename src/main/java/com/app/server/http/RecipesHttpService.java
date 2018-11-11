package com.app.server.http;

import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.Recipe;
import com.app.server.services.RecipesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.json.JSONObject;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("recipes")

public class RecipesHttpService {
    private RecipesService service;
    private ObjectWriter ow;


    public RecipesHttpService() {
        service = RecipesService.getInstance();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }


    @OPTIONS
    @PermitAll
    public Response optionsById() {
        return Response.ok().build();
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getAll() {

        return new APPResponse(service.getAll());
    }

    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("id") String id) {

        return new APPResponse(service.getOne(id));
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse create(Object request) {
        return new APPResponse(service.create(request));
    }

    @PATCH
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse update(@PathParam("id") String id, Object request){

        return new APPResponse(service.update(id, request));

    }

    @DELETE
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse delete(@PathParam("id") String id) {

        return new APPResponse(service.delete(id));
    }

    @DELETE
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse delete() {

        return new APPResponse(service.deleteAll());
    }

    @GET
    @Path("{id}/comments")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getAllComments(@PathParam("id") String id) {

        return new APPResponse(service.getAllComments(id));
    }

    @GET
    @Path("{id}/comments/{cid}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getOneComment(@PathParam("id") String id, @PathParam("cid") String cid) {

        return new APPResponse(service.getOneComment(id, cid));
    }

    @POST
    @Path("{id}/comments")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse createOneComment(@PathParam("id") String id, Object request) {
        return new APPResponse(service.createOneComment(id, request));
    }

    @PATCH
    @Path("{id}/comments/{cid}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse updateOneComment(@PathParam("id") String id, @PathParam("cid") String cid, Object request){

        return new APPResponse(service.updateOneComment(cid, request));

    }

    @DELETE
    @Path("{id}/comments/")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse deleteAllComments(@PathParam("id") String rid) {

        return new APPResponse(service.deleteAllComments(rid));
    }

    @DELETE
    @Path("{id}/comments/{cid}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse deleteOneComment(@PathParam("id") String rid, @PathParam("cid") String cid) {

        return new APPResponse(service.deleteOneComment(rid, cid));
    }
}
