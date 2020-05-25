package com.application.integration;


import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@Profile("staging")
@RestController
@RequestMapping("/api/test")
public class TestController {

}
