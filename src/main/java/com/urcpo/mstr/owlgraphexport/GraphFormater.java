package com.urcpo.mstr.owlgraphexport;


import com.urcpo.mstr.owlgraphexport.beans.Diagram;



public class GraphFormater {

    public static String toCystoscape( final Diagram dia ) {
        return dia.toCystoscape();
    }

//    public static String toCytoscape( final Diagram dia ) {
//    
//        String res = "";
//        ObjectMapper mapper = new ObjectMapper();
//        //mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
//       // Map<String, Diagram> elements = Collections.singletonMap("elements", dia);
//        try {
//            res = mapper.defaultPrettyPrintingWriter().writeValueAsString( dia );
//        } catch ( IOException e ) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return res;
//    }
}
