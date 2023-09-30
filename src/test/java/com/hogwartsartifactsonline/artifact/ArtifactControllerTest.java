package com.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogwartsartifactsonline.artifact.converter.ArtifactDTOtoArtifactConverter;
import com.hogwartsartifactsonline.artifact.converter.ArtifactToArtifactDTOConverter;
import com.hogwartsartifactsonline.artifact.dto.ArtifactDTO;
import com.hogwartsartifactsonline.system.Result;
import com.hogwartsartifactsonline.system.StatusCode;
import com.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.hogwartsartifactsonline.wizard.Wizard;
import com.hogwartsartifactsonline.wizard.dto.WizardDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@WebMvcTest(ArtifactController.class)
@AutoConfigureMockMvc
class ArtifactControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ArtifactService artifactService;

    List<Artifact> artifactList;
    @BeforeEach
    void setUp() {
        this.artifactList = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("112233445566778899");
        a1.setName("Artifact 1");
        a1.setDescription("Artifact 1 description");
        a1.setImageUrl("imageUrl");
        this.artifactList.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("223344556677889911");
        a2.setName("Artifact 2");
        a2.setDescription("Artifact 2 description");
        a2.setImageUrl("imageUrl2");
        this.artifactList.add(a2);

        Artifact a3 = new Artifact();
        a3.setId("334455667788990011");
        a3.setName("Artifact 3");
        a3.setDescription("Artifact 3 description");
        a3.setImageUrl("imageUrl3");
        this.artifactList.add(a3);

        Artifact a4 = new Artifact();
        a4.setId("445566778899001122");
        a4.setName("Artifact 4");
        a4.setDescription("Artifact 4 description");
        a4.setImageUrl("imageUrl4");
        this.artifactList.add(a4);

        Artifact a5 = new Artifact();
        a5.setId("556677889900112233");
        a5.setName("Artifact 5");
        a5.setDescription("Artifact 5 description");
        a5.setImageUrl("imageUrl5");
        this.artifactList.add(a5);

    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void tesFindArtifactByIdSuccess() throws Exception {
        // Given

        given(this.artifactService.findById("112233445566778899")).willReturn(this.artifactList.get(0));

        // When and then
        this.mockMvc.perform(get("/api/v1/artifacts/112233445566778899")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("112233445566778899"))
                .andExpect(jsonPath("$.data.name").value("Artifact 1"));
    }

    @Test
    void tesFindArtifactByIdNotFound() throws Exception {
        // Given
        given(this.artifactService.findById("1250808601744904191")).willThrow(new ObjectNotFoundException("Artifact","1250808601744904191"));

        // When and then
        this.mockMvc.perform(get("/api/v1/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Couldn't found artifact"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        // given
        given(this.artifactService.findAll()).willReturn(this.artifactList);

        // when and then
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag")).value(true)
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifactList.size())))
                .andExpect(jsonPath("$.data[0].id").value("112233445566778899"))
                .andExpect(jsonPath("$.data[0].name").value("Artifact 1"))
                .andExpect(jsonPath("$.data[1].id").value("223344556677889911"))
                .andExpect(jsonPath("$.data[1].name").value("Artifact 2"));
    }

    @Test
    public void test_validArtifactIdWithOwner() {
        // Arrange
        String artifactId = "validId";
        Artifact artifact = new Artifact();
        artifact.setId(artifactId);
        Wizard owner = new Wizard();
        owner.setId(1);
        artifact.setOwner(owner);
        // Mock the artifactService
        ArtifactService artifactService = mock(ArtifactService.class);
        when(artifactService.findById(artifactId)).thenReturn(artifact);
        // Mock the artifactToArtifactDTOConverter
        ArtifactToArtifactDTOConverter converter = mock(ArtifactToArtifactDTOConverter.class);
        ArtifactDTOtoArtifactConverter converter2 = mock(ArtifactDTOtoArtifactConverter.class);

        ArtifactDTO dto = new ArtifactDTO("validId", "Artifact", "Description", "image.jpg", new WizardDTO(1, "Owner",2));
        when(converter.convert(artifact)).thenReturn(dto);
        // Create the controller
        ArtifactController controller = new ArtifactController(artifactService, converter, converter2);

        // Act
        Result result = controller.findArtifactById(artifactId);

        // Assert
        assertTrue(result.isFlag());
        assertEquals(StatusCode.SUCCESS, result.getCode());
        assertEquals("Find One Success", result.getMessage());
        assertEquals(dto, result.getData());
    }

    @Test
    public void test_invalidArtifactId() {
        // Arrange
        String artifactId = "invalidId";
        // Mock the artifactService
        ArtifactService artifactService = mock(ArtifactService.class);
        when(artifactService.findById(artifactId)).thenThrow(new ObjectNotFoundException("Artifact",artifactId));
        // Mock the artifactToArtifactDTOConverter
        ArtifactToArtifactDTOConverter artifactToArtifactDTOConverter = mock(ArtifactToArtifactDTOConverter.class);
        ArtifactDTOtoArtifactConverter converter2 = mock(ArtifactDTOtoArtifactConverter.class);
        // Create the controller
        ArtifactController controller = new ArtifactController(artifactService, artifactToArtifactDTOConverter, converter2);

        // Act
        Exception exception = assertThrows(ObjectNotFoundException.class, () -> controller.findArtifactById(artifactId));

        // Assert
        assertEquals("Couldn't found artifact", exception.getMessage());
    }

    @Test
    public void test_noArtifactsFound() {
        // Arrange
        when(artifactService.findAll()).thenReturn(Collections.emptyList());
        String artifactId = "invalidId";
        // Act
        ArtifactService artifactService = mock(ArtifactService.class);
        when(artifactService.findById(artifactId)).thenThrow(new ObjectNotFoundException("Artifact",artifactId));
        // Mock the artifactToArtifactDTOConverter
        ArtifactToArtifactDTOConverter artifactToArtifactDTOConverter = mock(ArtifactToArtifactDTOConverter.class);
        ArtifactDTOtoArtifactConverter converter2 = mock(ArtifactDTOtoArtifactConverter.class);

        // Create the controller
        ArtifactController controller = new ArtifactController(artifactService, artifactToArtifactDTOConverter, converter2);
        Result result = controller.findAllArtifacts();

        // Assert
        assertTrue(result.isFlag());
        assertEquals(StatusCode.SUCCESS, result.getCode().intValue());
        assertEquals("Find All Success", result.getMessage());
        assertTrue(((List) result.getData()).isEmpty());
        assertEquals(0, ((List) result.getData()).size());
    }

    @Test
    void testAddArtifact() throws Exception {
        // Given
        ArtifactDTO artifactDTO = new ArtifactDTO(null, "Artifact 7",
                                        "Artifact description", "imageURL",null);
        String jsonData = this.objectMapper.writeValueAsString(artifactDTO);

        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("12233");
        savedArtifact.setName("Artifact 7");
        savedArtifact.setDescription("Artifact description");
        savedArtifact.setImageUrl("ImageURL");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);
        // When and Then
        this.mockMvc.perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON).content(jsonData).accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag")).value(true)
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));
    }

    @Test
    void updateArtifactSuccess() throws Exception {
        // Given
        ArtifactDTO artifactDTO = new ArtifactDTO("12233", "Artifact 7",
                "Artifact description", "imageURL",null);
        String jsonData = this.objectMapper.writeValueAsString(artifactDTO);

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("12233");
        updatedArtifact.setName("Artifact 7");
        updatedArtifact.setDescription("A new description");
        updatedArtifact.setImageUrl("ImageURL");

        given(this.artifactService.update(eq("12233"),Mockito.any(Artifact.class))).willReturn(updatedArtifact);
        // When and Then
        this.mockMvc.perform(put("/api/v1/artifact/12233").contentType(MediaType.APPLICATION_JSON).content(jsonData).accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag")).value(true)
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("12233"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));
    }

    @Test
    void testUpdateArtifactErrorWithNonExistentId() throws Exception {
        // Given
        ArtifactDTO artifactDTO = new ArtifactDTO("12233", "Artifact 7",
                "Artifact description", "imageURL",null);
        String jsonData = this.objectMapper.writeValueAsString(artifactDTO);

        given(this.artifactService.update(eq("12233"),Mockito.any(Artifact.class))).willThrow(new ObjectNotFoundException("Artifact","12233"));
        // When and Then
        this.mockMvc.perform(put("/api/v1/artifact/12233").contentType(MediaType.APPLICATION_JSON).content(jsonData).accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag")).value(true)
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Couldn't found artifact"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        // Given
        doNothing().when(this.artifactService).deleteArtifact("12233");

        // When and then
        this.mockMvc.perform(delete("/api/v1/artifact/12233").accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag")).value(true)
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact deleted"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void testDeleteArtifactErrorNotFound() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("Artifact","12233")).when(this.artifactService).deleteArtifact("12233");

        // When and then
        this.mockMvc.perform(delete("/api/v1/artifact/12233").accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag")).value(true)
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Couldn't found artifact"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}