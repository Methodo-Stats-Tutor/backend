package com.urcpo.mst.services;

import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xenei.jena.entities.MissingAnnotation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ReadWrite;
import com.urcpo.mst.beans.Choice;
import com.urcpo.mst.beans.ChoiceText;
import com.urcpo.mst.beans.ChoiceZone;
import com.urcpo.mst.beans.PubliZone;
import com.urcpo.mst.beans.PublicationAnnot;
import com.urcpo.mst.beans.Qcm;
import com.urcpo.mst.beans.QcmTry;
import com.urcpo.mst.beans.Qcms;
import com.urcpo.mst.beans.Question;
import com.urcpo.mst.beans.Tag;
import com.urcpo.mst.beans.Teacher;
import com.urcpo.mst.beans.User;
import com.urcpo.mst.servlets.ConnectTDB;
import com.urcpo.mst.utils.MstUtils;

public class QcmService {

    private static final Logger log = Logger.getLogger( QcmService.class );
    private String              json;

    public QcmService() {

    }

    // QUAND SAVE & firsttime, OU SAVEFINAL
    public Qcm createQcm( String json ) throws Exception {
        this.json = json;
        Qcm qcm = null;
        Gson gson = new Gson();
String qcmUid = MstUtils.uid();
        try {
            ConnectTDB.dataset.begin( ReadWrite.WRITE );
            qcm = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(),qcmUid, Qcm.class );
            JsonElement je = gson.fromJson( json, JsonElement.class );
            JsonObject jo = je.getAsJsonObject();
            log.error( "ok0" );
            qcm.setTeacher( ConnectTDB.readWrite(
                    ConnectTDB.dataset.getDefaultModel(),
                    jo.get( "userUid" ).getAsString(), Teacher.class ) );
            qcm.setName( jo.get( "name" ).getAsString() );
            qcm.setDifficulty( jo.get( "difficulty" ).getAsInt() );
            qcm.setD8Add( MstUtils.now() );
            qcm.setFinished( jo.get( "finished" ).getAsBoolean() );
            PublicationAnnot pa;
            for ( int j = 0; j < jo.get( "courseMaterialUid" ).getAsJsonArray().size(); j++ ) {
                pa = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(),
                        jo.get( "courseMaterialUid" ).getAsJsonArray().get( j ).getAsString(), PublicationAnnot.class );
                qcm.addRefersToPubliAnnot( pa );
            }
            log.error( "ok1" );

            // QUESTION
            Question quest;
            Integer rank;
            ChoiceZone cz;
            ChoiceText ct;
            JsonObject jq = jo.get( "questions" ).getAsJsonObject();
            log.error( "ok2" );

