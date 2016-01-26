package com.urcpo.mst.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
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
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;

//Principe:
//1) récupérer les notions que l'étudiant ne maitrise pas 
//2) Récupérer les exercices qui ne needNotion pas l'une de ces notions
public class ReasonerExerciceToDo extends ReasonerService {

    private static final Logger log = Logger.getLogger(ReasonerQcmMaitriseNotion.class);
    private String student;
    private ArrayList<java.net.URI> needNotion;
    private ArrayList<java.net.URI> giveNotion;

    public ReasonerExerciceToDo(String student) {
        this.student = student;
        createSubModel();
        reasonOverSubModel();
        //   processResults();
    }

    @Override
    public void createSubModel() {
        ConnectTDB.dataset.begin(ReadWrite.WRITE);
        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX mst: <http://methodo-stats-tutor.com#>\n"
                + "CONSTRUCT { "
                + "?STUD a mst:Student .\n"
                + "?QCM a mst:Qcm .\n"
                + "?QCMTRY a mst:QcmTry .\n"
                + "?QCMTRY mst:refersQcm ?QCM .\n"
                + "?STU mst:hasQcmTry ?QCMTRY .\n"
                + "?QCMTRY mst:mark ?M .\n"
                + "?QCM mst:giveNotion ?N.\n"
                + "} "
                + "WHERE {";
        String queryEnd = "}";

        String queryStudent
                = queryBegin
                //hasValidateExo Begin
                + "?STUD a mst:Student .\n"
                + "?QCM a mst:Qcm .\n"
                + "?QCMTRY a mst:QcmTry .\n"
                + "?QCMTRY mst:refersQcm ?QCM .\n"
                + "?STU mst:hasQcmTry ?QCMTRY .\n"
                + "?QCMTRY mst:mark ?M .\n"
                //hasValidateExo End
                //maitriseNotion beg
                + "?QCM mst:giveNotion ?N .\n"
                //maitriseNotion end
                + "FILTER (?STUD = mst:" + this.student + ")\n"
                + queryEnd;
        QueryExecution q1 = QueryExecutionFactory.create(queryStudent, ConnectTDB.dataset);
        Model modelStudent = q1.execConstruct();

        mergeModelsInSubModel(modelStudent);
        ConnectTDB.dataset.end();
    }

    @Override
    public void reasonOverSubModel() {

        ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, subModel);
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream("/tmp/testOnt.xml");
            ontModel.write(outputStream, "RDF/XML");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void processResults() {

    }

    public ArrayList<java.net.URI> getNotMasteredNotion() {
        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX mst: <http://methodo-stats-tutor.com#>"
                + "SELECT ?N "
                + "WHERE {";
        String queryEnd = "}";

        // create a query that asks for the color of the wine that
        // would go with each meal course
        String queryStr2
                = queryBegin
                + "{ ?STU mst:dontMasterNotion ?N }\n"
                + " MINUS \n"
                + "{ ?STU mst:masterNotion ?N }\n"
                + "FILTER (?STU = mst:" + this.student + ")\n"
                + queryEnd;
        log.error(queryStr2);
        Query query2 = QueryFactory.create(queryStr2);
        ResultSet results = SparqlDLExecutionFactory.create(query2, ontModel).execSelect();
        // log.error(ResultSetFormatter.asText(results));
        ArrayList<java.net.URI> aru = new ArrayList<java.net.URI>();
        while (results.hasNext()) {
            aru.add(java.net.URI.create(results.next().get("?N").asLiteral().getString()));
        }
        return aru;
    }

    public String getExerciseToDo(ArrayList<java.net.URI> notMasteredNotion) {
        String filter = "";
        if (notMasteredNotion.size() > 0) {
            filter = "FILTER (?NOT  NOT IN  (" + listToString(notMasteredNotion) + "))\n";
        }

        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX mst: <http://methodo-stats-tutor.com#>\n"
                + "SELECT *\n"
                + "WHERE {\n";
        String queryEnd = "}";

        // create a query that asks for the color of the wine that
        // would go with each meal course
        String queryStr2
                = queryBegin
                +"{\n"
                + "{?qcm a mst:Qcm .\n"
                + "}\n"
                + "MINUS\n"
                + "{\n"
                + "SELECT ?qcm\n"
                + "WHERE {\n"
                + "?qcm a mst:Qcm .\n"
                + "?qcm mst:needNotion ?N .\n"
                + "BIND(IRI(str(?N)) as ?NOT)\n"
                + filter
                + "}\n"
                + "}\n"
                + "                ?qcm a mst:Qcm .\n"
                + "                 ?qcm mst:name  ?name .\n"
                + "                 ?qcm mst:d8Add ?d8add .\n"
                + "                 OPTIONAL{?qcm mst:teacher ?teacher .\n"
                + "                 ?teacher mst:nom ?fname .\n"
                + "                 ?teacher mst:prenom ?lname }.\n"
                + "values ?block {'true'}.\n"
                + "                 ?qcm mst:difficulty ?difficulty .\n"
                + "                  OPTIONAL{?qcmtry mst:refersQcm ?qcm .\n"
                + "                  ?qcmtry mst:finished false .\n"
                + "                ?qcmtry mst:json ?json .\n"
                + "                 ?qcmtry mst:user mst:" + this.student + " }\n"
                +"}\n"
                +"UNION {\n"
                                + "{\n"
                + "SELECT ?qcm\n"
                + "WHERE {\n"
                + "?qcm a mst:Qcm .\n"
                + "?qcm mst:needNotion ?N .\n"
                + "BIND(IRI(str(?N)) as ?NOT)\n"
                + filter
                + "} }\n"
                + "                ?qcm a mst:Qcm .\n"
                + "                 ?qcm mst:name  ?name .\n"
                + "                 ?qcm mst:d8Add ?d8add .\n"
                + "                 OPTIONAL{?qcm mst:teacher ?teacher .\n"
                + "                 ?teacher mst:nom ?fname .\n"
                + "                 ?teacher mst:prenom ?lname }.\n"
                + "values ?block {'false'}.\n"
                + "                 ?qcm mst:difficulty ?difficulty .\n}"
                + queryEnd;
        //s log.error(queryStr2);
        //     Query query2 = QueryFactory.create(queryStr2);
        return ConnectTDB.getSparqlResultAsJson(queryStr2);
//        ResultSet results = SparqlDLExecutionFactory.create(query2, ontModel).execSelect();
//         log.error(ResultSetFormatter.asText(results));
//        ArrayList<java.net.URI> aru = new ArrayList<java.net.URI>();
//        while (results.hasNext()) {
//            aru.add(java.net.URI.create(results.next().get("?EXO").getString()));
//        }
//        return aru;
//    }
    }

    private String listToString(ArrayList<java.net.URI> notMasteredNotion) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < notMasteredNotion.size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("<");
            sb.append(notMasteredNotion.get(i).toString());
            sb.append(">");
        }
        return sb.toString();
    }

    /**
     * @return the needNotion
     */
    public ArrayList<java.net.URI> getNeedNotion() {
        return needNotion;
    }

    /**
     * @return the giveNotion
     */
    public ArrayList<java.net.URI> getGiveNotion() {
        return giveNotion;
    }

}
//liste des exo à faire = total exo - exo pas a faire
// exo pas à faire = exo dont une notion n'est pas maitrisée

