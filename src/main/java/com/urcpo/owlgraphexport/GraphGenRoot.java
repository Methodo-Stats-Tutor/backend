package com.urcpo.owlgraphexport;

import java.io.FileNotFoundException;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class GraphGenRoot extends GraphGen {

    private Resource root;
    private Model modelNotion;

    public GraphGenRoot(final OntModel ontmodel, final Model modelNotion, final Resource root,
            final Property hierarchyPattern) throws FileNotFoundException {
        super(hierarchyPattern);
        this.root = root;
        this.ontmod = ontmodel;
        this.modelNotion = modelNotion;
    }

    public void pushNodesInDiaFromRoot() {
        // dia.addNode( ontmod.getOntResource( root) );
        StmtIterator stmt = ontmod.listStatements(null, hierarchyPattern, root);
        while (stmt.hasNext()) {
            OntResource r = ontmod.getOntResource(stmt.next().getSubject().getURI());
            dia.addNode(r, modelNotion);
        }
    }

    public void pushEdgesInDia() {
        StmtIterator stmt = ontmod.getBaseModel().listStatements(null, hierarchyPattern, (Resource) null);
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

    //    private void initBFS() {
//        final List<Resource> thingPath = new ArrayList<>();
//        thingPath.add(root);
//        queue.offer(thingPath);
//    }
//    public void BFSWithRoot() {
//        int count = 0;
//        initBFS();
//        final List<List<Resource>> results = new ArrayList<>();
//        while (!queue.isEmpty()) {
//            count++;
//            System.out.println(count);
//            final List<Resource> path = queue.poll();
//            // dia.addNode( path );
//            results.add(path);
//            if (path.size() < depth) {
//                final Resource last = path.get(path.size() - 1);
//                System.out.println("dernier :" + last);
//                final StmtIterator stmt = ontmod.listStatements(null, RDFS.subClassOf, last);
//                while (stmt.hasNext()) {
//                    final List<Resource> extPath = new ArrayList<>(path);
//                    OntResource r = ontmod.getOntResource(stmt.next().getSubject());
//                    System.out.println(r.toString());
//                    extPath.add(r);
//                    if (r != null) {
//                        dia.addNode(r);
//                    }
//                    queue.offer(extPath);
//                }
//            }
//        }
//    }
}
