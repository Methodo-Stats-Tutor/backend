package com.urcpo.mstr.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Resource;
import com.urcpo.mstr.utils.MstrUtils;

@Subject( namespace = "http://methodo-stat-tutor.com#", types = { "http://methodo-stat-tutor.com#Admin",
        "http://methodo-stat-tutor.com#User", "http://methodo-stat-tutor.com#Teacher" } )
public class Admin extends User implements JsonBeans {

    @Override
    @Predicate( impl = true )
    public void setNom( String title ) {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true )
    public String getNom() {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true )
    public void setPrenom( String title ) {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true )
    public String getPrenom() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true )
    public void setMel( String title ) {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true )
    public String getMel() {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true )
    public void setConnect( Boolean b ) {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true )
    public Boolean isConnect() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true )
    public void setPwd( String pwd ) {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true )
    public String getPwd() {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true )
    public void setD8Add( XSDDateTime xsdDateTime ) {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true )
    public XSDDateTime getD8Add() {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true )
    public void addD8Connexion( XSDDateTime xsdDateTime ) {
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



}
