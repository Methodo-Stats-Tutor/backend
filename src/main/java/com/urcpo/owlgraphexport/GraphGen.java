package com.urcpo.owlgraphexport;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
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
    private final String                  owlFile;
    protected final Queue<List<Resource>> queue = new LinkedList<>();
    protected final Integer               depth;
    protected final Property              hierarchyPattern;

    public GraphGen( final String owlFile, final Integer depth, final Property hierarchyPattern )
            throws FileNotFoundException {
        this.owlFile = owlFile;
        this.depth = depth;
        this.hierarchyPattern = hierarchyPattern;
        initOnts();
    }

    private void initOnts() throws FileNotFoundException {
        ontmod = ModelFactory.createOntologyModel();
        InputStream in = null;
        in = new FileInputStream( owlFile );
        ontmod.read( in, null );
        
        mod = ModelFactory.createDefaultModel();
        InputStream in2 = null;
        in2 = new FileInputStream( owlFile );
        mod.read( in2, null );
    }

    public Diagram getDia() {
        return dia;
    }

}
