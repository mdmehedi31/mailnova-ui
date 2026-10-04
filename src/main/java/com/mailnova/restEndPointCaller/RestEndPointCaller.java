package com.mailnova.restEndPointCaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.function.Consumer;

@Service
public class RestEndPointCaller {


    @Autowired
    private RestClient restClient;
/*
    @Autowired
    private SecurityContext securityContext;*/
    private static final Logger log = LoggerFactory.getLogger(RestEndPointCaller.class);
    @Autowired
    private ObjectMapper objectMapper;

    public RestEndPointCaller(/*, SecurityContext securityContext*/) {
        //this.restClient = restClient;
        //this.securityContext = securityContext;
    }


   /* @Bean
    public RestClient createRestClientBean(SecurityContext securityContext) {
        log.info("Creating RestClient Bean >>>>");
        return RestClient.builder().requestInterceptor((request,body,execution)->{

            String token= securityContext.getCurrentUser().getJwtToken();
            if (token != null) {
                request.getHeaders().setBearerAuth(token);
            }
            return execution.execute(request, body);
        }).build();
    }*/

    public <E,T> T postCallerWithRequestBody(String path, E requestType, Class<T> responseBody) {
        try {

            log.info("The rest client url is : {}", this.restClient.options().retrieve());
            return this.restClient.post().
                    uri(path).
                    body(requestType).
                    headers(e->{
                        e.addAll(getHeaders());
                    }).
                    retrieve().
                    body(responseBody);

        } catch (HttpClientErrorException e) {
            log.error("An exception thrown HttpClientErrorException catch part : "+e.getMessage());
            throw e;
        } catch (HttpServerErrorException e) {
            log.error("An exception thrown HttpServerErrorException catch part "+e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An exception thrown HttpServerErrorException catch part "+e.getMessage());
            throw e;
        }
    }

    public <E,T> T postCallerWithRequestBodyAndPathvariable(String path, E requestBody, Class<T> responseBody, Object... pathValue) {
        try {

            log.info("The rest client url is : {}", this.restClient.options().retrieve());
            return this.restClient.post().
                    uri(path, pathValue).
                    body(requestBody).
                    headers(e->{
                        e.addAll(getHeaders());
                    }).
                    retrieve().
                    body(responseBody);

        } catch (HttpClientErrorException e) {
            log.error("An exception thrown HttpClientErrorException catch part : "+e.getMessage());
            throw e;
        } catch (HttpServerErrorException e) {
            log.error("An exception thrown HttpServerErrorException catch part "+e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An exception thrown HttpServerErrorException catch part "+e.getMessage());
            throw e;
        }
    }

    public <T> T postCallerWithRequestParams(String path, MultiValueMap<String, String> requestParam, Class<T> responseBody) {
        try{
            return restClient.post()
                    .uri(ub->ub.path(path).
                            queryParams(requestParam).
                            build()).
                    headers(e->{
                        e.addAll(getHeaders());
                    }).
                    retrieve()
                    .body(responseBody);
        } catch(HttpServerErrorException e){
            throw e;
        }
        catch(HttpClientErrorException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public <E,T> T putCallerWithRequestBody(String path, E requestBody, Class<T> responseBody){
        try{
            return restClient.put().
                    uri(path).
                    body(requestBody).
                    headers(e->{
                        e.addAll(getHeaders());
                    }).
                    retrieve().
                    body(responseBody);

        }catch(HttpClientErrorException e){
            throw e;
        }catch(HttpServerErrorException e){
            throw e;
        }
        catch(Exception e){
            throw e;
        }
    }

    public <E, T> T putCallerWithRequestParam(String path, MultiValueMap<String, String> requestParam, Class<T> responseBody){
        try{
            return restClient.put().uri(ub-> ub.
                    path(path).
                    queryParams(requestParam).
                    build()).
                    headers(e->{
                        e.addAll(getHeaders());
                    }).
                    retrieve().
                    body(responseBody);
        }catch(HttpClientErrorException e){
            throw e;
        }catch(HttpServerErrorException e){
            throw e;
        }
        catch (Exception e) {
            throw  e;
        }
    }

    public <T> T getCallerWithRequestParam(String path,MultiValueMap<String, String> requestParam, Class<T> responseBody){
        try{
            URI uri = UriComponentsBuilder
                    .fromPath(path)
                    .queryParams(requestParam)
                    .build()
                    .toUri();

            System.out.println("URL: " + uri);
            return restClient.get().
                    uri(ub->ub.path(path).
                            queryParams(requestParam).
                            build()).
                    headers(e -> {
                        e.addAll(getHeaders());
                    }).
                    retrieve().
                    body(responseBody);
        } catch (Exception e) {
            throw e;
        }
    }

    public <T> T getWithRequestParam(String path, ParameterizedTypeReference<T> responseBody,MultiValueMap<String, String> requestParam){
        try{
            return restClient.get().
                    uri(ub->ub.path(path).
                            queryParams(requestParam).
                            build()).
                    headers(e->{
                        e.addAll(getHeaders());
                    }).
                    retrieve().
                    body(responseBody);
        }catch(HttpServerErrorException e){
            throw e;
        }
        catch(HttpClientErrorException e){
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public <T> T putCallerWithPathVariable(String path,Class<T> responseBody, Object... pathValue){
        try{
            return restClient.put().
                    uri(path, pathValue).
                    headers(e->{
                        e.addAll(getHeaders());
                    }).
                    retrieve().
                    body(responseBody);
        }catch(HttpClientErrorException e){
            throw e;
        }
        catch (HttpServerErrorException e){
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public <T> T getCaller(String path, Class<T> responseBody){
        try{
            return restClient.get().uri(path).headers(e->{
                e.addAll(getHeaders());
            }).retrieve().body(responseBody);
        } catch(HttpClientErrorException e){
            throw e;
        }
        catch(HttpServerErrorException e){
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }
    public <T> T getCallerWithPathVariable(String path, Class<T> responseBody, Object... pathValue){
        try{

            return restClient.get().
                    uri(path, pathValue).
                    headers(e->{
                        e.addAll(getHeaders());
                    }).
                    retrieve().
                    body(responseBody);

        }catch (HttpClientErrorException.Unauthorized e){
            System.out.println("Status Code: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());
            throw e;
        }
        catch(HttpClientErrorException e){
            System.out.println("Status Code: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());
            throw e;
        }
        catch(HttpServerErrorException e){
            System.out.println("Status Code: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());
            throw e;
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public <T> T getCallerWithPathVariable(String path, ParameterizedTypeReference<T> responseBody, Object... pathValue){
        try{
            return restClient.get().
                    uri(path, pathValue).
                    headers(e->{
                       e.addAll(getHeaders());
                    }).
                    retrieve().
                    body(responseBody);

        }catch(HttpClientErrorException e){
            throw e;
        }
        catch(HttpServerErrorException e){
            throw e;
        }
        catch(Exception e){
            throw e;
        }
    }


    public <T, R> R postCallerForMultipart(
            String uri,
            String jsonPartName,
            T requestObject,
            String filePartName,
            MultipartFile file,
            Class<R> responseType
    ) throws IOException {

        try {

            log.info("This is from Multipart File post caller <:::-----:::>");
            MultiValueMap<String, Object> body =
                    new LinkedMultiValueMap<>();

            HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

            body.add(
                    jsonPartName,
                    new HttpEntity<>(
                            objectMapper.writeValueAsString(requestObject),
                            jsonHeaders
                    )
            );

            if (file != null) {

                ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getName();
                    }
                };

                HttpHeaders fileHeaders = new HttpHeaders();
                fileHeaders.setContentType(
                        MediaType.parseMediaType("text/csv")
                );
                body.add(filePartName, new HttpEntity<>(resource, fileHeaders));
            }


            return restClient.post()
                    .uri(uri)
                    /*    .headers(headers -> {
                            if (headerCustomizer != null) {
                                headerCustomizer.accept(headers);
                            }
                        })*/
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(responseType);

        } catch (HttpClientErrorException e) {
            throw e;
        } catch (HttpServerErrorException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

    }

   private HttpHeaders getHeaders(){

        /*CurrentUser currentUser = securityContext.getCurrentUser();
        HttpHeaders headers = new HttpHeaders();

        System.out.println("Current user value is : "+currentUser);
        if(currentUser != null) {
            System.out.println("Current user is not null... ... ...!");
            System.out.println("token is :: "+currentUser.getJwtToken());

            headers.setBearerAuth(currentUser.getJwtToken());
        }*/
       HttpHeaders headers = new HttpHeaders();
        return headers;
    }
}
