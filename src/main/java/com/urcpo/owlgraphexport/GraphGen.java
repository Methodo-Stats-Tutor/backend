package com.urcpo.owlgraphexport;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.urcpo.mst.servlets.LoadOnto;
import com.urcpo.owlgraphexport.beans.Diagram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

public abstract class GraphGen {
    protected Diagram                     dia   = new Diagram();
    protected OntModel                    ontmod;
    protected Model                       mod;
    protected final Queue<List<Resource>> queue = new LinkedList<>();
    protected final Property              hierarchyPattern;

    public GraphGen(  final Property hierarchyPattern )
            throws FileNotFoundException {
        this.hierarchyPattern = hierarchyPattern;
    }

    private void initOnts() throws FileNotFoundException {

    }

    public Diagram getDia() {
        return dia;
    }

}
