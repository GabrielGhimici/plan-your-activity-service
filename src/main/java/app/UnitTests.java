package app;

import app.dao.LoginDaoI;
import app.dto.AuthDTO;
import app.dto.RegisterDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

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

    @Test
    public void testRegister() throws URISyntaxException
    {
        RestTemplate restTemplate = new RestTemplate();

        String email = "admin@gmail.com";
        String password = "parola";
        AuthDTO Adto = new AuthDTO();
        Adto.setEmail(email);
        Adto.setPassword(password);

        final String loginUrl = "http://localhost:" + randomServerPort + "/login";
        URI uri = new URI(loginUrl);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,Adto,String.class);

        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        String token = result.getBody().toString();

        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String random = new String(array, Charset.forName("UTF-8"));

        final String registerUrl = "http://localhost:" + randomServerPort + "/service/register";
        uri = new URI(registerUrl);

        RegisterDTO Rdto = new RegisterDTO();
        Rdto.setEmail(random);
        Rdto.setName(random);
        Rdto.setPassword(random);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        try {
            date = format.parse("1995/09/06");
        } catch (ParseException e) {
            Assert.fail();

        }

        Rdto.setBorn(date);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<RegisterDTO> entity = new HttpEntity<RegisterDTO>(Rdto, headers);

        result = restTemplate.postForEntity(uri,entity,String.class);

        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
    }
}
