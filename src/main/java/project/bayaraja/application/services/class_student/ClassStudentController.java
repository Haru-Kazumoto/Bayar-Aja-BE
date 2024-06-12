package project.bayaraja.application.services.class_student;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bayaraja.application.services.class_student.dto.request.CreateClassStudentDto;
import project.bayaraja.application.services.class_student.interfaces.ClassStudentService;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.utils.BaseResponse;

import java.util.Collections;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/api/class-student")
@Tag(name = "ClassStudent")
public class ClassStudentController {

    private final ClassStudentService classStudentService;

    @PostMapping("/create-one")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse<ClassStudentEntity>> createSingleData(@RequestBody @Valid CreateClassStudentDto dto){
        ClassStudentEntity createdData = this.classStudentService.createSingleClass(dto);

        BaseResponse<ClassStudentEntity> response = new BaseResponse<>(
                Collections.singletonList("OK"),
                createdData
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-multi")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse<List<ClassStudentEntity>>> createMultiData(@RequestBody @Valid List<CreateClassStudentDto> dtos){
        List<ClassStudentEntity> createdData = classStudentService.createMultiClass(dtos);

        BaseResponse<List<ClassStudentEntity>> response = new BaseResponse<>(
                Collections.singletonList("OK"),
                createdData
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-detail-class-student")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse<ClassStudentEntity>> getDetailClass(
            @Parameter(name = "class-id", example = "1", required = true)
            @RequestParam("class-id")
            Integer id
    ){
        ClassStudentEntity detailClass = classStudentService.getDetailClass(id);

        BaseResponse<ClassStudentEntity> response = new BaseResponse<>(
                Collections.singletonList("OK"),
                detailClass
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-students")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse<ClassStudentEntity>> addStudentsToClass(
            @Parameter(name = "class-id", required = true)
            @RequestParam("class-id")
            Integer id,
            @RequestBody List<Integer> studentIds
    ){
        ClassStudentEntity updatedClass = classStudentService.addStudentsToClass(id, studentIds);

        BaseResponse<ClassStudentEntity> response = new BaseResponse<>(
                Collections.singletonList("OK"),
                updatedClass
        );

        return ResponseEntity.ok(response);
    }

}
