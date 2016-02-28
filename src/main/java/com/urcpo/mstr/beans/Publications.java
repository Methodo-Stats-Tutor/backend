package com.urcpo.mstr.beans;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import java.util.ArrayList;

import com.urcpo.mstr.utils.MstrUtils;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Resource;
@Subject( namespace="http://methodo-stat-tutor.com#", types={"http://methodo-stat-tutor.com#Publications"})
public class Publications implements JsonBeans {
    
    @Predicate( impl = true)
    public ArrayList<Publication> getHasPublication() {
        throw new EntityManagerRequiredException();
    }
    
    @Predicate( impl = true)
    public void addHasPublication( Publication publi ) {
        throw new EntityManagerRequiredException();
    }


    @Override
    @Predicate( impl = true )
    public Resource getResource() {
        throw new EntityManagerRequiredException();

    }
    public String getUid(){
        return getResource().toString().replaceAll("^.*#","");
    }
    @Override
    public String toJson(){
        return MstrUtils.joinArray(getHasPublication());
    }
}
