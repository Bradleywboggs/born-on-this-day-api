package com.bboggs.bornonthisday.controllers;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.bboggs.bornonthisday.models.BornOnThisDayApi.parseAndFilterResponse;
import static com.bboggs.bornonthisday.models.BornOnThisDayApi.getResponse;

@Controller
@RequestMapping("")
public class BornOnThisDayController {
    @GetMapping("")
    public String displayForm(Model model) {
        model.addAttribute("title","Born on This Day");
        return "form";
    }

    @PostMapping("results")
    public String processForm(Model model, @RequestParam int mm, int dd) {
        JSONArray jsonArray = parseAndFilterResponse(getResponse(mm, dd));
        model.addAttribute("title", "Born On This Day");
        model.addAttribute("jsonArray", jsonArray);
        return "form-result";
    }
}
