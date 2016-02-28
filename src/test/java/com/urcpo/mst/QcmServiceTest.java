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
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;

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

public class QcmServiceTest {
    @Before
    public void init() {
    }

   @Test
    public void test() throws Exception   {
     System.out.println("test");
//        QcmService qs = new QcmService();
//        String uri = "ide22c2b36-5d52-453f-b422-10732c60fccf";
//        String answer = "{\"finished\":true,\"note\":null,\"result\":{\"1\":{\"answers\":{\"id2af35640-3bf2-4005-8a0c-d587b261b175\":{\"value\":true},\"id8cc5d629-d469-4ecd-b811-7611fe16f031\":{\"value\":false},\"id8aa9c701-75dd-4445-b78f-1a79e6ec669c\":{\"value\":false,\"help\":true}},\"finished\":true}},\"d8add\":null,\"d8last\":null,\"id_qcm\":null,\"nowRank\":1} ";
//      
//            qs.correctAnswers( "ide22c2b36-5d52-453f-b422-10732c60fccf", answer );
//    
//    }
   }
 //  @Test
   public void test2() throws Exception   {
       // create the model and load the data.
       Model model = ModelFactory.createDefaultModel().read( "/home/nps/Projets/stat-tutor/STATO/owlapi.xrdf" );

       // owlAnnotationProperties are the properties used to represent
       // annotated axioms in RDF/XML.
       Set<Property> owlAnnotationProperties = new HashSet<Property>() {{
//           add( RDF.type );
//           add( OWL2.annotatedProperty );
//           add( OWL2.annotatedSource );
           add( RDFS.subClassOf );
           add( OWL2.equivalentClass );
           add( OWL2.intersectionOf );
           add( OWL2.allValuesFrom );
       }};

       // Find the axioms in the model.  For each axiom, iterate through the 
       // its properties, looking for those that are *not* used for encoding the 
       // annotated axiom.  Those that are left are the annotations.
       ResIterator axioms = model.listSubjectsWithProperty( RDFS.subClassOf );
       while ( axioms.hasNext() ) {
           Resource axiom = axioms.next();
           if(axiom.toString().equals( "http://purl.obolibrary.org/obo/OBI_0000275" )){//une seule fois...
               StmtIterator stmts = axiom.listProperties();
             //  printRecurs( stmts, model )
           while ( stmts.hasNext() ) {
               Statement stmt = stmts.next();
              if ( owlAnnotationProperties.contains( stmt.getPredicate() )) {
                //  if(stmt.getObject().isAnon()){
                     
                      StmtIterator axioms2 =   model.listStatements( stmt.getObject().asResource(), null, (RDFNode)null );
//                      System.out.println(model.getRDFNode( stmt.getObject().asNode() ));
                   //   ResIterator axioms2 =  stmt.getObject().getModel().listResourcesWithProperty( OWL.allValuesFrom );
                 
                      while ( axioms2.hasNext() ) {
                          Statement axiom2 = axioms2.next();
                          if ( owlAnnotationProperties.contains( axiom2.getPredicate() )) {
                          if(stmt.getObject().isAnon()){
                          StmtIterator    axioms3 = model.listStatements( axiom2.getSubject().asResource(), null, (RDFNode)null );
                          while ( axioms3.hasNext() ) {
                              Statement axiom3 = axioms3.next();
                              System.out.println(" S : "+ axiom3.getSubject() + " P :" + axiom3.getPredicate() + " O : "+axiom3.getObject());
                          }
                          }else{
                              
                              System.out.println(" S : "+ axiom2.getSubject() + " P :" + axiom2.getPredicate() + " O : "+axiom2.getObject());
 
                          }
                          }
                         // if(!stmt.getSubject().isAnon())
             
//                          if(axiom2.getPredicate().equals( OWL2.intersectionOf )){
//                              tmp2 +=  stmt.getPredicate() + " and  " + axiom2.getObject() ;
//                              }
                      
                      }
            //          System.out.println("anon : "+ tmp1 + tmp2);
                 // }else{
              //        System.out.println( "o " + stmt.getObject() );
                      
                  //}
              }
           }
         }
       }
   }
   
