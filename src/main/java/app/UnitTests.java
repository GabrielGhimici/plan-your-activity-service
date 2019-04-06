package app;

import app.dao.LoginDaoI;
import app.dto.AuthDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
public class UnitTests {

    @LocalServerPort
    int randomServerPort;

//  Just an example for the rest of the tests
/*  @Test
    public void testLogoutSuccess() throws URISyntaxException
    {
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "http://localhost:" + randomServerPort + "/service/logout";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> result = restTemplate.postForEntity(uri,null,String.class);

        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("OK"));
    }
*/

    @Test
    public void testLogin() throws URISyntaxException
    {
        RestTemplate restTemplate = new RestTemplate();
        LoginDaoI util = new LoginDaoI();

        final String baseUrl = "http://localhost:" + randomServerPort + "/login";
        URI uri = new URI(baseUrl);

        String email = "admin@gmail.com";
        String password = "parola";
        AuthDTO dto = new AuthDTO();
        dto.setEmail(email);
        dto.setPassword(password);

        ResponseEntity<String> result = restTemplate.postForEntity(uri,dto,String.class);

        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());

        dto.setPassword("123");

        try {
            result = restTemplate.postForEntity(uri, dto, String.class);
            Assert.fail();
        }catch (HttpClientErrorException ex) {
            //Verify unauthorized
            Assert.assertEquals(401, ex.getRawStatusCode());
        }
    }
}
