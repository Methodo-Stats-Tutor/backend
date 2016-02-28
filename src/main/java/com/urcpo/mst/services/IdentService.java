package com.urcpo.mst.services;

import org.apache.log4j.Logger;
import org.xenei.jena.entities.MissingAnnotation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.urcpo.mst.beans.Admin;
import com.urcpo.mst.beans.Guest;
import com.urcpo.mst.beans.Qcm;
import com.urcpo.mst.beans.QcmTry;
import com.urcpo.mst.beans.Student;
import com.urcpo.mst.beans.Teacher;
import com.urcpo.mst.beans.User;
import com.urcpo.mst.servlets.ConnectTDB;
import static com.urcpo.mst.servlets.ConnectTDB.dataset;
import com.urcpo.mst.utils.MstUtils;
import com.urcpo.mst.utils.UserEnum;

public class IdentService {

    private static final Logger log = Logger.getLogger(IdentService.class);
    private String json;

    public IdentService() {
    }

    public User setUser(String id, UserEnum type) throws Exception {
        try {
            this.json = json;
            Qcm qcm = null;
            Gson gson = new Gson();
            QueryExecution qexec = null;
            // ConnectTDB.dataset.end();
            log.info("ici 0!");
            User qcms = null;

            ConnectTDB.dataset.begin(ReadWrite.WRITE);
            qcms = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), id, User.class);
            switch (type) {
                case admin:
                    qcms = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), id, Admin.class);
                    break;
                case teacher:
                    qcms = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), id, Teacher.class);
                    break;
                case student:
                    qcms = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), id, Student.class);
                    break;
                default:
                    qcms = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), id, Guest.class);
                    break;
            }

            if (qcms.getD8Add() == null) {
                qcms.setD8Add(MstUtils.now());
            }
            ConnectTDB.dataset.commit();
            return qcms;
        } catch (Exception e) {
            log.error(type.toString() + " ABC " + e);
            throw new Exception(e.getMessage());
        } finally {
            ConnectTDB.dataset.end();
        }
    }

    public void updateUserProfile(String uid, String profile) throws MissingAnnotation {
        try {
            Gson gson = new Gson();
            JsonElement je = gson.fromJson(profile, JsonElement.class);
            JsonObject jo = je.getAsJsonObject();
            ConnectTDB.dataset.begin(ReadWrite.WRITE);
            User qcm = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), uid, User.class);
            if (jo.has("lName")) {
                qcm.setNom(jo.get("lName").getAsString());
            }
            if (jo.has("fName")) {
                qcm.setPrenom(jo.get("fName").getAsString());
            }
            ConnectTDB.dataset.commit();
        } catch (Exception e) {
            log.error(MstUtils.formatLog(e));
        } finally {
            ConnectTDB.dataset.end();
        }

    }

    public String getUserProfile(String uid) throws MissingAnnotation {
        try {
            ConnectTDB.dataset.begin(ReadWrite.READ);
            User qcm = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), uid, User.class);
            String res = qcm.toJson();
            return res;
        } catch (Exception e) {
            log.error(MstUtils.formatLog(e));
        } finally {
            ConnectTDB.dataset.end();
        }
        return "";
    }

}
