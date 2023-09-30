package com.hogwartsartifactsonline.artifact.converter;

import com.hogwartsartifactsonline.artifact.Artifact;
import com.hogwartsartifactsonline.artifact.dto.ArtifactDTO;
import com.hogwartsartifactsonline.wizard.Wizard;
import com.hogwartsartifactsonline.wizard.converter.WizardToWizardDTOConverter;
import com.hogwartsartifactsonline.wizard.dto.WizardDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDTOConverter implements Converter<Artifact, ArtifactDTO> {
    private final WizardToWizardDTOConverter wizardToWizardDtoConverter;

    public ArtifactToArtifactDTOConverter(WizardToWizardDTOConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }
    @Override
    public ArtifactDTO convert(Artifact source) {
        ArtifactDTO artifactDTO = new ArtifactDTO(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl(),
                source.getOwner() != null ? this.wizardToWizardDtoConverter.convert(source.getOwner()): null);
        return artifactDTO;
    }
}
