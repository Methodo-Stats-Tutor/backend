package com.urcpo.mst.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.impl.EntityManagerImpl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.urcpo.mst.servlets.ConnectTDB;
import com.urcpo.mst.utils.MstUtils;
import com.urcpo.mst.webservices.CourseMaterialRestService;
import com.urcpo.mst.beans.PubliZone;
import com.urcpo.mst.beans.Publication;
import com.urcpo.mst.beans.PublicationAnnot;
import com.urcpo.mst.beans.Publications;
import com.urcpo.mst.beans.Tag;
import com.urcpo.mst.beans.Teacher;
import com.urcpo.mst.beans.User;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public abstract class ReasonerService {
    private static final Logger log = Logger.getLogger( ReasonerService.class );
    protected Model             subModel;
    protected OntModel          ontModel;

    public abstract void createSubModel();

    public abstract void reasonOverSubModel();

    public abstract ResultSet GetResults();

    protected void mergeModelsInSubModel( Model... mods ) {
        subModel = ModelFactory.createDefaultModel().read( "/app/mst/ontologies/int/mst.owl" );
        int i = 0;
        for ( Model mod : mods ) {
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream( "/tmp/test"+i +".xml" );
                mod.write( outputStream, "RDF/XML" );

            } catch ( FileNotFoundException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            subModel = ModelFactory.createUnion( subModel, mod );
            i++;
        }
    }

    protected void logQueryResult( ResultSet result ) {
        log.debug( ResultSetFormatter.asText( result ) );
    }

}