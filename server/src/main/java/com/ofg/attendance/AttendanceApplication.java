package com.ofg.attendance;

import com.ofg.attendance.model.entity.Lecture;
import com.ofg.attendance.model.entity.Role;
import com.ofg.attendance.model.entity.User;
import com.ofg.attendance.model.request.*;
import com.ofg.attendance.model.response.CourseResponse;
import com.ofg.attendance.model.response.InstructorResponse;
import com.ofg.attendance.model.response.LectureResponse;
import com.ofg.attendance.repository.RoleRepository;
import com.ofg.attendance.repository.UserRepository;
import com.ofg.attendance.service.abstracts.CourseService;
import com.ofg.attendance.service.abstracts.InstructorService;
import com.ofg.attendance.service.abstracts.LectureService;
import com.ofg.attendance.service.abstracts.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class AttendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }

    @Bean
    public CommandLineRunner createUsers(UserRepository userRepository,
                                         RoleRepository roleRepository,
                                         PasswordEncoder passwordEncoder,
                                         CourseService courseService,
                                         LectureService lectureService,
                                         InstructorService instructorService,
                                         StudentService studentService) {
        return args -> {
            seedData(userRepository, roleRepository, passwordEncoder, courseService, lectureService, instructorService, studentService);
        };
    }

    private static void seedData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CourseService courseService, LectureService lectureService, InstructorService instructorService, StudentService studentService) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_USER");
                    return roleRepository.save(role);
                });

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_ADMIN");
                    return roleRepository.save(role);
                });

        Role instructorRole = roleRepository.findByName("ROLE_INSTRUCTOR")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_INSTRUCTOR");
                    return roleRepository.save(role);
                });

        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_STUDENT");
                    return roleRepository.save(role);
                });

        UUID courseIdPrg = null;
        UUID courseIdMat = null;
        UUID courseIdPhy = null;

        if (userRepository.findByEmail("omer@gmail.com").isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);

            User user = new User();
            user.setEmail("omer@gmail.com");
            user.setPassword(passwordEncoder.encode("P4ssword"));
            user.setFirstName("omer");
            user.setLastName("gulhan");
            user.setPhoneNumber("+905531234567");
            user.setProfileImage("default.png");
            user.setActive(true);
            user.setRoles(roles);
            userRepository.save(user);
        }

        if (userRepository.findByEmail("faruk@gmail.com").isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            User user = new User();
            user.setEmail("faruk@gmail.com");
            user.setPassword(passwordEncoder.encode("P4ssword"));
            user.setFirstName("faruk");
            user.setLastName("gulhan");
            user.setPhoneNumber("+905531234568");
            user.setProfileImage("default.png");
            user.setActive(true);
            user.setRoles(roles);
            userRepository.save(user);
        }

        if (userRepository.findByEmail("instructor1@gmail.com").isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            roles.add(instructorRole);

            User user = new User();
            user.setEmail("instructor1@gmail.com");
            user.setPassword(passwordEncoder.encode("P4ssword"));
            user.setFirstName("instructor1");
            user.setLastName("gulhan");
            user.setPhoneNumber("+905531234561");
            user.setProfileImage("default.png");
            user.setActive(true);
            user.setRoles(roles);
            User savedUser = userRepository.save(user);

            InstructorResponse instructorResponse = instructorService.addInstructor(new InstructorCreateRequest(savedUser.getId(), "Engineering"));
            CourseResponse coursePrg = courseService.addCourse(savedUser.getId(), new CourseCreateRequest("Programming 1", "PRG1001"));
            LectureResponse lecturePrg1 = lectureService.addLecture(savedUser.getId(),
                    new LectureCreateRequest(coursePrg.id(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2)));
            LectureResponse lecturePrg2 = lectureService.addLecture(savedUser.getId(),
                    new LectureCreateRequest(coursePrg.id(), LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2)));
            LectureResponse lecturePrg3 = lectureService.addLecture(savedUser.getId(),
                    new LectureCreateRequest(coursePrg.id(), LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).plusHours(2)));

            courseIdPrg = coursePrg.id();
        }

        if (userRepository.findByEmail("instructor2@gmail.com").isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            roles.add(instructorRole);

            User user = new User();
            user.setEmail("instructor2@gmail.com");
            user.setPassword(passwordEncoder.encode("P4ssword"));
            user.setFirstName("instructor2");
            user.setLastName("gulhan");
            user.setPhoneNumber("+905531234562");
            user.setProfileImage("default.png");
            user.setActive(true);
            user.setRoles(roles);
            User savedUser = userRepository.save(user);

            instructorService.addInstructor(new InstructorCreateRequest(savedUser.getId(), "Law"));
            CourseResponse courseMat = courseService.addCourse(savedUser.getId(), new CourseCreateRequest("Mathematics 1", "MAT1001"));
            LectureResponse lectureMat1 = lectureService.addLecture(savedUser.getId(),
                    new LectureCreateRequest(courseMat.id(), LocalDateTime.now().plusDays(1).plusHours(2), LocalDateTime.now().plusDays(1).plusHours(4)));
            LectureResponse lectureMat2 = lectureService.addLecture(savedUser.getId(),
                    new LectureCreateRequest(courseMat.id(), LocalDateTime.now().plusDays(2).plusHours(2), LocalDateTime.now().plusDays(2).plusHours(4)));
            LectureResponse lectureMat3 = lectureService.addLecture(savedUser.getId(),
                    new LectureCreateRequest(courseMat.id(), LocalDateTime.now().plusDays(3).plusHours(2), LocalDateTime.now().plusDays(3).plusHours(4)));

            CourseResponse coursePhy = courseService.addCourse(savedUser.getId(), new CourseCreateRequest("Physics 1", "PHY1001"));
            LectureResponse lecturePhy1 = lectureService.addLecture(savedUser.getId(),
                    new LectureCreateRequest(coursePhy.id(), LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4).plusHours(2)));
            LectureResponse lecturePhy2 = lectureService.addLecture(savedUser.getId(),
                    new LectureCreateRequest(coursePhy.id(), LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2)));
            LectureResponse lecturePhy3 = lectureService.addLecture(savedUser.getId(),
                    new LectureCreateRequest(coursePhy.id(), LocalDateTime.now().plusDays(6), LocalDateTime.now().plusDays(6).plusHours(2)));

            courseIdMat = courseMat.id();
            courseIdPhy = coursePhy.id();
        }

        if (userRepository.findByEmail("student1@gmail.com").isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            roles.add(studentRole);

            User user = new User();
            user.setEmail("student1@gmail.com");
            user.setPassword(passwordEncoder.encode("P4ssword"));
            user.setFirstName("student1");
            user.setLastName("gulhan");
            user.setPhoneNumber("+905531234563");
            user.setProfileImage("default.png");
            user.setActive(true);
            user.setRoles(roles);
            User savedUser = userRepository.save(user);

            studentService.addStudent(new StudentCreateRequest(savedUser.getId()));

            courseService.assignStudentToCourse(new AssignStudentToCourseRequest(savedUser.getId(), courseIdPrg));
            courseService.assignStudentToCourse(new AssignStudentToCourseRequest(savedUser.getId(), courseIdMat));
            courseService.assignStudentToCourse(new AssignStudentToCourseRequest(savedUser.getId(), courseIdPhy));
        }

        if (userRepository.findByEmail("student2@gmail.com").isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            roles.add(studentRole);

            User user = new User();
            user.setEmail("student2@gmail.com");
            user.setPassword(passwordEncoder.encode("P4ssword"));
            user.setFirstName("student2");
            user.setLastName("gulhan");
            user.setPhoneNumber("+905531234564");
            user.setProfileImage("default.png");
            user.setActive(true);
            user.setRoles(roles);
            User savedUser = userRepository.save(user);

            studentService.addStudent(new StudentCreateRequest(savedUser.getId()));

            courseService.assignStudentToCourse(new AssignStudentToCourseRequest(savedUser.getId(), courseIdPrg));
        }

        if (userRepository.findByEmail("student3@gmail.com").isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            roles.add(studentRole);

            User user = new User();
            user.setEmail("student3@gmail.com");
            user.setPassword(passwordEncoder.encode("P4ssword"));
            user.setFirstName("student3");
            user.setLastName("gulhan");
            user.setPhoneNumber("+905531234565");
            user.setProfileImage("default.png");
            user.setActive(true);
            user.setRoles(roles);
            User savedUser = userRepository.save(user);

            studentService.addStudent(new StudentCreateRequest(savedUser.getId()));

            courseService.assignStudentToCourse(new AssignStudentToCourseRequest(savedUser.getId(), courseIdMat));
            courseService.assignStudentToCourse(new AssignStudentToCourseRequest(savedUser.getId(), courseIdPhy));
        }
    }
}
