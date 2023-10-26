package data;

public enum Gender {

    MALE("Male"),
    FEMALE("Female"),
    OTHER("undefined");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
