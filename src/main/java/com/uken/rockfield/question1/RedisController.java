package com.uken.rockfield.question1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisController {
    
    @Autowired
    private RedisBuffer buffer;

    @RequestMapping(value = "/{key}/{value}", method = RequestMethod.GET)
    public void setInRedis(@PathVariable String key, @PathVariable String value) {
        buffer.setValue(key, value);
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public String getInRedis(@PathVariable String key) {
        return buffer.getValue(key);
    }
}
