//package pl.edu.umg.linkumg.controllers;
//
//import jakarta.servlet.http.HttpSession;
//import lombok.AllArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import pl.edu.umg.linkumg.dto.LoginDto;
//import pl.edu.umg.linkumg.models.User;
//import pl.edu.umg.linkumg.service.UserService;
//
//import java.util.Optional;
//
//@RestController
//@AllArgsConstructor
//public class LoginController {
//    private UserService userService;
//    private ModelMapper modelMapper;
//
//    @PostMapping("/login")
//    public @ResponseBody ResponseEntity login(@RequestBody LoginDto loginDto, HttpSession session) {
//        Optional<User> optionalUser = userService.findUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
//        if (optionalUser.isPresent()) {
//            session.setAttribute("user", optionalUser.get());
//            return new ResponseEntity("Welcome " + optionalUser.get().getEmail(), HttpStatus.OK);
//
//        }
//        return new ResponseEntity("User is missing", HttpStatus.NOT_FOUND);
//    }
//}
