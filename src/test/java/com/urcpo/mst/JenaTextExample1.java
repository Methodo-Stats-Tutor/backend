package com.urcpo.mst;

import org.apache.jena.atlas.lib.StrUtils ;
import org.apache.jena.atlas.logging.LogCtl ;
import org.apache.jena.query.text.EntityDefinition ;
import org.apache.jena.query.text.TextDatasetFactory ;
import org.apache.jena.query.text.TextQuery ;
import org.apache.jena.riot.RDFDataMgr ;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.RAMDirectory ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import com.hp.hpl.jena.query.* ;
import com.hp.hpl.jena.rdf.model.Model ;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.util.QueryExecUtils ;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.RDFS ;
import java.io.File;
import java.io.IOException;
import org.apache.jena.riot.Lang;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
/** Build a text search dataset */
public class JenaTextExample1
{
    static { LogCtl.setLog4j() ; }
    static Logger log = LoggerFactory.getLogger("JenaTextExample") ;
    @Test
    public  void Test() throws IOException
    {
        TextQuery.init();
        Dataset ds = createCode() ;
      //  Dataset ds = createAssembler() ;
        loadData(ds ) ;
        queryData(ds) ;
    }
    
    public static Dataset createCode() throws IOException 
    {
        log.info("Construct an in-memory dataset with in-memory lucene index using code") ;
        // Build a text dataset by code.
        // Here , in-memory base data and in-memeory Lucene index
        // Base data
      //  Dataset ds1 = DatasetFactory.createMem() ; 
         Dataset         ds1 = TDBFactory.createDataset("/app/mstr/triple-store");
        // Define the index mapping 
        EntityDefinition entDef = new EntityDefinition("uri", "text", RDFS.label.asNode()) ;
        // Lucene, in memory.
  //      Directory dir =  new RAMDirectory();
        Directory dir = FSDirectory.open(new File("/app/mstr/lucene"));
        
        // Join together into a dataset
        Dataset ds = TextDatasetFactory.createLucene(ds1, dir, entDef,null) ;
        
        return ds ;
    }
    public static Dataset createAssembler() 
    {
        log.info("Construct text dataset using an assembler description") ;
        // There are two datasets in the configuration:
        // the one for the base data and one with text index.
        // Therefore we need to name the dataset we are interested in. 
        Dataset ds = DatasetFactory.assemble("/home/nps/text-config.ttl", "http://localhost/jena_example/#text_dataset") ;
        return ds ;
    }
    
    public static void loadData(Dataset dataset)
    {
        log.info("Start loading") ;
        long startTime = System.nanoTime() ;
        dataset.begin(ReadWrite.WRITE) ;
        try {
           // Model m = dataset.getDefaultModel() ;
            // Dataset        
      //  dataset.begin(ReadWrite.WRITE);
        //adding new stmt
        Resource r = dataset.getDefaultModel().createResource("1");
        r.addProperty(RDFS.label, dataset.getDefaultModel().createLiteral("TITI"));
        //adding new stmt
       Resource r2 = dataset.getDefaultModel().createResource("2");
        r2.addProperty(RDFS.label, dataset.getDefaultModel().createLiteral("TOTO"));
            dataset.commit() ;
        } finally { dataset.end() ; }
        
        long finishTime = System.nanoTime() ;
        double time = (finishTime-startTime)/1.0e6 ;
        log.info(String.format("Finish loading - %.2fms", time)) ;
    }
    public static void queryData(Dataset dataset)
    {
        log.info("START") ;
        long startTime = System.nanoTime() ;
        String pre = StrUtils.strjoinNL
            ( "PREFIX : <http://example/>"
            , "PREFIX text: <http://jena.apache.org/text#>"
            , "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>") ;
        
        String qs = StrUtils.strjoinNL
            ( "SELECT * "
            , " { ?s text:query (rdfs:label 'TITI') ;"
            , "      rdfs:label ?label"
            , " }") ; 
        
        dataset.begin(ReadWrite.READ) ;
        try {
            Query q = QueryFactory.create(pre+"\n"+qs) ;
            QueryExecution qexec = QueryExecutionFactory.create(q , dataset) ;
            QueryExecUtils.executeQuery(q, qexec) ;
        } finally { dataset.end() ; }
        long finishTime = System.nanoTime() ;
        double time = (finishTime-startTime)/1.0e6 ;
        log.info(String.format("FINISH - %.2fms", time)) ;
    }
}

