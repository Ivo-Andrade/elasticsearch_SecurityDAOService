package br.com.looplex.dao.user;

import br.com.looplex.models.ElasticsearchUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserDaoImpl
    extends UserDao
{

    RestTemplate restTemplate = new RestTemplate();

    // TODO: Host handling
    String host = "http://localhost:9200/";

    // TODO: Auth login to be done this way? Safe?
    String authUsername = "elastic";
    String authPassword = "elasticpassword";

    public void create(ElasticsearchUser newInstance) {

        restTemplate.exchange(
            host + "_security/user/" + newInstance.getUsername()
            , HttpMethod.POST
            , new HttpEntity<String>(setPayload(newInstance), getHeaders())
            , JsonNode.class
        );

    }

    public List<ElasticsearchUser> findAll() {

        ResponseEntity<JsonNode> requestResponse;

        requestResponse = restTemplate.exchange(
            host + "_security/user"
            , HttpMethod.GET
            , new HttpEntity<String>(null, getHeaders())
            , JsonNode.class
        );

        List<ElasticsearchUser> userList = new ArrayList<ElasticsearchUser>();

        Iterator<JsonNode> itr = requestResponse.getBody().elements();

        while(itr.hasNext()) {
            userList.add( getUser( itr.next().toString() ) );
        }

        return userList;

    }

    public ElasticsearchUser findById(String id) {

        ResponseEntity<JsonNode> requestResponse;

        requestResponse = restTemplate.exchange(
            host + "_security/user/" + id
            , HttpMethod.GET
            , new HttpEntity<String>(null, getHeaders())
            , JsonNode.class
        );

        Iterator<JsonNode> itr = requestResponse.getBody().elements();

        if(itr.hasNext()) {
            return getUser( itr.next().toString() );
        }

        else return null;
    }

    public ElasticsearchUser update(ElasticsearchUser transientObject) {

        restTemplate.exchange(
            host + "_security/user/" + transientObject.getUsername()
            , HttpMethod.PUT
            , new HttpEntity<String>(setPayload(transientObject), getHeaders())
            , JsonNode.class
        );

        // TODO: HTTP Request does not return updated user (Keep return value as is?)
        return findById(transientObject.getUsername());
    }

    public void delete(ElasticsearchUser persistentObject) {

        restTemplate.exchange(
            host + "_security/user/" + persistentObject.getUsername()
            , HttpMethod.DELETE
            , new HttpEntity<String>(setPayload(persistentObject), getHeaders())
            , JsonNode.class
        );

    }

    @Override
    public void enableUser(String id) {

        restTemplate.exchange(
            host + "_security/user/" + id + "/_enable"
            , HttpMethod.PUT
            , new HttpEntity<String>(null, getHeaders())
            , JsonNode.class
        );

    }

    @Override
    public void disableUser(String id) {

        restTemplate.exchange(
            host + "_security/user/" + id + "/_disable"
            , HttpMethod.PUT
            , new HttpEntity<String>(null, getHeaders())
            , JsonNode.class
        );

    }

    // TODO: Auxiliary Functions Addressing (New class, stay here?)

    public HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(authUsername, authPassword);
        return headers;
    }

    public String setPayload(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // TODO: Unexpected exception (Stay as it is?)
            e.printStackTrace();
            return null;
        }
    }

    public ElasticsearchUser getUser(String str) {
        try {
            return new ObjectMapper().readValue(str, ElasticsearchUser.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
