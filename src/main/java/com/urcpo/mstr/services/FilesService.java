package com.urcpo.mstr.services;

import java.io.IOException;

import org.apache.log4j.Logger;
import com.hp.hpl.jena.query.ReadWrite;
import com.urcpo.mstr.beans.Picture;
import com.urcpo.mstr.beans.Publication;
import com.urcpo.mstr.beans.Publications;
import com.urcpo.mstr.beans.Teacher;
import com.urcpo.mstr.servlets.ConnectTDB;
import com.urcpo.mstr.utils.MstrUtils;

public class FilesService {

    private static final Logger log = Logger.getLogger( FilesService.class );
    private String              json;

    public FilesService() {
    }

    public String createPublication( String fileName, String filePath, String pmid, String journal,
            String title,
            String author, String abstracte, String userUid ) throws Exception {
        try {
            String fileUid = MstrUtils.uid();
            ConnectTDB.dataset.begin( ReadWrite.READ );
            Teacher tc = ConnectTDB.read( ConnectTDB.dataset.getDefaultModel(), userUid,
                    Teacher.class );
            ConnectTDB.dataset.end();

            ConnectTDB.dataset.begin( ReadWrite.WRITE );
            Publication pb = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), MstrUtils.uid(),
                    Publication.class );
            Publications pcs = ConnectTDB.read( ConnectTDB.dataset.getDefaultModel(), "publications",
                    Publications.class );
            pcs.addHasPublication( pb );
            pb.setHasTeacher( tc );
            pb.setPmid( pmid );
            pb.setTitle( title );
            pb.setJournal( journal );
            pb.setAbstract( abstracte );
            pb.setAuthor( author );
            pb.setFileName( fileName );

            pb.setD8Add(MstrUtils.now() );
            pb.setPath(MstrUtils.REP_SAVE_FILE + MstrUtils.REP_SAVE_PUBLI + fileUid + ".php" );

            MstrUtils.validatePojo( pb );
            pdf2html( fileName, filePath, fileUid );
            MstrUtils.validatePojo( pb );
            ConnectTDB.dataset.commit();

            return "{\"status\":\"ok\"}";
        } catch ( Exception e ) {
            ConnectTDB.dataset.abort();
            throw new Exception( e.getMessage() );
        } finally {
            ConnectTDB.dataset.end();
        }
    }

    public Object createPicture( String fileName, String filePath, String title, String description,
            String userUid ) throws Exception {
        try {
            String fileUid = MstrUtils.uid();
            ConnectTDB.dataset.begin( ReadWrite.READ );
            Teacher tc = ConnectTDB.read( ConnectTDB.dataset.getDefaultModel(), userUid,
                    Teacher.class );
            ConnectTDB.dataset.end();

            ConnectTDB.dataset.begin( ReadWrite.WRITE );
            Picture pb = ConnectTDB.readWrite(ConnectTDB.dataset.getDefaultModel(), MstrUtils.uid(),
                    Picture.class );

            pb.setTitle( title );
            pb.setDescription( description );
            pb.setHasTeacher( tc );
            pb.setFileName( fileName );
            pb.setD8Add(MstrUtils.now() );
            pb.setPath(MstrUtils.REP_SAVE_FILE + MstrUtils.REP_SAVE_PUBLI + fileUid + ".php" );

            MstrUtils.validatePojo( pb );
            img2html( fileName, filePath, fileUid );
            ConnectTDB.dataset.commit();

            return "{\"status\":\"ok\"}";
        } catch ( Exception e ) {
            ConnectTDB.dataset.abort();
            throw new Exception( e.getMessage() );
        } finally {
            ConnectTDB.dataset.end();
        }
    }

    private void pdf2html( String nomFichier, String chemin, String suffix ) {

        try {
            log.error( String.format( "%s %s %s %s", "pdf2php.sh", chemin, nomFichier, suffix ) );
            Process p = new ProcessBuilder( "pdf2php.sh", chemin, nomFichier, suffix ).start();
            p.waitFor();
        } catch ( IOException | InterruptedException e ) {
            // TODO Auto-generated catch block
            log.error( e.getMessage() );
        }
        return;
    }
    

    private void img2html( String nomFichier, String chemin, String suffix ) {

        try {
            log.error( String.format( "%s %s %s %s", "pdf2html.sh", chemin, nomFichier, suffix ) );
            Process p = new ProcessBuilder( "img2html.sh", chemin, nomFichier, suffix ).start();
            p.waitFor();
        } catch ( IOException | InterruptedException e ) {
            // TODO Auto-generated catch block
            log.error( e.getMessage() );
        }
        return;
    }

}
