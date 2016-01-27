package com.urcpo.mst.beans;

import org.joda.time.DateTime;
import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.urcpo.mst.utils.MstUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

@Subject( namespace = "http://methodo-stats-tutor.com#", types = { "http://methodo-stats-tutor.com#ChoiceText",
        "http://methodo-stats-tutor.com#Choice" } )
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
        m.put( "uid", MstUtils.quote( getUid() ) );
        m.put( "label", MstUtils.quote( getLabel() ) );
        m.put( "help", MstUtils.quote( getHelp() ) );
        return MstUtils.joinMap( m );
    }

}
