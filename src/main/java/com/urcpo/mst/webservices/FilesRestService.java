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
import com.urcpo.mst.utils.MstUtils;
import com.urcpo.mst.utils.UserEnum;


@Path( "/files" )
public class FilesRestService {
    private static final Logger log           = Logger.getLogger( FilesRestService.class );

    @POST
    @Path( "/upload/publi/" )
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    @Produces( MediaType.APPLICATION_JSON )
    public Response uploadImage( @FormDataParam( "pmid" ) String pmid,
            @FormDataParam( "journal" ) String journal,
            @FormDataParam( "title" ) String title,
            @FormDataParam( "author" ) String author,
            @FormDataParam( "abstract" ) String abstracte,
            @FormDataParam( "userUid" ) String userUid,
            FormDataMultiPart form ) {

        try {
            FormDataBodyPart filePart = form.getField( "file" );  
            ContentDisposition fileDetail = filePart.getContentDisposition();
            InputStream uploadedInputStream = filePart.getValueAs( InputStream.class );
            Long fileLength = fileDetail.getSize();
            String originalFileName = fileDetail.getFileName();
            String originalFileExtension = originalFileName.substring( originalFileName.lastIndexOf( "." ) );
        
            // transfer to upload folder
            String storageDirectory = MstUtils.REP_FILES  + MstUtils.REP_SAVE_PUBLI;
         
            File newFilePath = new File( storageDirectory + "/" + originalFileName );
      
            saveFile( uploadedInputStream, newFilePath.toPath().toString() );
            FilesService fs = new FilesService();
      

            return Response
                    .status( 200 )
                    .entity(
                            fs.createPublication( originalFileName, storageDirectory, pmid, journal, title, author,
                                    abstracte, userUid ) )
                    .header( "Access-Control-Allow-Headers", "Content-Type" )
                    .header( "Access-Control-Allow-Origin", "*" ).build();
        } catch ( Exception e ) {
            log.error( e.getMessage() );
            return Response.status( HttpStatus.SC_BAD_REQUEST ).entity( e.getMessage() )
                    .header( "Access-Control-Allow-Origin", "*" ).build();
        }

    }
    @POST
    @Path( "/upload/picture/" )
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    @Produces( MediaType.APPLICATION_JSON )
    public Response uploadImage( 
            @FormDataParam( "title" ) String title,
            @FormDataParam( "description" ) String description,
            @FormDataParam( "userUid" ) String userUid,
            FormDataMultiPart form ) {
    
        try {
            FormDataBodyPart filePart = form.getField( "file" );  
            ContentDisposition fileDetail = filePart.getContentDisposition();
            InputStream uploadedInputStream = filePart.getValueAs( InputStream.class );
            Long fileLength = fileDetail.getSize();
            String originalFileName = fileDetail.getFileName();
            String originalFileExtension = originalFileName.substring( originalFileName.lastIndexOf( "." ) );
        
            // transfer to upload folder
            String storageDirectory = MstUtils.REP_FILES + MstUtils.REP_SAVE_PUBLI;
         
            File newFilePath = new File( storageDirectory + "/" + originalFileName );
      
            saveFile( uploadedInputStream, newFilePath.toPath().toString() );
            FilesService fs = new FilesService();
      

            return Response
                    .status( 200 )
                    .entity(
                            fs.createPicture( originalFileName, storageDirectory, title, description,  userUid ) )
                    .header( "Access-Control-Allow-Headers", "Content-Type" )
                    .header( "Access-Control-Allow-Origin", "*" ).build();
        } catch ( Exception e ) {
            log.error( e.getMessage() );
            return Response.status( HttpStatus.SC_BAD_REQUEST ).entity( e.getMessage() )
                    .header( "Access-Control-Allow-Origin", "*" ).build();
        }

    }

    // save uploaded file to a defined location on the server
    private void saveFile( InputStream uploadedInputStream, String serverLocation ) throws IOException {

        try {
            OutputStream outpuStream = new FileOutputStream( new File(
                    serverLocation ) );
            int read = 0;
            byte[] bytes = new byte[1024];

            outpuStream = new FileOutputStream( new File( serverLocation ) );
            while ( ( read = uploadedInputStream.read( bytes ) ) != -1 ) {
                outpuStream.write( bytes, 0, read );
            }

            outpuStream.flush();
            outpuStream.close();

            uploadedInputStream.close();
        } catch ( IOException e ) {
            throw e;
        }

    }

    @OPTIONS
    @Path( "/upload/publi/" )
    public Response myResource2() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" ).header( "AccessControlAllowCredentials", true )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }
    @OPTIONS
    @Path( "/upload/picture/" )
    public Response myResource23() {
        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
                .header( "Access-Control-Allow-Methods", "GET, POST" ).header( "AccessControlAllowCredentials", true )
                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
    }

}