            for ( Map.Entry<String, JsonElement> entry : jq.entrySet() ) {
                rank = Integer.parseInt( entry.getKey() );
                quest = ConnectTDB.readWrite(
                        ConnectTDB.dataset.getDefaultModel(),
                        MstUtils.uid(), Question.class );
                qcm.addHasQuestion( quest );
                JsonObject jq2 = entry.getValue().getAsJsonObject();
                quest.setRank( jq2.get( "rank" ).getAsInt() );
                quest.setStatement( jq2.get( "statement" ).getAsString() );
                log.error( "ok3" );

                quest.setDifficulty( jq2.get( "difficulty" ).getAsInt() );
                if ( jq2.get( "timelimit" ) != null )
                    quest.setTimeLimit( jq2.get( "timelimit" ).getAsInt() );
                // CHOICEZONE
                String czUid;
                JsonObject jqczInfo;
                log.error( "ok4" );

                JsonObject jqcz = jq2.get( "choicezone" ).getAsJsonObject();
                log.error( jqcz.toString() );
                //if(!jqcz.toString().equals( "{}" ))
                for ( Map.Entry<String, JsonElement> entry2 : jqcz.entrySet() ) {
                    log.error( "ok41" );
                    czUid = entry2.getKey();
                    jqczInfo = entry2.getValue().getAsJsonObject();
                   
                    cz = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(),
                            MstUtils.uid(), ChoiceZone.class );
                    quest.addHasChoiceZone( cz );
                    log.error( "ok42" );
                    //cz.setValue( jqczInfo.get( "value" ).getAsBoolean() );
                    if ( jqczInfo.get( "value" ) == null ) {
                        cz.setValue( false );
                    } else {
                        cz.setValue( jqczInfo.get( "value" ).getAsBoolean() );
                    }
                    log.error( "ok43" );
                    if ( jqczInfo.get( "gameOver" ) == null ) {
                        cz.setGameOver( false );
                    } else {
                        cz.setGameOver( jqczInfo.get( "gameOver" ).getAsBoolean() );
                    }
                    log.error( "ok44" );
                    if ( jqczInfo.get( "correction" ) != null )
                        cz.setCorrection( jqczInfo.get( "correction" ).getAsString() );
                    log.error( "ok45" );
                    if ( jqczInfo.get( "help" ) != null )
                        cz.setHelp( jqczInfo.get( "help" ).getAsString() );
                    log.error( "ok46" );
                    cz.setHasPubliZone( ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(),
                            czUid, PubliZone.class ) );
                    log.error( "ok47" );

                }

                // CHOICETEXT
                String ctUid;
                JsonObject jqctInfo;
                log.error( "ok5" );

                JsonObject jqct = jq2.get( "choicetext" ).getAsJsonObject();
              //  if(!jqct.toString().equals( "{}" ))
                for ( Map.Entry<String, JsonElement> entry2 : jqct.entrySet() ) {
                    ctUid = entry2.getKey();
                    jqctInfo = entry2.getValue().getAsJsonObject();
                    ct = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(),
                            MstUtils.uid(), ChoiceText.class );
                    quest.addHasChoiceText( ct );
                    // ct.setId( Integer.parseInt( ctUid ) );
                    log.error( "ok6" );

                    if ( jqctInfo.get( "value" ) == null ) {
                        ct.setValue( false );
                    } else {
                        ct.setValue( jqctInfo.get( "value" ).getAsBoolean() );
                    }
                    if ( jqctInfo.get( "gameOver" ) == null ) {
                        ct.setGameOver( false );
                    } else {
                        ct.setGameOver( jqctInfo.get( "gameOver" ).getAsBoolean() );
                    }
                    ct.setLabel( jqctInfo.get( "label" ).getAsString() );
                    if ( jqctInfo.get( "correction" ) != null )
                        ct.setCorrection( jqctInfo.get( "correction" ).getAsString() );
                    if ( jqctInfo.get( "help" ) != null )
                        ct.setHelp( jqctInfo.get( "help" ).getAsString() );
                }
            }
            qcm.setJson( json );
            qcm.setD8Last( MstUtils.now() );
    
            MstUtils.validatePojo( qcm );

            ConnectTDB.dataset.commit();
            // FIN AJOUT PUBLI
            
          
        } catch ( Exception e )
        {
            log.error( json );
            throw new Exception( e.getMessage() );
        } finally {
            ConnectTDB.dataset.end();
        }
      //les notions en jeux
        
        ReasonerQcmMaitriseNotion rq = new ReasonerQcmMaitriseNotion( qcmUid );
        ConnectTDB.dataset.begin( ReadWrite.WRITE );
        qcm = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(),qcmUid, Qcm.class );
        int i = 0;
        while(i< rq.getGiveNotion().size()){
        qcm.addGiveNotion( rq.getGiveNotion().get( i ) );
        i++;
        }
         i = 0;
        while(i< rq.getNeedNotion().size()){
            qcm.addNeedNotion( rq.getNeedNotion().get( i ) );
            i++;
        }

        ConnectTDB.dataset.commit();
        ConnectTDB.dataset.end();


        //fin les notions en jeux 
        
        return qcm;

    }
    

    // Save, not first TIME
    public Qcm updateQcm( JsonObject json, String qcmUid
            ) throws Exception {
        Qcm qcm = null;
        ConnectTDB.dataset.begin( ReadWrite.WRITE );
        log.error( "b2" );
        try {
            log.error( "b3" );

            qcm = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(), qcmUid, Qcm.class );
            log.error( json.toString() );

            qcm.setName( json.get( "name" ).getAsString() );
            log.error( "b4" );
            qcm.setDifficulty( json.get( "difficulty" ).getAsInt() );
            qcm.setD8Last( MstUtils.now() );
            qcm.setJson( json.toString() );
            qcm.setFinished( json.get( "finished" ).getAsBoolean() );
            log.error( "b5" );
            MstUtils.validatePojo( qcm );

            ConnectTDB.dataset.commit();
            // FIN AJOUT PUBLI
            return qcm;
        } catch ( Exception e )
        {
            log.error( json );
            throw new Exception( e.getMessage() );
        } finally {
            ConnectTDB.dataset.end();
        }

    }

    public String getQcms( String userUid ) throws Exception {
        String sparqlQueryString = "PREFIX mst: <http://methodo-stats-tutor.com#>\n" +
                "SELECT * { \n" +
                "                ?qcm a mst:Qcm .\n" +
                "                ?qcm mst:name  ?name .\n" +
                "                ?qcm mst:d8Add ?d8add .\n" +
                "                OPTIONAL{?qcm mst:teacher ?teacher .\n" +
                "                ?qcm mst:d8Last ?d8Last .\n" +
                "                ?teacher mst:nom ?fname .\n" +
                "                ?teacher mst:prenom ?lname }.\n" +
                "                ?qcm mst:finished ?finished\n" +
                "                }";
        return ConnectTDB.getSparqlResultAsJson( sparqlQueryString );
    }

    public String getQcmTrys( String userUid ) throws Exception {
        String sparqlQueryString = "PREFIX mst: <http://methodo-stats-tutor.com#>\n" +
                "SELECT * { \n" +
                "                ?qcm a mst:Qcm .\n" +
                "                 ?qcm mst:name  ?name .\n" +
                "                 ?qcm mst:d8Add ?d8add .\n" +
                "                 OPTIONAL{?qcm mst:teacher ?teacher .\n" +
                "                 ?teacher mst:nom ?fname .\n" +
                "                 ?teacher mst:prenom ?lname }.\n" +
                "                 ?qcm mst:difficulty ?difficulty .\n" +
                "                  OPTIONAL{?qcmtry mst:refersQcm ?qcm .\n" +
                "                  ?qcmtry mst:finished false .\n" +
                "                ?qcmtry mst:json ?json .\n" +
                "                 ?qcmtry mst:user mst:"+ userUid +" }\n" +
                "                 }";
        return ConnectTDB.getSparqlResultAsJson( sparqlQueryString );
    }

    public String getQcm( String id ) throws Exception {
        ConnectTDB.dataset.begin( ReadWrite.READ );
        Qcm qcm = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(), id, Qcm.class );
        String res = qcm.toJson();
        ConnectTDB.dataset.end();
        return res;

    }

    public String getQcmTry( String uri ) {
        String sparqlQueryString = "SELECT * { "
                + "?qcmtry <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://methodo-stats-tutor.com#QcmTry> ."
                + " ?qcmtry <http://methodo-stats-tutor.com#refersQcm>  <http://methodo-stats-tutor.com#" + uri + "> ."
                + " ?qcmtry <http://methodo-stats-tutor.com#d8Add>  ?d8add ."
                + " ?qcmtry <http://methodo-stats-tutor.com#finished>  ?finished .  "
                + " OPTIONAL{ ?qcmtry <http://methodo-stats-tutor.com#json>  ?json } . "
                + " filter (  ?finished = false )"
                + " }";
        return ConnectTDB.getSparqlResultAsJson( sparqlQueryString );
    }

    public String createQcmTry( String qcmUid, String userUid ) throws Exception {
        ConnectTDB.dataset.begin( ReadWrite.WRITE );
        String uid = MstUtils.uid();
        QcmTry qcmTry = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(), uid, QcmTry.class );
        Qcm qcm = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(), qcmUid, Qcm.class );

        qcmTry.setRefersQcm( qcm );
        qcmTry.setFinished( false );
        qcmTry.setD8Add( MstUtils.now() );
        qcmTry.setD8Last( MstUtils.now() );
        User usr = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(), userUid, User.class );
        qcmTry.setUser( usr );
        usr.addHasQcmTry( qcmTry );
        ConnectTDB.dataset.commit();
        ConnectTDB.dataset.end();
        return uid;

    }

    public void updateQcmTry( String uriQcm, String json ) throws Exception {
        Gson gson = new Gson();
        JsonElement je = gson.fromJson( json, JsonElement.class );
        JsonObject jo = je.getAsJsonObject();
        ConnectTDB.dataset.begin( ReadWrite.WRITE );
        QcmTry qcmTry = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(), uriQcm, QcmTry.class );
        qcmTry.setD8Last( MstUtils.now() );
        if ( jo.has( "json" ) )
            qcmTry.setJson( jo.get( "json" ).getAsString() );
        if ( jo.has( "finished" ) )
            qcmTry.setFinished( true );
        if ( jo.has( "mark" ) )
            qcmTry.setMark( jo.get( "mark" ).getAsDouble() );
        ConnectTDB.dataset.commit();
        ConnectTDB.dataset.end();

    }

    private boolean questionIsGameOver( String uid ) throws MissingAnnotation {
        ConnectTDB.dataset.begin( ReadWrite.READ );
        Choice qcm = null;
        qcm = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(), uid, Choice.class );
        ConnectTDB.dataset.end();
        return qcm.getGameOver();

    }

    private boolean checkAnswer( String uid, boolean value ) throws MissingAnnotation {
        ConnectTDB.dataset.begin( ReadWrite.READ );
        log.debug( uid + " : " + "ok !" );
        Choice qcm = null;
        qcm = ConnectTDB.readWrite( ConnectTDB.dataset.getDefaultModel(), uid, Choice.class );
        log.debug( uid + " : " + qcm.getValue() );
        boolean rep;
        rep = value == qcm.getValue() ? true : false;
        log.debug( rep );
        ConnectTDB.dataset.end();
        return rep;
    }

    public String correctAnswers( String uriQcm, String answers ) throws Exception {

        Integer totAns = 0;
        Integer semiTotAns = 0;
        double note = 0;
        double semiNote = 0;
        double add = 0.0;
        boolean gameOver;
        JsonObject result = new JsonObject();
        JsonObject temp = new JsonObject();
        JsonObject temp1 = new JsonObject();
        JsonObject temp2 = new JsonObject();
        JsonObject last = new JsonObject();

        Gson gson = new Gson();
        JsonElement je = gson.fromJson( answers, JsonElement.class );
        JsonObject jo = je.getAsJsonObject();

        JsonObject res = jo.get( "result" ).getAsJsonObject();
        for ( Map.Entry<String, JsonElement> entry : res.entrySet() ) {

            String quest = entry.getKey();
            JsonObject ans = entry.getValue().getAsJsonObject().get( "answers" ).getAsJsonObject();
            temp1 = new JsonObject();
            temp2 = new JsonObject();
            semiTotAns = 0;
            semiNote = 0;
            gameOver = false;
            for ( Map.Entry<String, JsonElement> entry2 : ans.entrySet() ) {
                totAns++;
                semiTotAns++;
                temp = new JsonObject();
                JsonObject rep = entry2.getValue().getAsJsonObject();
                if ( rep.get( "help" ) != null ) {
                    add = 0.5;
                    temp.addProperty( "help", true );
                    temp.addProperty( "note", add );
                } else {
                    add = 1;
                    temp.addProperty( "help", false );
                    temp.addProperty( "note", add );
                }
                if ( checkAnswer( entry2.getKey(), rep.get( "value" ).getAsBoolean() ) ) {
                    note += add;
                    semiNote += add;
                } else {
                    gameOver = questionIsGameOver( entry2.getKey() );
                    if ( gameOver )
                        semiNote = 0;
                    note += 0;
                    semiNote += 0;
                }

                temp1.add( entry2.getKey(), temp );
                temp2.add( "detail", temp1 );
                temp2.addProperty( "semiNote", semiNote );
                temp2.addProperty( "gameOver", gameOver );
                temp2.addProperty( "semiTot", semiTotAns );
                if ( gameOver ) {
                    break;
                }

            }

            result.add( quest, temp2 );

            last.add( "result", result );
            last.addProperty( "mark", String.format( "%.2f", ( note / totAns * 20 ) ) );
        }
        // ajouter la note !
        JsonElement jea = gson.fromJson( answers, JsonElement.class );
        JsonObject joa = jea.getAsJsonObject();
        joa.addProperty( "mark", ( note / totAns * 20 ) );
        updateQcmTry( uriQcm, joa.toString() );

        return last.toString();
    }
    
    public String getQcmNotion(String qcmUid){
        ReasonerQcmMaitriseNotion rq = new ReasonerQcmMaitriseNotion( qcmUid );
        String str = rq.getQcmNotion();
        return str;
    }
}
