package com.urcpo.mst.beans;



import org.joda.time.DateTime;
import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.rdf.model.Literal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;


@Subject( namespace="http://methodo-stats-tutor.com#", types={"http://methodo-stats-tutor.com#Exercice"} )
public abstract class Exercice extends CourseMaterial {
    @Override
    @Predicate( impl = true)
    public void setAuthor( String ens ) {
        throw new EntityManagerRequiredException();
        
    }
    
    @Override
    @Predicate( impl = true)
   public  Teacher  getTeacher() {
        throw new EntityManagerRequiredException();
   
    }

    @Override
    @Predicate( impl = true)
    public void setD8Add( XSDDateTime ens ) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true)
    public XSDDateTime getD8Add() {
        throw new EntityManagerRequiredException();
    }
    




}
