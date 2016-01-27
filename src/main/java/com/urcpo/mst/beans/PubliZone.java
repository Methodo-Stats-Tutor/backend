package com.urcpo.mst.beans;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Resource;

import org.apache.commons.lang3.StringUtils;

import com.urcpo.mst.utils.MstUtils;

@Subject( namespace = "http://methodo-stats-tutor.com#", types = { "http://methodo-stats-tutor.com#PubliZone" } )
public class PubliZone implements JsonBeans {

    @Predicate( impl = true ,emptyIsNull=true )
    public void setRefersPubliAnnot( PublicationAnnot ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public PublicationAnnot getRefersPubliAnnot() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    public String getColor() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void setColor( String nom ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    public String getLabel() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void setLabel( String nom ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    public String getId() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void setId( String nom ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public ArrayList<Tag> getHasTag() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void removeHasTag() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public boolean hasHasTag() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void addHasTag(Tag tag) {
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

    public String toJson() {
        Map<String, Object> m = new HashMap();
        m.put( "uid", MstUtils.quote( getUid() ) );
        m.put( "id", MstUtils.quote( getId() ) );
        m.put( "label", MstUtils.quote( getLabel() ) );
        m.put( "color", MstUtils.quote( getColor() ) );
        m.put( "publiannotuid", MstUtils.quote( getRefersPubliAnnot().getUid() ) );
      //  m.put( "publititle", MstUtils.quote(getRefersPubliAnnot().getAnnotsPublication().getTitle()) );
        m.put( "tags", MstUtils.joinArray( getHasTag() ) );
        return MstUtils.joinMap( m );
    }

}
