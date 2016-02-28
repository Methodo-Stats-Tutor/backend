package com.urcpo.mst.webservices;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.urcpo.mst.beans.*;
import com.urcpo.mst.services.CourseMaterialService;
import com.urcpo.mst.utils.MstUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/courseMaterials")
public class CourseMaterialRestService {

    private static final Logger log = Logger.getLogger(CourseMaterialRestService.class);

    @GET
    @Path("/publis")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublis() {
        CourseMaterialService userService = new CourseMaterialService();
        return Response.status(200).entity(userService.getPublis().toJson())
                .header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/{userUid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseMaterials(@PathParam("userUid") String userUid) throws Exception {
        CourseMaterialService ps = new CourseMaterialService();
        return Response.status(200).entity(ps.getCourseMaterials(userUid))
                .header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    @Path("/annot")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCourseMaterialAnnot(String json) {
        Gson gson = new Gson();
        JsonElement je = gson.fromJson(json, JsonElement.class);
        JsonObject jo = je.getAsJsonObject();
        String courseMaterialAnnot = jo.get("courseMaterialAnnot").getAsJsonObject().toString();
        String userUid = jo.get("userUid").getAsString();
        String courseMaterialUid = jo.get("courseMaterialUid").getAsString();

        try {
            CourseMaterialService pas = new CourseMaterialService();
            PublicationAnnot test = pas.createPubliAnnot(courseMaterialAnnot, userUid, courseMaterialUid);
            return Response.status(200).entity("{\"cmActualUid\":\"" + test.getUid() + "\"}").header("Access-Control-Allow-Origin", "*").build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e);
            return Response.status(500).entity(e.toString()).build();
        }
    }

    @PUT
    @Path("/annot/{courseMaterialAnnotUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourseMaterialAnnot(@PathParam("courseMaterialAnnotUid") String courseMaterialAnnotUid, String json) {
        Gson gson = new Gson();
        JsonElement je = gson.fromJson(json, JsonElement.class);
        JsonObject jo = je.getAsJsonObject();
        String courseMaterialAnnot = jo.get("courseMaterialAnnot").getAsJsonObject().toString();
        String userUid = jo.get("userUid").getAsString();
        try {
            CourseMaterialService pas = new CourseMaterialService();
            PublicationAnnot test = pas.updateCourseMaterialAnnot(courseMaterialAnnot, courseMaterialAnnotUid, userUid);
            return Response.status(200).entity("{\"cmActualUid\":\"" + test.getUid() + "\"}").header("Access-Control-Allow-Origin", "*").build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e);
            return Response.status(500).entity(e.toString()).build();
        }
    }

    @GET
    @Path("/publis/annot")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublisAnnot() {
        CourseMaterialService userService = new CourseMaterialService();
        try {
            return Response.status(200).entity(userService.getPublisAnnot()).header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception e) {
            return Response.status(500).header("Access-Control-Allow-Origin", "*")
                    .build();
        }
    }

    @GET
    @Path("/publis/annot/{uri}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPubliAnnot(@PathParam("uri") String uri) throws Exception {
        CourseMaterialService userService = new CourseMaterialService();
        String json = userService.getPubliAnnot(uri);
        try {
            return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e);
            return Response.status(500).build();
        }
    }

    @OPTIONS
    @Path("/publis/annot.*")
    public Response myResource() {
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT")
                .header("Access-Control-Allow-Headers", "Content-Type").header("Access-Control-Max-Age", "86400")
                .header("Allow", "GET, HEAD, POST, TRACE, OPTIONS").build();
    }

    @OPTIONS
    @Path("/{userUid}")
    public Response myResourceeaze() {
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT")
                .header("Access-Control-Allow-Headers", "Content-Type").header("Access-Control-Max-Age", "86400")
                .header("Allow", "GET, HEAD, POST, TRACE, OPTIONS").build();
    }

    @OPTIONS
    @Path("/annot")
    public Response myResourceeaze2() {
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT")
                .header("Access-Control-Allow-Headers", "Content-Type").header("Access-Control-Max-Age", "86400")
                .header("Allow", "GET, HEAD, POST, TRACE, OPTIONS").build();
    }

    @OPTIONS
    @Path("/annot/{courseMaterialAnnotUid}")
    public Response myResourc() {
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT")
                .header("Access-Control-Allow-Headers", "Content-Type").header("Access-Control-Max-Age", "86400")
                .header("Allow", "GET, HEAD, POST, TRACE, OPTIONS").build();
    }
//    @OPTIONS
//    @Path(  "/{subResources:.*}" )
//    public Response myResource2() {
//        return Response.ok().header( "Access-Control-Allow-Origin", "*" )
//                .header( "Access-Control-Allow-Methods", "GET, POST, PUT" )
//                .header( "Access-Control-Allow-Headers", "Content-Type" ).header( "Access-Control-Max-Age", "86400" )
//                .header( "Allow", "GET, HEAD, POST, TRACE, OPTIONS" ).build();
//    }

}
