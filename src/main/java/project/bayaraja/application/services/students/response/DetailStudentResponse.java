package project.bayaraja.application.services.students.response;

import java.util.Date;

public record DetailStudentResponse(
        Integer id,
        String name,
        String address,
        String phone,
        String profile_picture,
        String grade,
        Date join_at,
        String year_period,
        Boolean is_graduate
) {
}
