/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author igoens
 */
public class ExecuteSqlScript {

    public final static char QUERY_ENDS = ';';
    private File script;
    private Connection conn;
    private Statement stat;

    /**
     * @param args
     * @throws SQLException
     */
    /** Create new ExecuteSqlScript */
    public ExecuteSqlScript(String scriptFileName, Connection Conn) throws SQLException {
        conn = Conn;
        script = new File(scriptFileName);
        stat = conn.createStatement();
    }

    protected void loadScript() throws IOException, SQLException {
        /** Load SQL scripts to a buffer */
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(script));
        StringBuffer query = new StringBuffer();
        boolean queryEnds = false;

        while ((line = reader.readLine()) != null) {
            if (isComment(line)) {
                continue;
            }

            queryEnds = checkStatementEnds(line);
            query.append(line);

            if (queryEnds) {
                stat.addBatch(query.toString());
                query.setLength(0);
            }
        }
    }

    private boolean isComment(String line) {
        /** Check if it's comment line */
        if ((line != null) && (line.length() > 0)) {
            return (line.charAt(0) == '#');
        }
        return false;
    }

    public void execute() throws IOException, SQLException {
        /** Execute batch script */
        stat.executeBatch();
    }

    private boolean checkStatementEnds(String s) {
        /** Check if statements end */
        return (s.indexOf(QUERY_ENDS) != -1);
    }
}

