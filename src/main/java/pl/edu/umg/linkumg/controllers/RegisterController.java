//package pl.edu.umg.linkumg.controllers;
//
//import lombok.AllArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import pl.edu.umg.linkumg.dto.RegisterEmployerDto;
//import pl.edu.umg.linkumg.dto.RegisterStudentDto;
//import pl.edu.umg.linkumg.models.Employer;
//import pl.edu.umg.linkumg.models.Student;
//import pl.edu.umg.linkumg.service.EmployerService;
//import pl.edu.umg.linkumg.service.StudentService;
//
//@RestController
//@AllArgsConstructor
//public class RegisterController {
//    private EmployerService employerService;
//    private StudentService studentService;
//    private ModelMapper modelMapper;
//
//    @PostMapping("/register/employer")
//    public @ResponseBody ResponseEntity register(@RequestBody RegisterEmployerDto registerEmployerDto) {
//        if (checkEmail(registerEmployerDto.getEmail()) && checkPassword(registerEmployerDto.getPassword()) && checkName(registerEmployerDto.getFirstname()) && checkName(registerEmployerDto.getLastname()) && registerEmployerDto.getUserRole() != null && registerEmployerDto.getUserRole().equals("employer")) {
//            if (checkName(registerEmployerDto.getCompanyName()) && checkName(registerEmployerDto.getNIPNumber())) {
//                Employer employer = employerService.saveOrUpdate(modelMapper.map(registerEmployerDto, Employer.class));
//                if (employer.getUserId() != null) {
//                    return new ResponseEntity("OK", HttpStatus.OK);
//                } else {
//                    return new ResponseEntity("PAYMENT_REQUIRED", HttpStatus.PAYMENT_REQUIRED);
//                }
//            }
//        }
//        return new ResponseEntity("BAD_REQUEST", HttpStatus.BAD_REQUEST);
//    }
//
//    @PostMapping("/register/student")
//    public @ResponseBody ResponseEntity registerStudent(@RequestBody RegisterStudentDto registerStudentDtoDto) {
//        if (checkEmail(registerStudentDtoDto.getEmail()) && checkPassword(registerStudentDtoDto.getPassword()) && checkName(registerStudentDtoDto.getFirstname()) && checkName(registerStudentDtoDto.getLastname()) && registerStudentDtoDto.getUserRole() != null && registerStudentDtoDto.getUserRole().equals("student")) {
//            if (registerStudentDtoDto.getIndexNumber() != 0 && checkName(registerStudentDtoDto.getFaculty())) {
//                Student student = studentService.save(modelMapper.map(registerStudentDtoDto, Student.class));
//                if (student.getUserId() != null) {
//                    return new ResponseEntity("OK", HttpStatus.OK);
//                } else {
//                    return new ResponseEntity("PAYMENT_REQUIRED", HttpStatus.PAYMENT_REQUIRED);
//                }
//            }
//        }
//        return new ResponseEntity("BAD_REQUEST", HttpStatus.BAD_REQUEST);
//    }
//
//    private boolean checkEmail(String email) {
//        return email.matches("(?:[a-z0-9!#$%&'*+\\=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
//    }
//
//    private boolean checkPassword(String password) {
//        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
//    }
//
//    private boolean checkName(String name) {
//        return !(name == null || name.isEmpty());
//    }
//}
