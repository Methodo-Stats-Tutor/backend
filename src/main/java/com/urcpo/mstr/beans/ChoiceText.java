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

@Subject( namespace = "http://methodo-stat-tutor.com#", types = { "http://methodo-stat-tutor.com#ChoiceText",
        "http://methodo-stat-tutor.com#Choice" } )
public class ChoiceText extends Choice implements JsonBeans {
    @Predicate( impl = true )
    public void setGameOver( boolean b ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setLabel( String a ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    @NotNull
    public String getLabel() {
        throw new EntityManagerRequiredException();
    }


    @Override
    @Predicate( impl = true )
    public void setValue( Boolean ens ) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @NotNull
    @Predicate( impl = true )
    public Boolean getValue() {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true )
    public void setHelp( String ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public String getHelp() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public void setCorrection( String ens ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true )
    public String getCorrection() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true )
    public void setGameOver( Boolean ens ) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate( impl = true )
    @NotNull
    public Boolean getGameOver() {
        throw new EntityManagerRequiredException();
    }

    @Override
    public String toJson() {
        Map<String, Object> m = new HashMap();
        m.put("uid", MstrUtils.quote( getUid() ) );
        m.put("label", MstrUtils.quote( getLabel() ) );
        m.put("help", MstrUtils.quote( getHelp() ) );
        return MstrUtils.joinMap( m );
    }

}
