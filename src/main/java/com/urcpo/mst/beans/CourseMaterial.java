package com.urcpo.mst.beans;

import org.joda.time.DateTime;
import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Subject( namespace = "http://methodo-stats-tutor.com#" )
public abstract class CourseMaterial implements JsonBeans {

    @Predicate( impl = true )
    public void setAuthor( String ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public Teacher getTeacher() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true )
    public void setTeacher(Teacher ens) {
        throw new EntityManagerRequiredException();
    }


    @Predicate( impl = true )
    public void removeAuthor( Teacher ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setD8Add( XSDDateTime ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public XSDDateTime getD8Add() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public String getAuthor() {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true, name="needNotion" )
    public ArrayList<java.net.URI> getNeedNotion() {
        throw new EntityManagerRequiredException();

    }
    
    @Predicate( impl = true )
    public void addNeedNotion(java.net.URI uri) {
        throw new EntityManagerRequiredException();

    }
    
    @Predicate( impl = true, name="needNotion" )
    public void removeNeedNotion(java.net.URI uri) {
        throw new EntityManagerRequiredException();

    }
    @Predicate( impl = true, name="giveNotion" )
    public ArrayList<java.net.URI> getGiveNotion() {
        throw new EntityManagerRequiredException();

    }
    @Predicate( impl = true )
    public void addGiveNotion(java.net.URI uri) {
        throw new EntityManagerRequiredException();

    }
    
    @Predicate( impl = true, name="giveNotion" )
    public void removeGiveNotion(java.net.URI uri) {
        throw new EntityManagerRequiredException();

    }

}