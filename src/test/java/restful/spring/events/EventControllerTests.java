package restful.spring.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
// @ContextConfiguration(locations = "classpath:WEB-INF/dispatcher-servlet.xml")
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml"})
// @EnableJpaRepositories("reposotory")
public class EventControllerTests {
	
	@Autowired
    private WebApplicationContext context; // MockMvc 객체 생성을 위한 context
	
    private MockMvc mockMvc;    // controller에 request를 수행해주는 mock 객체
    
    ObjectMapper objectMapper; // 객체를 json 형식으로 변경 시 사용
    
    @Autowired
    EventController eventController;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(eventController)
            .build();
        objectMapper = new ObjectMapper();
    }
    
    @Test
    public void createEvent() throws Exception {
    	
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2010, 11, 23, 14, 23))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 30, 14, 23))
                .beginEventDateTime(LocalDateTime.of(2018, 12, 5, 14, 30))
                .endEventDateTime(LocalDateTime.of(2018, 12, 6, 14, 30))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("Npee Corp.")
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(event)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("id").exists())
                    .andExpect(header().exists(HttpHeaders.LOCATION))
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE));
    }
}
