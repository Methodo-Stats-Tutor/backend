package com.urcpo.mstr.owlgraphexport.beans;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import java.util.Iterator;

public class Diagram {
    private List<Element> nodes = new ArrayList<Element>();
    private List<Element> edges = new ArrayList<Element>();

    public void addNode( OntResource node, Model modelNotion ) {
        Node n = new Node();
        n.setResource( node );
        n.setId( node.getLocalName() );
        n.setPercentFinished(node,modelNotion);
        nodes.add( n );
    }

    public void addEdge( OntResource source, OntResource target ) {
        Edge e = new Edge();
        e.setSource( source );
        e.setTarget( target );
        edges.add( e );
    }

    public Boolean containsNode( OntResource ressource ) {
        Boolean res = false;
        for ( int i = 0; i < nodes.size(); i++ ) {
            if ( ( (Node) nodes.get( i ) ).getResource().equals( ressource ) ) {
                res = true;
                break;
            }
        }
        return res;
    }

    public String toCystoscape() {
        return "{\n\"nodes\":[\n" + listToString( nodes ) + "],\n\"edges\":[\n" + listToString( edges ) + "] \n }" ;
    }

    private String listToString( List<Element> eles ) {
        StringBuilder res = new StringBuilder();
        
        Iterator<Element> elIt = eles.iterator();
        while(elIt.hasNext()){
         res.append( elIt.next().toCystoscape() );
            if(elIt.hasNext())
            res.append( ",\n" );
        }
//        
        return res.toString();
    }
}
