package com.ex.org.exceptionstest.com.ex.lib.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by orenegauthier on 03/09/2017.
 */
public class APIImplTest {


    @Test
    public void testSendReport() throws Exception {
        APIImpl api = new APIImpl();
        api.postToDelete(API.URL,bowlingJson("Oren", "Daniella"), "myID");

    }

    String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE',"
            + "'name':'Bowling',"
            + "'round':4,"
            + "'lastSaved':1367702411696,"
            + "'dateStarted':1367702378785,"
            + "'players':["
            + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
            + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
            + "]}";
    }

}