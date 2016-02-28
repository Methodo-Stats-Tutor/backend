package com.urcpo.mstr.beans;

import org.joda.time.DateTime;
import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.urcpo.mstr.utils.MstrUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

@Subject( namespace = "http://methodo-stat-tutor.com#", types = { "http://methodo-stat-tutor.com#ChoiceZone",
        "http://methodo-stat-tutor.com#Choice" } )
public class ChoiceZone extends Choice implements JsonBeans {

    @Predicate( impl = true ,emptyIsNull=true )
    public void setGameOver( boolean b ) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true ,emptyIsNull=true )

    public void setGameOver( Boolean ens ) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    public Boolean getGameOver() {
        throw new EntityManagerRequiredException();
    }
    
  

    @Predicate( impl = true ,emptyIsNull=true )
    public void setHasPubliZone( PubliZone a ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public PubliZone getHasPubliZone() {
        throw new EntityManagerRequiredException();
    }


    @Override
    @Predicate( impl = true ,emptyIsNull=true )
    public void setType( String ens ) {
        throw new EntityManagerRequiredException();
    }



    @Override
    @Predicate( impl = true ,emptyIsNull=true )
    public void setValue( Boolean ens ) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true ,emptyIsNull=true )
    @NotNull
    public Boolean getValue() {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true ,emptyIsNull=true )
    public void setHelp( String ens ) {
        throw new EntityManagerRequiredException();

    }

    @Override
    @Predicate( impl = true ,emptyIsNull=true )
    public String getHelp() {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void setCorrection( String ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public String getCorrection() {
        throw new EntityManagerRequiredException();
    }

    @Override
    public String toJson() {
        Map<String, Object> m = new HashMap();
        m.put("uid", MstrUtils.quote( getUid() ) );
        m.put("help", MstrUtils.quote( getHelp() ) );
        m.put( "publizone", getHasPubliZone().toJson() );
        m.put("refersCourseMaterialUid",
                MstrUtils.quote( getHasPubliZone().getRefersPubliAnnot().getUid() ) );
        return MstrUtils.joinMap( m );

    }

}
