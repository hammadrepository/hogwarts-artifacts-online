package com.hogwartsartifactsonline.artifact;

import com.hogwartsartifactsonline.artifact.converter.ArtifactDTOtoArtifactConverter;
import com.hogwartsartifactsonline.artifact.converter.ArtifactToArtifactDTOConverter;
import com.hogwartsartifactsonline.artifact.dto.ArtifactDTO;
import com.hogwartsartifactsonline.system.Result;
import com.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArtifactController {

    private final ArtifactService  artifactService;
    private final ArtifactToArtifactDTOConverter artifactToArtifactDTOConverter;
    private final ArtifactDTOtoArtifactConverter artifactDTOtoArtifactConverter;
    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDTOConverter artifactToArtifactDTOConverter, ArtifactDTOtoArtifactConverter artifactDTOtoArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDTOConverter = artifactToArtifactDTOConverter;
        this.artifactDTOtoArtifactConverter = artifactDTOtoArtifactConverter;
    }

    @GetMapping(value = "/api/v1/artifacts/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
        Artifact artifact = this.artifactService.findById(artifactId);
        ArtifactDTO dto = this.artifactToArtifactDTOConverter.convert(artifact);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", dto);
    }
@GetMapping(value = "/api/v1/artifacts")
    public Result findAllArtifacts(){
    List<Artifact> artifact = this.artifactService.findAll();
    List<ArtifactDTO> artifactDTOS = artifact.stream().map(this.artifactToArtifactDTOConverter::convert)
            .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", artifactDTOS);
    }

    @PostMapping("/api/v1/artifacts")
    public Result addArtifact(@Valid @RequestBody ArtifactDTO artifactDTO){
        // Convert ArtifactDTOtoArtifact
          Artifact newArtifact=  this.artifactDTOtoArtifactConverter.convert(artifactDTO);
          Artifact  savedArtifact = this.artifactService.save(newArtifact);
          ArtifactDTO savedArtifactDTO = this.artifactToArtifactDTOConverter.convert(savedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedArtifactDTO);
    }

    @PutMapping("/api/v1/artifact/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDTO artifactDTO){
        Artifact update = this.artifactDTOtoArtifactConverter.convert(artifactDTO);
        Artifact updatedArtifact = this.artifactService.update(artifactId, update);
        ArtifactDTO artifactDTO1 = this.artifactToArtifactDTOConverter.convert(updatedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Update Success",null);
    }
    @DeleteMapping("/api/v1/artifact/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId){
        this.artifactService.deleteArtifact(artifactId);
        return new Result(true, StatusCode.SUCCESS, "Artifact deleted",null);
    }
}
