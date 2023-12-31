package com.hogwartsartifactsonline.artifact;

import com.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectDeletedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final IdWorker idWorker;
    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    public Artifact findById(String artifactId){
        return this.artifactRepository.findById(artifactId).orElseThrow(() -> new
                ObjectNotFoundException("Artifact",artifactId));
    }

    public List<Artifact> findAll(){
    return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact artifact){
        artifact.setId(idWorker.nextId() + "");

        return this.artifactRepository.save(artifact);
    }

    public Artifact update(String artifactId, Artifact update){
         return this.artifactRepository.findById(artifactId).map((oldArtifact) -> {
             oldArtifact.setName(update.getName());
             oldArtifact.setDescription(update.getDescription());
             oldArtifact.setImageUrl(update.getImageUrl());
             return this.artifactRepository.save(oldArtifact);
         }).orElseThrow(()-> new ObjectNotFoundException("Artifact",artifactId));
    }

    public void deleteArtifact(String artifactId){

         this.artifactRepository.findById(artifactId)
                .orElseThrow(()-> new ObjectNotFoundException("Artifact",artifactId));
        this.artifactRepository.deleteById(artifactId);


    }
}
