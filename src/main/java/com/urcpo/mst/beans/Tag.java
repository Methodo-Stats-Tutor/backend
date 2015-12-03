package com.urcpo.mst.beans;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;

import com.urcpo.mst.utils.MstUtils;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Resource;

@Subject( namespace = "http://methodo-stats-tutor.com#", types = { "http://methodo-stats-tutor.com#Tag" } )
public class Tag implements JsonBeans {


    @Predicate( impl = true )
    public URI getRefersTo() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setRefersTo( URI nom ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setAccuracy( Integer nom ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public Integer getAccuracy() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true )
    public java.net.URI getUri() {
        throw new EntityManagerRequiredException();
    }
    @Predicate( impl = true )
    public void  setUri(java.net.URI uri) {
        throw new EntityManagerRequiredException();
    }
    public String getUid(){
        return getResource().toString().replaceAll("^.*#","");
    }
    @Override
    @Predicate( impl = true )
    public Resource getResource() {
        throw new EntityManagerRequiredException();

    }
    @Override
    public String toJson() {
       return null;
    }

}
