package com.urcpo.mst.beans;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Resource;
import com.urcpo.mst.utils.MstUtils;

import org.apache.commons.lang3.StringUtils;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Subject( namespace = "http://methodo-stats-tutor.com#", types = { "http://methodo-stats-tutor.com#Picture","http://methodo-stats-tutor.com#CourseMaterial" } )
public class Picture extends CourseMaterial implements JsonBeans {
    @NotNull
    @Predicate( impl = true )
    public String getFileName() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true )
    public void setFileName( String svg ) {
        throw new EntityManagerRequiredException();
    }
    @NotNull
    @Predicate( impl = true )
    public String getDescription() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true )
    public void setDescription( String svg ) {
        throw new EntityManagerRequiredException();
    }

    @NotNull
    @Predicate( impl = true )
    public String getPath() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true )
    public void setPath( String svg ) {
        throw new EntityManagerRequiredException();
    }




    @NotNull
    @Predicate( impl = true )
    public String getTitle() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true )
    public void setTitle( String pmid ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void addHasPublicationAnnot( PublicationAnnot publi ) {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true )
    public ArrayList<PublicationAnnot> getHasPublicationAnnot() {
        throw new EntityManagerRequiredException();
    }

    @NotNull
    @Predicate( impl = true )
    public Teacher getHasTeacher() {
        throw new EntityManagerRequiredException();

    }
    @Predicate( impl = true )
    public void setHasTeacher( Teacher usr ) {
        throw new EntityManagerRequiredException();

    }

    @NotNull
    @Override
    @Predicate( impl = true )
    public XSDDateTime getD8Add() {
        throw new EntityManagerRequiredException();

    }
    @Predicate( impl = true )
    public void setD8Add( XSDDateTime ens ) {
        throw new EntityManagerRequiredException();

    }


    @Override
    @Predicate( impl = true )
    public Resource getResource() {
        throw new EntityManagerRequiredException();

    }

    public String getUid() {
        return getResource().toString().replaceAll( "^.*#", "" );
    }

    @Override
    public String toJson() {

        Map<String, Object> m = new HashMap();
        m.put( "fileName", MstUtils.quote( getFileName() ) );
        m.put( "title", MstUtils.quote( getTitle() ) );
        m.put( "description", MstUtils.quote( getDescription() ) );
        m.put( "path", MstUtils.quote( getPath() ) );
        m.put( "hasTeacherFname", MstUtils.quote( getHasTeacher().getPrenom() ) );
        m.put( "hasTeacherLname", MstUtils.quote( getHasTeacher().getNom() ) );
        m.put( "publiannot", MstUtils.joinArray( getHasPublicationAnnot() ) );
        return MstUtils.joinMap( m );
    }

}
