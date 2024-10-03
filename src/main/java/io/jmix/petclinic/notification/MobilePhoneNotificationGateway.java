package io.jmix.petclinic.notification;

public interface MobilePhoneNotificationGateway {

    String NAME = "mobilePhoneNotificationGateway";

    void sendNotification(String phoneNumber, String text);
}
