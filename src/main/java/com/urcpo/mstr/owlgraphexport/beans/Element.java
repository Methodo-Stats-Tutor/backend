package com.urcpo.mstr.owlgraphexport.beans;

import java.util.Hashtable;

public abstract class Element {
    private Hashtable<String, String> props = new Hashtable<String, String>();

    public Hashtable<String, String> getProps() {
        return props;
    }

    public void setProp( String key, String value ) {
        this.props.put( key, value );
    }

    abstract String toCystoscape();
    
    protected String propsToCystoscape(){
        return ", \"grabbable\": true, \"classes\":\"test\" ";
        
    }
}
