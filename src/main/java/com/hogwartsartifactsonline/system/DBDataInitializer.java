package com.hogwartsartifactsonline.system;

import com.hogwartsartifactsonline.artifact.Artifact;
import com.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.hogwartsartifactsonline.wizard.Wizard;
import com.hogwartsartifactsonline.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {
    private final ArtifactRepository artifactRepository;

    private final WizardRepository wizardRepository;

    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Artifact a1 = new Artifact();
        a1.setId("112233445566778899");
        a1.setName("Artifact 1");
        a1.setDescription("Artifact 1 description");
        a1.setImageUrl("imageUrl");

        Artifact a2 = new Artifact();
        a2.setId("223344556677889911");
        a2.setName("Artifact 2");
        a2.setDescription("Artifact 2 description");
        a2.setImageUrl("imageUrl2");

        Artifact a3 = new Artifact();
        a3.setId("334455667788990011");
        a3.setName("Artifact 3");
        a3.setDescription("Artifact 3 description");
        a3.setImageUrl("imageUrl3");

        Artifact a4 = new Artifact();
        a4.setId("445566778899001122");
        a4.setName("Artifact 4");
        a4.setDescription("Artifact 4 description");
        a4.setImageUrl("imageUrl4");


        Artifact a5 = new Artifact();
        a5.setId("556677889900112233");
        a5.setName("Artifact 5");
        a5.setDescription("Artifact 5 description");
        a5.setImageUrl("imageUrl5");

        Artifact a6 = new Artifact();
        a6.setId("556677889900112133");
        a6.setName("Artifact 6");
        a6.setDescription("Artifact 6 description");
        a6.setImageUrl("imageUrl6");

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Wizard 1");
        w1.addArtifact(a1);
        w1.addArtifact(a3);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Wizard 2");
        w2.addArtifact(a2);
        w2.addArtifact(a4);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Wizard 3");
        w3.addArtifact(a5);

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);

        artifactRepository.save(a6);
    }
}
