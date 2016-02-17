package com.urcpo.owlgraphexport;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;

public class GraphGenRoot extends GraphGen {

    private Resource root;

    public GraphGenRoot(final String owlFile, final Integer depth, final Resource root,
            final Property hierarchyPattern) throws FileNotFoundException {
        super(owlFile, depth, hierarchyPattern);
        this.root = root;
    }

    private void initBFS() {
        final List<Resource> thingPath = new ArrayList<>();
        thingPath.add(root);
        queue.offer(thingPath);
    }

    public void pushNodesInDiaFromRoot() {
        // dia.addNode( ontmod.getOntResource( root) );
        StmtIterator stmt = ontmod.listStatements(null, RDFS.subClassOf, root);
        while (stmt.hasNext()) {
            OntResource r = ontmod.getOntResource(stmt.next().getSubject().getURI());
            System.out.println(r.toString());
            dia.addNode(r);
        }
    }

    public void pushEdgesInDia() {
        StmtIterator stmt = mod.listStatements(null, RDFS.subClassOf, (Resource) null);
        while (stmt.hasNext()) {
            Statement test = stmt.next();
            OntResource source = ontmod.getOntResource(test.getSubject());
            OntResource target = ontmod.getOntResource(test.getObject().asResource());
            if (target.isURIResource()
                    && source.isURIResource()
                    && dia.containsNode(source)
                    && dia.containsNode(target)) {
                dia.addEdge(source, target);
            }
        }
    }

    public void BFSWithRoot() {
        int count = 0;
        initBFS();
        final List<List<Resource>> results = new ArrayList<>();
        while (!queue.isEmpty()) {
            count++;
            System.out.println(count);
            final List<Resource> path = queue.poll();
            // dia.addNode( path );
            results.add(path);
            if (path.size() < depth) {
                final Resource last = path.get(path.size() - 1);
                System.out.println("dernier :" + last);
                final StmtIterator stmt = ontmod.listStatements(null, RDFS.subClassOf, last);
                while (stmt.hasNext()) {
                    final List<Resource> extPath = new ArrayList<>(path);
                    OntResource r = ontmod.getOntResource(stmt.next().getSubject());
                    System.out.println(r.toString());
                    extPath.add(r);
                    if (r != null) {
                        dia.addNode(r);
                    }
                    queue.offer(extPath);
                }
            }
        }
    }

}
