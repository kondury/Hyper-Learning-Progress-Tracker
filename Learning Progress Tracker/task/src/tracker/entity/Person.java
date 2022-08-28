package tracker.entity;

import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Person {
    public static final String UNKNOWN_ID = "Unknown";
    @NonNull @Setter
    private String id = UNKNOWN_ID;
    @NonNull private final String firstName;
    @NonNull private final String lastName;
    @NonNull private final String email;

    public boolean hasId() {
        return !id.isEmpty() && !UNKNOWN_ID.equals(id);
    }
}
