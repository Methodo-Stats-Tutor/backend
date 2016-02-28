package com.urcpo.mstr.reasoner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.urcpo.mstr.servlets.ConnectTDB;
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
                + "PREFIX mstr: <http://methodo-stat-tutor.com#>\n"
                + "CONSTRUCT { "
                + "?STUD a mstr:Student .\n"
                + "?QCM a mstr:Qcm .\n"
                + "?QCMTRY a mstr:QcmTry .\n"
                + "?QCMTRY mstr:refersQcm ?QCM .\n"
                + "?STU mstr:hasQcmTry ?QCMTRY .\n"
                + "?QCMTRY mstr:mark ?M .\n"
                + "?QCM mstr:giveNotion ?N.\n"
                + "} "
                + "WHERE {";
        String queryEnd = "}";

        String queryStudent
                = queryBegin
                //hasValidateExo Begin
                + "?STUD a mstr:Student .\n"
                + "?QCM a mstr:Qcm .\n"
                + "?QCMTRY a mstr:QcmTry .\n"
                + "?QCMTRY mstr:refersQcm ?QCM .\n"
                + "?STU mstr:hasQcmTry ?QCMTRY .\n"
                + "?QCMTRY mstr:mark ?M .\n"
                //hasValidateExo End
                //maitriseNotion beg
                + "?QCM mstr:giveNotion ?N .\n"
                //maitriseNotion end
                + "FILTER (?STUD = mstr:" + this.student + ")\n"
                + "FILTER (?QCMTRY = mstr:" + this.exotry + ")\n"
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
                + "PREFIX mstr: <http://methodo-stat-tutor.com#>"
                + "SELECT ?QCMTRY "
                + "WHERE {";
        String queryEnd = "}";


        String queryStr2
                = queryBegin
                + "?STU mstr:hasValidateExoTry ?QCMTRY \n"
                + "FILTER (?QCMTRY = mstr:" + this.exotry + ")\n"
                + "FILTER (?STU = mstr:" + this.student + ")\n"
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

