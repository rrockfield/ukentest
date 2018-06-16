package com.uken.rockfield.question1;

import com.uken.rockfield.Application;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class ChallengeB_IT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RedisBuffer buffer;

    @Test
    public void testRedisController() throws Exception {
        String baseUrl = "http://localhost:" + port + "/redis/";
        int n = 30;
        for (int i = 0; i < n; i++) {
            String url = baseUrl + "key-" + i + "/value-" + i;
            Response response = RestAssured.get(url);
            Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
            Thread.sleep(500);
        }
        for (int i = 0; i < n; i++) {
            Assert.assertTrue(buffer.getValue("key-" + i).equals("value-" + i));
        }
        buffer.clear();
    }
}
