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
                + "PREFIX mstr: <http://methodo-stat-tutor.com#>\n"
                + "CONSTRUCT { "
                + "?STUD a mstr:Student .\n"
                + "?QCM a mstr:Qcm .\n"
                + "?STUD mstr:masterNotion ?NOT.\n"
                + "?QCM mstr:giveNotion ?N.\n"
                + "?QCM mstr:needNotion ?N1 .\n"
                + "} "
                + "WHERE {";
        String queryEnd = "}";

        String queryStudent
                = queryBegin
                //hasValidateExo Begin
                + "?STUD a mstr:Student .\n"
                + "?QCM a mstr:Qcm .\n"
                + "OPTIONAL {?STUD mstr:masterNotion ?NOT }\n"
//                + "?QCMTRY a mstr:QcmTry .\n"
//                + "?QCMTRY mstr:refersQcm ?QCM .\n"
//                + "?STU mstr:hasQcmTry ?QCMTRY .\n"
//                + "?QCMTRY mstr:mark ?M .\n"
                //hasValidateExo End
                //maitriseNotion beg
                + "OPTIONAL{?QCM mstr:giveNotion ?N }\n"
                + "OPTIONAL {?QCM mstr:needNotion ?N1 }\n"
                //maitriseNotion end
                + "FILTER (?STUD = mstr:" + this.student + ")\n"
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
    public ArrayList<java.net.URI> getNotMasteredNotion() {
        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX mstr: <http://methodo-stat-tutor.com#>"
                + "SELECT ?N "
                + "WHERE {";
        String queryEnd = "}";


        String queryStr2
                = queryBegin
                + "{ ?STU mstr:dontMasterNotion ?N }\n"
                + " MINUS \n"
                + "{ ?STU mstr:masterNotion ?N }\n"
                + "FILTER (?STU = mstr:" + this.student + ")\n"
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

    //?VALID -> l'exercice a déja été validé 
    public String getExerciseToDo(ArrayList<java.net.URI> notMasteredNotion) {
        String filter = "";
    //    if (notMasteredNotion.size() > -1) {
            filter = "FILTER (?NOT IN  (" + listToString(notMasteredNotion) + "))\n";
    //    }

        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX mstr: <http://methodo-stat-tutor.com#>\n"
                + "SELECT *\n"
                + "WHERE {\n";
        String queryEnd = "}";

        String queryStr2
                = queryBegin
                +"{\n"
                + "{?qcm a mstr:Qcm .\n"
                + "}\n"
                + "MINUS\n"
                // MOINS les QCM dont toutes les notions ne sont pas maitrisées (= au moins une notion )
                + "{\n"
                + "SELECT ?qcm\n"
                + "WHERE {\n"
                + "?qcm a mstr:Qcm .\n"
                + "?qcm mstr:needNotion ?N .\n"
                + "BIND(IRI(str(?N)) as ?NOT)\n"
                + filter
                + "}\n"
                + "}\n"
                + "?qcm a mstr:Qcm .\n"
                + "?qcm mstr:name  ?name .\n"
                + "?qcm mstr:d8Add ?d8add .\n"
                + "OPTIONAL{?qcm mstr:teacher ?teacher .\n"
                + "?teacher mstr:nom ?fname .\n"
                + "?teacher mstr:prenom ?lname }.\n"
                + "OPTIONAL{?VALID mstr:hasValidateExo ?qcm ."
                + "FILTER (?VALID = mstr:"+this.student+")}.\n"
                + "values ?block {'true'}.\n"
                + "?qcm mstr:difficulty ?difficulty .\n"
                + "OPTIONAL{?qcmtry mstr:refersQcm ?qcm .\n"
                + "?qcmtry mstr:finished false .\n"
                + "?qcmtry mstr:json ?json .\n"
                + "?qcmtry mstr:user mstr:" + this.student + " }\n"
                +"}\n"
                //c'était les qcm dont les notions sont maitrisées (BLOCK = TRUE)
                +"UNION {\n"
                // auxquels on ajoute les qcm dont les notions sont pas maitrisées (BLOCK = FALSE)
                + "{\n"
                + "SELECT ?qcm\n"
                + "WHERE {\n"
                + "?qcm a mstr:Qcm .\n"
                + "?qcm mstr:needNotion ?N .\n"
                + "BIND(IRI(str(?N)) as ?NOT)\n"
                + filter
                + "} }\n"
                + "?qcm a mstr:Qcm .\n"
                + "?qcm mstr:name  ?name .\n"
                + "?qcm mstr:d8Add ?d8add .\n"
                + "OPTIONAL{?qcm mstr:teacher ?teacher .\n"
                + "?teacher mstr:nom ?fname .\n"
                + "?teacher mstr:prenom ?lname }.\n"
                + "OPTIONAL{?VALID mstr:hasValidateExo ?qcm ."
                + "FILTER (?VALID = mstr:"+this.student+")}.\n"
                + "values ?block {'false'}.\n"
                + "?qcm mstr:difficulty ?difficulty .\n}"
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

