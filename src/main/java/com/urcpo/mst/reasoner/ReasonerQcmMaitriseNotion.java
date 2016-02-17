package com.urcpo.mst.reasoner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.urcpo.mst.servlets.ConnectTDB;
import com.urcpo.mst.utils.MstUtils;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;

public class ReasonerQcmMaitriseNotion extends ReasonerService {
    private static final Logger     log = Logger.getLogger( ReasonerQcmMaitriseNotion.class );
    private String                  exercise;
    private ArrayList<java.net.URI> needNotion;
    private ArrayList<java.net.URI> giveNotion;

    public ReasonerQcmMaitriseNotion( String exercise ) {
        this.exercise = exercise;
        createSubModel();
        reasonOverSubModel();
        processResults();
    }

    @Override
    public void createSubModel() {
        ConnectTDB.dataset.begin( ReadWrite.WRITE );
        String queryBegin =
                "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                        "PREFIX mst: <http://methodo-stats-tutor.com#>\r\n" +
                        "\r\n" +
                        "CONSTRUCT { " +
                        "?EXO a mst:Exercice .\n" +
                        "?Q a mst:Question .\n" +
                        "?PZ a mst:PubliZone .\n" +
                        "?T a mst:Tag .\n" +
                        "?CZ a mst:ChoiceZone .\n" +
                        "?EXO mst:hasQuestion ?Q .\n" +
                        "?Q mst:hasChoiceZone ?CZ .\n" +
                        "?CZ mst:hasPubliZone ?PZ .\n" +
                        "?PZ mst:hasTag ?T .\n" +
                        "?T mst:uri ?URI .\n" +
                        "?EXO mst:needNotion ?NN .\n" +
                        "?EXO mst:giveNotion ?GN .\n" +
                        "} " +
                        "WHERE {";
        String queryEnd = "}";

        String queryStudent =
                queryBegin +
                        "?EXO a mst:Exercice .\n" +
                        "OPTIONAL {?EXO mst:needNotion ?NN} .\n" +
                        "OPTIONAL {?EXO mst:giveNotion ?GN} .\n" +
                        "?Q a mst:Question .\n" +
                        "?PZ a mst:PubliZone .\n" +
                        "?T a mst:Tag .\n" +
                        "?CZ a mst:ChoiceZone .\n" +
                        "?EXO mst:hasQuestion ?Q .\n" +
                        "?Q mst:hasChoiceZone ?CZ .\n" +
                        "?CZ mst:hasPubliZone ?PZ .\n" +
                        "?PZ mst:hasTag ?T .\n" +
                        "?T mst:uri ?URI .\n" +
                        "FILTER (?EXO = mst:" + this.exercise + ")\n" +
                        queryEnd;
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
    public void processResults() {
        String queryBegin =
                "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX mst: <http://methodo-stats-tutor.com#>" +
                        "SELECT ?NOTION " +
                        "WHERE {";
        String queryEnd = "}";

        // create a query that asks for the color of the wine that
        // would go with each meal course

        String queryStr2 =
                queryBegin +
                        " ?EXO mst:traiteNotion ?NOTION ." +
                        "FILTER (?EXO = mst:" + this.exercise + ")\n" +
                        queryEnd;
        log.error( queryStr2 );
        Query query2 = QueryFactory.create( queryStr2 );
        ResultSet results = SparqlDLExecutionFactory.create( query2, ontModel ).execSelect();
        ArrayList<java.net.URI> needNotion = new ArrayList<java.net.URI>();
        ArrayList<java.net.URI> giveNotion = new ArrayList<java.net.URI>();

        while ( results.hasNext() ) {
            RDFNode a = results.next().get( "?NOTION" );
            giveNotion.add( java.net.URI.create( a.asLiteral().getString() ) );
            needNotion.addAll( getSuperClassFromUri( a.asLiteral().getString() ) );

        }
        // remove duplicates from neednotion
        Set<java.net.URI> hs = new HashSet<>();
        hs.addAll( needNotion );
        needNotion.clear();
        needNotion.addAll( hs );
        // end remove duplicate

        this.needNotion = needNotion;
        this.giveNotion = giveNotion;

        //ajout des notions au modele 
        int i = 0;
        while ( i < needNotion.size() ) {
            log.error( needNotion.get( i ).toString() );
            addStatementToOntModel( "needNotion", needNotion.get( i ).toString() );
            i++;
        }
        i = 0;
        while ( i < giveNotion.size() ) {
            log.error( giveNotion.get( i ).toString() );
            addStatementToOntModel( "giveNotion", giveNotion.get( i ).toString() );
            i++;
        }

    }

    private void addStatementToOntModel( String prop, String lit ) {
        Resource s = ontModel.getIndividual( "http://methodo-stats-tutor.com#" + this.exercise );
        Property p = ontModel.getDatatypeProperty( "http://methodo-stats-tutor.com#" + prop );
        Literal o = ontModel.createTypedLiteral( lit, XSDDatatype.XSDanyURI );
        Statement st = ResourceFactory.createStatement( s, p, o );
        ontModel.add( st );
        log.error(st);
    }

    // NEED -> SUPERCLASSE
    private ArrayList<java.net.URI> getSuperClassFromUri( String uri ) {
        String queryBegin =
                "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX mst: <http://methodo-stats-tutor.com#>\n" +
                        "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX owl:      <http://www.w3.org/2002/07/owl#> \n" +
                        "PREFIX sparqldl: <http://pellet.owldl.com/ns/sdle#>\n " +
                        "SELECT DISTINCT ?SUPERCLASS ?LAB ?COM \n" +
                        "WHERE {";
        String queryEnd = "}";


        String queryStr2 =
                queryBegin +
                        // "?titi  owl:Class ?CLASS ." +
                        // "?NOTION a ?CLASS ." +
                        "?CLASS sparqldl:directSubClassOf ?SUPERCLASS ." +
                        "OPTIONAL {?SUPERCLASS rdfs:label ?LAB} ." +
                        "OPTIONAL {?SUPERCLASS rdfs:comment ?COM} ." +
                        "FILTER (?CLASS = <" + uri + ">)" +
                        queryEnd;
        log.error( queryStr2 );
        Query query2 = QueryFactory.create( queryStr2 );
        ResultSet results = SparqlDLExecutionFactory.create( query2, ontModel ).execSelect();
        ArrayList<java.net.URI> aru = new ArrayList<java.net.URI>();
        while ( results.hasNext() ) {
            aru.add( java.net.URI.create( results.next().get( "?SUPERCLASS" ).toString() ) );
        }
        return aru;
    }

    public String getQcmNotion() {
        String queryBegin =
                "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX mst: <http://methodo-stats-tutor.com#>\n" +
                        "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX owl:      <http://www.w3.org/2002/07/owl#> \n" +
                        "PREFIX sparqldl: <http://pellet.owldl.com/ns/sdle#>\n " +
                        "SELECT ?NOTION ?NOTIONLAB ?NOTIONCOMM ?TYP  \n" +
                        "WHERE {";
        String queryEnd = "}";

        // create a query that asks for the color of the wine that
        // would go with each meal course
        String queryStr2 =
                queryBegin +
                        "{?EXO a mst:Qcm .\n" +
                        " ?EXO mst:traiteNotion ?NOTION .\n" +
                        " BIND(IRI(str(?NOTION)) as ?NOTION_IRI) .\n" +
                        "OPTIONAL { ?NOTION_IRI rdfs:label ?NOTIONLAB } .\n" +
                        "OPTIONAL { ?NOTION_IRI rdfs:comment ?NOTIONCOMM } \n" +
                        "values ?TYP { 'traite' }\n" +
                        "FILTER (?EXO = mst:" + this.exercise + ")\n" +
                        "}\n" +
                        "UNION \n" +
                        "{?EXO a mst:Qcm .\n" +
                        " ?EXO mst:needNotion ?NOTION .\n" +
                        " BIND(IRI(str(?NOTION)) as ?NOTION_IRI) .\n" +
                        "OPTIONAL { ?NOTION_IRI rdfs:label ?NOTIONLAB } .\n" +
                        "OPTIONAL { ?NOTION_IRI rdfs:comment ?NOTIONCOMM } \n" +
                        "values ?TYP { 'need' }\n" +
                        "FILTER (?EXO = mst:" + this.exercise + ")" +
                        "}" +
                        "UNION \n" +
                        "{?EXO a mst:Qcm .\n" +
                        " ?EXO mst:giveNotion ?NOTION .\n" +
                        " BIND(IRI(str(?NOTION)) as ?NOTION_IRI) .\n" +
                        "OPTIONAL { ?NOTION_IRI rdfs:label ?NOTIONLAB } .\n" +
                        "OPTIONAL { ?NOTION_IRI rdfs:comment ?NOTIONCOMM } \n" +
                        "values ?TYP { 'give' }\n" +
                        "FILTER (?EXO = mst:" + this.exercise + ")" +
                        " }" +
                        queryEnd;
        log.error( queryStr2 );
        Query query2 = QueryFactory.create( queryStr2 );
        ResultSet results = SparqlDLExecutionFactory.create( query2, ontModel ).execSelect();
        String str = MstUtils.getSparqlResultsetAsJson( results );
        return str;
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
