package com.hogwartsartifactsonline.artifact.converter;

import com.hogwartsartifactsonline.artifact.Artifact;
import com.hogwartsartifactsonline.artifact.dto.ArtifactDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDTOtoArtifactConverter implements Converter<ArtifactDTO, Artifact> {
    @Override
    public Artifact convert(ArtifactDTO source) {
        Artifact artifact = new Artifact();
        artifact.setId(source.id());
        artifact.setName(source.name());
        artifact.setDescription(source.description());
        artifact.setImageUrl(source.imageUrl());
        return artifact;
    }
}
