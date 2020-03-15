package restful.spring.events;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

// HATEOAS의 'MediaTypes.HAL_JSON_UTF8_VALUE'을 사용하기 위해서는 hateoas 0.25대 의 구버전을 사용해야함
// 최신버전(1.0)에서는 HAL_JSON_VALUE 사용

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {
	
	@Autowired
	EventRepositoryImpl eventRepository;
	
	@PostMapping
	public ResponseEntity<Event> createEvent(@RequestBody Event event) {
		Event newEvent = this.eventRepository.save(event);
		URI createdURI = linkTo(EventController.class).slash(newEvent.getId()).toUri();
		return ResponseEntity.created(createdURI).body(newEvent);
	}
}
