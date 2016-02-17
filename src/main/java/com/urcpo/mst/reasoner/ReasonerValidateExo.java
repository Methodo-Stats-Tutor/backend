package com.urcpo.mst.reasoner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.urcpo.mst.servlets.ConnectTDB;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

//Principe:
//1) récupérer les notions que l'étudiant ne maitrise pas 
//2) Récupérer les exercices qui ne needNotion pas l'une de ces notions
public class ReasonerValidateExo extends ReasonerService {

    private static final Logger log = Logger.getLogger(ReasonerQcmMaitriseNotion.class);
    private String student;
    private String exotry;
    private ArrayList<java.net.URI> needNotion;
    private ArrayList<java.net.URI> giveNotion;

    public ReasonerValidateExo(String student, String exotry) {
        this.student = student;
        this.exotry = exotry;
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
                + "FILTER (?QCMTRY = mst:" + this.exotry + ")\n"
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

//notions non maitrisées = toutes les notions - les notions maitrisées 
    public Boolean isExoTryValidated() {
        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX mst: <http://methodo-stats-tutor.com#>"
                + "SELECT ?QCMTRY "
                + "WHERE {";
        String queryEnd = "}";


        String queryStr2
                = queryBegin
                + "?STU mst:hasValidateExoTry ?QCMTRY \n"
                + "FILTER (?QCMTRY = mst:" + this.exotry + ")\n"
                + "FILTER (?STU = mst:" + this.student + ")\n"
                + queryEnd;
        log.error(queryStr2);
        Query query2 = QueryFactory.create(queryStr2);
        ResultSet results = SparqlDLExecutionFactory.create(query2, ontModel).execSelect();
       //  log.error(ResultSetFormatter.asText(results));
    // results.next().get("?QCM").asLiteral().getString();
        return results.hasNext();
    }


}
//liste des exo à faire = total exo - exo pas a faire
// exo pas à faire = exo dont une notion n'est pas maitrisée

