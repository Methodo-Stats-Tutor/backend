package com.urcpo.mstr.reasoner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.urcpo.mstr.servlets.LoadOnto;

public abstract class ReasonerService {
    private static final Logger log = Logger.getLogger( ReasonerService.class );
    protected Model             subModel;
    protected OntModel          ontModel;

    public abstract void createSubModel();

    public abstract void reasonOverSubModel();

    public abstract void processResults();

    protected void mergeModelsInSubModel( Model... mods ) {
        subModel = LoadOnto.ontologie;
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