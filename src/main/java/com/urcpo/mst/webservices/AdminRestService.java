package com.urcpo.mst.webservices;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

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

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.xenei.jena.entities.MissingAnnotation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.multipart.MultiPart;
import com.urcpo.mst.beans.User;
import com.urcpo.mst.services.FilesService;
import com.urcpo.mst.services.IdentService;
import com.urcpo.mst.services.QcmService;
import com.urcpo.mst.servlets.LoadOnto;
import com.urcpo.mst.utils.MstUtils;
import com.urcpo.mst.utils.UserEnum;
import com.urcpo.mst.servlets.LoadOnto;

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
