package org.digital_academy.patient;

import jakarta.persistence.*;


@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String breed;
    private String gender;
    private String ownerDNI;
    private String ownerName;
    private String phone;

    // Constructor vacío
    public Patient() {
    }

    // Constructor con todos los campos (excepto id)
    public Patient(String name, int age, String breed, String gender, String ownerDNI, String ownerName, String phone) {
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.gender = gender;
        this.ownerDNI = ownerDNI;
        this.ownerName = ownerName;
        this.phone = phone;
    }

    // Constructor con todos los campos incluyendo id
    public Patient(Long id, String name, int age, String breed, String gender, String ownerDNI, String ownerName, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.gender = gender;
        this.ownerDNI = ownerDNI;
        this.ownerName = ownerName;
        this.phone = phone;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOwnerDNI() {
        return ownerDNI;
    }

    public void setOwnerDNI(String ownerDNI) {
        this.ownerDNI = ownerDNI;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Builder manual
    public static PatientBuilder builder() {
        return new PatientBuilder();
    }

    public static class PatientBuilder {
        private Long id;
        private String name;
        private int age;
        private String breed;
        private String gender;
        private String ownerDNI;
        private String ownerName;
        private String phone;

        public PatientBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PatientBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PatientBuilder age(int age) {
            this.age = age;
            return this;
        }

        public PatientBuilder breed(String breed) {
            this.breed = breed;
            return this;
        }

        public PatientBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public PatientBuilder ownerDNI(String ownerDNI) {
            this.ownerDNI = ownerDNI;
            return this;
        }

        public PatientBuilder ownerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public PatientBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Patient build() {
            return new Patient(id, name, age, breed, gender, ownerDNI, ownerName, phone);
        }
    }
}
