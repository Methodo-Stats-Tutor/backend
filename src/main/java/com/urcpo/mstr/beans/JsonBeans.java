package com.urcpo.mstr.beans;

import java.lang.StringBuilder;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.urcpo.mstr.utils.MstrUtils;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
@Subject( namespace="http://example.com/PA4RDF#" )
public interface JsonBeans extends ResourceWrapper{

    public String toJson() ;
    public String getUid();
    
}
