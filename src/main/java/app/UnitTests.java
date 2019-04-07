package app;

import app.dao.LoginDaoI;
import app.dto.AuthDTO;
import app.dto.EventDTO;
import app.dto.RegisterDTO;
import app.response.AttendantPOJO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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
        Assert.assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());

        dto.setPassword("123");

        try {
            result = restTemplate.postForEntity(uri, dto, String.class);
            Assert.fail();
        }catch (HttpClientErrorException ex) {
            //Verify unauthorized
            Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), ex.getRawStatusCode());
        }
    }

    @Test
    public void testLogout() throws URISyntaxException
    {
        RestTemplate restTemplate = new RestTemplate();

        final String loginUrl = "http://localhost:" + randomServerPort + "/login";
        URI uri = new URI(loginUrl);

        String email = "admin@gmail.com";
        String password = "parola";
        AuthDTO dto = new AuthDTO();
        dto.setEmail(email);
        dto.setPassword(password);

        ResponseEntity<String> result = restTemplate.postForEntity(uri,dto,String.class);

        //Verify request succeed
        Assert.assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        String token = result.getBody().toString();


        final String logoutUrl = "http://localhost:" + randomServerPort + "/service/logout";
        uri = new URI(logoutUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        result = restTemplate.postForEntity(uri,entity,String.class);

        //Verify request succeed
        Assert.assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());

        try {
            result = restTemplate.postForEntity(uri, entity, String.class);
            Assert.fail();
        }catch (HttpClientErrorException ex) {
            //Verify unauthorized
            Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), ex.getRawStatusCode());
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
        Assert.assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
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
        Assert.assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());

        //This part of the test cannot be runned because of the issue #1
        /*try {
            result = restTemplate.postForEntity(uri, Rdto, String.class);
            Assert.fail();
        }catch (HttpClientErrorException ex) {
            //Verify unauthorized
            Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), ex.getRawStatusCode());
        }*/
    }

    @Test
    public void testEventCreation() throws URISyntaxException
    {
        RestTemplate restTemplate = new RestTemplate();

        final String loginUrl = "http://localhost:" + randomServerPort + "/login";
        URI uri = new URI(loginUrl);

        String email = "admin@gmail.com";
        String password = "parola";
        AuthDTO dto = new AuthDTO();
        dto.setEmail(email);
        dto.setPassword(password);

        ResponseEntity<String> result = restTemplate.postForEntity(uri,dto,String.class);

        //Verify request succeed
        Assert.assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        String token = result.getBody().toString();

        final String logoutUrl = "http://localhost:" + randomServerPort + "/service/addEvent";
        uri = new URI(logoutUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        EventDTO event = new EventDTO();
        event.setDescription("Unit test");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");


        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        date = calendar.getTime();
        String strDate = dateFormat.format(date);
        String strTime = timeFormat.format(date);
        event.setStart_date(strDate);
        event.setStart_time(strTime);

        calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        date = calendar.getTime();
        strDate = dateFormat.format(date);
        strTime = timeFormat.format(date);
        event.setFinish_date(strDate);
        event.setFinish_time(strTime);

        /* No attendants list for now. Will be implemented in the future */
        event.setAttendants(new AttendantPOJO[0]);

        HttpEntity<EventDTO> entity = new HttpEntity<EventDTO>(event, headers);

        result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);

        //Verify request succeed
        Assert.assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());


        calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -4);
        date = calendar.getTime();
        strDate = dateFormat.format(date);
        strTime = timeFormat.format(date);
        event.setFinish_date(strDate);
        event.setFinish_time(strTime);

        entity = new HttpEntity<EventDTO>(event, headers);

        try {
            result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
            Assert.fail();
        }catch (HttpClientErrorException ex) {
            //Verify unauthorized
            Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), ex.getRawStatusCode());
        }
    }
}
