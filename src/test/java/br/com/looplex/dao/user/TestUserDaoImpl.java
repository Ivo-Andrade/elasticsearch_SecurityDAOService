package br.com.looplex.dao.user;

import br.com.looplex.models.ElasticsearchUser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Arrays;

public class TestUserDaoImpl {

    // TODO: Test with DB

    @Test
    public void testCreateWithNewUser() {

        ElasticsearchUser newUser = new ElasticsearchUser();
        newUser.setUsername("testCreateWithNewUser");
        newUser.setPassword("testCreateWithNewUser");
        newUser.setEmail("test@test.user");

        try {
            new UserDaoImpl().create(newUser);
        } finally {
            new UserDaoImpl().delete(newUser);
        }
    }

    @Test
    public void testCreateWithExistingUser() {

        ElasticsearchUser newUser = new ElasticsearchUser();
        newUser.setUsername("testCreateWithExistingUser");
        newUser.setPassword("testCreateWithExistingUser");
        newUser.setEmail("test@test.user");

        ElasticsearchUser newUser2 = new ElasticsearchUser();
        newUser2.setUsername("testCreateWithExistingUser");
        newUser2.setPassword("testCreateWithExistingUser");
        newUser2.setEmail("test@test.user");

        try {
            new UserDaoImpl().create(newUser);
            new UserDaoImpl().create(newUser2);
        } finally {
            new UserDaoImpl().delete(newUser);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullUser() {
        new UserDaoImpl().create(null);
    }

    @Test
    public void testFindAll() {

        ElasticsearchUser newUser1 = new ElasticsearchUser();
        newUser1.setUsername("testFindAll1");
        newUser1.setPassword("testFindAll1");
        newUser1.setEmail("test@test.user");

        ElasticsearchUser newUser2 = new ElasticsearchUser();
        newUser2.setUsername("testFindAll2");
        newUser2.setPassword("testFindAll2");
        newUser2.setEmail("test@test.user");

        ElasticsearchUser newUser3 = new ElasticsearchUser();
        newUser3.setUsername("testFindAll3");
        newUser3.setPassword("testFindAll3");
        newUser3.setEmail("test@test.user");

        new UserDaoImpl().create(newUser1);
        new UserDaoImpl().create(newUser2);
        new UserDaoImpl().create(newUser3);

        try {
            Assert.assertEquals(ArrayList.class, new UserDaoImpl().findAll().getClass());
        } finally {
            new UserDaoImpl().delete(newUser1);
            new UserDaoImpl().delete(newUser2);
            new UserDaoImpl().delete(newUser3);
        }
    }

    @Test
    public void testFindByIdWithExistingId() {

        // TODO: Can't create-check-delete ("User not found")

        ElasticsearchUser newUser = new ElasticsearchUser();
        newUser.setUsername("testFindByIdWithExistingId");
        newUser.setPassword("testFindByIdWithExistingId");
        newUser.setEmail("test@test.user");
        new UserDaoImpl().create(newUser);

        try {
            Assert.assertEquals(Arrays.asList(
                new UserDaoImpl().findById("testFindByIdWithExistingId").getUsername(),
                new UserDaoImpl().findById("testFindByIdWithExistingId").getPassword(),
                new UserDaoImpl().findById("testFindByIdWithExistingId").getEmail()
            ), Arrays.asList(
                newUser.getUsername(),
                null,
                newUser.getEmail()
            ));
        } finally {
            new UserDaoImpl().delete(newUser);
        }

    }

    @Test(expected = HttpClientErrorException.class)
    public void testFindByIdWithNonExistingId() {
        new UserDaoImpl().findById("testFindByIdWithNonExistingId");
    }

    @Test(expected = HttpClientErrorException.class)
    public void testFindByIdWithNullId() {
        new UserDaoImpl().findById(null);
    }

    @Test
    public void testUpdateWithExistingUser() {

        // TODO: Can't create-check-delete ("User not found")

        ElasticsearchUser newUser = new ElasticsearchUser();
        newUser.setUsername("testUpdateWithExistingUser");
        newUser.setPassword("testUpdateWithExistingUser");
        newUser.setEmail("test@test.user");

        try {
            new UserDaoImpl().create(newUser);
            newUser.setPassword("testUpdateWithExistingUser_newInfo");
            new UserDaoImpl().update(newUser);
            Assert.assertEquals(Arrays.asList(
                    new UserDaoImpl().findById("testUpdateWithExistingUser").getUsername(),
                    new UserDaoImpl().findById("testUpdateWithExistingUser").getPassword(),
                    new UserDaoImpl().findById("testUpdateWithExistingUser").getEmail()
            ), Arrays.asList(
                    newUser.getUsername(),
                    null,
                    newUser.getEmail()
            ));
        } finally {
            new UserDaoImpl().delete(newUser);
        }

    }

    @Test
    public void testUpdateWithNonExistingUser() {

        ElasticsearchUser newUser = new ElasticsearchUser();
        newUser.setUsername("testUpdateWithNonExistingUser");
        newUser.setPassword("testUpdateWithNonExistingUser");
        newUser.setEmail("test@test.user");

        // TODO: Creates object if absent
        try {
            new UserDaoImpl().update(newUser);
        } finally {
            new UserDaoImpl().delete(newUser);
        }


    }

    @Test(expected = NullPointerException.class)
    public void testUpdateWithNullUser() {

        new UserDaoImpl().update(null);

    }

    @Test(expected = HttpClientErrorException.class)
    public void testDeleteWithExistingUser() {

        ElasticsearchUser newUser = new ElasticsearchUser();
        newUser.setUsername("testDeleteWithExistingUser");
        newUser.setPassword("testDeleteWithExistingUser");
        newUser.setEmail("test@test.user");

        new UserDaoImpl().create(newUser);
        new UserDaoImpl().delete(newUser);
        new UserDaoImpl().findById("testDeleteWithExistingUser");

    }

    @Test(expected = HttpClientErrorException.class)
    public void testDeleteWithNonExistingUser() {

        ElasticsearchUser newUser = new ElasticsearchUser();
        newUser.setUsername("testDeleteWithNonExistingUser");
        newUser.setPassword("testDeleteWithNonExistingUser");
        newUser.setEmail("test@test.user");

        new UserDaoImpl().delete(newUser);

    }

    @Test(expected = NullPointerException.class)
    public void testDeleteWithNullUser() {
        new UserDaoImpl().delete(null);
    }

    @Test
    public void testGetHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("elastic","elasticpassword");
        Assert.assertEquals(new UserDaoImpl().getHeaders(),headers);
    }

    @Test
    public void testSetPayloadWithValidObject() {

        ElasticsearchUser newUser = new ElasticsearchUser();
        newUser.setUsername("testSetPayloadWithValidObject");
        newUser.setPassword("testSetPayloadWithValidObject");
        newUser.setEmail("test@test.user");

        Assert.assertEquals(new UserDaoImpl().setPayload(newUser)
                , "{\"username\":\"testSetPayloadWithValidObject\",\"email\":\"test@test.user\",\"password\":\"testSetPayloadWithValidObject\",\"roles\":[]}");

    }

    @Test
    public void testSetPayloadWithEmptyObject() {

        ElasticsearchUser newUser = new ElasticsearchUser();

        Assert.assertEquals(new UserDaoImpl().setPayload(newUser)
                , "{\"roles\":[]}");

    }

    @Test
    public void testSetPayloadWithNullObject() {

        Assert.assertEquals(new UserDaoImpl().setPayload(null)
                , "null");

    }

    @Test
    public void testGetUserWithValidString() {

        ElasticsearchUser newUser = new ElasticsearchUser();
        newUser.setUsername("testGetUserWithValidString");
        newUser.setPassword("testGetUserWithValidString");
        newUser.setEmail("test@test.user");

        Assert.assertEquals(new UserDaoImpl().getUser("{\"username\":\"testGetUserWithValidString\",\"password\":\"testGetUserWithValidString\",\"email\":\"test@test.user\"}")
                , newUser);
    }

    @Test
    public void testGetUserWithInvalidString() {
        // TODO: Not picking up UnrecognizedPropertyException
        Assert.assertEquals(new UserDaoImpl().getUser("{\"incorrect-username\":\"testGetUserWithValidString\",\"incorrect-password\":\"testGetUserWithValidString\",\"incorrect-email\":\"test@test.user\"}"),null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUserWithNullString() {
        new UserDaoImpl().getUser(null);
    }

}
