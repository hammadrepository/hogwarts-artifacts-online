package com.hogwartsartifactsonline.artifact.dto;

import com.hogwartsartifactsonline.wizard.dto.WizardDTO;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDTO(String id,
                          @NotEmpty(message = "Name is required") String name,
                          @NotEmpty(message = "Description is required") String description,
                          @NotEmpty(message = "Image is required") String imageUrl,
                          WizardDTO owner){
}
