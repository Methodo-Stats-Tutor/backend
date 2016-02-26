package com.urcpo.mst;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Literal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import org.apache.jena.query.text.EntityDefinition;
import org.apache.jena.query.text.TextDatasetFactory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import com.hp.hpl.jena.tdb.TDB;
import org.apache.jena.query.text.TextQuery;

public class LuceneTest {

    /**
     * @param args
     */
    @Before
    public void init() {
    }

    @Test
    public void test() throws Exception {
        TextQuery.init();
        Dataset dsa = DatasetFactory.assemble(
                "/home/nps/text-config.ttl",
                "http://localhost/jena_example/#text_dataset");
        dsa.begin(ReadWrite.WRITE);
        //adding new stmt
        Resource r = dsa.getDefaultModel().createResource("1");
        r.addProperty(RDFS.label, dsa.getDefaultModel().createLiteral("BOB"));
        //adding new stmt
        Resource r2 = dsa.getDefaultModel().createResource("2");
        r2.addProperty(RDFS.label, dsa.getDefaultModel().createLiteral("B"));
          dsa.commit();
          dsa.end();
           dsa.begin(ReadWrite.READ);
        //querying it
        String queryString = "PREFIX : <http://localhost/jena_example/#>"
                + "PREFIX text: <http://jena.apache.org/text#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "\n"
                + "SELECT ?s ?uri ?label\n"
                + "{ ?s text:query 'B' ; \n"
                + "     rdfs:label ?label ;\n"
                + "}";
        System.out.println(queryString);
        Query q = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(q, dsa.getDefaultModel());
        ResultSet results = qexec.execSelect();
        System.out.println(ResultSetFormatter.asText(results));
      
    }

}
