package com.urcpo.mstr.webservices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import com.urcpo.mstr.services.FilesService;
import com.urcpo.mstr.utils.MstrUtils;


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
            String storageDirectory = MstrUtils.REP_FILES  + MstrUtils.REP_SAVE_PUBLI;
         
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
            String storageDirectory = MstrUtils.REP_FILES + MstrUtils.REP_SAVE_PUBLI;
         
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
