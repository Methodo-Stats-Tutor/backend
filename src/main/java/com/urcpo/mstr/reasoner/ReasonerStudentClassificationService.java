package com.urcpo.mstr.reasoner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ReasonerStudentClassificationService extends ReasonerService {

    private static final Logger log = Logger.getLogger(ReasonerStudentClassificationService.class);
    private String student;

    public ReasonerStudentClassificationService(String student) {
        this.student = student;
        createSubModel();
        reasonOverSubModel();
        processResults();
    }

    @Override
    public void createSubModel() {
        ConnectTDB.dataset.begin(ReadWrite.READ);
        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX mstr: <http://methodo-stat-tutor.com#>\n"
                + "\n"
                + "CONSTRUCT { "
                + "?some ?pre ?obj."
                + "?some mstr:hasQcmTry ?qcmt ."
                + "?qcmt mstr:mark ?m ."
                + "?qcmt mstr:refersQcm ?qcm ."
                + "?qcmt a mstr:QcmTry ."
                + "?qcm a mstr:Qcm ."
                + "} "
                + "WHERE {";
        String queryEnd = "}";

        String queryStudent
                = queryBegin
                + "?some a mstr:Student .\n"
                + "?some ?pre ?obj .\n"
                + "?some mstr:hasQcmTry ?qcmt .\n"
                + "?qcmt mstr:mark ?m .\n"
                + "?qcm a mstr:Qcm .\n"
                + "?qcmt mstr:refersQcm ?qcm .\n"
                + "?qcmt a mstr:QcmTry .\n"
                + "\n"
                + "{\n"
                + "SELECT ?qcm (max(?m) as ?max)\n"
                + "WHERE {\n"
                + "?qcmt mstr:mark ?m . \n"
                + "}\n"
                + "GROUP BY ?qcm\n"
                + "}\n"
                + "FILTER ( ?some = mstr:admin) \n"
                + "FILTER ( ?m = ?max) \n"
                + queryEnd;

        log.error(queryStudent);
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

        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX mstr: <http://methodo-stat-tutor.com#>"
                + "SELECT ?student ?pred ?qcm "
                + "WHERE {";
        String queryEnd = "}";

        String queryStr2
                = queryBegin
                + " ?student mstr:hasFinished ?qcm ."
                + " ?student ?pred ?qcm ."
                + // " <http://methodo-stat-tutor.com#id02093377-59ba-4629-a0ed-08ad7115afeb> ?pre ?obj   .\r\n"
                // +
                queryEnd;
        log.error(queryStr2);
        Query query2 = QueryFactory.create(queryStr2);
        ResultSet results = SparqlDLExecutionFactory.create(query2, ontModel).execSelect();
        logQueryResult(results);

    }

}
