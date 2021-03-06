package com.urcpo.mstr.services;

import com.urcpo.mstr.reasoner.ReasonerQcmMaitriseNotion;
import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;

import java.util.Map;

import org.apache.log4j.Logger;
import org.xenei.jena.entities.MissingAnnotation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.urcpo.mstr.beans.Choice;
import com.urcpo.mstr.beans.ChoiceText;
import com.urcpo.mstr.beans.ChoiceZone;
import com.urcpo.mstr.beans.PubliZone;
import com.urcpo.mstr.beans.PublicationAnnot;
import com.urcpo.mstr.beans.Qcm;
import com.urcpo.mstr.beans.QcmTry;
import com.urcpo.mstr.beans.Question;
import com.urcpo.mstr.beans.Student;
import com.urcpo.mstr.beans.Teacher;
import com.urcpo.mstr.beans.User;
import com.urcpo.mstr.servlets.ConnectTDB;
import com.urcpo.mstr.servlets.LoadOnto;
import com.urcpo.mstr.utils.MstrUtils;
import java.util.logging.Level;

public class QcmService {

    private static final Logger log = Logger.getLogger(QcmService.class);
    private String json;

    public QcmService() {

    }

    public Qcm createQcm(String json) throws Exception {
        this.json = json;
        Qcm qcm = null;
        Gson gson = new Gson();
        JsonElement je = gson.fromJson(json, JsonElement.class);
        JsonObject jo = je.getAsJsonObject();
        String qcmUid;
        Boolean firstime = false;

        try {
            ConnectTDB.dataset.begin(ReadWrite.WRITE);

            qcmUid = MstrUtils.uid();

            qcm = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), qcmUid, Qcm.class);
            qcm.setTeacher(ConnectTDB.readWrite(
                    ConnectTDB.dataset.getDefaultModel(),
                    jo.get("userUid").getAsString(), Teacher.class));
            qcm.setD8Add(MstrUtils.now());
            ConnectTDB.dataset.commit();

        } catch (Exception e) {
            log.error(json);
            throw new Exception(e.getMessage());
        } finally {
            ConnectTDB.dataset.end();
        }
        return qcm;
    }

    // QUAND SAVE & firsttime, OU SAVEFINAL
    public Qcm saveQcm(String json) throws Exception {
        this.json = json;
        Qcm qcm = null;
        Gson gson = new Gson();
        JsonElement je = gson.fromJson(json, JsonElement.class);
        JsonObject jo = je.getAsJsonObject();
        String qcmUid;
        try {
            ConnectTDB.dataset.begin(ReadWrite.WRITE);

            qcmUid = jo.get("uid").getAsString();

            qcm = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), qcmUid, Qcm.class);
            // qcm.setTeacher( ConnectTDB.readWrite(
            // ConnectTDB.dataset.getDefaultModel(),
            // jo.get( "userUid" ).getAsString(), Teacher.class ) );
            // qcm.setD8Add( MstUtils.now() );
            qcm.setName(jo.get("name").getAsString());
            qcm.setDifficulty(jo.get("difficulty").getAsInt());

            qcm.setFinished(jo.get("finished").getAsBoolean());
            PublicationAnnot pa;
            for (int j = 0; j < jo.get("courseMaterialUid").getAsJsonArray().size(); j++) {
                pa = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(),
                        jo.get("courseMaterialUid").getAsJsonArray().get(j).getAsString(), PublicationAnnot.class);
                qcm.addRefersToPubliAnnot(pa);
            }

            // QUESTION
            Question quest;
            Integer rank;
            ChoiceZone cz;
            ChoiceText ct;
            JsonObject jq = jo.get("questions").getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : jq.entrySet()) {
                rank = Integer.parseInt(entry.getKey());
                quest = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(),
                        MstrUtils.uid(), Question.class);
                qcm.addHasQuestion(quest);
                JsonObject jq2 = entry.getValue().getAsJsonObject();
                quest.setRank(jq2.get("rank").getAsInt());
                quest.setStatement(jq2.get("statement").getAsString());

                quest.setDifficulty(jq2.get("difficulty").getAsInt());
                if (jq2.get("timelimit") != null) {
                    quest.setTimeLimit(jq2.get("timelimit").getAsInt());
                }
                // CHOICEZONE
                String czUid;
                JsonObject jqczInfo;

                JsonObject jqcz = jq2.get("choicezone").getAsJsonObject();
                // if(!jqcz.toString().equals( "{}" ))
                for (Map.Entry<String, JsonElement> entry2 : jqcz.entrySet()) {
                    czUid = entry2.getKey();
                    jqczInfo = entry2.getValue().getAsJsonObject();

                    cz = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(),
                            MstrUtils.uid(), ChoiceZone.class);
                    quest.addHasChoiceZone(cz);
                    // cz.setValue( jqczInfo.get( "value" ).getAsBoolean() );
                    if (jqczInfo.get("value") == null) {
                        cz.setValue(false);
                    } else {
                        cz.setValue(jqczInfo.get("value").getAsBoolean());
                    }
                    if (jqczInfo.get("gameOver") == null) {
                        cz.setGameOver(false);
                    } else {
                        cz.setGameOver(jqczInfo.get("gameOver").getAsBoolean());
                    }
                    if (jqczInfo.get("correction") != null) {
                        cz.setCorrection(jqczInfo.get("correction").getAsString());
                    }
                    if (jqczInfo.get("help") != null) {
                        cz.setHelp(jqczInfo.get("help").getAsString());
                    }
                    cz.setHasPubliZone(ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(),
                            czUid, PubliZone.class));
                }

                // CHOICETEXT
                String ctUid;
                JsonObject jqctInfo;

                JsonObject jqct = jq2.get("choicetext").getAsJsonObject();
                // if(!jqct.toString().equals( "{}" ))
                for (Map.Entry<String, JsonElement> entry2 : jqct.entrySet()) {
                    ctUid = entry2.getKey();
                    jqctInfo = entry2.getValue().getAsJsonObject();
                    ct = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(),
                            MstrUtils.uid(), ChoiceText.class);
                    quest.addHasChoiceText(ct);
                    // ct.setId( Integer.parseInt( ctUid ) );

                    if (jqctInfo.get("value") == null) {
                        ct.setValue(false);
                    } else {
                        ct.setValue(jqctInfo.get("value").getAsBoolean());
                    }
                    if (jqctInfo.get("gameOver") == null) {
                        ct.setGameOver(false);
                    } else {
                        ct.setGameOver(jqctInfo.get("gameOver").getAsBoolean());
                    }
                    ct.setLabel(jqctInfo.get("label").getAsString());
                    if (jqctInfo.get("correction") != null) {
                        ct.setCorrection(jqctInfo.get("correction").getAsString());
                    }
                    if (jqctInfo.get("help") != null) {
                        ct.setHelp(jqctInfo.get("help").getAsString());
                    }
                }
            }
            qcm.setJson(json);
            qcm.setD8Last(MstrUtils.now());

            MstrUtils.validatePojo(qcm);

            ConnectTDB.dataset.commit();
            // FIN AJOUT PUBLI

        } catch (Exception e) {
            log.error(json);
            throw new Exception(e.getMessage());
        } finally {
            ConnectTDB.dataset.end();
        }

        return qcm;

    }

    public Qcm saveQcmNotion(String json, String qcmUid) throws Exception {
        Qcm qcm = null;
        Gson gson = new Gson();
        JsonElement je = gson.fromJson(json, JsonElement.class);
        JsonObject jo = je.getAsJsonObject();
        // les notions en jeux
        try {
            ConnectTDB.dataset.begin(ReadWrite.WRITE);
            qcm = ConnectTDB.readWrite(
                    ConnectTDB.dataset.getDefaultModel(), qcmUid, Qcm.class);
            for (Map.Entry<String, JsonElement> entry : jo.get("traite").getAsJsonObject().entrySet()) {
                qcm.addTraiteNotion(java.net.URI.create(entry.getKey().toString()));
            }
            for (Map.Entry<String, JsonElement> entry : jo.get("give").getAsJsonObject().entrySet()) {
                qcm.addGiveNotion(java.net.URI.create(entry.getKey().toString()));
            }
            for (Map.Entry<String, JsonElement> entry : jo.get("need").getAsJsonObject().entrySet()) {
                qcm.addNeedNotion(java.net.URI.create(entry.getKey().toString()));
            }
//            for (int j = 0; j < jo.get("give").getAsJsonArray().size(); j++) {
//                qcm.addGiveNotion(java.net.URI.create(jo.get("give").getAsJsonArray().get(j).getAsString()));
//            }
//            for (int j = 0; j < jo.get("need").getAsJsonArray().size(); j++) {
//                qcm.addNeedNotion(java.net.URI.create(jo.get("need").getAsJsonArray().get(j).getAsString()));
//            }
            ConnectTDB.dataset.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            ConnectTDB.dataset.end();
        }
        // fin les notions en jeux

        return qcm;
    }

    // Save, not first TIME
    public Qcm updateQcm(JsonObject json, String qcmUid
    ) throws Exception {
        Qcm qcm = null;
        ConnectTDB.dataset.begin(ReadWrite.WRITE);
        try {
            qcm = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), qcmUid, Qcm.class);
            log.error(json.toString());
            qcm.setName(json.get("name").getAsString());
            qcm.setDifficulty(json.get("difficulty").getAsInt());
            qcm.setD8Last(MstrUtils.now());
            qcm.setJson(json.toString());
            qcm.setFinished(json.get("finished").getAsBoolean());
            MstrUtils.validatePojo(qcm);

            ConnectTDB.dataset.commit();
            // FIN AJOUT PUBLI
            return qcm;
        } catch (Exception e) {
            log.error(json);
            throw new Exception(e.getMessage());
        } finally {
            ConnectTDB.dataset.end();
        }

    }

    public String getQcms(String userUid) throws Exception {
        String sparqlQueryString = "PREFIX mstr: <http://methodo-stat-tutor.com#>\n"
                + "SELECT * { \n"
                + "                ?qcm a mstr:Qcm .\n"
                + "                ?qcm mstr:name  ?name .\n"
                + "                ?qcm mstr:d8Add ?d8add .\n"
                + "                OPTIONAL{?qcm mstr:teacher ?teacher .\n"
                + "                ?qcm mstr:d8Last ?d8Last .\n"
                + "                ?teacher mstr:nom ?fname .\n"
                + "                ?teacher mstr:prenom ?lname }.\n"
                + "                ?qcm mstr:finished ?finished\n"
                + "                }";
        return ConnectTDB.getSparqlResultAsJson(sparqlQueryString);
    }

    public String getQcmTrys(String userUid) throws Exception {
        String sparqlQueryString = "PREFIX mstr: <http://methodo-stat-tutor.com#>\n"
                + "SELECT * { \n"
                + "?qcm a mstr:Qcm .\n"
                + "?qcm mstr:name  ?name .\n"
                + "?qcm mstr:d8Add ?d8add .\n"
                + "OPTIONAL{?qcm mstr:teacher ?teacher .\n"
                + "?teacher mstr:nom ?fname .\n"
                + "?teacher mstr:prenom ?lname }.\n"
                + "?qcm mstr:difficulty ?difficulty .\n"
                + "OPTIONAL{?qcmtry mstr:refersQcm ?qcm .\n"
                + "?qcmtry mstr:finished false .\n"
                + "?qcmtry mstr:json ?json .\n"
                + "?qcmtry mstr:user mstr:" + userUid + " }\n"
                + "}";
        return ConnectTDB.getSparqlResultAsJson(sparqlQueryString);
    }

    public String getQcm(String id) throws Exception {
        ConnectTDB.dataset.begin(ReadWrite.READ);
        Qcm qcm = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), id, Qcm.class);
        String res = qcm.toJson();
        ConnectTDB.dataset.end();
        return res;

    }

    public String getQcmTry(String uri) {
        String sparqlQueryString = "SELECT * { "
                + "?qcmtry <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://methodo-stat-tutor.com#QcmTry> ."
                + " ?qcmtry <http://methodo-stat-tutor.com#refersQcm>  <http://methodo-stat-tutor.com#" + uri + "> ."
                + " ?qcmtry <http://methodo-stat-tutor.com#d8Add>  ?d8add ."
                + " ?qcmtry <http://methodo-stat-tutor.com#finished>  ?finished .  "
                + " OPTIONAL{ ?qcmtry <http://methodo-stat-tutor.com#json>  ?json } . "
                + " filter (  ?finished = false )"
                + " }";
        return ConnectTDB.getSparqlResultAsJson(sparqlQueryString);
    }

    public String createQcmTry(String qcmUid, String userUid) throws Exception {
        ConnectTDB.dataset.begin(ReadWrite.WRITE);
        String uid = MstrUtils.uid();
        QcmTry qcmTry = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), uid, QcmTry.class);
        Qcm qcm = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), qcmUid, Qcm.class);

        qcmTry.setRefersQcm(qcm);
        qcmTry.setFinished(false);
        qcmTry.setD8Add(MstrUtils.now());
        qcmTry.setD8Last(MstrUtils.now());
        User usr = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), userUid, User.class);
        qcmTry.setUser(usr);
        usr.addHasQcmTry(qcmTry);
        ConnectTDB.dataset.commit();
        ConnectTDB.dataset.end();
        return uid;

    }

    public void updateQcmTry(String uriQcm, String json) throws Exception {
        Gson gson = new Gson();
        JsonElement je = gson.fromJson(json, JsonElement.class);
        JsonObject jo = je.getAsJsonObject();
        ConnectTDB.dataset.begin(ReadWrite.WRITE);
        QcmTry qcmTry = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), uriQcm, QcmTry.class);
        qcmTry.setD8Last(MstrUtils.now());
        if (jo.has("json")) {
            qcmTry.setJson(jo.get("json").getAsString());
        }
        if (jo.has("finished")) {
            qcmTry.setFinished(true);
        }
        if (jo.has("mark")) {
            qcmTry.setMark(jo.get("mark").getAsDouble());
        }
        ConnectTDB.dataset.commit();
        ConnectTDB.dataset.end();

    }

    private boolean questionIsGameOver(String uid) throws MissingAnnotation {
        ConnectTDB.dataset.begin(ReadWrite.READ);
        Choice qcm = null;
        qcm = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), uid, Choice.class);
        ConnectTDB.dataset.end();
        return qcm.getGameOver();

    }

    private boolean checkAnswer(String uid, boolean value) throws MissingAnnotation {
        ConnectTDB.dataset.begin(ReadWrite.READ);
        log.debug(uid + " : " + "ok !");
        Choice qcm = null;
        qcm = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), uid, Choice.class);
        log.debug(uid + " : " + qcm.getValue());
        boolean rep;
        rep = value == qcm.getValue() ? true : false;
        log.debug(rep);
        ConnectTDB.dataset.end();
        return rep;
    }

    public String correctAnswers(String uriQcm, String answers) throws Exception {

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
        JsonElement je = gson.fromJson(answers, JsonElement.class);
        JsonObject jo = je.getAsJsonObject();

        JsonObject res = jo.get("result").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : res.entrySet()) {

            String quest = entry.getKey();
            JsonObject ans = entry.getValue().getAsJsonObject().get("answers").getAsJsonObject();
            temp1 = new JsonObject();
            temp2 = new JsonObject();
            semiTotAns = 0;
            semiNote = 0;
            gameOver = false;
            for (Map.Entry<String, JsonElement> entry2 : ans.entrySet()) {
                totAns++;
                semiTotAns++;
                temp = new JsonObject();
                JsonObject rep = entry2.getValue().getAsJsonObject();
                if (rep.get("help") != null) {
                    add = 0.5;
                    temp.addProperty("help", true);
                    temp.addProperty("note", add);
                } else {
                    add = 1;
                    temp.addProperty("help", false);
                    temp.addProperty("note", add);
                }
                if (checkAnswer(entry2.getKey(), rep.get("value").getAsBoolean())) {
                    note += add;
                    semiNote += add;
                } else {
                    gameOver = questionIsGameOver(entry2.getKey());
                    if (gameOver) {
                        semiNote = 0;
                    }
                    note += 0;
                    semiNote += 0;
                }

                temp1.add(entry2.getKey(), temp);
                temp2.add("detail", temp1);
                temp2.addProperty("semiNote", semiNote);
                temp2.addProperty("gameOver", gameOver);
                temp2.addProperty("semiTot", semiTotAns);
                if (gameOver) {
                    break;
                }

            }

            result.add(quest, temp2);

            last.add("result", result);
            last.addProperty("mark", String.format("%.2f", (note / totAns * 20)));
        }
        // ajouter la note !
        JsonElement jea = gson.fromJson(answers, JsonElement.class);
        JsonObject joa = jea.getAsJsonObject();
        joa.addProperty("mark", (note / totAns * 20));
        updateQcmTry(uriQcm, joa.toString());

        return last.toString();
    }

    // suggère les notions manipulées par ce qcm
    public String getQcmNotionSuggest(String qcmUid) {
        ReasonerQcmMaitriseNotion rq = new ReasonerQcmMaitriseNotion(qcmUid);
        String str = rq.getQcmNotion();
        return str;
    }

    // déduit les notions manipulées par ce qcm
    public String getQcmNotion(String qcmUid) {
        String queryBegin
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX mstr: <http://methodo-stat-tutor.com#>\n"
                + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX owl:      <http://www.w3.org/2002/07/owl#> \n"
                + "PREFIX sparqldl: <http://pellet.owldl.com/ns/sdle#>\n "
                + "SELECT ?NOTION ?NOTIONLAB ?NOTIONCOMM ?TYP  \n"
                + "WHERE {";
        String queryEnd = "}";

        String queryStr2
                = queryBegin
                + "{?EXO a mstr:Qcm .\n"
                + " ?EXO mstr:traiteNotion ?NOTION .\n"
                + " BIND(IRI(str(?NOTION)) as ?NOTION_IRI) .\n"
                + "OPTIONAL { ?NOTION_IRI rdfs:label ?NOTIONLAB } .\n"
                + "OPTIONAL { ?NOTION_IRI rdfs:comment ?NOTIONCOMM } \n"
                + "values ?TYP { 'traite' }\n"
                + "FILTER (?EXO = mstr:" + qcmUid + ")\n"
                + "}\n"
                + "UNION \n"
                + "{?EXO a mstr:Qcm .\n"
                + " ?EXO mstr:needNotion ?NOTION .\n"
                + " BIND(IRI(str(?NOTION)) as ?NOTION_IRI) .\n"
                + "OPTIONAL { ?NOTION_IRI rdfs:label ?NOTIONLAB } .\n"
                + "OPTIONAL { ?NOTION_IRI rdfs:comment ?NOTIONCOMM } \n"
                + "values ?TYP { 'need' }\n"
                + "FILTER (?EXO = mstr:" + qcmUid + ")"
                + "}"
                + "UNION \n"
                + "{?EXO a mstr:Qcm .\n"
                + " ?EXO mstr:giveNotion ?NOTION .\n"
                + " BIND(IRI(str(?NOTION)) as ?NOTION_IRI) .\n"
                + "OPTIONAL { ?NOTION_IRI rdfs:label ?NOTIONLAB } .\n"
                + "OPTIONAL { ?NOTION_IRI rdfs:comment ?NOTIONCOMM } \n"
                + "values ?TYP { 'give' }\n"
                + "FILTER (?EXO = mstr:" + qcmUid + ")"
                + " }"
                + queryEnd;
        log.error(queryStr2);
        Query query2 = QueryFactory.create(queryStr2);
        ConnectTDB.dataset.begin(ReadWrite.READ);
        ResultSet results = SparqlDLExecutionFactory
                .create(query2, LoadOnto.ontologie.add(ConnectTDB.dataset.getDefaultModel()))//On merge l'onto MST avec les Faits, pour récupérer les labels
                .execSelect();
        String str = MstrUtils.getSparqlResultsetAsJson(results);
        ConnectTDB.dataset.end();
        return str;
    }

    public void setQcmValidate(String student, String qcmtry) {
        ConnectTDB.dataset.begin(ReadWrite.WRITE);
        try {
            Student s = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), student, Student.class);
            QcmTry qt = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), qcmtry, QcmTry.class);
            Qcm q = qt.getRefersQcm();
            s.addHasValidateExo(q);
            qt.setValidated(true);
            ConnectTDB.dataset.commit();
        } catch (MissingAnnotation ex) {
            java.util.logging.Logger.getLogger(QcmService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectTDB.dataset.end();
        }

    }

    //pour un étudiant et un  qcm donné, retourne tous les essais réalisés
    public String getStudentQcmtrys(String usrUid, String qcmUid) {
        String prefix
                = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX mstr:  <http://methodo-stat-tutor.com#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n";

        String sparqlQueryString = "SELECT ?qcmtry ?d8last ?mark ?validated { "
                + "?qcmtry a mstr:QcmTry ."
                + "?qcmtry mstr:refersQcm  ?qcm ."
                + "?qcmtry mstr:d8Last  ?d8last ."
                + "?qcmtry mstr:mark  ?mark ."
                + "OPTIONAL{ ?qcmtry mstr:validated  ?validated }"
                + "FILTER ( ?qcm = mstr:" + qcmUid + " )"
                + "} ORDER BY ASC(?d8last)";
        
        return ConnectTDB.getSparqlResultAsJson(prefix + sparqlQueryString);
    }
    
    //
}
