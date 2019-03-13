package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.api.ApiResponse;
import com.github.catalin.cretu.conference.api.ErrorView;
import com.github.catalin.cretu.conference.api.event.AuthorView;
import com.github.catalin.cretu.conference.api.event.CreateEventRequest;
import com.github.catalin.cretu.conference.api.event.CreateEventResponse;
import com.github.catalin.cretu.conference.api.event.EventView;
import com.github.catalin.cretu.conference.api.event.EventsResponse;
import com.github.catalin.cretu.conference.api.event.PeriodView;
import com.github.catalin.cretu.conference.path.api;
import com.github.catalin.cretu.conference.result.ErrorResult;
import com.github.catalin.cretu.conference.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.github.catalin.cretu.conference.api.ApiResponse.inactiveFeatureError;
import static com.github.catalin.cretu.conference.togglz.FeatureToggle.EVENTS;
import static java.util.Objects.requireNonNullElseGet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@RestController
public class EventsController {

    private EventService eventService;

    public EventsController(final EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(path = api.Events)
    public ApiResponse<EventsResponse> findAllEvents() {
        log.debug("Find all events");
        if (EVENTS.isNotActive()) {
            return inactiveFeatureEventsResponse();
        }
        var events = eventService.findAll()
                .stream()
                .map(EventsController::toEventView)
                .collect(toSet());

        return ApiResponse.ok(EventsResponse.builder()
                .events(events)
                .build());
    }

    private static ApiResponse<EventsResponse> inactiveFeatureEventsResponse() {
        return ApiResponse.internalError(EventsResponse.builder()
                .errors(inactiveFeatureError(EVENTS))
                .build());
    }

    private static EventView toEventView(final Event event) {
        return EventView.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .period(PeriodView.builder()
                        .startDateTime(event.getPeriod().getStartDateTime())
                        .endDateTime(event.getPeriod().getEndDateTime())
                        .build())
                .author(AuthorView.builder()
                        .name(event.getAuthor().getName())
                        .jobTitle(event.getAuthor().getJobTitle())
                        .companyName(event.getAuthor().getCompanyName())
                        .build())
                .build();
    }

    @PostMapping(path = api.Events)
    public ApiResponse<CreateEventResponse> createEvent(final @RequestBody CreateEventRequest createEventRequest) {
        if (EVENTS.isNotActive()) {
            return inactiveFeatureCreateEventResponse();
        }
        var eventResult = eventService.createEvent(toEvent(createEventRequest));

        return toCreateEventResponse(eventResult);
    }

    private static ApiResponse<CreateEventResponse> inactiveFeatureCreateEventResponse() {
        return ApiResponse.internalError(CreateEventResponse.builder()
                .errors(inactiveFeatureError(EVENTS))
                .build());
    }

    private static Event toEvent(final CreateEventRequest createEventRequest) {
        PeriodView period = requireNonNullElseGet(createEventRequest.getPeriod(), PeriodView::new);
        AuthorView author = requireNonNullElseGet(createEventRequest.getAuthor(), AuthorView::new);

        return Event.builder()
                .title(createEventRequest.getTitle())
                .description(createEventRequest.getDescription())
                .location(createEventRequest.getLocation())
                .period(Period.builder()
                        .startDateTime(period.getStartDateTime())
                        .endDateTime(period.getEndDateTime())
                        .build())
                .author(Author.builder()
                        .name(author.getName())
                        .jobTitle(author.getJobTitle())
                        .companyName(author.getCompanyName())
                        .build())
                .build();
    }

    private static ApiResponse<CreateEventResponse> toCreateEventResponse(final Result<Event> event) {
        if (event.hasErrors()) {
            return ApiResponse.badRequest(
                    toCreateEventErrorResponse(event.getErrors()));
        }
        return ApiResponse.ok(CreateEventResponse.builder()
                .id(event.get().getId())
                .build());
    }

    private static CreateEventResponse toCreateEventErrorResponse(final Set<ErrorResult> errors) {
        return CreateEventResponse.builder()
                .errors(errors.stream()
                        .map(errorResult -> ErrorView.builder()
                                .code(errorResult.getCode())
                                .message(errorResult.getMessage())
                                .build())
                        .collect(toList()))
                .build();
    }
}