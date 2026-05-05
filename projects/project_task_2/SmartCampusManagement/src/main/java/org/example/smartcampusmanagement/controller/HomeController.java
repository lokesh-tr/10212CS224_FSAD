package org.example.smartcampusmanagement.controller;

import org.example.smartcampusmanagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventService eventService;

    @Autowired
    public HomeController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Show top 6 upcoming events on the landing page
        var upcoming = eventService.getUpcomingEvents();
        model.addAttribute("featuredEvents", upcoming.stream().limit(6).toList());
        model.addAttribute("totalEvents", upcoming.size());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
