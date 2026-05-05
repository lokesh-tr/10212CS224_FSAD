package org.example.smartcampusmanagement.controller;

import jakarta.validation.Valid;
import org.example.smartcampusmanagement.dto.EventSearchDTO;
import org.example.smartcampusmanagement.dto.EventStatsDTO;
import org.example.smartcampusmanagement.entity.Event;
import org.example.smartcampusmanagement.entity.Registration;
import org.example.smartcampusmanagement.service.EventService;
import org.example.smartcampusmanagement.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;
    private final RegistrationService registrationService;

    @Autowired
    public AdminController(EventService eventService,
                           RegistrationService registrationService) {
        this.eventService = eventService;
        this.registrationService = registrationService;
    }

    // ─── Dashboard ──────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<EventStatsDTO> stats = eventService.getAllEventStats();
        long totalEvents = eventService.getAllEvents().size();
        long totalRegistrations = stats.stream().mapToLong(EventStatsDTO::getTotalRegistrations).sum();

        model.addAttribute("stats", stats);
        model.addAttribute("totalEvents", totalEvents);
        model.addAttribute("totalRegistrations", totalRegistrations);
        return "admin/dashboard";
    }

    // ─── List All Events ────────────────────────────

    @GetMapping("/events")
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("searchDTO", new EventSearchDTO());
        model.addAttribute("departments", eventService.getAllDepartments());
        model.addAttribute("eventTypes", eventService.getAllEventTypes());
        return "admin/events";
    }

    // ─── Search Events ──────────────────────────────

    @GetMapping("/events/search")
    public String searchEvents(@ModelAttribute EventSearchDTO searchDTO, Model model) {
        model.addAttribute("events", eventService.searchEvents(searchDTO));
        model.addAttribute("searchDTO", searchDTO);
        model.addAttribute("departments", eventService.getAllDepartments());
        model.addAttribute("eventTypes", eventService.getAllEventTypes());
        return "admin/events";
    }

    // ─── Create Event ───────────────────────────────

    @GetMapping("/events/new")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("formTitle", "Create New Event");
        model.addAttribute("formAction", "/admin/events/new");
        return "admin/event-form";
    }

    @PostMapping("/events/new")
    public String createEvent(@Valid @ModelAttribute("event") Event event,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Create New Event");
            model.addAttribute("formAction", "/admin/events/new");
            return "admin/event-form";
        }

        eventService.createEvent(event);
        redirectAttributes.addFlashAttribute("successMessage", "Event created successfully!");
        return "redirect:/admin/events";
    }

    // ─── Edit Event ─────────────────────────────────

    @GetMapping("/events/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        model.addAttribute("formTitle", "Edit Event");
        model.addAttribute("formAction", "/admin/events/" + id + "/edit");
        return "admin/event-form";
    }

    @PostMapping("/events/{id}/edit")
    public String updateEvent(@PathVariable Long id,
                              @Valid @ModelAttribute("event") Event event,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Edit Event");
            model.addAttribute("formAction", "/admin/events/" + id + "/edit");
            return "admin/event-form";
        }

        eventService.updateEvent(id, event);
        redirectAttributes.addFlashAttribute("successMessage", "Event updated successfully!");
        return "redirect:/admin/events";
    }

    // ─── Delete Event ───────────────────────────────

    @PostMapping("/events/{id}/delete")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        eventService.deleteEvent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Event deleted successfully!");
        return "redirect:/admin/events";
    }

    // ─── View Registrations ─────────────────────────

    @GetMapping("/events/{id}/registrations")
    public String viewRegistrations(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        List<Registration> registrations = registrationService.getRegistrationsByEvent(id);
        EventStatsDTO stats = eventService.getEventStats(id);

        model.addAttribute("event", event);
        model.addAttribute("registrations", registrations);
        model.addAttribute("stats", stats);
        return "admin/registrations";
    }
}