   public String printRecurs2( StmtIterator stmts, Model mod ) {
       Set<Property> owlAnnotationProperties = new HashSet<Property>() {{
//         add( RDF.type );
//         add( OWL2.annotatedProperty );
//         add( OWL2.annotatedSource );
         add( RDFS.subClassOf );
         add( OWL2.equivalentClass );
        
     }};

     return "";
     
      // if (! owlAnnotationProperties.contains( rdfNode. )) {return "";}
//       if ( stmts.isAnon() ) {
//           StmtIterator axioms2 = mod.listStatements( (Resource) stmts, OWL.allValuesFrom, (RDFNode) null );
//           String tmp = "";
//           while ( axioms2.hasNext() ) {
//               Statement axiom2 = axioms2.next();
//              tmp += " " +printRecurs( axiom2.getObject().asResource(), mod );
//
//           };
//           return "( " + tmp+ " )";
//       } else {
//
//          return( "( " +stmts.toString()+ " )\n" );
//       }
  }
   //@Test
   public void test3() throws Exception   {
       // create the model and load the data.
       Model model = ModelFactory.createDefaultModel().read( "/home/nps/Projets/stat-tutor/STATO/owlapi.xrdf" );

       // owlAnnotationProperties are the properties used to represent
       // annotated axioms in RDF/XML.
       Set<Property> owlAnnotationProperties = new HashSet<Property>() {{
//           add( RDF.type );
//           add( OWL2.annotatedProperty );
//           add( OWL2.annotatedSource );
           add( RDFS.subClassOf );
           add( OWL2.equivalentClass );
       }};

       // Find the axioms in the model.  For each axiom, iterate through the 
       // its properties, looking for those that are *not* used for encoding the 
       // annotated axiom.  Those that are left are the annotations.
       ResIterator axioms = model.listSubjectsWithProperty( RDFS.subClassOf );
       while ( axioms.hasNext() ) {
           Resource axiom = axioms.next();
           if(axiom.toString().equals( "http://purl.obolibrary.org/obo/OBI_1110108" )){//une seule fois...
               StmtIterator stmts = axiom.listProperties();
               printRecurs2( stmts, model );
         }
       }
   }
   @Test
   public void testPellet() throws Exception   {
       // create the model and load the data.
       Model model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC);
        model.read( "/home/nps/Projets/stat-tutor/STATO/stato.owl" );

       /* ... ;any additional model modification ... */

    // get the instances of a class
//       OntClass Person = model.getOntClass( "http://purl.obolibrary.org/obo/OBI_0000275" );         
//      StmtIterator instances = Person.listProperties();
//      while(instances.hasNext()){
//          System.out.println(instances.next().getString());
//     }
   }
   
   @Test
   public  void test4() {
       System.getProperties().put("http.proxyHost", "10.172.139.21");
       System.getProperties().put("http.proxyPort", "3128");
       usageWithDefaultModel();
       
       usageWithOntModel();
   }
   
   public static void usageWithDefaultModel() {
       System.out.println("Results with plain RDF Model");
       System.out.println("----------------------------");
       System.out.println();

   
       // ontology that will be used
       String ont = "http://protege.cim3.net/file/pub/ontologies/koala/koala.owl#";
       String ns = "http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#";
       
       // create Pellet reasoner
       Reasoner reasoner = PelletReasonerFactory.theInstance().create();
       
       // create an empty model
       Model emptyModel = ModelFactory.createDefaultModel( );
       
       // create an inferencing model using Pellet reasoner
       InfModel model = ModelFactory.createInfModel( reasoner, emptyModel );
           
       // read the file
       model.read( ont );
       
       // print validation report
       ValidityReport report = model.validate();
       printIterator( report.getReports(), "Validation Results" );
       
       // print superclasses
       Resource c = model.getResource( ns + "MaleStudentWith3Daughters" );         
       printIterator(model.listObjectsOfProperty(c, RDFS.subClassOf), "All super classes of " + c.getLocalName());
       
       System.out.println();
   }

   public static void usageWithOntModel() {    
       System.out.println("Results with OntModel");
       System.out.println("---------------------");
       System.out.println();

       // ontology that will be used
       String ont = "http://protege.cim3.net/file/pub/ontologies/koala/koala.owl#";
       String ns = "http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#";
       
       // create an empty ontology model using Pellet spec
       OntModel model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );
           
       // read the file
       model.read( ont );
       
       // print validation report
       ValidityReport report = model.validate();
       printIterator( report.getReports(), "Validation Results" );
       
       // print superclasses using the utility function
       OntClass c = model.getOntClass( ns + "MaleStudentWith3Daughters" );         
       printIterator(c.listSuperClasses(), "All super classes of " + c.getLocalName());
       // OntClass provides function to print *only* the direct subclasses 
       printIterator(c.listSuperClasses(true), "Direct superclasses of " + c.getLocalName());
       
       System.out.println();
   }
   
   public static void printIterator(Iterator<?> i, String header) {
       System.out.println(header);
       for(int c = 0; c < header.length(); c++)
           System.out.print("=");
       System.out.println();
       
       if(i.hasNext()) {
           while (i.hasNext()) 
               System.out.println( i.next() );
       }       
       else
           System.out.println("<EMPTY>");
       
       System.out.println();
   }

   
}
