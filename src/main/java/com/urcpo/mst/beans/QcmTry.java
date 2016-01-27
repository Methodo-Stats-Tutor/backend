package com.urcpo.mst.beans;

import java.util.ArrayList;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Resource;
import com.urcpo.mst.utils.MstUtils;

@Subject( namespace = "http://methodo-stats-tutor.com#", types = { "http://methodo-stats-tutor.com#QcmTry" } )
public class QcmTry implements JsonBeans {

    @Predicate( impl = true )
    public Qcm getRefersQcm() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setRefersQcm( Qcm c ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setJson( String ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public String getJson() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setMark( double d ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public double getMark() {
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
    public void setD8Last( XSDDateTime ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public XSDDateTime getD8Last() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setFinished( Boolean ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public Boolean getFinished() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true )
    public void setUser( User usr ) {
        throw new EntityManagerRequiredException();
    }
    
    @Override
    @Predicate( impl = true )
    public Resource getResource() {
        throw new EntityManagerRequiredException();

    }
    public String getUid(){
        return getResource().toString().replaceAll("^.*#","");
    }
    @Predicate( impl = true )
    public User getUser() {
        throw new EntityManagerRequiredException();
    }
    
    @Override
    public String toJson() {

        return null;
        // + "\"author\" : \"" + getAuthor() +
    }
}
