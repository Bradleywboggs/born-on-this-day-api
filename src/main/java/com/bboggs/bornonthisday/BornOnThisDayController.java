package com.bboggs.bornonthisday;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.bboggs.bornonthisday.BornOnThisDayApi.parseAndFilterResponse;
import static com.bboggs.bornonthisday.BornOnThisDayApi.getResponse;

@RestController
@CrossOrigin(origins = {"botd.chonkeys.com", "web.botd.chonkeys.local"})
public class BornOnThisDayController {
    @GetMapping(value = "onthisday", produces = "application/json")
    String processForm(@RequestParam int mm, @RequestParam int dd) throws IOException {
        JSONArray jsonArray = parseAndFilterResponse(getResponse(mm, dd));

        return jsonArray.toString();
    }
}

