package com.urcpo.mst.servlets;

import java.beans.Statement;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasoner;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.impl.EntityManagerImpl;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
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
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.urcpo.mst.beans.JsonBeans;
import com.urcpo.mst.reasoner.ReasonerExerciceToDo;
import com.urcpo.mst.reasoner.ReasonerQcmMaitriseNotion;
import com.urcpo.mst.reasoner.ReasonerStudentClassificationService;
import com.urcpo.mst.utils.MstUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.logging.Level;
import org.apache.jena.query.text.EntityDefinition;
import org.apache.jena.query.text.TextDatasetFactory;
import org.apache.jena.query.text.TextQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

/**
 * Servlet implementation class ConnectTDB
 */
@WebServlet(value = "/start", loadOnStartup = 1)
public class ConnectTDB extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static Dataset dataset;
    private static final Logger logger = Logger.getLogger(ConnectTDB.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConnectTDB() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static <T extends JsonBeans> T readWrite(Model model, String id, Class<T> T,
            final Class<?>... secondaryClasses) throws MissingAnnotation {
        EntityManagerImpl em = (EntityManagerImpl) EntityManagerFactory.getEntityManager();
        T pa = em.read(em.addInstanceProperties(
                model.getResource(em.getSubject(T).namespace() + id),
                T),
                T);
        return pa;
    }

    public static <T extends JsonBeans> T read(Model model, String id, Class<T> T,
            final Class<?>... secondaryClasses) throws MissingAnnotation {
        EntityManagerImpl em = (EntityManagerImpl) EntityManagerFactory.getEntityManager();
        T pa = em.read(
                model.getResource(em.getSubject(T).namespace() + id),
                T);
        return pa;
    }

    public static Literal getUriFromId(String id, Class T) {
        EntityManagerImpl em = (EntityManagerImpl) EntityManagerFactory.getEntityManager();
        return ResourceFactory.createTypedLiteral(em.getSubject(T).namespace() + id);
    }

    public static String getSparqlResultAsJson(String sparqlQuery) {
        logger.debug(String.format("REQUETE SPARQL : %s", sparqlQuery));
        ResultSet results = null;
        QueryExecution qexec = null;
        dataset.begin(ReadWrite.READ);

        try {
            Query query = QueryFactory.create(sparqlQuery);
            qexec = QueryExecutionFactory.create(query, ConnectTDB.dataset);
            results = qexec.execSelect();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ResultSetFormatter.outputAsJSON(outStream, results);
            qexec.close();
            return outStream.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {

            dataset.end();
        }
    }

    public static String getSparqlResultAsJsonFromReasoner(String sparqlQuery, OntModel om) {
        logger.debug(String.format("REQUETE SPARQL : %s", sparqlQuery));
        ResultSet results = null;
        QueryExecution qexec = null;
        dataset.begin(ReadWrite.READ);
        OntModel ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC,ModelFactory.createUnion(ModelFactory.createDefaultModel().read( "/app/mst/ontologies/int/mst.owl" ), dataset.getDefaultModel()));
 OutputStream outputStream;
        try {
            outputStream = new FileOutputStream("/tmp/testOnt.xml");
            ontModel.write(outputStream, "RDF/XML");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
             Query query = QueryFactory.create(sparqlQuery);
            qexec = QueryExecutionFactory.create(query, ontModel );
            results = qexec.execSelect();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ResultSetFormatter.outputAsJSON(outStream, results);
            qexec.close();
            return outStream.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {

            dataset.end();
        }
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        // proxy
        Properties prop = MstUtils.readMstConfig();
        System.setProperty("http.proxyHost", prop.getProperty("http.proxyHost"));
        System.setProperty("http.proxyPort", prop.getProperty("http.proxyPort"));
        // init triple store the first time
        dataset = DatasetFactory.assemble("/app/mst/config/text-config.ttl", "http://localhost/jena_example/#text_dataset");
        logger.error("FIN CREATION TDB");
        // Dataset dataset1 = TDBFactory.createDataset(prop.getProperty("tdb.directory"));

        //    EntityDefinition entDef = new EntityDefinition("uri", "text", RDFS.label.asNode()) ;
        // Lucene, in memory.
        //  Directory dir =  null;
//        try {
//            dir = FSDirectory.open(new File("/app/mst/lucene"));
//        } catch (IOException ex) {
//            java.util.logging.Logger.getLogger(ConnectTDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Directory dir = null;
//        try {
//            dir = FSDirectory.open(new File("/app/mst/lucene"));
//        } catch (IOException ex) {
//          logger.info("avance-1");
//        }
        // Join together into a dataset
        // dataset = TextDatasetFactory.createLucene(dataset1, dir, entDef,null) ;
        //  dataset = DatasetFactory.assemble("/app/mst/config/text-config.ttl", "http://localhost/jena_example/#text_dataset");
        //  dataset.end();
//        dataset.begin(ReadWrite.WRITE);
//        try {
//            GraphStore graphStore = GraphStoreFactory.create(dataset);
//            dataset.commit();
//        } finally {
//            dataset.end();
//        }
        //  ReasonerStudentClassificationService rs = new ReasonerStudentClassificationService("admin");
        //  rs.GetResults();
        //     ReasonerExerciceToDo a = new ReasonerExerciceToDo("admin");
        //   logger.error("masterNot" + a.getExerciseToDo(a.getNotMasteredNotion()));
        //begin lucene
        // dataset.begin(ReadWrite.WRITE);
        // TextQuery.init();
        //  dataset = DatasetFactory.assemble("/app/mst/config/text-config.ttl", "http://localhost/jena_example/#text_dataset");
        //  logger.error("FIN CREATION LUCENE2");
//                    //
//        Resource r = dataset.getDefaultModel().createResource(MstUtils.uid());
//        r.addProperty(RDFS.label, dataset.getDefaultModel().createLiteral("TITI"));
//        //adding new stmt logger.error("FIN CREATION LUCENE");
//         logger.error("FIN CREATION LUCENE3");
//       Resource r2 = dataset.getDefaultModel().createResource(MstUtils.uid());
//        r2.addProperty(RDFS.label, dataset.getDefaultModel().createLiteral("TOTO"));
//        //
        //   dataset.commit();
        //   logger.error("FIN CREATION LUCENE");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // TODO Auto-generated method stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // TODO Auto-generated method stub
    }

}
