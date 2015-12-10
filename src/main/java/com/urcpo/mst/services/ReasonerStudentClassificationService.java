package com.urcpo.mst.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.impl.EntityManagerImpl;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.urcpo.mst.servlets.ConnectTDB;
import com.urcpo.mst.utils.MstUtils;
import com.urcpo.mst.webservices.CourseMaterialRestService;
import com.urcpo.mst.beans.PubliZone;
import com.urcpo.mst.beans.Publication;
import com.urcpo.mst.beans.PublicationAnnot;
import com.urcpo.mst.beans.Publications;
import com.urcpo.mst.beans.Tag;
import com.urcpo.mst.beans.Teacher;
import com.urcpo.mst.beans.User;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class ReasonerStudentClassificationService extends ReasonerService {
    private static final Logger log = Logger.getLogger( ReasonerStudentClassificationService.class );
    private String              student;

    public ReasonerStudentClassificationService( String student ) {
        this.student = student;

    }

    @Override
    public void createSubModel() {
        ConnectTDB.dataset.begin( ReadWrite.READ );
        String queryBegin =
                "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                        "PREFIX mst: <http://methodo-stats-tutor.com#>\r\n" +
                        "PREFIX wine: <http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#>\r\n"
                        +
                        "\r\n" +
                        "CONSTRUCT { " +
                         "?some ?pre ?obj." +
                         "?some mst:hasQcmTry ?qcmt ." +
                         "?qcmt mst:mark ?m ." +
                         "?qcmt mst:refersQcm ?qcm ." +
                         "?qcmt a mst:QcmTry ." +
                         "?qcm a mst:Qcm ." +
                         "} " +
                        "WHERE {";
        String queryEnd = "}";

        String queryStudent =
                queryBegin +
                        "?some a mst:Student .\n" + 
                        "?some ?pre ?obj .\n" + 
                        "?some mst:hasQcmTry ?qcmt .\n" + 
                        "?qcmt mst:mark ?m .\n" + 
                        "?qcm a mst:Qcm .\n" + 
                        "?qcmt mst:refersQcm ?qcm .\n" + 
                        "?qcmt a mst:QcmTry .\n" + 
                        "\n" + 
                        "{\n" + 
                        "SELECT ?qcm (max(?m) as ?max)\n" + 
                        "WHERE {\n" + 
                        "?qcmt mst:mark ?m . \n" + 
                        "}\n" + 
                        "GROUP BY ?qcm\n" + 
                        "}\n" + 
                        "FILTER ( ?some = mst:admin) \n" + 
                        "FILTER ( ?m = ?max) \n" + 
                        queryEnd;
        
log.error( queryStudent );
        QueryExecution q1 = QueryExecutionFactory.create( queryStudent, ConnectTDB.dataset );
        Model modelStudent = q1.execConstruct();

        mergeModelsInSubModel( modelStudent );
        ConnectTDB.dataset.end();
    }

    @Override
    public void reasonOverSubModel() {
       
        ontModel = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC, subModel );
       OutputStream outputStream;
    try {
        outputStream = new FileOutputStream( "/tmp/testOnt.xml" );
        ontModel.write( outputStream, "RDF/XML" );
    } catch ( FileNotFoundException e ) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
      
    }

    @Override
    public ResultSet GetResults() {
        createSubModel();
        reasonOverSubModel();
        String queryBegin =
                "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX mst: <http://methodo-stats-tutor.com#>" +
                        "PREFIX wine: <http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#>" +
                        "SELECT ?student ?pred ?qcm " +
                        "WHERE {";
        String queryEnd = "}";

        // create a query that asks for the color of the wine that
        // would go with each meal course
      
        String queryStr2 =
                queryBegin +
                        " ?student mst:hasFinished ?qcm ." +
                        " ?student ?pred ?qcm ." +
                        // " <http://methodo-stats-tutor.com#id02093377-59ba-4629-a0ed-08ad7115afeb> ?pre ?obj   .\r\n"
                        // +
                        queryEnd;
        log.error( queryStr2 );
        Query query2 = QueryFactory.create( queryStr2 );
        ResultSet results = SparqlDLExecutionFactory.create( query2, ontModel ).execSelect();
        logQueryResult( results );
        return results;
    }

}
