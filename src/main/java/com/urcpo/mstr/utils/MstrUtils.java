package com.urcpo.mstr.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.urcpo.mstr.beans.JsonBeans;

public class MstrUtils {
    private static final Logger log            = Logger.getLogger(MstrUtils.class );

    public static final String  REP_FILES      = "/app/mstr/files/";
    public static final String  REP_SAVE_FILE  = "mstr_files/";
    public static final String  REP_SAVE_PUBLI = "publi/";

    public static String formatLog( Object e ) {

        String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String className = fullClassName.substring( fullClassName.lastIndexOf( "." ) + 1 );
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

        return String.format( "%s.%s() : %s, %s", className, methodName, lineNumber, e );
    }

    public static String uid() {
        return "id" + UUID.randomUUID().toString();
    }

    public static XSDDateTime now() {
        return new XSDDateTime( GregorianCalendar.getInstance() );
    }

    public static void validatePojo( JsonBeans test ) throws Exception {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<JsonBeans>> constraintViolations = validator.validate( test );
        Gson gson = new Gson();

        JsonObject dataset = new JsonObject();

        if ( constraintViolations.size() > 0 ) {
            for ( ConstraintViolation<JsonBeans> contraintes : constraintViolations ) {
                dataset.addProperty( contraintes.getPropertyPath().toString(),
                        contraintes.getMessage() );
            }
            log.error( dataset.toString() );
            throw new Exception( dataset.toString() );
        }

    }

    public static String strExtract( String str, String regexp ) {
        String res = null;
        Pattern p = Pattern.compile( regexp );
        Matcher m = p.matcher( str );
        if ( m.find() ) {
            res = m.group( 1 );
        }
        return res;
    }

    public static Boolean contains( String pat, String str ) {
        return Pattern.compile( pat ).matcher( str ).find();
    }

    public static String joinArray( ArrayList<? extends JsonBeans> list ) {
        try {
            StringBuilder out = new StringBuilder();
            out.append( "[" );
            for ( JsonBeans o : list )
            {
                log.error( o.toJson() );
                if ( out.length() > 1 )
                    out.append( "," );
                out.append( o.toJson() );
            }
            out.append( "]" );
            return out.toString();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( list.toString() + e.getMessage() );
            return null;
        }
    }

    public static String quote( String str ) {
        if ( str == null )
            return null;
        return "\"" + str.trim().replaceAll( "\"", "\\\\\"" ) + "\"";
    }

    public static String quoteDate( XSDDateTime dt ) {
        if ( dt == null )
            return null;
        // SimpleDateFormat format1 = new
        // SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
        return "\"" + sdf.format( dt.asCalendar().getTimeInMillis() ) + "\"";
    }

    public static String joinMap( Map<String, ? extends Object> mp ) {
        try {
            Iterator it = mp.entrySet().iterator();
            StringBuilder out = new StringBuilder();
            out.append( "{" );
            while ( it.hasNext() ) {
                Map.Entry pair = (Map.Entry) it.next();
                if ( pair.getValue() != null ) {
                    if ( out.length() > 1 ) {
                        out.append( "," );
                    }
                    out.append( "\"" );
                    out.append( pair.getKey() );
                    out.append( "\"" );
                    out.append( " : " );
                    out.append( pair.getValue() );
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
            out.append( "}" );
            return out.toString();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( mp.toString() );
            return null;
        }
    }

    public static String joinLookup( ArrayList<? extends JsonBeans> list ) {
        try {
            StringBuilder out = new StringBuilder();
            out.append( "{" );
            for ( JsonBeans o : list )
            {
                if ( out.length() > 1 )
                    out.append( "," );
                out.append( "\"" ).append( o.getUid() ).append( "\"" );
                out.append( ":" );
                out.append( o.toJson() );
            }
            out.append( "}" );
            return out.toString();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            log.error( list.toString() );
            return null;
        }
    }

    public static String readFile( String path, Charset encoding )
            throws IOException
    {
        byte[] encoded = Files.readAllBytes( Paths.get( path ) );
        return new String( encoded, encoding );
    }

    public static Properties readMstConfig()
    {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream( "/app/mstr/config/mstr.properties" );
        } catch ( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            prop.load( input );
        } catch ( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return prop;
    }

    public static String getSparqlResultsetAsJson( ResultSet results ) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ResultSetFormatter.outputAsJSON( outStream, results );
            return outStream.toString();
        } catch ( Exception e ) {
            return null;
        } finally {
        }
    }
}
