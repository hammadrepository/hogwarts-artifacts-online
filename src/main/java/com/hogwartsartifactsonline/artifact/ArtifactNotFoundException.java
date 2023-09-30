package com.hogwartsartifactsonline.artifact;

public class ArtifactNotFoundException extends RuntimeException{

    public ArtifactNotFoundException(String id){
        super("Couldn't found artifact");
    }
}
