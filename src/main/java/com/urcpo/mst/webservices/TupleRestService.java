package com.urcpo.mst.webservices;
import com.urcpo.mst.beans.*;
import com.urcpo.mst.services.TupleService;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

@Path("/tuples")
public class TupleRestService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDefaultUserInJSON() {
        TupleService ts = new TupleService();
        try {
            return Response.status(200).entity(ts.getTuples()).header("Access-Control-Allow-Origin", "*").build();
        } catch ( Exception e ) {
            return Response.status(500).entity(e.getMessage()).header("Access-Control-Allow-Origin", "*").build();
        }
    }
    
    @POST
    @Path("/getQuery/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getQuery(String query) {
        TupleService ts = new TupleService();
        try {
            return Response.status(200).entity(ts.getQuery(query)).header("Access-Control-Allow-Origin", "*").build();
        } catch ( Exception e ) {
            
            return Response.status(200).entity(e.getMessage()).header("Access-Control-Allow-Origin", "*").build();
        }
    }
    @OPTIONS
    @Path(  "/getQuery/" )
    public Response myResource2() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }
    
    @POST
    @Path("/getQueryOnto/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getQueryOnto(String query) {
        TupleService ts = new TupleService();
        try {
            return Response.status(200).entity(ts.getQueryOnto(query)).header("Access-Control-Allow-Origin", "*").build();
        } catch ( Exception e ) {
            
            return Response.status(401).entity(e.getMessage()).header("Access-Control-Allow-Origin", "*").build();
        }
    }
    @OPTIONS
    @Path(  "/getQueryOnto/" )
    public Response myResource2Onto() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }
    @POST
    @Path("/getChildrenOnto/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getQueryChildrenOnto(String query) {
        TupleService ts = new TupleService();
        try {
            return Response.status(200).entity(ts.getChildrenOnto(query)).header("Access-Control-Allow-Origin", "*").build();
        } catch ( Exception e ) {
            
            return Response.status(401).entity(e.getMessage()).header("Access-Control-Allow-Origin", "*").build();
        }
    }
    @OPTIONS
    @Path(  "/getChildrenOnto/" )
    public Response myResource2childrenOnto() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }
    @POST
    @Path("/getQueryOntoSearch/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getQueryOntoSearch(String query) {
        TupleService ts = new TupleService();
        try {
            return Response.status(200).entity(ts.getQueryOntoSearch(query)).header("Access-Control-Allow-Origin", "*").build();
        } catch ( Exception e ) {
            
            return Response.status(401).entity(e.getMessage()).header("Access-Control-Allow-Origin", "*").build();
        }
    }
    @OPTIONS
    @Path(  "/getQueryOntoSearch/" )
    public Response myResource2SearchOnto() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }
    @POST
    @Path("/getOntoIdInfo/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getOntoIdInfo(String query) {
        TupleService ts = new TupleService();
        try {
            return Response.status(200).entity(ts.getOntoIdInfo(query)).header("Access-Control-Allow-Origin", "*").build();
        } catch ( Exception e ) {
            
            return Response.status(401).entity(e.getMessage()).header("Access-Control-Allow-Origin", "*").build();
        }
    }
    @OPTIONS
    @Path(  "/getOntoIdInfo/" )
    public Response getOntoIdInfo() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }
    
}
