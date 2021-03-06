package com.urcpo.mstr.servlets;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.impl.EntityManagerImpl;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.urcpo.mstr.beans.Publications;
import com.urcpo.mstr.beans.Qcms;
import com.urcpo.mstr.utils.MstrUtils;
import org.mindswap.pellet.jena.PelletReasonerFactory;

/**
 * Servlet implementation class connectTDB
 */
@WebServlet(value = "/loadonto", loadOnStartup = 1)
public class LoadOnto extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static OntModel ontologie;
    public static Dataset dataset;
    private static final Logger logger = Logger.getLogger(LoadOnto.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadOnto() {
        super(); // TODO Auto-generated constructor stub
    }

    public static void mergeOntos() {
        Properties prop = MstrUtils.readMstConfig();
//        String[] ontos = prop.getProperty("onto.int").split(";");
        ontologie = ModelFactory.createOntologyModel();
          ontologie.add(FileManager.get().loadModel(prop.getProperty("onto.directory") + prop.getProperty("onto.int")));
//        for (String onto : ontos) {
//            String fich = prop.getProperty("onto.directory") + onto;
//            ontologie.add(FileManager.get().loadModel(fich));
//        }
    }

    /**
     * @see HttpServlet#HttpServlet()
     */
    public void init() throws ServletException {
        logger.debug("initialisation");
        FileOutputStream out = null;
        mergeOntos();
        logger.info("Fin initialisation");
        logger.info("Début création publications");
        Publications publis = null;
        Qcms qcms = null;

        EntityManagerImpl em = (EntityManagerImpl) EntityManagerFactory.getEntityManager();

        ConnectTDB.dataset.begin(ReadWrite.WRITE);
        try {
            publis = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), "publications", Publications.class);
            qcms = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), "qcms", Qcms.class);
            ConnectTDB.dataset.commit();
        } catch (MissingAnnotation e) {
        } finally {
            ConnectTDB.dataset.end();
        }
    }

    public static String getSparqlResultAsJson(String sparqlQuery) {
        logger.debug(String.format("REQUETE SPARQL : %s", sparqlQuery));
        ResultSet results = null;
        QueryExecution qexec = null;
        // Dataset dt = ontologie.g
        // dataset.begin( ReadWrite.READ );

        try {
            Query query = QueryFactory.create(sparqlQuery);

            qexec = QueryExecutionFactory.create(query, ontologie.getBaseModel());
            results = qexec.execSelect();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ResultSetFormatter.outputAsJSON(outStream, results);
            qexec.close();
            return outStream.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {

            // dataset.end();
        }
    }

//transforme un model en ontmodel
    public static OntModel reasonOverModel(OntModel om) {
        OntModel ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, om);
        return ontModel;
    }

    public void destroy() {
        ontologie.close();
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
