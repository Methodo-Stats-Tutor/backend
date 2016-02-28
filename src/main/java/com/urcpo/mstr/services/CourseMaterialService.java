package com.urcpo.mstr.services;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.impl.EntityManagerImpl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.urcpo.mstr.servlets.ConnectTDB;
import com.urcpo.mstr.utils.MstrUtils;
import com.urcpo.mstr.beans.PubliZone;
import com.urcpo.mstr.beans.Publication;
import com.urcpo.mstr.beans.PublicationAnnot;
import com.urcpo.mstr.beans.Publications;
import com.urcpo.mstr.beans.Tag;
import com.urcpo.mstr.beans.Teacher;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.urcpo.mstr.servlets.LoadOnto;

public class CourseMaterialService {
    private static final Logger log = Logger.getLogger( CourseMaterialService.class );

    public Publications getPublis() {
        Publications publis = null;
        EntityManagerImpl em = (EntityManagerImpl) EntityManagerFactory.getEntityManager();

        ConnectTDB.dataset.begin( ReadWrite.READ );
        try {
            publis = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(), "publications",
                    Publications.class );
            log.info( publis.toJson() );
        } catch ( MissingAnnotation e ) {
            log.error( e.getMessage() );
        } finally {
            ConnectTDB.dataset.end();
        }

