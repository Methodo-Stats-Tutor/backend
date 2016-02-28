package com.urcpo.mstr.webservices;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import com.urcpo.mstr.servlets.LoadOnto;

@Path( "/admin" )
public class AdminRestService {
    private static final Logger log           = Logger.getLogger(AdminRestService.class );

    
       @GET
    @Path( "/refreshonto" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response refreshOnto() {
          LoadOnto.mergeOntos();
        return Response.status( 200 ).entity( "" )
                .header( "Access-Control-Allow-Origin", "*" ).build();
    }


    @OPTIONS
    @Path( "/refreshonto/" )
    public Response refreshonto() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" ).header( "AccessControlAllowCredentials", true )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }


}
