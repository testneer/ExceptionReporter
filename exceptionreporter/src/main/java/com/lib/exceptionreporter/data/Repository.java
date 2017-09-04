package com.lib.exceptionreporter.data;


import com.lib.exceptionreporter.model.ExceptionReport;

/**
 * Created by orenegauthier on 02/09/2017.
 */

public interface Repository {

     void saveExceptionReport(ExceptionReport exceptionReport);
     void sendStoredReports();

}
