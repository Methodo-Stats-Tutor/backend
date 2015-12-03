package com.urcpo.mst.beans;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.urcpo.mst.utils.MstUtils;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Resource;

@Subject( namespace = "http://methodo-stats-tutor.com#", types = { "http://methodo-stats-tutor.com#PublicationAnnot" } )
public class PublicationAnnot extends CourseMaterial implements JsonBeans {

    @Predicate( impl = true, emptyIsNull = true )
    public String setAnnotsPublication( Publication p ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    
    public Publication getAnnotsPublication() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public String setRefersCourseMaterial( CourseMaterial p ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    @NotNull
    public Publication getRefersCourseMaterial() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    @NotNull
    public String getNom() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public void setHasTeacher( Teacher nom ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    @NotNull
    public Teacher getHasTeacher() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public void setNom( String nom ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    @Valid
    public ArrayList<PubliZone> getHasPubliZone() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public void removeHasPubliZone( PubliZone P ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public boolean hasHasPubliZone( PubliZone P ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public boolean addHasPubliZone( PubliZone p ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public String getAnnotoriousJson() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public void setAnnotoriousJson( String nom ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public void setJson( String courseMaterialAnnotJson ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    @NotNull
    public String getJson() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public String getAnnotatorJson() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public void setAnnotatorJson( String nom ) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true, emptyIsNull = true )
    public Resource getResource() {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true, emptyIsNull = true )
    public void setFinished( boolean b ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true, emptyIsNull = true )
    public void setD8Last( XSDDateTime now ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true, emptyIsNull = true )
    public XSDDateTime getD8Last() {
        throw new EntityManagerRequiredException();
    }

    public String getUid() {
        return getResource().toString().replaceAll( "^.*#", "" );
    }

    @Override
    public String toJson() {
        Map<String, Object> m = new HashMap();
        m.put( "uid", MstUtils.quote( getUid() ) );
        m.put( "nom", MstUtils.quote( getNom() ) );
        m.put( "json", MstUtils.quote( getJson() ) );
        m.put( "d8Last", MstUtils.quoteDate( getD8Last() ) );
        m.put( "courseMaterialTitle", MstUtils.quote( getRefersCourseMaterial().getTitle() ) );
        m.put( "annotoriousJson", MstUtils.quote( getAnnotoriousJson() ) );
        m.put( "annotatorJson", MstUtils.quote( getAnnotatorJson() ) );
        m.put( "path", MstUtils.quote( getRefersCourseMaterial().getPath() ) );
        m.put( "courseMaterialUid", MstUtils.quote( getRefersCourseMaterial().getUid() ) );
        m.put( "publiZone", MstUtils.joinArray( getHasPubliZone() ) );
        return MstUtils.joinMap( m );
    }

}
