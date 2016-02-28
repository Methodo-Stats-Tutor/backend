package com.urcpo.mstr.beans;

import org.joda.time.DateTime;
import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.urcpo.mstr.utils.MstrUtils;

import java.util.ArrayList;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Subject( namespace = "http://methodo-stat-tutor.com#", types = { "http://methodo-stat-tutor.com#Qcm","http://methodo-stat-tutor.com#Exercice" } )
public class Qcm extends Exercice implements JsonBeans {

    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    @Valid
    public ArrayList<Question> getHasQuestion() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void addHasQuestion( Question c ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void setDifficulty( Integer ens ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    @Min(1)
    @Max(5)
    public Integer getDifficulty() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void setName( String ens ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    public String getName() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true ,emptyIsNull=true )
    public void setTeacher( Teacher ens ) {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true ,emptyIsNull=true )
    public void removeAuthor( Teacher ens ) {
        throw new EntityManagerRequiredException();

    }

    @Override
    @NotNull
    @Predicate( impl = true ,emptyIsNull=true )
    public Teacher getTeacher() {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true ,emptyIsNull=true )
    public void setD8Add( XSDDateTime ens ) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @NotNull
    @Predicate( impl = true ,emptyIsNull=true )
    public XSDDateTime getD8Add() {
        throw new EntityManagerRequiredException();
    }


    @Predicate( impl = true ,emptyIsNull=true )
    public void setFinished( boolean b ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    public XSDDateTime getD8Last() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public XSDDateTime setD8Last( XSDDateTime a ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void setJson( String ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    public String getJson() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    public ArrayList<PublicationAnnot> getRefersToPubliAnnot() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void addRefersToPubliAnnot( PublicationAnnot c ) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true ,emptyIsNull=true )
    public Resource getResource() {
        throw new EntityManagerRequiredException();

    }

    public String getUid() {
        return getResource().toString().replaceAll( "^.*#", "" );
    }

    @Override
    public String toJson() {
        Map<String, Object> m = new HashMap();
        m.put("uid", MstrUtils.quote( getUid() ) );
        m.put("d8add", MstrUtils.quoteDate( getD8Add() ) );
        m.put( "difficulty", getDifficulty() );
        m.put("name", MstrUtils.quote(getName()) );
        m.put("teacher", MstrUtils.quote( getTeacher().getNom() + " " + getTeacher().getPrenom() ) );
        m.put("questions", MstrUtils.joinArray( getHasQuestion() ) );
        m.put("qcmJson", MstrUtils.quote( getJson() ) );
        m.put("referstopubliannot", MstrUtils.joinArray( getRefersToPubliAnnot() ) );
        return MstrUtils.joinMap( m );
    }

 

}
