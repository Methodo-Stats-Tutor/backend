package com.urcpo.owlgraphexport.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.hp.hpl.jena.ontology.OntResource;

public class Diagram {
    private List<Element> nodes = new ArrayList<Element>();
    private List<Element> edges = new ArrayList<Element>();

    public void addNode( OntResource node ) {
        Node n = new Node();
        n.setResource( node );
        n.setId( node.getLocalName() );
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
        return "elements:{\nnodes:[\n" + listToString( nodes ) + "],\nedges:[\n" + listToString( edges ) + "] \n}";
    }

    private String listToString( List<Element> eles ) {
        StringBuilder res = new StringBuilder();
        for ( Element ele : eles ) {
            res.append( ele.toCystoscape() );
            res.append( ",\n" );
        }
        return res.toString();
    }
}
