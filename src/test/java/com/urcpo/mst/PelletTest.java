package com.urcpo.mst;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.urcpo.mstr.services.QcmService;
import com.urcpo.mstr.servlets.ConnectTDB;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ValidityReport;

import org.mindswap.pellet.jena.PelletReasonerFactory;





//import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class PelletTest {
    private static final String ontology    = "data/university0-0.owl";
    private static final String[]   queries     = new String[] {
                                        // One of the original LUBM queries
            "data/lubm-query4.sparql",
            // A SPARQL-DL query
            "data/lubm-sparql-dl.sparql",
            // A SPARQL-DL with the SPARQL-DL extensions vocabulary
            "data/lubm-sparql-dl-extvoc.sparql" };
    @Before
    public void init() {
        System.out.println("BOB");
    }
    
    public void run() {
        for ( int i = 0; i < queries.length ; i++ ) {
            String query = queries[i];
            
            // First create a Jena ontology model backed by the Pellet reasoner
            // (note, the Pellet reasoner is required)
            OntModel m = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );
    
            // Then read the data from the file into the ontology model
            m.read( ontology );
    
            // Now read the query file into a query object
            Query q = QueryFactory.read( query );
    
            // Create a SPARQL-DL query execution for the given query and
            // ontology model
            QueryExecution qe = SparqlDLExecutionFactory.create( q, m );
    
            // We want to execute a SELECT query, do it, and return the result set
            ResultSet rs = qe.execSelect();
    
            // Print the query for better understanding
            System.out.println(q.toString());
            
            // There are different things we can do with the result set, for
            // instance iterate over it and process the query solutions or, what we
            // do here, just print out the results
            ResultSetFormatter.out( rs );
            
            // And an empty line to make it pretty
            System.out.println();
        }
    }

  // @Test
    public void test() throws Exception   {
//       SPARQLDLExample app = new SPARQLDLExample();
//       app.run();
       run();
   }
   
  @Test
   public void BnodeQueryExample() throws Exception {
       PelletOptions.TREAT_ALL_VARS_DISTINGUISHED = false;
       String ns = "http://www.w3.org/TR/2003/PR-owl-guide-20031209/food#";
        
       // create an empty ontology model using Pellet spec
       OntModel model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );        
           
       // read the file
       model.read( "data/wine.owl" );      

       // getOntClass is not used because of the same reason mentioned above
       // (i.e. avoid unnecessary classifications)
       Resource RedMeatCourse = model.getResource( ns + "RedMeatCourse" ); 
       Resource PastaWithLightCreamCourse = model.getResource( ns + "PastaWithLightCreamCourse" ); 
       
       // create two individuals Lunch and dinner that are instances of  
       // PastaWithLightCreamCourse and RedMeatCourse, respectively
       model.createIndividual( ns + "MyLunch", PastaWithLightCreamCourse);
       model.createIndividual( ns + "MyDinner", RedMeatCourse);
       
       String queryBegin = 
           "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
           "PREFIX food: <http://www.w3.org/TR/2003/PR-owl-guide-20031209/food#>\r\n" + 
           "PREFIX wine: <http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#>\r\n" + 
           "\r\n" + 
           "SELECT ?Meal ?WineColor\r\n" + 
           "WHERE {\r\n";
       String queryEnd = "}";
       
       // create a query that asks for the color of the wine that
       // would go with each meal course
       String queryStr1 =
           queryBegin + 
           "   ?Meal rdf:type food:MealCourse .\r\n" + 
           "   ?Meal food:hasDrink _:Wine .\r\n" + 
           "   _:Wine wine:hasColor ?WineColor" +
           queryEnd;

       // same query as above but uses a variable instead of a bnode        
       String queryStr2 =
           queryBegin + 
           "   ?Meal rdf:type food:MealCourse .\r\n" + 
           "   ?Meal food:hasDrink ?Wine .\r\n" + 
           "   ?Wine wine:hasColor ?WineColor" +
           queryEnd;

       Query query1 = QueryFactory.create( queryStr1 );
       Query query2 = QueryFactory.create( queryStr2 );

       // The following definitions from food ontology dictates that 
       // PastaWithLightCreamCourse has white wine and RedMeatCourse 
       // has red wine.
       //  Class(PastaWithLightCreamCourse partial 
       //        restriction(hasDrink allValuesFrom(restriction(hasColor value (White)))))
       //  Class(RedMeatCourse partial 
       //        restriction(hasDrink allValuesFrom(restriction(hasColor value (Red)))))
       //
       // PelletQueryEngine will successfully find the answer for the first query
       printQueryResults( 
           "Running first query with PelletQueryEngine...", 
           SparqlDLExecutionFactory.create( query1, model ), query1 );
       
       // The same query (with variables instead of bnodes) will not return same answers!
       // The reason is this: In the second query we are using a variable that needs to be 
       // bound to a specific wine instance. The reasoner knows that there is a wine (due
       // to the cardinality restriction in the ontology) but does not know the URI for  
       // that individual. Therefore, query fails because no binding can be found. 
       //
       // Note that this behavior is similar to what you get with "must-bind", "don't-bind" 
       // variables in OWL-QL. In this case, variables in the query are "must-bind" variables 
       // and bnodes are "don't-bind" variables.
       printQueryResults( 
           "Running second query with PelletQueryEngine...", 
           SparqlDLExecutionFactory.create( query2, model ), query2 );
       
       // When the standard QueryEngine of Jena is used we don't get the results even 
       // for the first query. The reason is Jena QueryEngine evaluates the query one triple 
       // at a time and thus fails when the wine individual is not found. PelletQueryEngine
       // evaluates the query as a whole and succeeds. (If the above feature, creating bnodes
       // automatically, is added to Pellet then you would get the same results here) 
       printQueryResults( 
           "Running first query with standard Jena QueryEngine...", 
           QueryExecutionFactory.create( query1, model ), query1 );     
       
   }
   public  void printQueryResults( String header, QueryExecution qe, Query query ) throws Exception {
       System.out.println(header);
       
       ResultSet results = qe.execSelect();

       ResultSetFormatter.out( System.out, results, query );
       
       System.out.println();
   }

   
}
