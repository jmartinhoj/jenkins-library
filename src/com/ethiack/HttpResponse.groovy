package com.ethiack;

class HttpResponse {    

    String body;    
    String message;    
    Integer statusCode;    
    boolean failure = false;    

    public HttpResponse(HttpURLConnection connection){    
        this.statusCode = connection.responseCode;    
        this.message = connection.responseMessage;    

        if(statusCode == 200 || statusCode == 201){    
            this.body = connection.content.text;//this would fail the pipeline if there was a 400    
        }else{    
            this.failure = true;    
            this.body = connection.getErrorStream().text;    
        }    

        connection = null; //set connection to null for good measure, since we are done with it    
    }       
}