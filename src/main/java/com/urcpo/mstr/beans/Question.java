package com.urcpo.mstr.beans;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.rdf.model.Resource;

import java.util.HashMap;
import java.util.Map;

import com.urcpo.mstr.utils.MstrUtils;

import java.util.ArrayList;

import javax.validation.Valid;

@Subject( namespace = "http://methodo-stat-tutor.com#", types = { "http://methodo-stat-tutor.com#Question" } )
public class Question implements JsonBeans {
    @Predicate( impl = true ,emptyIsNull=true )
    public ArrayList<PublicationAnnot> getRefersToPubliAnnot() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void addRefersToPubliAnnot( PublicationAnnot c ) {
        throw new EntityManagerRequiredException();
    }


    @Predicate( impl = true ,emptyIsNull=true )
    public void setDifficulty( Integer ens ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void setTimeLimit( Integer asInt ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public Integer getTimeLimit() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public Integer getDifficulty() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void setRank( Integer ens ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true ,emptyIsNull=true )
    public Integer getRank() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true , emptyIsNull=true )
    public void setStatement( String ens ) {
        throw new EntityManagerRequiredException();

    }

    @Predicate( impl = true ,emptyIsNull=true )
    public String getStatement() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    @Valid
    public ArrayList<Tag> getHasTag() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public boolean addHasTag() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    @Valid
    public ArrayList<Choice> getHasChoice() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    @Valid
    public ArrayList<ChoiceZone> getHasChoiceZone() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    @Valid
    public ArrayList<ChoiceText> getHasChoiceText() {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void addHasChoiceZone( ChoiceZone t ) {
        throw new EntityManagerRequiredException();
    }

    @Predicate( impl = true ,emptyIsNull=true )
    public void addHasChoiceText( ChoiceText t ) {
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

    @Override
    public String toJson() {
        Map<String, Object> m = new HashMap();
        m.put("uid", MstrUtils.quote( getUid() ) );
        m.put( "rank", getRank() );
        m.put("statement", MstrUtils.quote( getStatement() ) );
        m.put( "difficulty", getDifficulty() );
        m.put( "timelimit", getTimeLimit() );
        m.put("choicezone", MstrUtils.joinArray( getHasChoiceZone() ) );
        m.put("choicetext", MstrUtils.joinArray( getHasChoiceText() ) );
        m.put("tags", MstrUtils.joinArray( getHasTag() ) );
        return MstrUtils.joinMap( m );

    }

}
