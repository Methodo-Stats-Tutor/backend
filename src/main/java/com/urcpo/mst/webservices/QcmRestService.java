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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.urcpo.mst.beans.Qcm;
import com.urcpo.mst.services.CourseMaterialService;
import com.urcpo.mst.services.QcmService;

@Path( "/qcm" )
public class QcmRestService {
    private static final Logger log = Logger.getLogger( QcmRestService.class );

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response getDefaultUserInJSON() {
        CourseMaterialService userService = new CourseMaterialService();
        return Response.status( 200 ).entity( userService.getPublis().toJson() )
                .header( "Access-Control-Allow-Origin", "*" ).build();
    }

    @POST
    @Path( "/correct/{uid}" )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response correctAnswers( @PathParam( "uid" ) String uid, String answers ) throws Exception {
        QcmService qs = new QcmService();

        return Response.status( 200 ).entity( qs.correctAnswers( uid, answers ) )
                .header( "Access-Control-Allow-Origin", "*" ).build();
    }

    @POST
    @Path( "/qcm" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createQcm( String json ) {
        try {
            QcmService pas = new QcmService();
            return Response.status( 200 ).entity( "{\"qcmUid\":\"" + pas.createQcm( json ).getUid() + "\"}" )
                    .header( "Access-Control-Allow-Origin", "*" ).build();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( e.getMessage() );
            return Response.status( 500 ).entity( e.toString() ).build();
        }
    }
    
    @POST
    @Path( "/qcm/save" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response saveQcm( String json ) {
        try {
            QcmService pas = new QcmService();
            return Response.status( 200 ).entity( "{\"qcmUid\":\"" + pas.saveQcm( json ).getUid() + "\"}" )
                    .header( "Access-Control-Allow-Origin", "*" ).build();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( e.getMessage() );
            return Response.status( 500 ).entity( e.toString() ).build();
        }
    }
    
    @POST
    @Path( "/qcm/savenotion/{qcmUid}" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response saveQcmNotion( @PathParam( "qcmUid" ) String qcmUid, String json ) {
        try {
            QcmService pas = new QcmService();
            return Response.status( 200 ).entity( "{\"qcmUid\":\"" + pas.saveQcmNotion( json, qcmUid  ).getUid() + "\"}" )
                    .header( "Access-Control-Allow-Origin", "*" ).build();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( e.getMessage() );
            return Response.status( 500 ).entity( e.toString() ).build();
        }
    }

    @PUT
    @Path( "/qcm/{qcmUid}" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateQcm( @PathParam( "qcmUid" ) String qcmUid, String json ) {
        QcmService userService = new QcmService();
        try {
            Gson gson = new Gson();
            log.error( json );
            JsonElement je = gson.fromJson( json, JsonElement.class );
            JsonObject qcmJson = je.getAsJsonObject();
            Qcm qcm = userService.updateQcm( qcmJson, qcmUid );
            return Response.status( 200 ).entity( "{\"qcmUid\" : \"" + qcm.getUid() + "\"}" )
                    .header( "Access-Control-Allow-Origin", "*" )
                    .build();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( e );
            return Response.status( 500 ).header( "Access-Control-Allow-Origin", "*" ).build();
        }
    }
    
    @GET
    @Path( "/qcm/getQcmNotion/{uid}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getQcmNotion( @PathParam( "uid" ) String uid ) {
        QcmService qcmService = new QcmService();
        try {
            return Response.status( 200 ).entity( qcmService.getQcmNotion( uid ) )
                    .header( "Access-Control-Allow-Origin", "*" )
                    .build();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( e );
            return Response.status( 500 ).build();
        }
    }
    
    @GET
    @Path( "/qcms/{userUid}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getQcms( @PathParam( "userUid" ) String userUid ) {
        QcmService userService = new QcmService();
        try {
            return Response.status( 200 ).entity( userService.getQcms( userUid ) )
                    .header( "Access-Control-Allow-Origin", "*" )
                    .header( "Access-Control-Allow-Headers", "Authorization,Content-Type" )
                    .header( "Access-Control-Allow-Credentials", true ).build();
        } catch ( Exception e ) {
            return Response.status( 500 ).header( "Access-Control-Allow-Origin", "*" )
                    .build();
        }
    }

    @GET
    @Path( "/{uri}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getQcm( @PathParam( "uri" ) String uri ) {
        QcmService userService = new QcmService();
        try {
            return Response.status( 200 ).entity( userService.getQcm( uri ) )
                    .header( "Access-Control-Allow-Origin", "*" )
                    .build();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( e );
            return Response.status( 500 ).build();
        }
    }

    @GET
    @Path( "/qcmtry/{uri}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getQcmTry( @PathParam( "uri" ) String uri ) {
        QcmService userService = new QcmService();
        try {
            return Response.status( 200 ).entity( userService.getQcmTry( uri ) )
                    .header( "Access-Control-Allow-Origin", "*" )
                    .build();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( e );
            return Response.status( 500 ).build();
        }
    }
    

    
    @GET
    @Path( "/qcmtrys/{usrUri}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getQcmTrys( @PathParam( "usrUri" ) String usrUri ) {
        QcmService userService = new QcmService();
        try {
            return Response.status( 200 ).entity( userService.getQcmTrys( usrUri ) )
                    .header( "Access-Control-Allow-Origin", "*" )
                    .build();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( e );
            return Response.status( 500 ).build();
        }
    }
    @POST
    @Path( "/qcmtry" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createQcmTry( String json ) {
        Gson gson = new Gson();
        JsonElement je = gson.fromJson( json, JsonElement.class );
        JsonObject jo = je.getAsJsonObject();
        String qcmUid = jo.get( "qcmUid" ).getAsString();
        String userUid = jo.get( "userUid" ).getAsString();
        QcmService userService = new QcmService();
        try {
            String qcmTryUid = userService.createQcmTry( qcmUid, userUid );
            return Response.status( 200 ).entity( "{\"qcmTryUid\" : \"" + qcmTryUid + "\"}" )
                    .header( "Access-Control-Allow-Origin", "*" )
                    .build();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( e );
            return Response.status( 500 ).header( "Access-Control-Allow-Origin", "*" ).build();
        }
    }

    @PUT
    @Path( "/qcmtry/{uri}" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response updateQcmTry( @PathParam( "uri" ) String uri, String json ) {
        QcmService userService = new QcmService();
        try {
            userService.updateQcmTry( uri, json );
            return Response.status( 200 ).header( "Access-Control-Allow-Origin", "*" )
                    .build();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( e );
            return Response.status( 500 ).header( "Access-Control-Allow-Origin", "*" ).build();
        }
    }

    @OPTIONS
    @Path( "/qcmtry/{uid}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateQcmTry() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST, PUT" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, PUT, OPTIONS" ).build();
    }

    @OPTIONS
    @Path( "/qcm/{qcmUid}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateQcm() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST, PUT" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, PUT, OPTIONS" ).build();
    }

    @OPTIONS
    @Path( "/qcmtry" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createQcmTry() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }

    @OPTIONS
    @Path( "/annot.*" )
    public Response myResource() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }

    @OPTIONS
    @Path( "/qcm" )
    public Response myResource2() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" ).header( "AccessControlAllowCredentials", true )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }
    @OPTIONS
    @Path( "/qcm/save" )
    public Response saveOpt() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" ).header( "AccessControlAllowCredentials", true )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }
    @OPTIONS
    @Path( "/qcm/savenotion/{qcmUid}" )
    public Response saveQcmNotionOpt() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" ).header( "AccessControlAllowCredentials", true )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }
    @OPTIONS
    @Path( "/qcms/{userUid}" )
    public Response myResource23() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" )
                .header( "Access-Control-Allow-Credentials", true )
                .header( "Access-Control-Allow-Headers", "Authorization" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }

    @OPTIONS
    @Path( "/correct/{uid}" )
    public Response myResource234() {

        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST, PUT" )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, PUT, OPTIONS" ).build();
    }

}
