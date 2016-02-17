package com.urcpo.owlgraphexport.beans;

import com.hp.hpl.jena.ontology.OntResource;
import com.urcpo.owlgraphexport.Str;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Node extends Element {
    private String      id;

    private OntResource resource;

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    @JsonIgnore
    public OntResource getResource() {
        return resource;
    }

    public void setResource( OntResource resource ) {
        this.resource = resource;
    }

    public String toCystoscape() {

        return "{ data:{ id:" + Str.quote( id ) + ", name:" + Str.quote( resource.getLabel( null ) ) + " }"
                +  propsToCystoscape()  + ", weight: 65 }";
    }

}
