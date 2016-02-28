package com.urcpo.mst;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.mindswap.pellet.utils.ATermUtils;

import aterm.ATermAppl;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;

import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.urcpo.mstr.servlets.ConnectTDB;
/**
 * An example to show how to use OWLReasoner class. This example creates a JTree that displays the
 * class hierarchy. This is a simplified version of the class tree displayed in SWOOP.
 * 
 * usage: java ClassTree <ontology URI>
 * 
 * @author Evren Sirin
 */
public class IncrementalConsistencyExample {
    
    // namespaces that will be used
    static final String foaf            = "http://xmlns.com/foaf/0.1/";

    static final String mindswap        = "http://www.semanticweb.org/nps/ontologies/2015/11/untitled-ontology-9#";

    static final String mindswappers    = "http://www.semanticweb.org/nps/ontologies/2015/11/untitled-ontology-9#";

    @Test
    public  void test() throws Exception {

        // Set flags for incremental consistency
        PelletOptions.USE_COMPLETION_QUEUE = true;
        PelletOptions.USE_INCREMENTAL_CONSISTENCY = true;
        PelletOptions.USE_SMART_RESTORE = false;

        runWithPelletAPI();

     
        
     //  runWithJenaAPIAndPelletInfGraph();
        
    }

    public void runWithPelletAPI() throws Exception {
        PelletOptions.USE_UNIQUE_NAME_ASSUMPTION = true;
        System.out.println( "\nResults after applying changes through Pellet API" );
        System.out.println( "-------------------------------------------------" );

        // read the ontology with its imports
        OntModel model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );
        model.read( "/home/nps/Téléchargements/toto.owl" );

        // load the model to the reasoner
        model.prepare();

        // Get the KnolwedgeBase object
        KnowledgeBase kb = ((PelletInfGraph) model.getGraph()).getKB();

        // perform initial consistency check
        long s = System.currentTimeMillis();
        boolean consistent = kb.isConsistent();
        long e = System.currentTimeMillis();
        System.out.println( "Consistent? " + consistent + " (" + (e - s) + "ms)" );

        // perform ABox addition which results in a consistent KB
        ATermAppl concept = ATermUtils.makeTermAppl( mindswap + "Student" );
        ATermAppl individual = ATermUtils.makeTermAppl( mindswappers + "gaelle" );
        ATermAppl comment = ATermUtils.makeTermAppl(  "rdfs:comment" );

    //    kb.addIndividual( individual );
        kb.addType( individual, concept );
        
        System.out.println("anno"+ individual.getAnnotation( comment ));

        // perform incremental consistency check
        s = System.currentTimeMillis();
        consistent = kb.isConsistent();
        e = System.currentTimeMillis();
        System.out.println( "Consistent? " + consistent + " (" + (e - s) + "ms)" );

        // peform ABox addition which results in an inconsistent KB
        ATermAppl role = ATermUtils.makeTermAppl( mindswap + "dependsOn" );
        individual = ATermUtils.makeTermAppl( mindswappers + "gaelle" );
        ATermAppl mbox = ATermUtils.makeTermAppl( mindswappers + "nicolas" );
        kb.addPropertyValue( role, individual, mbox );

        // perform incremental consistency check
        s = System.currentTimeMillis();
        consistent = kb.isConsistent();
        e = System.currentTimeMillis();
        System.out.println( "Consistent? " + consistent + " (" + (e - s) + "ms)" );
        
        //query
        String queryBegin = 
                "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
                "PREFIX mstr: <http://www.semanticweb.org/nps/ontologies/2015/11/untitled-ontology-9#>\r\n" + 
                "PREFIX wine: <http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#>\r\n" + 
                "\r\n" + 
                "SELECT * \r\n" + 
                "WHERE {\r\n";
                String queryEnd = "}";

                // create a query that asks for the color of the wine that
                // would go with each meal course
                String queryStr2 =
                queryBegin + 
                "   ?user mstr:nePasProposerExercise ?bloq .\r\n" + 
                "   ?debloq a mstr:Exercise .\r\n" + 
                "   ?bloq mstr:dependsOn ?depends .\r\n" + 
                "   FILTER (?bloq != ?debloq) .\r\n" + 

                queryEnd;
                Query query2 = QueryFactory.create( queryStr2 );

                printQueryResults( 
                        "Running second query with PelletQueryEngine...", 
                        SparqlDLExecutionFactory.create( query2, model ), query2 );
                
     
    }

    
    
    public static void runWithJenaAPIAndPelletInfGraph() throws Exception {
        System.out.println( "\nResults after applying changes through Jena API using PelletInfGraph" );
        System.out.println( "-------------------------------------------------" );

        // read the ontology using model reader
        OntModel model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );
        model.setStrictMode( false );
        model.read( "/home/nps/Téléchargements/toto.owl" );
        
        //get the PelletInfGraph object
        PelletInfGraph pelletJenaGraph = ( PelletInfGraph )model.getGraph();
        
        // perform initial consistency check
        long s = System.currentTimeMillis();
        boolean consistent = pelletJenaGraph.isConsistent();
        long e = System.currentTimeMillis();
        System.out.println( "Consistent? " + consistent + " (" + (e - s) + "ms)" );

        // perform ABox addition which results in a consistent KB
        Resource concept = model.getResource( mindswap + "Student" );
        Individual individual = model.createIndividual( mindswappers + "gaelle", concept );

        // perform incremental consistency check
        s = System.currentTimeMillis();
        consistent = pelletJenaGraph.isConsistent();
        e = System.currentTimeMillis();
        System.out.println( "Consistent? " + consistent + " (" + (e - s) + "ms)" );


        // perform ABox addition which results in an inconsistent KB
        Property role = model.getProperty( mindswap + "dependsOn" );
        individual = model.getIndividual( mindswappers + "gaelle" );
        RDFNode mbox = model.getIndividual( mindswappers + "nicolas");
        individual.addProperty( role, mbox );

String queryBegin = 
"PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
"PREFIX mstr: <http://www.semanticweb.org/nps/ontologies/2015/11/untitled-ontology-9#>\r\n" + 
"PREFIX wine: <http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#>\r\n" + 
"\r\n" + 
"SELECT * \r\n" + 
"WHERE {\r\n";
String queryEnd = "}";

// create a query that asks for the color of the wine that
// would go with each meal course
String queryStr2 =
queryBegin + 
"   ?Meal mstr:nePasProposerExercise ?exo .\r\n" + 
queryEnd;
Query query2 = QueryFactory.create( queryStr2 );

printQueryResults( 
        "Running second query with PelletQueryEngine...", 
        SparqlDLExecutionFactory.create( query2, model ), query2 );
        // perform incremental consistency check
        s = System.currentTimeMillis();
        consistent = pelletJenaGraph.isConsistent();
        e = System.currentTimeMillis();
        System.out.println( "Consistent? " + consistent + " (" + (e - s) + "ms)" );
    }

    public static  void printQueryResults( String header, QueryExecution qe, Query query ) throws Exception {
        System.out.println(header);
        
        ResultSet results = qe.execSelect();

        ResultSetFormatter.out( System.out, results, query );
        
        System.out.println();
    }
}