        return publis;
    }

    public String getCourseMaterials( String userUid ) throws Exception {
        String sparqlQueryString = "PREFIX mstr:   <http://methodo-stat-tutor.com#>"
                + "SELECT * "
                + " { "
                + " ?cm a mstr:CourseMaterial ."
                + " ?cm a  ?type . "
                + " FILTER(?type != mstr:CourseMaterial) ."//on ne garde que son type
                + " ?cm mstr:title  ?title ."
                + " ?cm mstr:d8Add ?d8add ."
                + " ?cm mstr:fileName ?fileName ."
                + " ?cm mstr:path ?path ."
                + " OPTIONAL{"
                + " ?cm mstr:hasTeacher ?teacher ."
                + " ?teacher mstr:nom ?fname ."
                + " ?teacher mstr:prenom ?lname ."
                + "    }"
                + " OPTIONAL{"
                + " ?cmAnnotEnCours mstr:refersCourseMaterial ?cm ."
                + " ?cmAnnotEnCours mstr:finished false ."
                + " ?cmAnnotEnCours mstr:nom ?cmAnnotEnCoursNom ."
                + " ?cmAnnotEnCours mstr:d8Last ?d8LastEnCours ."
                + " ?cmAnnotEnCours mstr:hasTeacher mstr:" + userUid
                + "    }"
                + " OPTIONAL{"
                + " ?cmAnnotFini mstr:refersCourseMaterial ?cm ."
                + " ?cmAnnotFini mstr:finished true ."
                + " ?cmAnnotFini mstr:nom ?cmAnnotFiniNom ."
                + " ?cmAnnotFini mstr:d8Last ?d8LastFini ."
                + "     }"
                + " } ";
        return ConnectTDB.getSparqlResultAsJson( sparqlQueryString );
    }

    public String getPublisAnnot() throws Exception {
        ResultSet results = null;

        ConnectTDB.dataset.begin( ReadWrite.READ );
        String sparqlQueryString = "PREFIX mstr: <http://methodo-stat-tutor.com#>\n" + 
                "SELECT * {\n" + 
                "                 ?cmAnnot a mstr:PublicationAnnot .\n" + 
                "                 ?cmAnnot mstr:refersCourseMaterial  ?cm .\n" + 
                "?cmAnnot mstr:nom  ?cmAnnotTitle .\n" + 
                "?cm  mstr:path ?path .\n" + 
                "?cm  mstr:title ?cmTitle .\n" + 
                "?cmAnnot mstr:d8Last ?d8Last .\n" + 
                "?cmAnnot mstr:finished ?finished .\n" + 
                "?cm a  ?cmType . FILTER(?cmType != mstr:CourseMaterial) .\n" + 
                "OPTIONAL{\n" + 
                "?cmAnnot mstr:hasTeacher ?teacher.\n" + 
                "?teacher mstr:nom ?lname .\n" + 
                "?teacher mstr:prenom ?fname .\n" + 
                "}\n" + 
                "}";
        Query query = QueryFactory.create( sparqlQueryString );
        QueryExecution qexec = QueryExecutionFactory.create( query, ConnectTDB.dataset );
        try {
            results = qexec.execSelect();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ResultSetFormatter.outputAsJSON( outStream, results );
            return outStream.toString();
        } catch ( Exception e ) {
            throw e;
        } finally {
            qexec.close();
            ConnectTDB.dataset.end();
        }
    }

    public String getPubliAnnot( String id ) throws Exception {
        ConnectTDB.dataset.begin( ReadWrite.READ );
        PublicationAnnot pa = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(), id, PublicationAnnot.class );
        String result = pa.toJson();
        ConnectTDB.dataset.end();
        return result;
    }

    public PublicationAnnot createPubliAnnot( String courseMaterialAnnot, String userUid, String courseMaterialUid )
            throws Exception {
        PublicationAnnot pa = null;
        Gson gson = new Gson();
        try {

            JsonElement je = gson.fromJson( courseMaterialAnnot, JsonElement.class );
            JsonObject jo = je.getAsJsonObject();
            String nom = jo.get( "nom" ).getAsString();

            String annotoriousJson = jo.get( "annotoriousJson" ).getAsJsonArray().toString();

            String annotatorJson = jo.get( "annotatorJson" ).getAsJsonArray().toString();
            String courseMaterialAnnotJson = jo.get( "courseMaterialAnnotJson" ).getAsJsonObject().toString();
            JsonObject publiZone = jo.get( "publiZone" ).getAsJsonObject();
            Boolean finished = jo.get( "finished" ).getAsBoolean();

            ConnectTDB.dataset.begin( ReadWrite.WRITE );

            pa = createPubliAnnot(MstrUtils.uid(), ConnectTDB.dataset.getDefaultModel(), nom, annotoriousJson, annotatorJson,
                    publiZone );

            Publication publi = ConnectTDB.read( ConnectTDB.dataset.getDefaultModel(), courseMaterialUid,
                    Publication.class );

            publi.addHasPublicationAnnot( pa );
            pa.setRefersCourseMaterial( publi );

            Teacher usr = ConnectTDB.read( ConnectTDB.dataset.getDefaultModel(), userUid,
                    Teacher.class );
            usr.addHasPubliAnnot( pa );
            pa.setHasTeacher( usr );
            pa.setFinished( finished );
            pa.setD8Add(MstrUtils.now() );
            pa.setD8Last(MstrUtils.now() );
            pa.setJson(courseMaterialAnnotJson);
            MstrUtils.validatePojo( pa );
            ConnectTDB.dataset.commit();
            // FIN AJOUT PUBLI
            return pa;
        } catch ( Exception e )
        {
            throw new Exception( e );
        } finally {
            ConnectTDB.dataset.end();
        }
    }

    protected PublicationAnnot createPubliAnnot(String uidCm, Model model, String nom,
            String annotoriousJson, String annotatorJson, JsonObject publiZone ) throws Exception {
        PublicationAnnot pa = null;
        try {
            pa = ConnectTDB.readWrite( model, uidCm, PublicationAnnot.class );
            pa.setNom( nom );
            pa.setAnnotoriousJson( annotoriousJson );
            pa.setAnnotatorJson( annotatorJson );
            String uid;
         

            for ( Map.Entry<String, JsonElement> entry : publiZone.entrySet() ) {
                uid = MstrUtils.uid();
                PubliZone pz = ConnectTDB.readWrite( model, uid, PubliZone.class );
                pz.setColor( entry.getValue().getAsJsonObject().get( "color" ).getAsString() );
                pz.setId( entry.getValue().getAsJsonObject().get( "id" ).getAsString() );
                pz.setLabel( entry.getValue().getAsJsonObject().get( "label" ).getAsString() );
                      
             // Les tags
                for ( Map.Entry<String, JsonElement> tags : entry.getValue().getAsJsonObject().get( "tags" ).getAsJsonObject().entrySet() ) {
                    log.error( "OK" + tags.toString() );
                    uid = MstrUtils.uid();
                    Tag tag = ConnectTDB.readWrite( model, uid, Tag.class );
                    String uidUriTag = MstrUtils.uid();
                    tag.setUri( java.net.URI.create(tags.getKey() ));
              // add type...
      //        Resource instance1 =   model.getResource( "http://methodo-stat-tutor.com#" + uidUriTag );
       //       instance1.addProperty(RDF.type, tags.getKey().toString());
              // fin add type
               //     tag.setAccuracy( tags.getValue().getAsJsonObject().get("accuracy").getAsInt() );
                    pz.addHasTag(tag);
                }
                // ajout de cette zone à la publi
                pa.addHasPubliZone( pz );
                pz.setRefersPubliAnnot( pa );
            }
            
        } catch ( Exception e ) {
            throw new Exception( MstrUtils.formatLog( e ) );

        } finally {
        }
        return pa;
    }

    public PublicationAnnot updateCourseMaterialAnnot( String courseMaterialAnnot, String courseMaterialAnnotUid,
            String userUid ) throws Exception {
        PublicationAnnot pa = null;
        Gson gson = new Gson();
        try {

            JsonElement je = gson.fromJson( courseMaterialAnnot, JsonElement.class );
            JsonObject jo = je.getAsJsonObject();
            String nom = jo.get( "nom" ).getAsString();

            String annotoriousJson = jo.get( "annotoriousJson" ).getAsJsonArray().toString();
            String annotatorJson = jo.get( "annotatorJson" ).getAsJsonArray().toString();
            String courseMaterialAnnotJson = jo.get( "courseMaterialAnnotJson" ).getAsJsonObject().toString();
            Boolean finished = jo.get( "finished" ).getAsBoolean();
            JsonObject publiZone = jo.get( "publiZone" ).getAsJsonObject();


            ConnectTDB.dataset.begin( ReadWrite.WRITE );
            PublicationAnnot cma = ConnectTDB.read( ConnectTDB.dataset.getDefaultModel(), courseMaterialAnnotUid,
                    PublicationAnnot.class );
             for(PubliZone pz :cma.getHasPubliZone()){
             cma.removeHasPubliZone( pz );
             }
             cma = createPubliAnnot(courseMaterialAnnotUid,
             ConnectTDB.dataset.getDefaultModel(), nom,
             annotoriousJson, annotatorJson, publiZone );
            cma.setNom( nom );
            cma.setAnnotatorJson( annotatorJson );
            cma.setAnnotoriousJson( annotoriousJson );
            cma.setFinished( finished );
            cma.setJson(courseMaterialAnnotJson);
            cma.setD8Last(MstrUtils.now() );
            MstrUtils.validatePojo( cma );
            ConnectTDB.dataset.commit();
            return cma;
        } catch ( Exception e ) {
            log.error( e );
            throw new Exception( e );
        } finally {
            ConnectTDB.dataset.end();

        }
    }
}
