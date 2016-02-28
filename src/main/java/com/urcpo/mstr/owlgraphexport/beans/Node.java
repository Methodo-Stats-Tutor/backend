package com.urcpo.mstr.owlgraphexport.beans;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.urcpo.mstr.owlgraphexport.Str;
import org.apache.jena.iri.IRIRelativize;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Node extends Element {

    private String id;
    private Integer percentFinished;

    private OntResource resource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public OntResource getResource() {
        return resource;
    }

    public void setResource(OntResource resource) {
        this.resource = resource;
    }

    public void setPercentFinished(OntResource node, Model modelNotion) {
        if (modelNotion.contains(null, null, node) || node.getLocalName().equals("ExternalConcept")) {
            this.percentFinished = 100;
        } else {
            this.percentFinished = 0;
        }
    }

    public int getWidth() {
       return resource.getLocalName().equals("ExternalConcept")? 60 : 30;
         
    }

    public int getHeight() {
       return resource.getLocalName().equals("ExternalConcept")? 60 : 30;
    }

    public String toCystoscape() {

        return String.format("{ \"data\":{ \"id\": %s, \"name\": %s, \"percentFinished\": %s, \"width\":\"%d px\", \"height\":\"%d px\"  } %s, \"weight\": 65 }",
                Str.quote(id),
                Str.quote(resource.getLabel(null)),
                this.percentFinished,
                getWidth(),
                getHeight(),
                propsToCystoscape()
        );
    }

}
