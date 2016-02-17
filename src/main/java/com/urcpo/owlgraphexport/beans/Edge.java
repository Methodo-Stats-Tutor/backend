package com.urcpo.owlgraphexport.beans;

import com.hp.hpl.jena.ontology.OntResource;
import com.urcpo.owlgraphexport.Str;

public class Edge extends Element {

    public OntResource source;

    public OntResource target;

    public OntResource getSource() {
        return source;
    }

    public void setSource( OntResource source ) {
        this.source = source;
    }

    public OntResource getTarget() {
        return target;
    }

    public void setTarget( OntResource target ) {
        this.target = target;
    }

    public String toCystoscape() {
        return "{ data:{ source:" + Str.quote( source.getLocalName() ) + ", target:"
                + Str.quote( target.getLocalName() ) + " }"+
                propsToCystoscape() +", strength:70 }";
    }
}
