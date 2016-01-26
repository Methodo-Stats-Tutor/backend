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
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.urcpo.mst.beans.JsonBeans;
import com.urcpo.mst.services.ReasonerExerciceToDo;
import com.urcpo.mst.services.ReasonerQcmMaitriseNotion;
import com.urcpo.mst.services.ReasonerStudentClassificationService;
import com.urcpo.mst.utils.MstUtils;

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

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        // proxy
        Properties prop = MstUtils.readMstConfig();
        System.setProperty("http.proxyHost", prop.getProperty("http.proxyHost"));
        System.setProperty("http.proxyPort", prop.getProperty("http.proxyPort"));
        // init triple store the first time
        dataset = TDBFactory.createDataset(prop.getProperty("tdb.directory"));
        dataset.begin(ReadWrite.WRITE);
        try {
            GraphStore graphStore = GraphStoreFactory.create(dataset);
            dataset.commit();
        } finally {
            // dataset.end();
        }
        logger.error("FIN CREATION TDB");

      //  ReasonerStudentClassificationService rs = new ReasonerStudentClassificationService("admin");
        //  rs.GetResults();
        ReasonerExerciceToDo a = new ReasonerExerciceToDo("admin");
        logger.error("masterNot" + a.getExerciseToDo(a.getNotMasteredNotion()));
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
