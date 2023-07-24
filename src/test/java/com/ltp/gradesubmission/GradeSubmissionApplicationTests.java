package com.ltp.gradesubmission;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * - Integration test maps the entire request and  response lifecycle, verifies the interactions of the application's layers
 * - We need the Spring Context when integration testing, the test needs access to the entire Spring Container
 * - Unit tests comes first
 */

@SpringBootTest
@AutoConfigureMockMvc
//@TestPropertySource(
//	We can override application.properties with test specific properties
//)
class GradeSubmissionApplicationTests {


    @Autowired
    //We need MockMvc to mock GET or POST request against controller
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
    }

    @Test
    void testShowGradeForm() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/?id=123");

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("form"))
                .andExpect(model().attributeExists("grade"));
    }


    @Test
    void testSuccessfulSubmission() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post("/handleSubmit")
                .param("name", "Harry")
                .param("subject", "Potions")
                .param("score", "C-");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/grades"));
    }

    @Test
    void testUnsuccessfulSubmission() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/handleSubmit")
                .param("name", "    ")
                .param("subject", "     ")
                .param("score", "R+");

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("form"));
    }

    @Test
    void testGetGrades() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/grades");
        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("grades"))
                .andExpect(model().attributeExists("grades"));
    }


}
