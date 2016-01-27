package com.urcpo.mst;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;


public class AnnotationExample {

    /**
     * @param args
     */
    @Before
    public void init() {
    }
    
    @Test
    public void test() throws Exception   {
        // create the model and load the data.
        Model model = ModelFactory.createDefaultModel().read( "/home/nps/Projets/stat-tutor/STATO/products.owl" );

        // owlAnnotationProperties are the properties used to represent
        // annotated axioms in RDF/XML.
        Set<Property> owlAnnotationProperties = new HashSet<Property>() {{
            add( RDF.type );
            add( OWL2.annotatedProperty );
            add( OWL2.annotatedSource );
            add( OWL2.annotatedTarget );
        }};

        // Find the axioms in the model.  For each axiom, iterate through the 
        // its properties, looking for those that are *not* used for encoding the 
        // annotated axiom.  Those that are left are the annotations.
        ResIterator axioms = model.listSubjectsWithProperty( RDF.type, OWL2.Axiom );
        while ( axioms.hasNext() ) {
            Resource axiom = axioms.next();
            StmtIterator stmts = axiom.listProperties();
            while ( stmts.hasNext() ) {
                Statement stmt = stmts.next();
                if ( !owlAnnotationProperties.contains( stmt.getPredicate() )) {
                    System.out.println( stmt );
                }
            }
        }
    }
}