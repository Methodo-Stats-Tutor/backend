package com.urcpo.mstr.beans;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Resource;
import com.urcpo.mstr.utils.MstrUtils;

import org.apache.commons.lang3.StringUtils;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Subject( namespace = "http://methodo-stat-tutor.com#", types = { "http://methodo-stat-tutor.com#Publication","http://methodo-stat-tutor.com#CourseMaterial" } )
public class Publication extends CourseMaterial implements JsonBeans {
    @NotNull
    @Predicate( impl = true ,emptyIsNull=true)
    public String getFileName() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true ,emptyIsNull=true)
    public void setFileName( String svg ) {
        throw new EntityManagerRequiredException();
    }

    @NotNull
    @Predicate( impl = true,emptyIsNull=true )
    public String getPath() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true ,emptyIsNull=true)
    public void setPath( String svg ) {
        throw new EntityManagerRequiredException();
    }
  

    @NotNull
    @Predicate( impl = true ,emptyIsNull=true)
    public String getPmid() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true ,emptyIsNull=true)
    public void setPmid( String pmid ) {
        throw new EntityManagerRequiredException();
    }

    @NotNull
    @Size( min = 8 )
    @Predicate( impl = true ,emptyIsNull=true)
    public String getAbstract() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true , emptyIsNull=true)
    public void setAbstract( String pmid ) {
        throw new EntityManagerRequiredException();
    }


    @NotNull
    @Predicate( impl = true ,emptyIsNull=true)
    public String getJournal() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true ,emptyIsNull=true)
    public void setJournal( String pmid ) {
        throw new EntityManagerRequiredException();
    }


    @NotNull
    @Predicate( impl = true ,emptyIsNull=true)
    public String getTitle() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true ,emptyIsNull=true)
    public void setTitle( String pmid ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true)
    public void addHasPublicationAnnot( PublicationAnnot publi ) {
        throw new EntityManagerRequiredException();
    }
    
    @Predicate( impl = true ,emptyIsNull=true)
    public ArrayList<PublicationAnnot> getHasPublicationAnnot() {
        throw new EntityManagerRequiredException();
    }
    
    @NotNull
    @Predicate( impl = true ,emptyIsNull=true)
    public String getAuthor() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true ,emptyIsNull=true)
    public void setAuthor( String ens ) {
        throw new EntityManagerRequiredException();
    }

    @NotNull
    @Predicate( impl = true ,emptyIsNull=true)
    public Teacher getHasTeacher() {
        throw new EntityManagerRequiredException();

    }
    @Predicate( impl = true ,emptyIsNull=true)
    public void setHasTeacher( Teacher usr ) {
        throw new EntityManagerRequiredException();

    }



    @NotNull
    @Override
    @Predicate( impl = true ,emptyIsNull=true)
    public XSDDateTime getD8Add() {
        throw new EntityManagerRequiredException();

    }
    @Predicate( impl = true ,emptyIsNull=true)
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
        m.put("pmid", MstrUtils.quote( getPmid() ) );
        m.put("fileName", MstrUtils.quote( getFileName() ) );
        m.put("title", MstrUtils.quote( getTitle() ) );
        m.put("author", MstrUtils.quote( getAuthor() ) );
        m.put("journal", MstrUtils.quote( getJournal() ) );
        m.put("abstract", MstrUtils.quote( getAbstract() ) );
        m.put("path", MstrUtils.quote( getPath() ) );
        m.put("hasTeacherFname", MstrUtils.quote( getHasTeacher().getPrenom() ) );
        m.put("hasTeacherLname", MstrUtils.quote( getHasTeacher().getNom() ) );
        m.put("publiannot", MstrUtils.joinArray( getHasPublicationAnnot() ) );
        return MstrUtils.joinMap( m );
    }

}
