# Queuemanagement

This is a Android project which support upto Android Marshmello. This project can be used as a skeliton project for any Android base project.

Project Details
=================
User can login to the application and can check doctors available on a particular date. After that user can book an appointment with that doctor. Once appointment is fixed the time gets stored in application local database. 1 hour before the appointment mobile blutooth starts automatically (this is not implemented yet, for now it starts within one minute of booking) to find the Bluetooth address of a partcular tracking device of that chember where the doctor will sit. If the device is found then the appointment status changes based on a service call and the mobile bluetooth gets turned off (It doesn't turn off if initially it was turened on manually). If the tracking device is not found then also blutooth turns off to save battery but it will turn on again after 15 minutes to search again.