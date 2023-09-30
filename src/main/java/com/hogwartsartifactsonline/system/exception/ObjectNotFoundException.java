package com.hogwartsartifactsonline.system.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String objectName, String id) {
        super("Couldn't found " + objectName.toLowerCase());
    }

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find " + objectName + " with Id " + id + " :(");
    }
}
