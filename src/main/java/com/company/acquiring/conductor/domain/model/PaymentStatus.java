package com.company.acquiring.conductor.domain.model;

public enum PaymentStatus {

    PENDING("Ожидает обработки"),
    ROUTING("Определяется маршрут"),
    THREE_DS_REQUIRED("Требуется 3DS аутентификация"),
    PROCESSING("В процессе обработки"),
    AUTHORIZED("Авторизован"),
    CAPTURED("Успешно завершен"),
    FAILED("Ошибка"),
    REFUNDED("Возвращен"),
    CANCELED("Отменен");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}