package com.bboggs.bornonthisday;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.bboggs.bornonthisday.BornOnThisDayApi.parseAndFilterResponse;
import static com.bboggs.bornonthisday.BornOnThisDayApi.getResponse;

@Controller
public class BornOnThisDayController {
    @GetMapping("onthisday")
    public String processForm(Model model, @RequestParam int mm, @RequestParam int dd) {
        JSONArray jsonArray = parseAndFilterResponse(getResponse(mm, dd));
        model.addAttribute("jsonArray", jsonArray);
        return "on-this-day";
    }
}
