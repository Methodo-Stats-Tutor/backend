package com.urcpo.mst.reasoner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.urcpo.mst.beans.Student;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Level;
import org.xenei.jena.entities.MissingAnnotation;

//déduit les notions maitrisées par un étudiant, et les ajoute aux notions maitrisées.
public class ReasonerStudentMasterNotion extends ReasonerService {

    private static final Logger log = Logger.getLogger(ReasonerStudentMasterNotion.class);
    private String student;
    private String exo;
    private ArrayList<java.net.URI> notionMaster;

    public ReasonerStudentMasterNotion(String student, String exo) {
        this.student = student;
        this.exo = exo;
        createSubModel();
        reasonOverSubModel();
        //   processResults();
        findNotionMaster();
    }

    @Override
    public void createSubModel() {
        ConnectTDB.dataset.begin(ReadWrite.READ);
        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX mst: <http://methodo-stats-tutor.com#>\n"
                + "\n"
                + "CONSTRUCT { "
                + "?some a mst:Student .\n"
                + "?qcm a mst:Exercice .\n"
                + "?qcm mst:giveNotion ?notion .\n"
                + "?some mst:hasValidateExo ?qcm .\n"
                + "} "
                + "WHERE {";
        String queryEnd = "}";

        String queryStudent
                = queryBegin
                + "?some a mst:Student .\n"
                + "?qcm a mst:Exercice .\n"
                + "?qcm mst:giveNotion ?notion .\n"
                + "?some mst:hasValidateExo ?qcm .\n"
                + "FILTER ( ?some = mst:" + this.student + ") \n"
                + "FILTER ( ?qcm = mst:" + this.exo + ") \n"
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

    }

    public void findNotionMaster() {
        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX mst: <http://methodo-stats-tutor.com#>"
                + "SELECT ?NOTION "
                + "WHERE {";
        String queryEnd = "}";

        String queryStr2
                = queryBegin
                + " ?student mst:masterNotion ?NOTION ."
                //  + " ?student ?pred ?qcm ."
                + // " <http://methodo-stats-tutor.com#id02093377-59ba-4629-a0ed-08ad7115afeb> ?pre ?obj   .\r\n"
                // +
                queryEnd;
        log.error(queryStr2);
        Query query2 = QueryFactory.create(queryStr2);
        ResultSet results = SparqlDLExecutionFactory.create(query2, ontModel).execSelect();
        //  logQueryResult(results);
        //pour chaque notions, ajouter à l'array
        this.notionMaster = new ArrayList<java.net.URI>();
        while (results.hasNext()) {
            RDFNode a = results.next().get("?NOTION");
            this.notionMaster.add(java.net.URI.create(a.asLiteral().getString()));
        }
    }
//ajoute à un étudiant donné les notions maitrisées
    public String setNotionMaster() {
        ConnectTDB.dataset.begin(ReadWrite.WRITE);
        try {
            Student s = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), student, Student.class);
            Iterator<URI> itr = notionMaster.iterator();
            while (itr.hasNext()) {
                s.addMasterNotion(itr.next());
            }
            ConnectTDB.dataset.commit();
        } catch (MissingAnnotation ex) {
            log.error(ex);
        } finally {
            ConnectTDB.dataset.end();
        }
        return notionMaster.toString();
    }

}
