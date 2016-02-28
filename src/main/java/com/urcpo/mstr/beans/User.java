package com.urcpo.mstr.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Resource;
import com.urcpo.mstr.utils.MstrUtils;

@Subject( namespace = "http://methodo-stat-tutor.com#", types = { "http://methodo-stat-tutor.com#User" } )
public class User implements JsonBeans {

    @Predicate( impl = true )
    public void addHasPubliAnnot( PublicationAnnot qcmTry ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public ArrayList<PublicationAnnot> getHasPubliAnnot() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void addHasQcmTry( QcmTry qcmTry ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public ArrayList<QcmTry> getHasQcmTry() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setNom( String title ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public String getNom() {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true, emptyIsNull = true )
    public void setPrenom( String title ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true, emptyIsNull = true )
    public String getPrenom() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setMel( String title ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public String getMel() {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public void setConnect( Boolean b ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public Boolean isConnect() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setPwd( String pwd ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public String getPwd() {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public void setD8Add( XSDDateTime xsdDateTime ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public XSDDateTime getD8Add() {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public void addD8Connexion( XSDDateTime xsdDateTime ) {
        throw new EntityManagerRequiredException();

    }



    @Predicate( impl = true )
    public Resource getResource() {
        throw new EntityManagerRequiredException();

    }

    public String getUid() {
        return getResource().toString().replaceAll( "^.*#", "" );
    }

    public String toJson() {
        Map<String, Object> m = new HashMap();
        m.put("uid", MstrUtils.quote( getUid() ) );
        m.put("fName", MstrUtils.quote( getPrenom() ) );
        m.put("lName", MstrUtils.quote( getNom() ) );
        m.put("d8Add", MstrUtils.quoteDate( getD8Add() ) );
        return MstrUtils.joinMap( m );
    }

}
