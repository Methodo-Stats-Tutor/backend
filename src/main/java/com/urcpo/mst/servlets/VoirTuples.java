package com.urcpo.mst.servlets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 * Servlet implementation class VoirTuples
 */
@WebServlet("/VoirTuples")
public class VoirTuples extends HttpServlet {
	private static final long serialVersionUID = 1L;
	   public static final String  VUE_FORM         = "/WEB-INF/triples.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VoirTuples() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    ConnectTDB.dataset.begin( ReadWrite.READ );
	       try
	        {
	            // Do some queries
	            String sparqlQueryString1 = "SELECT (count(*) AS ?count) { ?s ?p ?o }" ;
	            String sparqlQueryString2 = "SELECT * { ?s ?p ?o   }" ;
	           
	            List res = execQuery(sparqlQueryString2, ConnectTDB.dataset) ;
	     //       execQuery(sparqlQueryString1, ConnectTDB.dataset) ;
	            request.setAttribute( "res", res );
	            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
	            ConnectTDB.dataset.commit();
	        } finally
	        {
	            ConnectTDB.dataset.end();
	        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
    public static List execQuery(String sparqlQueryString, Dataset dataset)
    {
        Query query = QueryFactory.create(sparqlQueryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, dataset) ;
       
        try {
            ResultSet results = qexec.execSelect() ;

          //  ResultSetFormatter.out(results) ;
            return  ResultSetFormatter.toList( results );
           // return ResultSetFormatter.asText( results );

          } finally { qexec.close() ; }
    }
    
    public static void printStatements(Model m, Resource s, Property p, Resource o) {
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
            
        }
    }

}
