package ru.blizzed.yandexgallery.data.repositories;

public class NoPermissionException extends RuntimeException {

    public NoPermissionException(String message) {
        super(message);
    }

}
