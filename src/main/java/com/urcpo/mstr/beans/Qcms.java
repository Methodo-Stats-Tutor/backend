package com.urcpo.mstr.beans;

import org.joda.time.DateTime;
import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.urcpo.mstr.utils.MstrUtils;

import java.util.ArrayList;

@Subject( namespace = "http://methodo-stat-tutor.com#", types = { "http://methodo-stat-tutor.com#Qcm" } )
public class Qcms extends Exercice implements JsonBeans {

    @Predicate( impl = true )
    public ArrayList<Qcm> getHasQcm() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void addHasQcm( Qcm q ) {
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
        return MstrUtils.joinArray( getHasQcm() );
    }

}
