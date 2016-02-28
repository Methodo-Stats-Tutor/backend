package com.urcpo.owlgraphexport;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDFS;
import static com.hp.hpl.jena.vocabulary.RDFS.Resource;
import com.urcpo.owlgraphexport.beans.User;

public class TestGraphGen {
//private static String OWLFILE = "/home/nps/Projets/stat-tutor/OWL/graph_tester.rdf";
    private static String OWLFILE = "/app/mst/ontologies/int/mst.owl";

    @Before
    public void init() {
    }

    @Test
    public void test() throws FileNotFoundException, UnsupportedEncodingException  {
//        Resource root = ResourceFactory.createResource("http://methodo-stats-tutor.com#ExternalConcept");
//        GraphGenRoot ggr = new GraphGenRoot( OWLFILE,  root, RDFS.subClassOf );
//        ggr.pushNodesInDiaFromRoot();
//        ggr.pushEdgesInDia();
//      //  PrintWriter writer = new PrintWriter( "/home/nps/owljson.json", "UTF-8" );
//       // writer.println( GraphFormater.toCystoscape( ggr.getDia() ) );
//       // writer.close();
//                System.out.println( GraphFormater.toCystoscape( ggr.getDia() ) );

    }
}