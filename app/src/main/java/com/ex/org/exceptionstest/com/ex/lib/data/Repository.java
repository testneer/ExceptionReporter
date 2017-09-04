package com.ex.org.exceptionstest.com.ex.lib.data;

import com.ex.org.exceptionstest.com.ex.lib.model.ExceptionReport;

/**
 * Created by orenegauthier on 02/09/2017.
 */

public interface Repository {

     void saveExceptionReport(ExceptionReport exceptionReport);
     void sendStoredReports();

}
