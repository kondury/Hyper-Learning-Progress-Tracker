package tracker.entity;

public enum Course {
    Java(1, 600),
    DSA(2, 400),
    Databases(3, 480),
    Spring(4, 550);

    public final int id;
    public final int completionPoints;
    Course(int id, int completionPoints) {
        this.id = id;
        this.completionPoints = completionPoints;
    }

    public static Course getById(int id) {
        for (Course course: values()) {
            if (course.id == id) {
                return course;
            }
        }
        throw new IllegalArgumentException(String.format("Incorrect course format: %s", id));
    }

    public static Course getByName(String name) {
        return Course.valueOf(name);
    }

}
