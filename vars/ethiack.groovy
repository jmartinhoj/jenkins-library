import com.ethiack.HttpResponse;

HttpResponse launchScan(List<String> urls) {
    echo "hello from ethiack.groovy"
    // Create a JSON builder
    def jsonBuilder = new groovy.json.JsonBuilder()

    // Build the JSON structure
    def json = jsonBuilder {
        "urls" urls
    }
    def jsonString = json.toString()
    // String json = JsonOutput.toJson({   
    //     urls: urls, 
    // });
    echo "jsonString: ${jsonString}"
    String requestUrl = "http://localhost:3001/v1/scans/launch";    

    return doPostHttpRequestWithJson(jsonString, requestUrl);    
}

HttpResponse doGetHttpRequest(String requestUrl){
    URL url = new URL(requestUrl);    
    HttpURLConnection connection = url.openConnection();    

    connection.setRequestMethod("GET");    

    //get the request    
    connection.connect();    

    //parse the response    
    HttpResponse resp = new HttpResponse(connection);    

    if(resp.isFailure()){    
        error("\nGET from URL: $requestUrl\n  HTTP Status: $resp.statusCode\n  Message: $resp.message\n  Response Body: $resp.body");    
    }    

    this.printDebug("Request (GET):\n  URL: $requestUrl");    
    this.printDebug("Response:\n  HTTP Status: $resp.statusCode\n  Message: $resp.message\n  Response Body: $resp.body");    

    return resp;    
}  

/**    
    * Posts the json content to the given url and ensures a 200 or 201 status on the response.    
    * If a negative status is returned, an error will be raised and the pipeline will fail.    
    */    
HttpResponse doPostHttpRequestWithJson(String json, String requestUrl){    
    return doHttpRequestWithJson(json, requestUrl, "POST");    
}    

/**    
    * Posts the json content to the given url and ensures a 200 or 201 status on the response.    
    * If a negative status is returned, an error will be raised and the pipeline will fail.    
    */    
HttpResponse doPutHttpRequestWithJson(String json, String requestUrl){    
    return doHttpRequestWithJson(json, requestUrl, "PUT");    
}

/**    
    * Post/Put the json content to the given url and ensures a 200 or 201 status on the response.    
    * If a negative status is returned, an error will be raised and the pipeline will fail.    
    * verb - PUT or POST    
    */    
HttpResponse doHttpRequestWithJson(String json, String requestUrl, String verb){    
    URL url = new URL(requestUrl);    
    HttpURLConnection connection = url.openConnection();    

    connection.setRequestMethod(verb);    
    connection.setRequestProperty("Content-Type", "application/json");    
    connection.doOutput = true;    

    //write the payload to the body of the request    
    def writer = new OutputStreamWriter(connection.outputStream);    
    writer.write(json);    
    writer.flush();    
    writer.close();    

    //post the request    
    connection.connect();    

    //parse the response    
    HttpResponse resp = new HttpResponse(connection);    

    if(resp.isFailure()){    
        error("\n$verb to URL: $requestUrl\n    JSON: $json\n    HTTP Status: $resp.statusCode\n    Message: $resp.message\n    Response Body: $resp.body");    
    }    

    // this.printDebug("Request ($verb):\n  URL: $requestUrl\n  JSON: $json");    
    // this.printDebug("Response:\n  HTTP Status: $resp.statusCode\n  Message: $resp.message\n  Response Body: $resp.body");    

    return resp;    
}
