package org.example.smartcampusmanagement.controller;

import jakarta.validation.Valid;
import org.example.smartcampusmanagement.entity.Event;
import org.example.smartcampusmanagement.entity.Feedback;
import org.example.smartcampusmanagement.entity.Registration;
import org.example.smartcampusmanagement.service.EventService;
import org.example.smartcampusmanagement.service.FeedbackService;
import org.example.smartcampusmanagement.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final EventService eventService;
    private final RegistrationService registrationService;
    private final FeedbackService feedbackService;

    @Autowired
    public StudentController(EventService eventService,
                             RegistrationService registrationService,
                             FeedbackService feedbackService) {
        this.eventService = eventService;
        this.registrationService = registrationService;
        this.feedbackService = feedbackService;
    }

    // ─── Browse Events ──────────────────────────────

    @GetMapping("/events")
    public String browseEvents(Model model) {
        model.addAttribute("events", eventService.getUpcomingEvents());
        model.addAttribute("pageTitle", "Upcoming Events");
        return "student/events";
    }

    // ─── Event Detail ───────────────────────────────

    @GetMapping("/events/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        Double avgRating = feedbackService.getAverageRating(id);
        List<Feedback> feedbacks = feedbackService.getFeedbackForEvent(id);

        model.addAttribute("event", event);
        model.addAttribute("averageRating", avgRating);
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("spotsLeft", event.getMaxCapacity() - event.getRegisteredCount());
        return "student/event-detail";
    }

    // ─── Registration Form ──────────────────────────

    @GetMapping("/events/{id}/register")
    public String showRegistrationForm(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        model.addAttribute("registration", new Registration());
        return "student/register";
    }

    @PostMapping("/events/{id}/register")
    public String submitRegistration(@PathVariable Long id,
                                     @Valid @ModelAttribute("registration") Registration registration,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("event", eventService.getEventById(id));
            return "student/register";
        }

        try {
            registrationService.registerForEvent(id, registration);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Successfully registered! Check your email for confirmation.");
            return "redirect:/student/events/" + id;
        } catch (Exception e) {
            model.addAttribute("event", eventService.getEventById(id));
            model.addAttribute("errorMessage", e.getMessage());
            return "student/register";
        }
    }

    // ─── My Registrations ───────────────────────────

    @GetMapping("/my-registrations")
    public String myRegistrations(@RequestParam(required = false) String email, Model model) {
        if (email != null && !email.isBlank()) {
            List<Registration> registrations = registrationService.getRegistrationsByEmail(email);
            model.addAttribute("registrations", registrations);
            model.addAttribute("searchEmail", email);
        }
        return "student/my-registrations";
    }

    // ─── Feedback Form ──────────────────────────────

    @GetMapping("/events/{id}/feedback")
    public String showFeedbackForm(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        model.addAttribute("feedback", new Feedback());
        return "student/feedback";
    }

    @PostMapping("/events/{id}/feedback")
    public String submitFeedback(@PathVariable Long id,
                                 @Valid @ModelAttribute("feedback") Feedback feedback,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("event", eventService.getEventById(id));
            return "student/feedback";
        }

        feedbackService.submitFeedback(id, feedback);
        redirectAttributes.addFlashAttribute("successMessage", "Thank you for your feedback!");
        return "redirect:/student/events/" + id;
    }
}
