package com.urcpo.mst.services;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.urcpo.mst.servlets.ConnectTDB;
import com.urcpo.mst.servlets.LoadOnto;

public class TupleService {
    private static final Logger log = Logger.getLogger( TupleService.class );

    public String getTuples() throws Exception {
        ResultSet results = null;

        ConnectTDB.dataset.begin( ReadWrite.READ );
        String sparqlQueryString = "SELECT * { ?s ?p ?o   }";
        Query query = QueryFactory.create( sparqlQueryString );
        QueryExecution qexec = QueryExecutionFactory.create( query, ConnectTDB.dataset );
        try {

            results = qexec.execSelect();

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ResultSetFormatter.outputAsJSON( outStream, results );
            return outStream.toString();
        } catch ( Exception e ) {
            log.error( e.getMessage() );
            throw new Exception( "ERREUR dans TupleService.getTuples" );
        } finally {
            qexec.close();

            ConnectTDB.dataset.end();
        }
    }

    public String getQuery( String query ) throws Exception {

        try {
            return ConnectTDB.getSparqlResultAsJson( query );
        } catch ( Exception e ) {
            throw new Exception( e.getMessage() );
        } finally {
        }
    }
    
    public String getQueryOnto( String query ) throws Exception {

        try {
           
            return LoadOnto.getSparqlResultAsJson( query );
        } catch ( Exception e ) {
            throw new Exception( e.getMessage() );
        } finally {
        }
    }
    public String getChildrenOnto( String query ) throws Exception {

        try {
            String query2 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
                    "PREFIX obo: <http://purl.obolibrary.org/obo/>\n" + 
                    "SELECT ?label ?id  ?hasChildren \n" + 
                    "WHERE \n" + 
                    "{\n" + 
                    "{\n" + 
                    "SELECT ?id  (count(?t ) as ?hasChildren) \n" + 
                    "WHERE {?id rdfs:subClassOf <"+query+"> .\n" + 
                    "\n" + 
                    "OPTIONAL {?t rdfs:subClassOf ?id . } .\n" + 
                    "}\n" + 
                    "GROUP BY ?id\n" + 
                    "}\n" + 
                    "OPTIONAL {?id rdfs:label ?label. } .\n" + 
        //            "OPTIONAL {?id obo:IAO_0000115 ?descr. } .\n" + 
                    "}";
            return LoadOnto.getSparqlResultAsJson( query2 );
        } catch ( Exception e ) {
            throw new Exception( e.getMessage() );
        } finally {
        }
    }
    
    
    
    public String getOntoIdInfo( String id ) throws Exception {

        try {
            String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
                    "PREFIX obo: <http://purl.obolibrary.org/obo/>\n" + 
                    "SELECT  ?descr ?rcommand  \n" + 
                    "WHERE\n" + 
                    "{\n" + 
                    "OPTIONAL{<" + id + "> obo:IAO_0000115 ?descr}.\n" + 
                    "OPTIONAL{<" + id + "> obo:STATO_0000041 ?rcommand}.\n"+
                    "}";
            return LoadOnto.getSparqlResultAsJson( query );
        } catch ( Exception e ) {
            throw new Exception( e.getMessage() );
        } finally {
        }
    }
    public String getQueryOntoSearch( String pattern ) throws Exception {

        try {
            String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
                    "PREFIX obo: <http://purl.obolibrary.org/obo/>\n" + 
                    "SELECT DISTINCT ?id ?label ?hasChildren\n" + 
                    "WHERE {\n" + 
                    "{\n" + 
                    "{ ?s rdfs:label ?o } UNION { ?s obo:IAO_0000115 ?o } UNION { ?s rdfs:comment ?o }.\n" + 
                    "FILTER regex(?o, \""+pattern+"\", \"i\")\n" + 
                    "}\n" + 
                    "{?id rdfs:label ?label} UNION  { ?id obo:IAO_0000115 ?label } UNION { ?id rdfs:comment ?label }.\n" + 
                    "FILTER (?id = ?s)\n" + 
                    "{\n" + 
                    "SELECT ?label ?s  ?hasChildren\n" + 
                    "WHERE\n" + 
                    "{\n" + 
                    "{\n" + 
                    "SELECT ?s  (count(?s ) as ?hasChildren)\n" + 
                    "WHERE {?s1 rdfs:subClassOf ?s .}\n" + 
                    "GROUP BY ?s\n" + 
                    "}\n" + 
                    "}\n" + 
                    "}\n" + 
                    "}\n" + 
                    "";
            return LoadOnto.getSparqlResultAsJson( query );
        } catch ( Exception e ) {
            throw new Exception( e.getMessage() );
        } finally {
        }
    }
}
