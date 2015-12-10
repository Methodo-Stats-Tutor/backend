package com.urcpo.mst.webservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.xenei.jena.entities.MissingAnnotation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.urcpo.mst.beans.User;
import com.urcpo.mst.services.IdentService;
import com.urcpo.mst.services.QcmService;
import com.urcpo.mst.utils.MstUtils;
import com.urcpo.mst.utils.UserEnum;

@Path( "/ident" )
public class IdentRestService {
    private static final Logger log = Logger.getLogger( IdentRestService.class );

    @GET
    @Path( "/admin" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response isAdmin() {
      
        try {
            return Response.status( 200 )
                    .header( "Access-Control-Allow-Origin", "http://localhost" )
                    .header( "Access-Control-Allow-Headers", "Authorization,Content-Type" )
                    .header( "Access-Control-Allow-Credentials", true ).build();
        } catch ( Exception e ) {
            return Response.status( 500 ).header( "Access-Control-Allow-Origin", "*" )
                    .build();
        }
    }

    @GET
    @Path( "/teacher" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response isTeacber() {
       
        try {
            return Response.status( 200 )
                    .header( "Access-Control-Allow-Origin", "http://localhost" )
                    .header( "Access-Control-Allow-Headers", "Authorization,Content-Type" )
                    .header( "Access-Control-Allow-Credentials", true ).build();
        } catch ( Exception e ) {
            return Response.status( 500 ).header( "Access-Control-Allow-Origin", "*" )
                    .build();
        }
    }

    @GET
    @Path( "/student" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response isStudent() {
     
        try {
            return Response.status( 200 )
                    .header( "Access-Control-Allow-Origin", "http://localhost" )
                    .header( "Access-Control-Allow-Headers", "Authorization,Content-Type" )
                    .header( "Access-Control-Allow-Credentials", true ).build();
        } catch ( Exception e ) {
            return Response.status( 500 ).header( "Access-Control-Allow-Origin", "*" )
                    .build();
        }
    }

    @GET
    @Path( "/guest" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response isguest() {
     
        try {
            return Response.status( 200 )
                    .header( "Access-Control-Allow-Origin", "http://localhost" )
                    .header( "Access-Control-Allow-Headers", "Authorization,Content-Type" )
                    .header( "Access-Control-Allow-Credentials", true ).build();
        } catch ( Exception e ) {
            return Response.status( 500 ).header( "Access-Control-Allow-Origin", "*" )
                    .build();
        }
    }

    @GET
    @Path( "/user/{uid}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getUserProfile( @PathParam( "uid" ) String uid ) {
        IdentService is = new IdentService();
        try {
            return Response.status( 200 ).entity( is.getUserProfile( uid ) )
                    .header( "Access-Control-Allow-Origin", "http://localhost" )
                    .header( "Access-Control-Allow-Headers", "Authorization,Content-Type" )
                    .header( "Access-Control-Allow-Credentials", true ).build();
        } catch ( Exception e ) {
            return Response.status( 500 ).header( "Access-Control-Allow-Origin", "*" )
                    .build();
        }
    }

    @PUT
    @Path( "/user/{uid}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateUserProfile( @PathParam( "uid" ) String uid, String profile ) throws MissingAnnotation {
        IdentService is = new IdentService();
        log.error( "test" );
        is.updateUserProfile( uid, profile );
        try {
            log.error( "test2" );
            return Response.status( 200 )
                    .header( "Access-Control-Allow-Origin", "*" )
                    .build();
        } catch ( Exception e ) {
            return Response.status( 500 ).header( "Access-Control-Allow-Origin", "*" )
                    .build();
        }
    }

    @POST
    @Path( "/post" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response get( String credentials ) {
        Gson gson = new Gson();

        try {
            JsonElement je = gson.fromJson( credentials, JsonElement.class );
            JsonObject jo = je.getAsJsonObject();

            String username = jo.get( "username" ).getAsString();
            String password = jo.get( "password" ).getAsString();

            Client client = Client.create();
            WebResource webResource = client
                    .resource( "http://localhost:8081/mst/rest/ident/admin" );
            client.addFilter( new HTTPBasicAuthFilter( username, password ) );
            UserEnum type;

            // Get the protected web page:
            webResource = client.resource( "http://localhost:8081/mst/rest/ident/admin" );
        

            ClientResponse response = webResource.get( ClientResponse.class );
            if ( response.getStatus() != 200 ) {// pas admin...
                webResource = client.resource( "http://localhost:8081/mst/rest/ident/teacher" );
                response = webResource.get( ClientResponse.class );
                if ( response.getStatus() != 200 ) {// pas teacher...
                    webResource = client.resource( "http://localhost:8081/mst/rest/ident/student" );
                    response = webResource.get( ClientResponse.class );
                    if ( response.getStatus() != 200 ) {// pas student...
                        webResource = client.resource( "http://localhost:8081/mst/rest/ident/guest" );
                        response = webResource.get( ClientResponse.class );
                        if ( response.getStatus() != 200 ) {// pas teacher...
                            return Response.status( 403 ).entity( response.getStatus() )
                                    .header( "Access-Control-Allow-Origin", "*" )
                                    .build();
                        } else {// guest
                            type = UserEnum.guest;
                        }

                    } else {// student
                        type = UserEnum.student;
                    }

                } else {// teacher
                    type = UserEnum.teacher;
                }
            } else {// admin
                type = UserEnum.admin;
            }
            IdentService ids = new IdentService();
            User usr = ids.setUser( username, type );
            String name = usr.getNom() + " " + usr.getPrenom();
            JsonObject dataset = new JsonObject();
            // add the property album_id to the dataset
            Gson gs = new Gson();
            JsonElement jes = gs.fromJson( usr.toJson(), JsonElement.class );
            JsonObject jos = jes.getAsJsonObject();
            jos.addProperty( "role", type.toString() );
//            dataset.addProperty( "userId", username );
//            dataset.addProperty( "userFName", usr.getPrenom() );
//            dataset.addProperty( "userLName", usr.getNom() );
//            dataset.addProperty( "userD8Add", MstUtils.quoteDate( usr.getD8Add() ) );
//            dataset.addProperty( "userRole", type.toString() );
            log.error( credentials );
            return Response.status( 200 ).entity( jos.toString() )
                    .header( "Access-Control-Allow-Origin", "*" ).build();
        } catch ( Exception e ) {
            return Response.status( 500 ).entity( e.getMessage() ).header( "Access-Control-Allow-Origin", "*" )
                    .build();
        }
    }

    @OPTIONS
    @Path( "/send" )
    public Response myResource2() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" ).header( "AccessControlAllowCredentials", true )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }

    @OPTIONS
    @Path( "/post" )
    public Response myResource23() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST, PUT" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, PUT, OPTIONS" ).build();
    }

    @OPTIONS
    @Path( "/user/{uid}" )
    public Response myResource234() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST, PUT" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, PUT, OPTIONS" ).build();
    }

}
