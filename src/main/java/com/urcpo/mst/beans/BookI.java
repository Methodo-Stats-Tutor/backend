package com.urcpo.mst.beans;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import com.hp.hpl.jena.util.iterator.ExtendedIterator;


@Subject( namespace="http://example.com/PA4RDF#" )
public interface BookI {

    @Predicate
    void setTitle( String title );
    String getTitle();

    @Predicate
    void addAuthor( String author );
    ExtendedIterator<String> getAuthor();

    @Predicate
    void setPageCount( Integer inta );
    boolean hasPageCount();
    int getPageCount();
}