package com.hogwartsartifactsonline.artifact;

import com.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.hogwartsartifactsonline.wizard.Wizard;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactRepository artifactRepository;
    @Mock
    IdWorker idWorker;

    @InjectMocks
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
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {

        // Given
        Artifact a = new Artifact();
        a.setId("123123123123123123");
        a.setDescription("This is an artifact");
        a.setName("Artifact");
        a.setImageUrl("imageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setOwner(w);

        given(artifactRepository.findById("123123123123123123"))
                .willReturn(Optional.of(a));
        // When
        Artifact returnedArtifact = artifactService.findById("123123123123123123");

        // Then
        assertEquals(returnedArtifact.getId(), a.getId());
    }

    @Test
    public void testFindByIdNotFound(){
        // Given
        given(artifactRepository.findById(Mockito.anyString())).willReturn(Optional.empty());
        // When
        Throwable thrown = catchThrowable(() -> {
            Artifact returnedArtifact = artifactService.findById("123123123123123123");
        });
        // Then

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Couldn't found artifact");
        verify(artifactRepository, times(1)).findById("123123123123123123");


    }
    @Test
    void testFindAllSuccess()
    {
        // given
        given(artifactRepository.findAll()).willReturn(this.artifactList);

        // when
        List<Artifact> actualArtifacts = this.artifactService.findAll();
        // then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifactList.size());

        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess(){
        // given
        Artifact artifact = new Artifact();
        artifact.setId("12333");
        artifact.setName("Artifact 7");
        artifact.setDescription("Description");
        artifact.setImageUrl("Image Url");

        given(idWorker.nextId()).willReturn(12333L);
        given(artifactRepository.save(artifact)).willReturn(artifact);
        // when
        Artifact artifactSaved = artifactService.save(artifact);
        // then
        assertThat(artifactSaved.getId()).isEqualTo("12333");
        assertThat(artifactSaved.getName()).isEqualTo(artifact.getName());
        assertThat(artifactSaved.getDescription()).isEqualTo(artifact.getDescription());
        assertThat(artifactSaved.getImageUrl()).isEqualTo(artifact.getImageUrl());
        verify(artifactRepository, times(1)).save(artifact);

    }

    @Test
    void testUpdateSuccess(){
        // Given
            Artifact oldArtifact =  new Artifact();
        oldArtifact.setId("12333");
        oldArtifact.setName("Artifact 7");
        oldArtifact.setDescription("Description");
        oldArtifact.setImageUrl("Image Url");

        Artifact update = new Artifact();
        oldArtifact.setId("12333");
        update.setName("Artifact 7");
        update.setDescription("New Description");
        update.setImageUrl("Image Url");

        given(artifactRepository.findById(oldArtifact.getId())).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        // When

        Artifact updateArtifact = artifactService.update(update.getId(), update);
        // Then
        assertThat(updateArtifact.getId()).isEqualTo(update.getId());
        assertThat(updateArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepository, times(1)).findById(oldArtifact.getId());
        verify(artifactRepository, times(1)).save(oldArtifact);

    }

    @Test
    void updateNotFound(){
        // Given
        Artifact update = new Artifact();
        update.setName("Artifact 7");
        update.setDescription("New Description");
        update.setImageUrl("Image Url");

        given(artifactRepository.findById("12333")).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            artifactService.update("12333", update);
        });
        // Then
        verify(artifactRepository, times(1)).findById("12333");
    }

    @Test
    void testDeleteSuccess(){
        // Given
        Artifact artifact = new Artifact();
        artifact.setId("12333");
        artifact.setName("Artifact 7");
        artifact.setDescription("New Description");
        artifact.setImageUrl("Image Url");

        given(artifactRepository.findById("12333")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("12333");
        // When
        artifactService.deleteArtifact("12333");

        // Then
        verify(artifactRepository, times(1)).deleteById("12333");

    }

    @Test
    void testDeleteNotFound(){
        // Given


        given(artifactRepository.findById("12333")).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            artifactService.deleteArtifact("12333");
        });
        // Then
        verify(artifactRepository, times(1)).findById("12333");

    }
}