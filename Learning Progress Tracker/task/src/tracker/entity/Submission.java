package tracker.entity;

import lombok.NonNull;

public record Submission(@NonNull Student student, @NonNull Course course, int points) {
}
